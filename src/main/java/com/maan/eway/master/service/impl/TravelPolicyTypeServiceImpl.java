package com.maan.eway.master.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.maan.eway.bean.TravelPolicyType;
import com.maan.eway.error.Error;
import com.maan.eway.master.req.TravelPolicyTypeGetReq;
import com.maan.eway.master.req.TravelPolicyTypeSaveReq;
import com.maan.eway.master.res.PolicyTypeSubCoverMasterGetRes;
import com.maan.eway.master.res.TravelPolicyTypeGetRes;
import com.maan.eway.master.res.TravelPolicyTypeGetRes1;
import com.maan.eway.master.service.TravelPolicyTypeService;
import com.maan.eway.repository.TravelPolicyTypeRepository;
import com.maan.eway.res.SuccessRes2;

@Service
public class TravelPolicyTypeServiceImpl implements TravelPolicyTypeService {
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	@PersistenceContext
	private EntityManager em;
	private Logger log=LogManager.getLogger(PolicyTypeMasterSubCoverServiceImple.class);
	
	@Autowired
	private TravelPolicyTypeRepository repository;

	@Override
	public List<Error> validateTravelPolicyType(TravelPolicyTypeSaveReq req) {
		List<Error> error = new ArrayList<Error>();
		try {
		Date today1 = new Date();
		Calendar cal = new GregorianCalendar();
		cal.setTime(today1);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		today1 = cal.getTime();  //beginning of the date
		
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.MILLISECOND, 999);
		Date todayEnd1 = cal.getTime(); //today end
		
		if(req.getPolicyTypeId()==null)
		{
			error.add(new Error("O1","Policy Type Id","Please Enter Policy Type Id"));
		}
		
		if(req.getPlanTypeId()==null)
		{
			error.add(new Error("O2","Plan Type Id","Please Enter Plan Type Id"));
		}
		
//		if(req.getCoverId()==null)
//		{
//			error.add(new Error("O3","Cover Id","Please Enter Cover Id"));
//		}
		
		
		if(req.getCompanyId()==null)
		{
			error.add(new Error("O6","Company Id","Please Enter Company Id"));
		}
		
		if(req.getProductId()==null)
		{
			error.add(new Error("O7","Product Id","Please Enter Product Id"));
		}
		
		if(req.getBranchCode()==null)
		{
			error.add(new Error("O8","Branch Code","Please Enter Branch Code"));
		}
		
		if(StringUtils.isBlank(req.getPolicyTypeDesc()))
		{
			error.add(new Error("O9","Policy Type Description","Please Enter Policy Type Description"));
		}
		else if(req.getPolicyTypeDesc().length()>100)
		{
			error.add(new Error("O9","Policy Type Description","Please Enter Policy Type Description below 100 character"));
		}
		
		if(StringUtils.isBlank(req.getPlanTypeDesc()))
		{
			error.add(new Error("10","Plan Type Description","Please Enter Plan Type Description"));
		}
		else if(req.getPlanTypeDesc().length()>100)
		{
			error.add(new Error("10","Plan Type Description","Please Enter Plan Type Description below 100 character"));
		}
		
		if(StringUtils.isBlank(req.getCoverDesc()))
		{
			error.add(new Error("11","Cover Description","Please Enter Cover Description"));
		}
		else if(req.getCoverDesc().length()>100)
		{
			error.add(new Error("11","Cover Description","Please Enter Cover Description below 100 character"));
		}

//		
		Calendar cal1 = new GregorianCalendar();
		Date today = new Date();
		cal1.setTime(today);
		cal1.add(Calendar.DAY_OF_MONTH, -1);
		cal1.set(Calendar.HOUR_OF_DAY, 23);
		cal1.set(Calendar.MINUTE, 50);
		today = cal1.getTime();
		
		if (req.getEffectiveDateStart() == null) {
			error.add(new Error("16", "EffectiveStartDate", "Please Enter Effective Start Date"));

		} else if (req.getEffectiveDateStart().before(today)) {
			error.add(new Error("16", "EffectiveStartDate", "Please Enter Effective Start Date as Future Date"));
		}

		
		if (StringUtils.isBlank(req.getCoverStatus())) {
			error.add(new Error("21", "Cover Status", "Please Enter Cover Status"));
		} else if (req.getCoverStatus().length() > 100) {
			error.add(new Error("21", "Cover Status", "Enter Cover Status within 100 Characters Only"));
		}
		
		if (StringUtils.isBlank(req.getCreatedBy())) {
			error.add(new Error("06", "CreatedBy", "Please Enter CreatedBy"));
		}else if (req.getCreatedBy().length() > 50) {
			error.add(new Error("06", "CreatedBy", "Please Enter CreatedBy within 100 Characters"));
		} 
				
		//Duplication find
		if(StringUtils.isNotBlank(req.getCompanyId()) &&  StringUtils.isNotBlank(req.getBranchCode()) && StringUtils.isNotBlank(req.getProductId()) && 
				StringUtils.isNotBlank(req.getPolicyTypeId()) && StringUtils.isNotBlank(req.getPlanTypeId()) && StringUtils.isNotBlank(req.getCoverDesc())
				&& StringUtils.isBlank(req.getCoverId())) {  //new insert
			
			List<TravelPolicyType> list = new ArrayList<TravelPolicyType>();
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<TravelPolicyType> query = cb.createQuery(TravelPolicyType.class);

			// Find All
			Root<TravelPolicyType> b = query.from(TravelPolicyType.class);

			// Select
			query.select(b);

			// Amend ID Max Filter
			Subquery<Long> amendId = query.subquery(Long.class);
			Root<TravelPolicyType> ocpm1 = amendId.from(TravelPolicyType.class);
			amendId.select(cb.max(ocpm1.get("amendId")));
			Predicate a1 = cb.equal(ocpm1.get("productId"), b.get("productId"));
			Predicate a2 = cb.equal(ocpm1.get("companyId"), b.get("companyId"));
			Predicate a3 = cb.equal(ocpm1.get("policyTypeId"),b.get("policyTypeId"));
			Predicate a4 = cb.equal(ocpm1.get("planTypeId"),b.get("planTypeId"));
			Predicate a7 = cb.equal(ocpm1.get("branchCode"), b.get("branchCode"));
	
			amendId.where(a1,a2,a3,a4,a7);

			Predicate n1 = cb.equal(b.get("amendId"),amendId);
			Predicate n2 = cb.equal(b.get("productId"), req.getProductId() );	
			Predicate n3 = cb.equal(b.get("companyId"), req.getCompanyId() );		
			Predicate n4 = cb.equal(b.get("branchCode"), req.getBranchCode());
			Predicate n6 =  cb.equal(b.get("policyTypeId"), req.getPolicyTypeId()); 
			Predicate n7 =  cb.equal(b.get("planTypeId"), req.getPlanTypeId()); 
			Predicate n8 = cb.lessThanOrEqualTo(b.get("effectiveStartdate"), today1);
			Predicate n9 = cb.greaterThanOrEqualTo(b.get("effectiveEnddate"), todayEnd1);
		//	Predicate n10 = cb.notEqual(b.get("coverId"), req.getCoverId());
		
			query.where(n1, n2,n3,n4,n6, n7, n8, n9);
			
			TypedQuery<TravelPolicyType> result = em.createQuery(query);
			list = result.getResultList();
			
			if(list.size()>0) {
				
				List<TravelPolicyType> dup = list.stream().filter(o -> o.getCoverDesc().equalsIgnoreCase(req.getCoverDesc())).collect(Collectors.toList());
				if(dup.size()>0)
					error.add(new Error("02", "CoverDesc","Cover '" + req.getCoverDesc() + "' Already Exists"));
							
			}
		}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return error;
	}

	@Override
	public SuccessRes2 insertTravelPolicyType(TravelPolicyTypeSaveReq req) {
		SuccessRes2 res = new SuccessRes2();
		TravelPolicyType saveData = new TravelPolicyType();
		DozerBeanMapper dozerMapper = new  DozerBeanMapper();
		try {
			Integer amendId = 0;
			Date StartDate = req.getEffectiveDateStart();
			String end = "31/12/2050";
			Date endDate = sdf.parse(end);
			long MILLS_IN_A_DAY = 1000*60*60*24;
			Date oldEndDate = new Date(req.getEffectiveDateStart().getTime()- MILLS_IN_A_DAY);
			Date entryDate = null;
			String createdBy ="";
			Integer coverId = 0;
			
			List<TravelPolicyType> old = repository.findByProductIdAndBranchCodeAndCompanyIdAndPolicyTypeIdAndPlanTypeIdOrderByCoverIdDesc(
					Integer.valueOf(req.getProductId()), req.getBranchCode(), req.getCompanyId(),
					Integer.valueOf(req.getPolicyTypeId()),Integer.valueOf(req.getPlanTypeId()) );
			
			if(StringUtils.isBlank(req.getCoverId())) {   //Insert
				
				coverId = old.get(0).getCoverId() + 1;
				entryDate = new Date();
				createdBy = req.getCreatedBy();
				res.setResponse("Saved Successfully");
				
			}
			else {  //update
				
				coverId = Integer.valueOf( req.getCoverId());
				
				List<TravelPolicyType> list = new ArrayList<TravelPolicyType>();
				
				// Find Latest Record
				CriteriaBuilder cb = em.getCriteriaBuilder();
				CriteriaQuery<TravelPolicyType> query = cb.createQuery(TravelPolicyType.class);

				// Find All
				Root<TravelPolicyType> b = query.from(TravelPolicyType.class);

				// Select
				query.select(b);

				List<Order> orderList = new ArrayList<Order>();
			    orderList.add(cb.desc(b.get("amendId")));
		
				Predicate n2 = cb.equal(b.get("productId"), req.getProductId() );	
				Predicate n3 = cb.equal(b.get("companyId"), req.getCompanyId() );		
				Predicate n4 = cb.equal(b.get("branchCode"), req.getBranchCode());
				Predicate n5 = cb.equal(b.get("coverId"), req.getCoverId());
				Predicate n6 =  cb.equal(b.get("policyTypeId"), req.getPolicyTypeId()); 
				Predicate n7 =  cb.equal(b.get("planTypeId"), req.getPlanTypeId()); 
			
				query.where(n2,n3,n4, n5, n6, n7) ;
			
				TypedQuery<TravelPolicyType> result = em.createQuery(query);
				int limit=0, offset=2;
				result.setFirstResult(limit * offset);
				result.setMaxResults(offset);
				list = result.getResultList();
				
				if(list.size()>0) {
					Date beforeOneDay = new Date(new Date().getTime()- MILLS_IN_A_DAY);
					
					if(list.get(0).getEffectiveStartdate().before(beforeOneDay)) {
						amendId = list.get(0).getAmendId()+1;
						entryDate = new Date();
						createdBy = req.getCreatedBy();
						TravelPolicyType lastRecord = list.get(0);
						lastRecord.setEffectiveEnddate(oldEndDate);
						repository.saveAndFlush(lastRecord);
					}
					else {
						amendId = list.get(0).getAmendId();
						entryDate = list.get(0).getEntryDate();
						createdBy = list.get(0).getUpdatedBy();
					
						if(list.size()>1) {
							TravelPolicyType lastRecord = list.get(1);	
							lastRecord.setEffectiveEnddate(oldEndDate);
							repository.saveAndFlush(lastRecord);
						}
					}
				}
				res.setResponse("Updated Successfully");
				
			}
			
			
			dozerMapper.map(req, saveData);
			
			saveData.setCoverId(coverId);
			saveData.setSubCoverId(0);
			saveData.setEffectiveStartdate(StartDate);
			saveData.setEffectiveEnddate(endDate);
			saveData.setEntryDate(entryDate);
			saveData.setUpdatedBy(req.getCreatedBy());
			saveData.setUpdatedDate(new Date());
			saveData.setAmendId(amendId);
			
			repository.saveAndFlush(saveData);
			res.setSuccessId(coverId.toString());
			
			}
		catch(Exception e) {
			e.printStackTrace();
			log.info("Exception is --> " + e.getMessage());
			return null;
		}
		return res;
	}

	

	@Override
	public TravelPolicyTypeGetRes1 getalltravelpolicytype(TravelPolicyTypeGetReq req) {
		TravelPolicyTypeGetRes1 res1 = new TravelPolicyTypeGetRes1();

		List<TravelPolicyTypeGetRes> resList = new ArrayList<TravelPolicyTypeGetRes>();

		DozerBeanMapper mapper = new DozerBeanMapper();
		
		try {
			Date today = new Date();
			Calendar cal = new GregorianCalendar();
			cal.setTime(today);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			today = cal.getTime();  //beginning of the date
			
			cal.set(Calendar.HOUR_OF_DAY, 23);
			cal.set(Calendar.MINUTE, 59);
			cal.set(Calendar.SECOND, 59);
			cal.set(Calendar.MILLISECOND, 999);
			Date todayEnd = cal.getTime(); //today end
			
		List<TravelPolicyType> list = new ArrayList<TravelPolicyType>();

		//Find Latest Record
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<TravelPolicyType> query = cb.createQuery(TravelPolicyType.class);

		//Find All
		Root<TravelPolicyType> b = query.from(TravelPolicyType.class);

		//Select
		query.select(b);

		//AmenId max filter
		Subquery<Long> maxAmendId = query.subquery(Long.class);
		Root<TravelPolicyType> ocpm1 = maxAmendId.from(TravelPolicyType.class);
		maxAmendId.select(cb.max(ocpm1.get("amendId")));
		Predicate a1 = cb.equal(ocpm1.get("companyId"), b.get("companyId"));
		Predicate a2 = cb.equal(ocpm1.get("productId"), b.get("productId"));
		Predicate a3 = cb.equal(ocpm1.get("policyTypeId"), b.get("policyTypeId"));
		Predicate a4 = cb.equal(ocpm1.get("planTypeId"), b.get("planTypeId"));
		Predicate a5 = cb.equal(ocpm1.get("branchCode"), b.get("branchCode"));
		
		maxAmendId.where(a1,a2,a3,a4,a5);

		Predicate n1 = cb.equal(b.get("companyId"), req.getCompanyId());
		Predicate n2 = cb.equal(b.get("productId"), req.getProductId());
		Predicate n3 = cb.equal(b.get("policyTypeId"), req.getPolicyTypeId());
		Predicate n4 = cb.equal(b.get("planTypeId"), req.getPlanTypeId());
		Predicate n5 = cb.equal(b.get("branchCode"), req.getBranchCode());
		Predicate n6 = cb.equal(b.get("amendId"),maxAmendId);
		Predicate n8 = cb.lessThanOrEqualTo(b.get("effectiveStartdate"), today);
		Predicate n9 = cb.greaterThanOrEqualTo(b.get("effectiveEnddate"), todayEnd);

		List<Order> orderList = new ArrayList<Order>();
		orderList.add(cb.desc(b.get("coverId")));
		query.where(n1, n2, n3, n4, n5, n6,n8,n9).orderBy(orderList);

		TypedQuery<TravelPolicyType> result = em.createQuery(query);
		result.setFirstResult(req.getLimit() * req.getOffset());
		result.setMaxResults( req.getOffset());
		list = result.getResultList();
		
		if(list.size()>0) {
			
			for(TravelPolicyType data : list) {
				TravelPolicyTypeGetRes res = new TravelPolicyTypeGetRes();
				res = mapper.map(data,  TravelPolicyTypeGetRes.class);
				resList.add(res);
				
			}
			res1.setTotalCount(list.size());
			res1.setTravelPolicyType(resList);
		}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return res1;
		}
	}
		
//		for (TravelPolicyType data : list) {
//			
//		TravelPolicyTypeGetRes res = new TravelPolicyTypeGetRes();
//		
//		
//		res = mapper.map(data, TravelPolicyTypeGetRes.class);
//		res.setPolicyTypeId(data.getPolicyTypeId().toString());
//		res.setPolicyTypeDesc(data.getPolicyTypeDesc());
//		res.setPlanTypeId(data.getPlanTypeId().toString());
//		res.setPlanTypeDesc(data.getPlanTypeDesc());
//		res.setCoverId(data.getCoverId().toString());
//		res.setSubCoverId(data.getSubCoverId().toString());
//		res.setAmendId(data.getAmendId().toString());
//		res.setCompanyId(data.getCompanyId().toString());
//		res.setProductId(data.getProductId().toString());
//		res.setBranchCode(data.getBranchCode());
//		res.setPolicyTypeDesc(data.getPolicyTypeDesc());
//		res.setPlanTypeDesc(data.getPlanTypeDesc());
//		res.setCoverDesc(data.getCoverDesc());
//		res.setSubCoverDesc(data.getSubCoverDesc());
//		res.setCurrency(data.getCurrency());
//		res.setSumInsured(data.getSumInsured());
//		res.setExcessAmt(data.getExcessAmt());
//		res.setEntryDate(data.getEntryDate());
//		res.setStatus(data.getStatus());
//		res.setRemarks(data.getRemarks()==null?"":data.getRemarks());
//		res.setEffectiveStartdate(data.getEffectiveStartdate());
//		res.setEffectiveEnddate(data.getEffectiveEnddate());
//		res.setUpdatedDate(data.getUpdatedDate());
//		res.setCoverStatus(data.getCoverStatus()==null?"":data.getCoverStatus());
//		resList.add(res);
//		}		
		
		
		
