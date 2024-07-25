package com.maan.eway.master.service.impl;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.maan.eway.bean.BrokerCommissionDetails;
import com.maan.eway.bean.PolicyTypeMaster;
import com.maan.eway.master.req.PolicyTypeMasterChangeStatusReq;
import com.maan.eway.master.req.PolicyTypeMasterGetAllReq;
import com.maan.eway.master.req.PolicyTypeMasterGetReq;
import com.maan.eway.master.req.PolicyTypeMasterSaveReq;
import com.maan.eway.master.res.PolicyTypeMasterGetRes;
import com.maan.eway.master.service.PolicyTypeMasterService;
import com.maan.eway.repository.BrokerCommissionDetailsRepository;
import com.maan.eway.repository.PolicyTypeMasterRepository;
import com.maan.eway.res.DropDownRes;
import com.maan.eway.res.SuccessRes;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import jakarta.transaction.Transactional;
@Service
@Transactional
public class PolicyTypeMasterServiceImpl implements PolicyTypeMasterService {

	@Autowired
	private PolicyTypeMasterRepository repo;
	
	@Autowired
	private BrokerCommissionDetailsRepository broRepo;
	
	@PersistenceContext
	private EntityManager em;
	
	
	Gson json = new Gson();
	
	private Logger log = LogManager.getLogger(PolicyTypeMasterServiceImpl.class);
	
	@Override
	public List<String> validatePolicyType(PolicyTypeMasterSaveReq req) {
		List<String> error = new ArrayList<String>();
		try {
			if (StringUtils.isBlank(req.getPolicyTypeName())) {
			//	error.add(new Error("01", "Policy Type Name", "Please Enter Policy Type Name "));
				error.add("1592");
				
			} else if (req.getPolicyTypeName().length() > 100) {
			//	error.add(new Error("01", "Policy Type Name", "Please Enter Policy Type Name within 100 Characters"));
				error.add("1593");
				
			} else if (StringUtils.isBlank(req.getPolicyTypeId()) &&  StringUtils.isNotBlank(req.getInsuranceId()) && StringUtils.isNotBlank(req.getProductId())) {
				List<PolicyTypeMaster> policyList = getPolicyTypeNameExistDetails(req.getPolicyTypeName() , req.getInsuranceId() , req.getProductId());
				if (policyList.size()>0 ) {
			//		error.add(new Error("01", "PolicyTypeName", "This Policy Name Already Exist "));
					error.add("1594");
				}
			}else if (StringUtils.isNotBlank(req.getPolicyTypeId()) &&  StringUtils.isNotBlank(req.getInsuranceId()) && StringUtils.isNotBlank(req.getProductId())) {
				List<PolicyTypeMaster> policyList = getPolicyTypeNameExistDetails(req.getPolicyTypeName() , req.getInsuranceId() , req.getProductId());
				
				if (policyList.size()>0 &&  (! req.getPolicyTypeId().equalsIgnoreCase(policyList.get(0).getPolicyTypeId().toString())) ) {
				//	error.add(new Error("01", "PolicyTypeName", "This Policy Name Already Exist "));
					error.add("1594");
				}
				
			}
			
			if (StringUtils.isBlank(req.getProductId())) {
			//	error.add(new Error("01", "Product ID", "Please Select Product ID"));
				error.add("1313");
			}
			// Date Validation
			Calendar cal = new GregorianCalendar();
			Date today = new Date();
			cal.setTime(today);
			cal.add(Calendar.DAY_OF_MONTH, -1);
			cal.set(Calendar.HOUR_OF_DAY, 23);
			cal.set(Calendar.MINUTE, 50);
			today = cal.getTime();
			if (req.getEffectiveDateStart() == null) {
			//	error.add(new Error("02", "EffectiveDateStart", "Please Enter Effective Date Start "));
				error.add("1261");

			} else if (req.getEffectiveDateStart().before(today)) {
			//	error.add(new Error("02", "EffectiveDateStart", "Please Enter Effective Date Start as Future Date"));
						
				error.add("1262");
			} 
			
			if (StringUtils.isNotBlank(req.getRemarks()) && req.getRemarks().length() > 100) {
			//	error.add(new Error("04", "Remarks", "Please Enter Remarks within 100 Characters"));
				error.add("1260");
			} 	
		
			//Status Validation
			if (StringUtils.isBlank(req.getStatus())) {
			//	error.add(new Error("05", "Status", "Please Select Status  "));
				error.add("1263");
			} else if (req.getStatus().length() > 1) {
			//	error.add(new Error("05", "Status", "Please Select Valid Status - One Character Only Allwed"));
				error.add("1264");
			}else if(!("Y".equalsIgnoreCase(req.getStatus())||"N".equalsIgnoreCase(req.getStatus())||"R".equalsIgnoreCase(req.getStatus())|| "P".equalsIgnoreCase(req.getStatus()))) {
			//	error.add(new Error("05", "Status", "Please Select Valid Status - Active or Deactive or Pending or Referral "));
				error.add("1265");
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			log.info("Log Details"+e.getMessage());;
			return null;
		}
		return error;
	}
	public List<PolicyTypeMaster> getPolicyTypeNameExistDetails(String PolicyTypeName , String InsuranceId , String productId  ) {
		List<PolicyTypeMaster> list = new ArrayList<PolicyTypeMaster>();
		try {
			Date today = new Date();
			// Find Latest Record
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<PolicyTypeMaster> query = cb.createQuery(PolicyTypeMaster.class);

			// Find All
			Root<PolicyTypeMaster> b = query.from(PolicyTypeMaster.class);

			// Select
			query.select(b);

			// Effective Date Max Filter
			Subquery<Long> amendId = query.subquery(Long.class);
			Root<PolicyTypeMaster> ocpm1 = amendId.from(PolicyTypeMaster.class);
			amendId.select(cb.max(ocpm1.get("amendId")));
			Predicate a1 = cb.equal(ocpm1.get("policyTypeId"), b.get("policyTypeId"));
			Predicate a2 = cb.equal(ocpm1.get("companyId"), b.get("companyId"));
			Predicate a4 = cb.lessThanOrEqualTo(ocpm1.get("effectiveDateStart"), today);
			Predicate a5 = cb.greaterThanOrEqualTo(ocpm1.get("effectiveDateEnd"), today);
			amendId.where(a1,a2,a4,a5);

			Predicate n1 = cb.equal(b.get("amendId"), amendId);
			Predicate n2 = cb.equal(cb.lower( b.get("policyTypeName")), PolicyTypeName.toLowerCase());
			Predicate n3 = cb.equal(b.get("companyId"),InsuranceId);
			Predicate n4 = cb.equal(b.get("productId"),productId);
			query.where(n1,n2,n3,n4);
			
			// Get Result
			TypedQuery<PolicyTypeMaster> result = em.createQuery(query);
			list = result.getResultList();		
		
		} catch (Exception e) {
			e.printStackTrace();
			log.info(e.getMessage());

		}
		return list;
	}

	@Override
	public SuccessRes insertPolicyType(PolicyTypeMasterSaveReq req) {
	SimpleDateFormat sdformat = new SimpleDateFormat("dd/MM/yyyy");
	SuccessRes res = new SuccessRes();
	PolicyTypeMaster saveData = new PolicyTypeMaster();
	List<PolicyTypeMaster> list = new ArrayList<PolicyTypeMaster>();
	DozerBeanMapper dozermapper = new DozerBeanMapper();
	try {
		Integer amendId=0;
		Date startDate = req.getEffectiveDateStart() ;
		String end = "31/12/2050";
		Date endDate = sdformat.parse(end);
		long MILLIS_IN_A_DAY = 1000 * 60 * 60 * 24;
		Date oldEndDate = new Date(req.getEffectiveDateStart().getTime() - MILLIS_IN_A_DAY);
		Date entryDate = null ;
		String createdBy = "" ;
		String policyId="";
	

	if(StringUtils.isBlank(req.getPolicyTypeId())) {
		//save
		Integer totalCount = getMasterTableCount( req.getInsuranceId() , req.getProductId());
		policyId = Integer.valueOf(totalCount+1).toString();
		entryDate = new Date();
		createdBy = req.getCreatedBy();
		res.setResponse("Saved Successfully");
		res.setSuccessId(policyId);
		}
	else {
		// Update
		policyId = req.getPolicyTypeId().toString();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<PolicyTypeMaster> query = cb.createQuery(PolicyTypeMaster.class);
		//Find All
		Root<PolicyTypeMaster> b = query.from(PolicyTypeMaster.class);
		//Select
		query.select(b);
//		//Effective Date Max Filter
//		Subquery<Long> effectiveDate = query.subquery(Long.class);
//		Root<PolicyTypeMaster> ocpm1 = effectiveDate.from(PolicyTypeMaster.class);
//		effectiveDate.select(cb.greatest(ocpm1.get("effectiveDateStart")));
//		Predicate a1 = cb.equal(ocpm1.get("policyTypeId"), b.get("policyTypeId"));
//		Predicate a2 = cb.lessThanOrEqualTo(ocpm1.get("effectiveDateStart"), startDate);
//		effectiveDate.where(a1,a2);
		// Order By
		List<Order> orderList = new ArrayList<Order>();
		orderList.add(cb.desc(b.get("effectiveDateStart")));
//		//where
		Predicate n1 = cb.equal(b.get("status"),"Y");
		//Predicate n2 = cb.equal(b.get("effectiveDateStart"),effectiveDate);
		Predicate n2 = cb.equal(b.get("policyTypeId"),req.getPolicyTypeId());
		Predicate n3 = cb.equal(b.get("companyId"), req.getInsuranceId());
		Predicate n4 = cb.equal(b.get("productId"), req.getProductId());
		
		query.where(n1,n2,n3,n4).orderBy(orderList);
		// Get Result
		TypedQuery<PolicyTypeMaster> result = em.createQuery(query);
		int limit = 0 , offset = 2 ;
		result.setFirstResult(limit * offset);
		result.setMaxResults(offset);
		list = result.getResultList();
		
		if(list.size()>0) {
			Date beforeOneDay = new Date(new Date().getTime() - MILLIS_IN_A_DAY);
		
			if ( list.get(0).getEffectiveDateStart().before(beforeOneDay)  ) {
				amendId = list.get(0).getAmendId() + 1 ;
				entryDate = new Date() ;
				createdBy = req.getCreatedBy();
					PolicyTypeMaster lastRecord = list.get(0);
					lastRecord.setEffectiveDateEnd(oldEndDate);
					repo.saveAndFlush(lastRecord);
				
			} else {
				amendId = list.get(0).getAmendId() ;
				entryDate = list.get(0).getEntryDate() ;
				createdBy = list.get(0).getCreatedBy();
				saveData = list.get(0) ;
				if (list.size()>1 ) {
					PolicyTypeMaster lastRecord = list.get(1);
					lastRecord.setEffectiveDateEnd(oldEndDate);
					repo.saveAndFlush(lastRecord);
				}
			
		    }
		}
			res.setResponse("Updated Successfully");
		res.setSuccessId(policyId);		
	}
	dozermapper.map(req,saveData);
	saveData.setEffectiveDateStart(startDate);
	saveData.setEffectiveDateEnd(endDate);
	saveData.setCreatedBy(createdBy);
	saveData.setStatus(req.getStatus());
	saveData.setCompanyId(req.getInsuranceId());
	saveData.setEntryDate(entryDate);
	saveData.setUpdatedDate(new Date());
	saveData.setUpdatedBy(req.getCreatedBy());
	saveData.setAmendId(amendId);saveData.setPolicyTypeId(Integer.valueOf(policyId));
	repo.saveAndFlush(saveData);
	log.info("Saved Details is --->"+ json.toJson(saveData));
	}
	catch(Exception e) {
		e.printStackTrace();
		log.info("Log Details"+ e.getMessage());
		return null;
	}
	return res;
	}
	
	public Integer getMasterTableCount(String companyId , String productId) {
		Integer data =0;
		try {
			List<PolicyTypeMaster> list = new ArrayList<PolicyTypeMaster>();
			// Find Latest Record 
			CriteriaBuilder cb= em.getCriteriaBuilder();
		CriteriaQuery<PolicyTypeMaster>	query = cb.createQuery(PolicyTypeMaster.class);
		//Find All
		Root<PolicyTypeMaster> b = query.from(PolicyTypeMaster.class);
		//Select
		query.select(b);
		// Effective Date Max Filter
		Subquery<Timestamp> effectiveDate = query.subquery(Timestamp.class);
		Root<PolicyTypeMaster> ocpm1 = effectiveDate.from(PolicyTypeMaster.class);
		effectiveDate.select(cb.greatest(ocpm1.get("effectiveDateStart")));
		Predicate a1 = cb.equal(ocpm1.get("policyTypeId"),b.get("policyTypeId"));
		Predicate a2 = cb.equal(ocpm1.get("companyId"), b.get("companyId"));
		Predicate a3 = cb.equal(ocpm1.get("productId"), b.get("productId"));
		
		effectiveDate.where(a1,a2,a3);

		// Order By
		List<Order> orderList = new ArrayList<Order>();
		orderList.add(cb.desc(b.get("policyTypeId")));
		
		Predicate n1 = cb.equal(b.get("effectiveDateStart"),effectiveDate);
		Predicate n2 = cb.equal(b.get("companyId"), companyId);
		Predicate n3 = cb.equal(b.get("productId"), productId);
		
		query.where(n1,n2,n3).orderBy(orderList);
	
		// Get Result
		TypedQuery<PolicyTypeMaster> result = em.createQuery(query);
		int limit = 0 , offset = 1 ;
		result.setFirstResult(limit * offset);
		result.setMaxResults(offset);
		list = result.getResultList();
		data = list.size() > 0 ? list.get(0).getPolicyTypeId() : 0 ;}
		catch(Exception e) {
			e.printStackTrace();
			log.info("Log Details"+ e.getMessage());
		}
		return data;
	}
	
	
	@Override
	public PolicyTypeMasterGetRes getPolicyType(PolicyTypeMasterGetReq req) {
		PolicyTypeMasterGetRes res = new PolicyTypeMasterGetRes();
		DozerBeanMapper mapper = new DozerBeanMapper();
		try {
			Date today = new Date();
			Calendar cal = new GregorianCalendar();
			cal.setTime(today);
			cal.set(Calendar.HOUR_OF_DAY, 23);
			cal.set(Calendar.MINUTE, 1);
			today = cal.getTime();

			List<PolicyTypeMaster> list = new ArrayList<PolicyTypeMaster>();

			// Find Latest Record
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<PolicyTypeMaster> query = cb.createQuery(PolicyTypeMaster.class);

			// Find All
			Root<PolicyTypeMaster> b = query.from(PolicyTypeMaster.class);

			// Select
			query.select(b);
			// Amend ID Max Filter
			Subquery<Long> amendId = query.subquery(Long.class);
			Root<PolicyTypeMaster> ocpm1 = amendId.from(PolicyTypeMaster.class);
			amendId.select(cb.max(ocpm1.get("amendId")));
			Predicate a1 = cb.equal(ocpm1.get("policyTypeId"), b.get("policyTypeId"));
			Predicate a2 = cb.equal(ocpm1.get("companyId"), b.get("companyId"));
			Predicate a3 = cb.equal(ocpm1.get("productId"),b.get("productId"));
			Predicate a4 = cb.lessThanOrEqualTo(ocpm1.get("effectiveDateStart"), today);

			query.where(a1, a2,a3,a4);

			// Order By
			List<Order> orderList = new ArrayList<Order>();
			orderList.add(cb.asc(b.get("policyTypeId")));

			// Where
			Predicate n1 = cb.equal(b.get("amendId"), amendId);
			Predicate n2 = cb.equal(b.get("companyId"), req.getInsuranceId());
			Predicate n3 = cb.equal(b.get("productId"), req.getProductId());
			Predicate n4 = cb.equal(b.get("policyTypeId"), req.getPolicyTypeId());
			
			query.where(n1,n2,n4,n3).orderBy(orderList);
			
			// Get Result
			TypedQuery<PolicyTypeMaster> result = em.createQuery(query);

			list = result.getResultList();
			list = list.stream().filter(distinctByKey(o -> Arrays.asList(o.getPolicyTypeId()))).collect(Collectors.toList());
			list.sort(Comparator.comparing(PolicyTypeMaster :: getPolicyTypeName ));
			
			res = mapper.map(list.get(0), PolicyTypeMasterGetRes.class);
			res.setPolicyTypeId(list.get(0).getPolicyTypeId().toString());
			res.setEntryDate(list.get(0).getEntryDate());
			res.setEffectiveDateStart(list.get(0).getEffectiveDateStart());
			res.setEffectiveDateEnd(list.get(0).getEffectiveDateEnd());
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Exception is ---> " + e.getMessage());
			return null;
		}
		return res;
	}


	private static <T> java.util.function.Predicate<T> distinctByKey(java.util.function.Function<? super T, ?> keyExtractor) {
	    Map<Object, Boolean> seen = new ConcurrentHashMap<>();
	    return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
	}

	@Override
	public List<PolicyTypeMasterGetRes> getallPolicyType(PolicyTypeMasterGetAllReq req) {
		List<PolicyTypeMasterGetRes> resList = new ArrayList<PolicyTypeMasterGetRes>();
		DozerBeanMapper mapper = new DozerBeanMapper();
		try {
		
			List<PolicyTypeMaster> list = new ArrayList<PolicyTypeMaster>();
			// Pagination
			
			// Find Latest Record
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<PolicyTypeMaster> query = cb.createQuery(PolicyTypeMaster.class);

			// Find All
			Root<PolicyTypeMaster> b = query.from(PolicyTypeMaster.class);

			// Select
			query.select(b);

			// Effective Date Max Filter
			Subquery<Long> amendId = query.subquery(Long.class);
			Root<PolicyTypeMaster> ocpm1 = amendId.from(PolicyTypeMaster.class);
			amendId.select(cb.max(ocpm1.get("amendId")));
			Predicate a1 = cb.equal(ocpm1.get("policyTypeId"), b.get("policyTypeId"));
			Predicate a3 = cb.equal(ocpm1.get("companyId"), b.get("companyId"));
			Predicate a4 = cb.equal(ocpm1.get("productId"),b.get("productId"));

			amendId.where(a1,a3,a4);

			// Order By
			List<Order> orderList = new ArrayList<Order>();
			orderList.add(cb.asc(b.get("policyTypeId")));

			// Where
			Predicate n1 = cb.equal(b.get("amendId"), amendId);
			Predicate n2 = cb.equal(b.get("companyId"), req.getInsuranceId());
			Predicate n3 = cb.equal(b.get("productId"), req.getProductId());
		
			query.where(n1,n2,n3).orderBy(orderList);
			
			// Get Result
			TypedQuery<PolicyTypeMaster> result = em.createQuery(query);
			list = result.getResultList();
			list = list.stream().filter(distinctByKey(o -> Arrays.asList(o.getPolicyTypeId()))).collect(Collectors.toList());
			list.sort(Comparator.comparing(PolicyTypeMaster :: getPolicyTypeName ));
			
			// Map
			for (PolicyTypeMaster data : list) {
				PolicyTypeMasterGetRes res = new PolicyTypeMasterGetRes();

				res = mapper.map(data, PolicyTypeMasterGetRes.class);
				res.setPolicyTypeId(data.getPolicyTypeId().toString());
				resList.add(res);
			}

		} catch (Exception e) {
			e.printStackTrace();
			log.info(e.getMessage());
			return null;

		}
		return resList;
	}

	@Override
	public List<PolicyTypeMasterGetRes> getallactivePolicyType(PolicyTypeMasterGetAllReq req) {
		List<PolicyTypeMasterGetRes> resList = new ArrayList<PolicyTypeMasterGetRes>();
		DozerBeanMapper mapper = new DozerBeanMapper();
		try {
			
			List<PolicyTypeMaster> list = new ArrayList<PolicyTypeMaster>();
			
			// Find Latest Record
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<PolicyTypeMaster> query = cb.createQuery(PolicyTypeMaster.class);

			// Find All
			Root<PolicyTypeMaster> b = query.from(PolicyTypeMaster.class);

			// Select
			query.select(b);

			// Effective Date Max Filter
			Subquery<Long> amendId = query.subquery(Long.class);
			Root<PolicyTypeMaster> ocpm1 = amendId.from(PolicyTypeMaster.class);
			amendId.select(cb.max(ocpm1.get("amendId")));
			Predicate a1 = cb.equal(ocpm1.get("policyTypeId"), b.get("policyTypeId"));
			Predicate a3 = cb.equal(ocpm1.get("companyId"), b.get("companyId"));
			Predicate a4 = cb.equal(ocpm1.get("productId"),b.get("productId"));

			amendId.where(a1,a3,a4);

			// Order By
			List<Order> orderList = new ArrayList<Order>();
			orderList.add(cb.asc(b.get("policyTypeId")));

			// Where
			Predicate n1 = cb.equal(b.get("amendId"), amendId);
			Predicate n2 = cb.equal(b.get("companyId"), req.getInsuranceId());
			Predicate n3 = cb.equal(b.get("productId"), req.getProductId());
			Predicate n4 = cb.equal(b.get("status"), "Y");
		
			query.where(n1,n2,n3,n4).orderBy(orderList);
			
			// Get Result
			TypedQuery<PolicyTypeMaster> result = em.createQuery(query);
			list = list.stream().filter(distinctByKey(o -> Arrays.asList(o.getPolicyTypeId()))).collect(Collectors.toList());
			list.sort(Comparator.comparing(PolicyTypeMaster :: getPolicyTypeName ));
			list = result.getResultList();

			// Map
			for (PolicyTypeMaster data : list) {
				PolicyTypeMasterGetRes res = new PolicyTypeMasterGetRes();

				res = mapper.map(data, PolicyTypeMasterGetRes.class);
				res.setPolicyTypeId(data.getPolicyTypeId().toString());
				resList.add(res);
			}

		} catch (Exception e) {
			e.printStackTrace();
			log.info(e.getMessage());
			return null;

		}
		return resList;
	}

	@Override
	public List<DropDownRes> getPolicyTypeMasterDropdown( PolicyTypeMasterGetAllReq req ) {
		List<DropDownRes> resList = new ArrayList<DropDownRes>();
		try {
			Date today  = new Date();
			Calendar cal = new GregorianCalendar();
			cal.setTime(today);
			cal.set(Calendar.HOUR_OF_DAY, 23);
			cal.set(Calendar.MINUTE, 1);
			today = cal.getTime();
			cal.set(Calendar.HOUR_OF_DAY, 1);
			cal.set(Calendar.MINUTE, 1);
			Date todayEnd = cal.getTime();
			
			List<PolicyTypeMaster> list = new ArrayList<PolicyTypeMaster>();
			
		
			if(StringUtils.isNotBlank(req.getLoginId()) && ! req.getLoginId().equalsIgnoreCase("guest") ) {
				
				List<BrokerCommissionDetails> broker = broRepo.findByCompanyIdAndLoginIdAndProductId(req.getInsuranceId(), req.getLoginId(), req.getProductId());
				if(broker.size()>0) {
					
				CriteriaBuilder cb = em.getCriteriaBuilder();
				CriteriaQuery<PolicyTypeMaster> query=  cb.createQuery(PolicyTypeMaster.class);


				// Find All
				Root<PolicyTypeMaster> c = query.from(PolicyTypeMaster.class);
				Root<BrokerCommissionDetails> b = query.from(BrokerCommissionDetails.class);
				//Select
				query.select(c);
				// Order By
				List<Order> orderList = new ArrayList<Order>();
				orderList.add(cb.asc(c.get("policyTypeId")));

				// Effective Date Start Max Filter
				Subquery<Timestamp> effectiveDate = query.subquery(Timestamp.class);
				Root<PolicyTypeMaster> ocpm1 = effectiveDate.from(PolicyTypeMaster.class);
				effectiveDate.select(cb.greatest(ocpm1.get("effectiveDateStart")));
				Predicate a1 = cb.equal(c.get("policyTypeId"),ocpm1.get("policyTypeId"));
				Predicate a2 = cb.lessThanOrEqualTo(ocpm1.get("effectiveDateStart"), today);
				Predicate a5 = cb.equal(c.get("companyId"),ocpm1.get("companyId"));
				Predicate a7 = cb.equal(c.get("productId"),ocpm1.get("productId"));
				effectiveDate.where(a1,a2,a5,a7);
				// Effective Date End Max Filter
				Subquery<Timestamp> effectiveDate2 = query.subquery(Timestamp.class);
				Root<PolicyTypeMaster> ocpm2 = effectiveDate2.from(PolicyTypeMaster.class);
				effectiveDate2.select(cb.greatest(ocpm2.get("effectiveDateEnd")));
				Predicate a3 = cb.equal(c.get("policyTypeId"),ocpm2.get("policyTypeId"));
				Predicate a4 = cb.greaterThanOrEqualTo(ocpm2.get("effectiveDateEnd"), todayEnd);
				Predicate a6 = cb.equal(c.get("companyId"),ocpm2.get("companyId"));
				Predicate a8 = cb.equal(c.get("productId"),ocpm2.get("productId"));
				effectiveDate2.where(a3,a4,a6,a8);
				
				Subquery<Timestamp> effectiveDate4 = query.subquery(Timestamp.class);
				Root<BrokerCommissionDetails> ocpm4 = effectiveDate4.from(BrokerCommissionDetails.class);
				effectiveDate4.select(cb.greatest(ocpm4.get("effectiveDateStart")));
				Predicate b1 = cb.equal(ocpm4.get("productId"), b.get("productId"));
				Predicate b2 = cb.equal(ocpm4.get("companyId"), b.get("companyId"));
				Predicate b3 = cb.lessThanOrEqualTo(ocpm4.get("effectiveDateStart"),today);
				Predicate b7 = cb.equal(ocpm4.get("loginId"), b.get("loginId"));
				Predicate b10 = cb.equal(ocpm4.get("policyType"), b.get("policyType"));
				effectiveDate4.where(b1,b2,b3,b7,b10);
		
				// Effective Date End
				Subquery<Timestamp> effectiveDate5 = query.subquery(Timestamp.class);
				Root<BrokerCommissionDetails> ocpm5 = effectiveDate5.from(BrokerCommissionDetails.class);
				effectiveDate5.select(cb.greatest(ocpm5.get("effectiveDateEnd")));
				Predicate b4 = cb.equal(b.get("productId"),ocpm5.get("productId") );
				Predicate b5 = cb.equal(ocpm5.get("companyId"), b.get("companyId"));
				Predicate b6 = cb.greaterThanOrEqualTo(ocpm5.get("effectiveDateEnd"), todayEnd);
				Predicate b8 = cb.equal(ocpm5.get("loginId"), b.get("loginId"));
				Predicate b9 = cb.equal(ocpm5.get("policyType"), b.get("policyType"));
				effectiveDate5.where(b4,b5,b6,b8, b9);
				// Where
				Predicate n1 = cb.equal(c.get("status"),"Y");
				Predicate n11 = cb.equal(c.get("status"),"R");
				Predicate n12 = cb.or(n1,n11);
				Predicate n2 = cb.equal(c.get("effectiveDateStart"),effectiveDate);
				Predicate n3 = cb.equal(c.get("effectiveDateEnd"),effectiveDate2);	
				Predicate n4 = cb.equal(c.get("companyId"),req.getInsuranceId());
				Predicate n5 = cb.equal(c.get("productId"),req.getProductId());

				Predicate m1 = cb.equal(b.get("loginId"),req.getLoginId());
				Predicate m2 = cb.equal(b.get("productId"),req.getProductId());  //start<=sysdate<=end

				Predicate m3 = cb.equal(b.get("status"),"Y");
				Predicate m4 = cb.lessThanOrEqualTo(b.get("effectiveDateStart"), effectiveDate4);
				Predicate m5 = cb.greaterThanOrEqualTo(b.get("effectiveDateEnd"), effectiveDate5);
				Predicate m6 = cb.equal(b.get("policyType"), c.get("policyTypeId").as(String.class));


				query.where(n12,n2,n3,n4,n5,m1,m2,m3,m4,m5,m6).orderBy(orderList);
				// Get Result
				TypedQuery<PolicyTypeMaster> result = em.createQuery(query);
				list = result.getResultList();
				for (PolicyTypeMaster data : list) {
					// Response
					DropDownRes res = new DropDownRes();
					res.setCode(data.getPolicyTypeId().toString());
					res.setCodeDesc(data.getPolicyTypeName());
					res.setCodeDescLocal(data.getPolicyTypeNameLocal());
					res.setStatus(data.getStatus());
					resList.add(res);
				}
				
				}else
					resList = getPolicyTypeMasterDrop(req);
				
			} else {
				
				resList = getPolicyTypeMasterDrop(req);
			}
			
			
		}
			catch(Exception e) {
				e.printStackTrace();
				log.info("Exception is --->"+e.getMessage());
				return null;
				}
			return resList;
		}
	
	

		public List<DropDownRes> getPolicyTypeMasterDrop(PolicyTypeMasterGetAllReq req) {
			List<DropDownRes> resList = new ArrayList<DropDownRes>();
			try {
				Date today = new Date();
				Calendar cal = new GregorianCalendar();
				cal.setTime(today);
				cal.set(Calendar.HOUR_OF_DAY, 23);
				cal.set(Calendar.MINUTE, 1);
				today = cal.getTime();
				cal.set(Calendar.HOUR_OF_DAY, 1);
				cal.set(Calendar.MINUTE, 1);
				Date todayEnd = cal.getTime();

				List<PolicyTypeMaster> list = new ArrayList<PolicyTypeMaster>();

				// Criteria
				CriteriaBuilder cb = em.getCriteriaBuilder();
				CriteriaQuery<PolicyTypeMaster> query = cb.createQuery(PolicyTypeMaster.class);

				// Find All
				Root<PolicyTypeMaster> c = query.from(PolicyTypeMaster.class);
				// Select
				query.select(c);
				// Order By
				List<Order> orderList = new ArrayList<Order>();
				orderList.add(cb.asc(c.get("policyTypeId")));

				// Effective Date Start Max Filter
				Subquery<Timestamp> effectiveDate = query.subquery(Timestamp.class);
				Root<PolicyTypeMaster> ocpm1 = effectiveDate.from(PolicyTypeMaster.class);
				effectiveDate.select(cb.greatest(ocpm1.get("effectiveDateStart")));
				Predicate a1 = cb.equal(c.get("policyTypeId"), ocpm1.get("policyTypeId"));
				Predicate a2 = cb.lessThanOrEqualTo(ocpm1.get("effectiveDateStart"), today);
				Predicate a5 = cb.equal(c.get("companyId"), ocpm1.get("companyId"));
				Predicate a7 = cb.equal(c.get("productId"), ocpm1.get("productId"));
				effectiveDate.where(a1, a2, a5, a7);
				// Effective Date End Max Filter
				Subquery<Timestamp> effectiveDate2 = query.subquery(Timestamp.class);
				Root<PolicyTypeMaster> ocpm2 = effectiveDate2.from(PolicyTypeMaster.class);
				effectiveDate2.select(cb.greatest(ocpm2.get("effectiveDateEnd")));
				Predicate a3 = cb.equal(c.get("policyTypeId"), ocpm2.get("policyTypeId"));
				Predicate a4 = cb.greaterThanOrEqualTo(ocpm2.get("effectiveDateEnd"), todayEnd);
				Predicate a6 = cb.equal(c.get("companyId"), ocpm2.get("companyId"));
				Predicate a8 = cb.equal(c.get("productId"), ocpm2.get("productId"));
				effectiveDate2.where(a3, a4, a6, a8);
				// Where
				Predicate n1 = cb.equal(c.get("status"), "Y");
				Predicate n11 = cb.equal(c.get("status"), "R");
				Predicate n12 = cb.or(n1, n11);
				Predicate n2 = cb.equal(c.get("effectiveDateStart"), effectiveDate);
				Predicate n3 = cb.equal(c.get("effectiveDateEnd"), effectiveDate2);
				Predicate n4 = cb.equal(c.get("companyId"), req.getInsuranceId());
				Predicate n5 = cb.equal(c.get("productId"), req.getProductId());

				query.where(n12, n2, n3, n4, n5).orderBy(orderList);
				// Get Result
				TypedQuery<PolicyTypeMaster> result = em.createQuery(query);
				list = result.getResultList();

				for (PolicyTypeMaster data : list) {
					// Response
					DropDownRes res = new DropDownRes();
					res.setCode(data.getPolicyTypeId().toString());
					res.setCodeDesc(data.getPolicyTypeName());
					res.setStatus(data.getStatus());
					resList.add(res);
				}
			} catch (Exception e) {
				e.printStackTrace();
				log.info("Exception is --->" + e.getMessage());
				return null;
			}
			return resList;
		}
		@Override
		public SuccessRes changeStatusOfPolicyType(PolicyTypeMasterChangeStatusReq req) {
			SimpleDateFormat sdformat = new SimpleDateFormat("dd/MM/yyyy");
			SuccessRes res = new SuccessRes();
			PolicyTypeMaster saveData = new PolicyTypeMaster();
			List<PolicyTypeMaster> list = new ArrayList<PolicyTypeMaster>();
			DozerBeanMapper dozermapper = new DozerBeanMapper();
			try {
				Integer amendId = 0;
				Date startDate = req.getEffectiveDateStart();
				String end = "31/12/2050";
				Date endDate = sdformat.parse(end);
				long MILLIS_IN_A_DAY = 1000 * 60 * 60 * 24;
				Date oldEndDate = new Date(req.getEffectiveDateStart().getTime() - MILLIS_IN_A_DAY);
				Date entryDate = null;
				String createdBy = "";
				String policyId = "";

				// Update
				policyId = req.getPolicyTypeId().toString();
				CriteriaBuilder cb = em.getCriteriaBuilder();
				CriteriaQuery<PolicyTypeMaster> query = cb.createQuery(PolicyTypeMaster.class);
				// Find All
				Root<PolicyTypeMaster> b = query.from(PolicyTypeMaster.class);
				// Select
				query.select(b);
				Subquery<Long> amendId2 = query.subquery(Long.class);
				Root<PolicyTypeMaster> ocpm1 = amendId2.from(PolicyTypeMaster.class);
				amendId2.select(cb.max(ocpm1.get("amendId")));
				Predicate a1 = cb.equal(ocpm1.get("policyTypeId"), b.get("policyTypeId"));
				Predicate a2 = cb.equal(ocpm1.get("companyId"), b.get("companyId"));
				Predicate a3 = cb.equal(ocpm1.get("productId"),b.get("productId"));
				amendId2.where(a1, a2,a3);
				//Orderby
				List<Order> orderList = new ArrayList<Order>();
				orderList.add(cb.desc(b.get("effectiveDateStart")));
				//Where
				Predicate n1 = cb.equal(b.get("policyTypeId"), req.getPolicyTypeId());
				Predicate n2 = cb.equal(b.get("companyId"), req.getInsuranceId());
				Predicate n3 = cb.equal(b.get("productId"), req.getProductId());
				Predicate n4 = cb.equal(b.get("amendId"),amendId2);
				
				query.where(n1,n2,n3,n4).orderBy(orderList);
	
				// Get Result
				TypedQuery<PolicyTypeMaster> result = em.createQuery(query);
				int limit = 0, offset = 2;
				result.setFirstResult(limit * offset);
				result.setMaxResults(offset);
				list = result.getResultList();

				if (list.size() > 0) {
					Date beforeOneDay = new Date(new Date().getTime() - MILLIS_IN_A_DAY);

					if (list.get(0).getEffectiveDateStart().before(beforeOneDay)) {
						amendId = list.get(0).getAmendId() + 1;
						entryDate = new Date();
						createdBy = req.getCreatedBy();
						PolicyTypeMaster lastRecord = list.get(0);
						lastRecord.setEffectiveDateEnd(oldEndDate);
						repo.saveAndFlush(lastRecord);

					} else {
						amendId = list.get(0).getAmendId();
						entryDate = list.get(0).getEntryDate();
						createdBy = list.get(0).getCreatedBy();
						saveData = list.get(0);
						if (list.size() > 1) {
							PolicyTypeMaster lastRecord = list.get(1);
							lastRecord.setEffectiveDateEnd(oldEndDate);
							repo.saveAndFlush(lastRecord);
						}

					}
				}
				res.setResponse("Updated Successfully");
				res.setSuccessId(policyId);

				dozermapper.map(list.get(0), saveData);
				saveData.setEffectiveDateStart(startDate);
				saveData.setEffectiveDateEnd(endDate);
				saveData.setCreatedBy(createdBy);
				saveData.setStatus(req.getStatus());
				saveData.setCompanyId(req.getInsuranceId());
				saveData.setEntryDate(entryDate);
				saveData.setUpdatedDate(new Date());
				saveData.setUpdatedBy(req.getCreatedBy());
				saveData.setAmendId(amendId);
				saveData.setPolicyTypeId(Integer.valueOf(policyId));
				repo.saveAndFlush(saveData);
				log.info("Saved Details is --->" + json.toJson(saveData));
				res.setResponse("Status Changed");
				res.setSuccessId(policyId);
			} catch (Exception e) {
				e.printStackTrace();
				log.info("Exception is --->" + e.getMessage());
				return null;
			}
			return res;
		}
}
