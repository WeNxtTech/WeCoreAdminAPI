package com.maan.eway.master.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;

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
	public List<String> validateTravelPolicyType(TravelPolicyTypeSaveReq req) {
		List<String> error = new ArrayList<String>();
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
//			error.add(new Error("O1","Policy Type Id","Please Enter Policy Type Id"));
			error.add("1990");
		}
		
		if(req.getPlanTypeId()==null)
		{
//			error.add(new Error("O2","Plan Type Id","Please Enter Plan Type Id"));
			error.add("1993");
		}

		if(req.getCompanyId()==null)
		{
//			error.add(new Error("O6","Company Id","Please Enter Company Id"));
			error.add("1255");
		}
		
		if(req.getProductId()==null)
		{
//			error.add(new Error("O7","Product Id","Please Enter Product Id"));
			error.add("1313");
		}
		
		if(req.getBranchCode()==null)
		{
//			error.add(new Error("O8","Branch Code","Please Enter Branch Code"));
			error.add("1256");
		}
		
		if(StringUtils.isBlank(req.getPolicyTypeDesc()))
		{
//			error.add(new Error("O9","Policy Type Description","Please Enter Policy Type Description"));
			error.add("1994");
		}
		else if(req.getPolicyTypeDesc().length()>100)
		{
//			error.add(new Error("O9","Policy Type Description","Please Enter Policy Type Description below 100 character"));
			error.add("1995");
		}
		
		if(StringUtils.isBlank(req.getPlanTypeDesc()))
		{
//			error.add(new Error("10","Plan Type Description","Please Enter Plan Type Description"));
			error.add("1996");
		}
		else if(req.getPlanTypeDesc().length()>100)
		{
//			error.add(new Error("10","Plan Type Description","Please Enter Plan Type Description below 100 character"));
			error.add("1997");
		}
		
		if(StringUtils.isBlank(req.getCoverDesc()))
		{
//			error.add(new Error("11","Cover Description","Please Enter Cover Description"));
			error.add("1998");
		}
		else if(req.getCoverDesc().length()>100)
		{
//			error.add(new Error("11","Cover Description","Please Enter Cover Description below 100 character"));
			error.add("1999");
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
//			error.add(new Error("16", "EffectiveStartDate", "Please Enter Effective Start Date"));
			error.add("1261");

		} else if (req.getEffectiveDateStart().before(today)) {
//			error.add(new Error("16", "EffectiveStartDate", "Please Enter Effective Start Date as Future Date"));
			error.add("1262");
		}

		
		if (StringUtils.isBlank(req.getCoverStatus())) {
//			error.add(new Error("21", "Cover Status", "Please Select Cover Status"));
			error.add("2000");
		} else if (req.getCoverStatus().length() > 1) {
//			error.add(new Error("21", "Cover Status", "Please Select Valid Cover Status - 1 Character Only Allowed"));
			error.add("2001");
		}else if(!("Y".equalsIgnoreCase(req.getCoverStatus())||"N".equalsIgnoreCase(req.getCoverStatus()))) {
//			error.add(new Error("21", "Cover Status", "Please Select Valid Cover Status - Active or Deactive or Pending or Referral "));
			error.add("2002");
		}
		
		if (StringUtils.isBlank(req.getCreatedBy())) {
//			error.add(new Error("06", "CreatedBy", "Please Enter CreatedBy"));
			error.add("1270");
		}else if (req.getCreatedBy().length() > 50) {
//			error.add(new Error("06", "CreatedBy", "Please Enter CreatedBy within 100 Characters"));
			error.add("1271");
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
			Predicate a6 = cb.equal(ocpm1.get("coverId"), b.get("coverId"));
			Predicate a5 = cb.equal(ocpm1.get("subCoverId"), "0");
	
			amendId.where(a1,a2,a3,a4,a7,a6,a5);

			Predicate n1 = cb.equal(b.get("amendId"),amendId);
			Predicate n2 = cb.equal(b.get("productId"), req.getProductId() );	
			Predicate n3 = cb.equal(b.get("companyId"), req.getCompanyId() );		
			Predicate n4 = cb.equal(b.get("branchCode"), req.getBranchCode());
			Predicate n6 =  cb.equal(b.get("policyTypeId"), req.getPolicyTypeId()); 
			Predicate n7 =  cb.equal(b.get("planTypeId"), req.getPlanTypeId()); 
//			Predicate n8 = cb.lessThanOrEqualTo(b.get("effectiveStartdate"), today1);
//			Predicate n9 = cb.greaterThanOrEqualTo(b.get("effectiveEnddate"), todayEnd1);
			Predicate n10 = cb.equal(b.get("subCoverId"), "0");
		
			query.where(n1, n2,n3,n4,n6, n7,n10);
			
			TypedQuery<TravelPolicyType> result = em.createQuery(query);
			list = result.getResultList();
			
			if(list.size()>0) {
				
				List<TravelPolicyType> dup = list.stream().filter(o -> (o.getCoverDesc()==null?"":o.getCoverDesc().trim().replaceAll("\\s+", "") ).equalsIgnoreCase(req.getCoverDesc().trim().replaceAll("\\s+", "") )).collect(Collectors.toList());
				if(dup.size()>0) {
//					error.add(new Error("02", "CoverDesc","Cover '" + req.getCoverDesc() + "' Already Exists"));
					error.add("2003");		 
					
				}
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
			Integer id=0;
			List<TravelPolicyType> old = repository.findByProductIdAndBranchCodeAndCompanyIdAndPolicyTypeIdAndPlanTypeIdOrderByCoverIdDesc(
					Integer.valueOf(req.getProductId()), req.getBranchCode(), req.getCompanyId(),
					Integer.valueOf(req.getPolicyTypeId()),Integer.valueOf(req.getPlanTypeId()) );
			
			if(StringUtils.isBlank(req.getCoverId())) {   //Insert
				if(old.size()>0 && old!=null) {
				id= old.get(0).getCoverId()==null?0:old.get(0).getCoverId();
				}
				coverId = id + 1;
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
				Predicate n8 =  cb.equal(b.get("subCoverId"), "0"); 
			
				query.where(n2,n3,n4, n5, n6, n7, n8) ;
			
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
		List<TravelPolicyType> list1 = new ArrayList<TravelPolicyType>();

		//Find Latest Record
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<TravelPolicyType> query = cb.createQuery(TravelPolicyType.class);

		//Find All
		Root<TravelPolicyType> b = query.from(TravelPolicyType.class);

		//Select
		query.select(b);

		Subquery<Long> maxAmendId = query.subquery(Long.class);
		Root<TravelPolicyType> ocpm1 = maxAmendId.from(TravelPolicyType.class);
		maxAmendId.select(cb.max(ocpm1.get("amendId")));
		Predicate a1 = cb.equal(ocpm1.get("companyId"), b.get("companyId"));
		Predicate a2 = cb.equal(ocpm1.get("productId"), b.get("productId"));
		Predicate a3 = cb.equal(ocpm1.get("policyTypeId"), b.get("policyTypeId"));
		Predicate a4 = cb.equal(ocpm1.get("planTypeId"), b.get("planTypeId"));
		Predicate a5 = cb.equal(ocpm1.get("branchCode"), b.get("branchCode"));
		Predicate a6 = cb.equal(ocpm1.get("coverId"), b.get("coverId"));
		Predicate a7 = cb.equal(ocpm1.get("subCoverId"), "0");
		maxAmendId.where(a1,a2,a3,a4,a5,a6, a7);
	

		// Order By
		List<Order> orderList = new ArrayList<Order>();
		orderList.add(cb.desc(b.get("coverId")));
		orderList.add(cb.desc(b.get("subCoverId")));


		Predicate n1 = cb.equal(b.get("companyId"), req.getCompanyId());
		Predicate n2 = cb.equal(b.get("productId"), req.getProductId());
		Predicate n3 = cb.equal(b.get("policyTypeId"), req.getPolicyTypeId());
		Predicate n4 = cb.equal(b.get("planTypeId"), req.getPlanTypeId());
		Predicate n5 = cb.equal(b.get("branchCode"), req.getBranchCode());
		Predicate n6 = cb.equal(b.get("amendId"),maxAmendId);
//		Predicate n8 = cb.lessThanOrEqualTo(b.get("effectiveStartdate"), today);
//		Predicate n9 = cb.greaterThanOrEqualTo(b.get("effectiveEnddate"), todayEnd);
		Predicate n10 = cb.equal(b.get("subCoverId"), "0");

		query.where(n1, n2,n3,n4,n6, n5, n10).orderBy(orderList) ;

		TypedQuery<TravelPolicyType> result = em.createQuery(query);
		result.setFirstResult(req.getLimit() * req.getOffset());
		result.setMaxResults( req.getOffset());
		list = result.getResultList();
		list = list.stream().filter(distinctByKey(o -> Arrays.asList(o.getCoverId()))).collect(Collectors.toList());
		
		TypedQuery<TravelPolicyType> result1 = em.createQuery(query);
		list1 = result1.getResultList();
		list1 = list1.stream().filter(distinctByKey(o -> Arrays.asList(o.getCoverId()))).collect(Collectors.toList());

		if(list.size()>0) {
			for (TravelPolicyType data : list) {
				
				TravelPolicyTypeGetRes res = new TravelPolicyTypeGetRes();
				
				res = mapper.map(data, TravelPolicyTypeGetRes.class);
				res.setPolicyTypeId(data.getPolicyTypeId().toString());
				res.setPolicyTypeDesc(data.getPolicyTypeDesc());
				res.setPlanTypeId(data.getPlanTypeId().toString());
				res.setPlanTypeDesc(data.getPlanTypeDesc());
				res.setCoverId(data.getCoverId().toString());
				res.setSubCoverId(data.getSubCoverId().toString());
				res.setAmendId(data.getAmendId().toString());
				res.setCompanyId(data.getCompanyId().toString());
				res.setProductId(data.getProductId().toString());
				res.setBranchCode(data.getBranchCode());
				res.setPolicyTypeDesc(data.getPolicyTypeDesc());
				res.setPlanTypeDesc(data.getPlanTypeDesc());
				res.setCoverDesc(data.getCoverDesc());
//				res.setSubCoverDesc(data.getSubCoverDesc());
//				res.setCurrency(data.getCurrency());
//				res.setSumInsured(data.getSumInsured());
//				res.setExcessAmt(data.getExcessAmt());
//				res.setEntryDate(data.getEntryDate());
//				res.setStatus(data.getStatus());
				res.setRemarks(data.getRemarks()==null?"":data.getRemarks());
				res.setEffectiveStartdate(data.getEffectiveStartdate());
				res.setEffectiveEnddate(data.getEffectiveEnddate());
				res.setUpdatedDate(data.getUpdatedDate());
				res.setCoverStatus(data.getCoverStatus()==null?"":data.getCoverStatus());
				resList.add(res);
				}		
				res1.setTotalCount(list1.size());
				res1.setTravelPolicyType(resList);

			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return res1;
		}
	

	private static <T> java.util.function.Predicate<T> distinctByKey(java.util.function.Function<? super T, ?> keyExtractor) {
	    Map<Object, Boolean> seen = new ConcurrentHashMap<>();
	    return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
	}

}

