package com.maan.eway.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.maan.eway.bean.ScreenFieldMaster;
import com.maan.eway.bean.ScreenFieldMaster.ScreenFieldMasterBuilder;
import com.maan.eway.master.req.Fieldvalues;
import com.maan.eway.master.req.GetFieldDetailsReq;
import com.maan.eway.master.req.SaveFieldDetailsReq;
import com.maan.eway.repository.ScreenFieldMasterRepository;
import com.maan.eway.res.CommonRes;
import com.maan.eway.service.FieldDetailsService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Service
public class FieldDetailsServiceImpl implements FieldDetailsService {

	private Logger logger = LogManager.getLogger(FieldDetailsServiceImpl.class);
	
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	
	private ObjectMapper objectMapper = new ObjectMapper();
	
	@PersistenceContext
	private EntityManager em;
	
	@Autowired
	private ScreenFieldMasterRepository fieldMasterRepo;
	
	@Override
	public CommonRes saveFieldDetails(SaveFieldDetailsReq req) {
		CommonRes res = new CommonRes();
		logger.info("saveField Req :: "+req.toString());
		try {
			Integer fieldId = 0;
			if(StringUtils.isBlank(req.getFieldId())) {
				CriteriaBuilder cb = em.getCriteriaBuilder();
				CriteriaQuery<Integer> cq = cb.createQuery(Integer.class);
				Root<ScreenFieldMaster> fmRoot = cq.from(ScreenFieldMaster.class);
				cq.multiselect(cb.coalesce(cb.sum(cb.max(fmRoot.get("fieldId")),1), 1).as(Integer.class));
				fieldId = em.createQuery(cq).getSingleResult();
			}else {
				fieldId = Integer.parseInt(req.getFieldId());
			}
			
			Optional<ScreenFieldMaster> existData = fieldMasterRepo.findById(fieldId);
			
			ScreenFieldMasterBuilder m = ScreenFieldMaster.builder()
				    .fieldId(fieldId)
				    .productId(req.getProductId() == null ? null : Integer.parseInt(req.getProductId()))
				    .sectionId(req.getSectionId() == null ? null : Integer.parseInt(req.getSectionId()))
				    .companyId(req.getInsuranceId() == null ? null : req.getInsuranceId())
				    .effectiveDate(req.getEffectiveDate() == null ? null : sdf.parse(req.getEffectiveDate()))
				    .fields(objectMapper.writeValueAsString(req.getFields()))
				    .fieldName(req.getFieldName() == null ? null : req.getFieldName())
				    .status(req.getStatus() == null ? "N" : req.getStatus());
				if (existData.isPresent()) {
					ScreenFieldMaster exitList = existData.get();
				    m.updatedBy(req.getLoginId() == null ? null : req.getLoginId())
				     .updatedDate(new Date())
				     .createdBy(exitList.getCreatedBy())
				     .entryDate(exitList.getEntryDate());
				} else {
				    m.entryDate(new Date())
				     .createdBy(req.getLoginId() == null ? null : req.getLoginId());
				}
				ScreenFieldMaster screenFieldMaster = m.build();
				fieldMasterRepo.save(screenFieldMaster);
				
				res.setMessage("SUCCESS");
				res.setCommonResponse("Saved Successfully");
				res.setIsError(false);
				res.setErrorMessage(null);
		logger.info("Exist into saveFieldDetails");
		}catch(Exception e) {
			res.setMessage("FAILED");
			res.setIsError(true);
			logger.info("Error in saveFieldDetails :: "+e.getStackTrace());
			e.printStackTrace();
		}
		return res;
	}

	@Override
	public CommonRes getFieldDetails(String fieldId,GetFieldDetailsReq req) {
		CommonRes res = new CommonRes();
		List<SaveFieldDetailsReq> responseList = new ArrayList<>();
		List<ScreenFieldMaster> fieldData = new ArrayList<>();
		if(fieldId!=null && StringUtils.isNotBlank(fieldId)) {
			fieldData.add(fieldMasterRepo.findById(Integer.parseInt(fieldId)).get());
		}else {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<ScreenFieldMaster> cq = cb.createQuery(ScreenFieldMaster.class);
			Root<ScreenFieldMaster> sRoot = cq.from(ScreenFieldMaster.class);
			
			cq.select(sRoot);
			List<Predicate> predicates = new ArrayList<Predicate>();
			if(StringUtils.isNotBlank(req.getInsuranceId())) {
				predicates.add(cb.equal(sRoot.get("companyId"), req.getInsuranceId()));
			}
			if(StringUtils.isNotBlank(req.getProductId())) {
				predicates.add(cb.equal(sRoot.get("productId"), Integer.parseInt(req.getProductId())));
			}
			if(StringUtils.isNotBlank(req.getSectionId())) {
				predicates.add(cb.equal(sRoot.get("sectionId"), Integer.parseInt(req.getSectionId())));
			}
			if(StringUtils.isNotBlank(req.getStatus())) {
				predicates.add(cb.equal(sRoot.get("status"), Integer.parseInt(req.getStatus())));
			}
			Predicate [] predicateArray = new Predicate[predicates.size()];
			predicates.toArray(predicateArray);
			
			cq.where(predicateArray).orderBy(cb.asc(sRoot.get("fieldId")));
			
			fieldData = em.createQuery(cq).getResultList();
		}
		if(!CollectionUtils.isEmpty(fieldData)) {
			fieldData.forEach(k -> {
				SaveFieldDetailsReq detailsReq = new SaveFieldDetailsReq();
				detailsReq.setFieldId(k.getFieldId()==null?null:k.getFieldId().toString());
				detailsReq.setInsuranceId(k.getCompanyId()==null?null:k.getCompanyId());
				detailsReq.setProductId(k.getProductId()==null?null:k.getProductId().toString());
				detailsReq.setSectionId(k.getSectionId()==null?null:k.getSectionId().toString());
				detailsReq.setStatus(k.getStatus()==null?null:k.getStatus());
				detailsReq.setLoginId(k.getUpdatedBy()==null?k.getCreatedBy()==null?"":k.getCreatedBy():k.getUpdatedBy());
				detailsReq.setFieldName(k.getFieldName()==null?null:k.getFieldName());
				detailsReq.setEffectiveDate(k.getEffectiveDate()==null?null:sdf.format(k.getEffectiveDate()));
				try {
					List<Map<String, Object>> fieldsList = objectMapper.readValue(k.getFields(), new TypeReference<List<Map<String, Object>>>(){});
					List<Fieldvalues> fieldsvalues = new ArrayList<Fieldvalues>();
					fieldsList.forEach(q -> {
						Fieldvalues m = Fieldvalues.builder()
								.value(q.get("Value")==null?"":q.get("Value").toString())
								.description(q.get("Description")==null?"":q.get("Description").toString())
								.build();
						fieldsvalues.add(m);
					});
					detailsReq.setFields(fieldsvalues);
					detailsReq.setMandatoryYN(fieldsvalues.stream().filter(f -> f.getDescription().equalsIgnoreCase("Mandatory"))
							.map(p -> p.getValue()).findFirst().orElse(""));
					detailsReq.setFieldType(fieldsvalues.stream().filter(f -> f.getDescription().equalsIgnoreCase("FieldType"))
							.map(p -> p.getValue()).findFirst().orElse(""));
				} catch (JsonProcessingException e) {
					e.printStackTrace();
				}
				responseList.add(detailsReq);
			});
			res.setCommonResponse(responseList);
			res.setMessage("SUCCESS");
			res.setErrorMessage(null);
			res.setIsError(false);
		}else {
			res.setCommonResponse(null);
			res.setMessage("FALSE");
			res.setIsError(true);
		}
		
		return res;
	}

}
