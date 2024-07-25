package com.maan.eway.master.service.impl;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.maan.eway.bean.CountryTaxSetup;
import com.maan.eway.master.req.CountryChangeStatusReq;
import com.maan.eway.master.req.CountryTaxDropDownReq;
import com.maan.eway.master.req.CountryTaxGetAllReq;
import com.maan.eway.master.req.CountryTaxGetReq;
import com.maan.eway.master.req.CountryTaxSaveReq;
import com.maan.eway.master.res.CountryTaxGetRes;
import com.maan.eway.master.service.CountryTaxSetupService;
import com.maan.eway.repository.CountryTaxSetupRepository;
import com.maan.eway.res.DropDownRes;
import com.maan.eway.res.SuccessRes2;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;

@Service
@Transactional
public class CountryTaxSetupServiceImpl implements CountryTaxSetupService {

	@PersistenceContext
	private EntityManager em;

	@Autowired
	private CountryTaxSetupRepository repo;

	Gson json = new Gson();

	private Logger log = LogManager.getLogger(CountryTaxSetupServiceImpl.class);

	@Override
	public List<String> validateCountryTax(CountryTaxSaveReq req) {
		List<String> errorList = new ArrayList<String>();

		try {	
//				
			if (StringUtils.isBlank(req.getCountryId()) ) {
			//	errorList.add(new Error("03", "CountryId", "Please Select Country Id "));
				errorList.add("1392");
			}
			else if(req.getCountryId().length()>20) {
				//errorList.add(new Error("03","CountryId","Pleaser enter the country between 20 characters"));
				errorList.add("1393");
			}
			
			if (StringUtils.isBlank(req.getCreatedBy())) {
			//	errorList.add(new Error("07", "CreatedBy", "Please Enter CreatedBy"));
				errorList.add("1270");
			} else if (req.getCreatedBy().length() > 100) {
			//	errorList.add(new Error("07", "CreatedBy", "Please Enter CreatedBy within 100 Characters"));
				errorList.add("1271");
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
			//	errorList.add(new Error("04", "EffectiveDateStart", "Please Enter Effective Date Start "));
				errorList.add("1261");

			} else if (req.getEffectiveDateStart().before(today)) {
				//errorList.add(new Error("04", "EffectiveDateStart", "Please Enter Effective Date Start as Future Date"));
						
				errorList.add("1262");
			}
			
			
			if (StringUtils.isBlank(req.getStatus())) {
			//	errorList.add(new Error("05", "Status", "Please Enter Status"));
				errorList.add("1263");
				} else if (req.getStatus().length() > 1) {
			//	errorList.add(new Error("05", "Status", "Enter Status in One Character Only"));
				errorList.add("1264");
				} else if(!("Y".equalsIgnoreCase(req.getStatus())||"N".equalsIgnoreCase(req.getStatus())||"R".equalsIgnoreCase(req.getStatus())|| "P".equalsIgnoreCase(req.getStatus()))) {
			//	errorList.add(new Error("05", "Status", "Please Select Valid Status - Active or Deactive or Pending or Referral "));
				errorList.add("1265");
				}

			
			if(StringUtils.isBlank(req.getRemarks())) {
			//	errorList.add(new Error("13","Remarks","Please Enter the Remarks"));
				errorList.add("1259");
			}
			else if(req.getRemarks().length()>100) {
			//	errorList.add(new Error("13","Remarks","Please Enter RegionId within 10 characters"));
				errorList.add("1260");
			}
			
			if (StringUtils.isBlank(req.getTaxName())) {
			//	errorList.add(new Error("01", "TaxName", "Please Enter Tax Name"));
				errorList.add("1394");
			}else if (req.getTaxName().length() > 100){
			//	errorList.add(new Error("01","TaxName", "Please Enter Tax Name within 100 Characters"));
				errorList.add("1395");
				
			}else if (StringUtils.isBlank(req.getTaxId()) && StringUtils.isNotBlank(req.getCountryId())) {
				List<CountryTaxSetup> taxList = getTaxNameExistDetails(req.getTaxName() , req.getCountryId());
				if (taxList.size()>0 ) {
				//	errorList.add(new Error("01", "TaxName", "This Tax Name Already Exist "));
					errorList.add("1396");
				}
				
			} else if(StringUtils.isNotBlank(req.getTaxId()) &&  StringUtils.isNotBlank(req.getCountryId())) {
				List<CountryTaxSetup> taxList = getTaxNameExistDetails(req.getTaxName() , req.getCountryId());
				if (taxList.size()>0 &&  (! req.getTaxId().equalsIgnoreCase(taxList.get(0).getTaxId().toString())) ) {
				//	errorList.add(new Error("01", "TaxName", "This Tax Name Already Exist "));
					errorList.add("1396");
				}

			}
			
			if (StringUtils.isBlank(req.getTaxDesc())) {
				//errorList.add(new Error("01", "TaxDesc", "Please Enter Tax Description"));
				errorList.add("1397");
			}else if (req.getTaxName().length() > 200){
			//	errorList.add(new Error("01","TaxName", "Please Enter Tax Name within 200 Characters"));
				errorList.add("1398");
				
			}
			
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
		}
		return errorList;
	}
	
	public List<CountryTaxSetup> getTaxNameExistDetails(String taxName , String countryId ) {
		List<CountryTaxSetup> list = new ArrayList<CountryTaxSetup>();
		try {
			Date today = new Date();
			// Find Latest Record
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<CountryTaxSetup> query = cb.createQuery(CountryTaxSetup.class);

			// Find All
			Root<CountryTaxSetup> b = query.from(CountryTaxSetup.class);

			// Select
			query.select(b);

			// Effective Date Max Filter
			Subquery<Long> amendId = query.subquery(Long.class);
			Root<CountryTaxSetup> ocpm1 = amendId.from(CountryTaxSetup.class);
			amendId.select(cb.max(ocpm1.get("amendId")));
			Predicate a1 = cb.equal(ocpm1.get("taxId"), b.get("taxId"));
			Predicate a2 = cb.lessThanOrEqualTo(ocpm1.get("effectiveDateStart"), today);
			Predicate a3 = cb.greaterThanOrEqualTo(ocpm1.get("effectiveDateEnd"), today);
			Predicate a4 = cb.equal(ocpm1.get("countryId"), b.get("countryId"));
			amendId.where(a1,a2,a3,a4);

			Predicate n1 = cb.equal(b.get("amendId"), amendId);
			Predicate n2 = cb.equal(cb.lower( b.get("taxName")), taxName.toLowerCase());
			Predicate n3 = cb.equal(cb.lower( b.get("countryId")), countryId );
			query.where(n1,n2,n3);
			
			// Get Result
			TypedQuery<CountryTaxSetup> result = em.createQuery(query);
			list = result.getResultList();		
		
		} catch (Exception e) {
			e.printStackTrace();
			log.info(e.getMessage());

		}
		return list;
	}
	
	public Long getMasterTableCount() {

		Long data = 0L;
		try {

			List<CountryTaxSetup> list = new ArrayList<CountryTaxSetup>();
			// Find Latest Record
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<CountryTaxSetup> query = cb.createQuery(CountryTaxSetup.class);

			// Find All
			Root<CountryTaxSetup> b = query.from(CountryTaxSetup.class);

			// Select
			query.select(b);
			
			// Order By
			List<Order> orderList = new ArrayList<Order>();
			orderList.add(cb.desc(b.get("taxId")));
			
			query.orderBy(orderList);
			// Get Result
			TypedQuery<CountryTaxSetup> result = em.createQuery(query);
			int limit = 0 , offset = 1 ;
			result.setFirstResult(limit * offset);
			result.setMaxResults(offset);
			list = result.getResultList();
			data = list.size() > 0 ? Long.valueOf(list.get(0).getTaxId()) : 0 ;
			

		} catch (Exception e) {
			e.printStackTrace();
			log.info(e.getMessage());

		}
		return data;
	}
	

	@Transactional
	@Override
	public SuccessRes2 saveCountryTax(CountryTaxSaveReq req) {
		SimpleDateFormat sdformat = new SimpleDateFormat("dd/MM/YYYY");
		SuccessRes2 res = new SuccessRes2();
		CountryTaxSetup saveData = new CountryTaxSetup();
		List<CountryTaxSetup> list = new ArrayList<CountryTaxSetup>();
		DozerBeanMapper dozerMapper = new DozerBeanMapper();

		try {
			Integer amendId = 0 ;
			String branchCode = "";
			Date startDate = req.getEffectiveDateStart() ;
			String end = "31/12/2050";
			Date endDate = sdformat.parse(end);
			long MILLIS_IN_A_DAY = 1000 * 60 * 60 * 24;
			Date oldEndDate = new Date(req.getEffectiveDateStart().getTime() - MILLIS_IN_A_DAY);
			Date entryDate = new Date() ;
			String createdBy = req.getCreatedBy(); ;
			String taxId = "";

			if (StringUtils.isBlank(req.getTaxId())) {
				// Save
				// Long totalCount = repo.count();
				Long totalCount = getMasterTableCount();
				taxId = Long.valueOf(totalCount + 1).toString();
				res.setResponse("Saved Successfully ");
				res.setSuccessId(taxId);

			} else {
				// Update
				// Get Less than Equal Today Record
				// Criteria
				taxId = req.getTaxId();
				CriteriaBuilder cb = em.getCriteriaBuilder();
				CriteriaQuery<CountryTaxSetup> query = cb.createQuery(CountryTaxSetup.class);

				// Find All
				Root<CountryTaxSetup> b = query.from(CountryTaxSetup.class);

				// Select
				query.select(b);

				// Where
				Predicate n1 = cb.equal(b.get("taxId"), req.getTaxId());
				Predicate n2 = cb.equal(b.get("countryId"), req.getCountryId());
				
				// Order By
				List<Order> orderList = new ArrayList<Order>();
				orderList.add(cb.desc(b.get("amendId")));
				
				query.where(n1, n2).orderBy(orderList);

				// Get Result
				TypedQuery<CountryTaxSetup> result = em.createQuery(query);
				int limit = 0 , offset = 2 ;
				result.setFirstResult(limit * offset);
				result.setMaxResults(offset);
				list = result.getResultList();

				if (list.size() > 0) {
					Date beforeOneDay = new Date(new Date().getTime() - MILLIS_IN_A_DAY);
					
					if ( list.get(0).getEffectiveDateStart().before(beforeOneDay)  ) {
						amendId = list.get(0).getAmendId() + 1 ;
						entryDate = new Date() ;
						createdBy = req.getCreatedBy();
						CountryTaxSetup lastRecord = list.get(0);
							lastRecord.setEffectiveDateEnd(oldEndDate);
							repo.saveAndFlush(lastRecord);
						
					} else {
						amendId = list.get(0).getAmendId() ;
						entryDate = list.get(0).getEntryDate() ;
						createdBy = list.get(0).getCreatedBy();
						saveData = list.get(0) ;
						if (list.size()>1 ) {
							CountryTaxSetup lastRecord = list.get(1);
							lastRecord.setEffectiveDateEnd(oldEndDate);
							repo.saveAndFlush(lastRecord);
						}
					
				    }
				}
				res.setResponse("Updated Successfully ");
				res.setSuccessId(taxId);

			}

			dozerMapper.map(req, saveData);
			saveData.setTaxId(Integer.valueOf(taxId));
			saveData.setTaxName(req.getTaxName());
			saveData.setTaxDesc(req.getTaxDesc());
			saveData.setEffectiveDateStart(startDate);
			saveData.setEffectiveDateEnd(endDate);
			saveData.setStatus(req.getStatus());
			saveData.setAmendId(amendId);
			saveData.setEntryDate(entryDate);
			saveData.setCreatedBy(createdBy);
			saveData.setUpdatedBy(req.getCreatedBy());
			saveData.setUpdatedDate(new Date());
			saveData.setTaxNameLocal(req.getCodeDescLocal());
			repo.saveAndFlush(saveData);

			log.info("Saved Details is ---> " + json.toJson(saveData));

		} catch (Exception e) {
			e.printStackTrace();
			log.info("Exception is --->" + e.getMessage());
			return null;
		}
		return res;
	}

	@Override
	public List<CountryTaxGetRes> getallCountryTax(CountryTaxGetAllReq req) {
		List<CountryTaxGetRes> resList = new ArrayList<CountryTaxGetRes>();
		DozerBeanMapper mapper = new DozerBeanMapper();
		try {
			List<CountryTaxSetup> list = new ArrayList<CountryTaxSetup>();

			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<CountryTaxSetup> query = cb.createQuery(CountryTaxSetup.class);
			// Find All
			Root<CountryTaxSetup> b = query.from(CountryTaxSetup.class);
		
			// Select
			query.select(b);
		
			// amendId Max Filter
			Subquery<Long> amendId = query.subquery(Long.class);
			Root<CountryTaxSetup> ocpm1 = amendId.from(CountryTaxSetup.class);
			amendId.select(cb.max(ocpm1.get("amendId")));
			Predicate a1 = cb.equal(ocpm1.get("taxId"), b.get("taxId"));
			Predicate a2 = cb.equal(ocpm1.get("countryId"), b.get("countryId"));
	
			amendId.where(a1, a2);

			// Order By
			List<Order> orderList = new ArrayList<Order>();
			orderList.add(cb.asc(b.get("taxName")));

			// Where
			Predicate n1 = cb.equal(b.get("amendId"), amendId);
			Predicate n2 = cb.equal(b.get("countryId"), req.getCountryId());
			query.where(n1, n2).orderBy(orderList);

			// Get Result
			TypedQuery<CountryTaxSetup> result = em.createQuery(query);
			list = result.getResultList();
			list.sort(Comparator.comparing(CountryTaxSetup :: getTaxName ));
			
			// Map
			for (CountryTaxSetup data : list) {
				CountryTaxGetRes res = new CountryTaxGetRes();

				res = mapper.map(data, CountryTaxGetRes.class);
				res.setTaxId(data.getTaxId().toString());
				res.setCodeDescLocal(data.getTaxNameLocal());
				resList.add(res);
			}

		} catch (Exception e) {
			e.printStackTrace();
			log.info(e.getMessage());
			return null;

		}
		return resList;
	}
	private static <T> java.util.function.Predicate<T> distinctByKey(java.util.function.Function<? super T, ?> keyExtractor) {
	    Map<Object, Boolean> seen = new ConcurrentHashMap<>();
	    return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
	}

	@Override
	public CountryTaxGetRes getByCountryTaxId(CountryTaxGetReq req) {
		CountryTaxGetRes res = new CountryTaxGetRes();
		DozerBeanMapper mapper = new DozerBeanMapper();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

		try {
			Date today = new Date();
			Calendar cal = new GregorianCalendar();
			cal.setTime(today);
			cal.set(Calendar.HOUR_OF_DAY, 23);
			cal.set(Calendar.MINUTE, 1);
			today = cal.getTime();

			// Criteria
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<CountryTaxSetup> query = cb.createQuery(CountryTaxSetup.class);
			List<CountryTaxSetup> list = new ArrayList<CountryTaxSetup>();

			// Find All
			Root<CountryTaxSetup> c = query.from(CountryTaxSetup.class);

			// Select
			query.select(c);

			// amendId Max Filter
			Subquery<Long> amendId = query.subquery(Long.class);
			Root<CountryTaxSetup> ocpm1 = amendId.from(CountryTaxSetup.class);
			amendId.select(cb.max(ocpm1.get("amendId")));
			Predicate a1 = cb.equal(c.get("taxId"), ocpm1.get("taxId"));
			Predicate a2 = cb.equal(c.get("countryId"), ocpm1.get("countryId"));

			amendId.where(a1, a2);

			// Order By
			List<Order> orderList = new ArrayList<Order>();
			orderList.add(cb.asc(c.get("taxName")));

			// Where

			Predicate n1 = cb.equal(c.get("amendId"), amendId);
			Predicate n2 = cb.equal(c.get("taxId"), req.getTaxId());
			Predicate n3 = cb.equal(c.get("countryId"), req.getCountryId());
			
			query.where(n1, n2, n3 ).orderBy(orderList);

			// Get Result
			TypedQuery<CountryTaxSetup> result = em.createQuery(query);
			list = result.getResultList();
			
			res = mapper.map(list.get(0), CountryTaxGetRes.class);
			res.setTaxId(list.get(0).getTaxId().toString());
			res.setEntryDate(list.get(0).getEntryDate());
			res.setEffectiveDateStart(list.get(0).getEffectiveDateStart());
			res.setEffectiveDateEnd(list.get(0).getEffectiveDateEnd());
			res.setCodeDescLocal(list.get(0).getTaxNameLocal());
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Exception is ---> " + e.getMessage());
			return null;
		}
		return res;
	}

	@Override
	public List<CountryTaxGetRes> getActiveCountryTax(CountryTaxGetAllReq req) {
		List<CountryTaxGetRes> resList = new ArrayList<CountryTaxGetRes>();
		DozerBeanMapper mapper = new DozerBeanMapper();
		try {
			List<CountryTaxSetup> list = new ArrayList<CountryTaxSetup>();

			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<CountryTaxSetup> query = cb.createQuery(CountryTaxSetup.class);
			// Find All
			Root<CountryTaxSetup> b = query.from(CountryTaxSetup.class);
		
			// Select
			query.select(b);
		
			// Effective Date Max Filter
			Subquery<Long> amendId = query.subquery(Long.class);
			Root<CountryTaxSetup> ocpm1 = amendId.from(CountryTaxSetup.class);
			amendId.select(cb.max(ocpm1.get("amendId")));
			Predicate a1 = cb.equal(ocpm1.get("taxId"), b.get("taxId"));
			Predicate a2 = cb.equal(ocpm1.get("countryId"), b.get("countryId"));
			Predicate a4 = cb.equal(ocpm1.get("stateId"), b.get("stateId"));
			amendId.where(a1, a2, a4);

			// Order By
			List<Order> orderList = new ArrayList<Order>();
			orderList.add(cb.asc(b.get("taxName")));

			// Where
			Predicate n1 = cb.equal(b.get("amendId"), amendId);
			Predicate n2 = cb.equal(b.get("countryId"), req.getCountryId());
			Predicate n3 = cb.equal(b.get("status"), "Y");

			query.where(n1, n2,n3).orderBy(orderList);

			// Get Result
			TypedQuery<CountryTaxSetup> result = em.createQuery(query);
			list = result.getResultList();
			
			
			// Map
			for (CountryTaxSetup data : list) {
				CountryTaxGetRes res = new CountryTaxGetRes();

				res = mapper.map(data, CountryTaxGetRes.class);
				res.setTaxId(data.getTaxId().toString());
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
	public List<DropDownRes> getCountryTaxDropdown(CountryTaxDropDownReq req) {
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

			// Criteria
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<CountryTaxSetup> query = cb.createQuery(CountryTaxSetup.class);
			List<CountryTaxSetup> list = new ArrayList<CountryTaxSetup>();

			// Find All
			Root<CountryTaxSetup> c = query.from(CountryTaxSetup.class);

			// Select
			query.select(c);

			// Order By
			List<Order> orderList = new ArrayList<Order>();
			orderList.add(cb.asc(c.get("taxName")));

			// Effective Date Max Filter
			Subquery<Timestamp> effectiveDate = query.subquery(Timestamp.class);
			Root<CountryTaxSetup> ocpm1 = effectiveDate.from(CountryTaxSetup.class);
			effectiveDate.select(cb.greatest(ocpm1.get("effectiveDateStart")));
			Predicate a1 = cb.equal(c.get("taxId"), ocpm1.get("taxId"));
			Predicate a2 = cb.lessThanOrEqualTo(ocpm1.get("effectiveDateStart"), today);
			Predicate a3 = cb.equal(c.get("countryId"), ocpm1.get("countryId"));

			effectiveDate.where(a1, a2, a3);
			// Effective Date End Max Filter
			Subquery<Timestamp> effectiveDate2 = query.subquery(Timestamp.class);
			Root<CountryTaxSetup> ocpm2 = effectiveDate2.from(CountryTaxSetup.class);
			effectiveDate2.select(cb.greatest(ocpm2.get("effectiveDateEnd")));
			Predicate a6 = cb.equal(c.get("countryId"), ocpm2.get("countryId"));
			Predicate a8 = cb.equal(c.get("taxId"), ocpm2.get("taxId"));
			Predicate a10 = cb.greaterThanOrEqualTo(ocpm2.get("effectiveDateEnd"), todayEnd);
			effectiveDate2.where(a6,  a8, a10);

			// Where

			Predicate n1 = cb.equal(c.get("status"),"Y");
			Predicate n11 = cb.equal(c.get("status"),"R");
			Predicate n12 = cb.or(n1,n11);
			Predicate n2 = cb.equal(c.get("effectiveDateStart"), effectiveDate);
			Predicate n3 = cb.equal(c.get("countryId"), req.getCountryId());
			Predicate n6 = cb.equal(c.get("effectiveDateEnd"), effectiveDate2);

			query.where(n12, n2, n3 , n6).orderBy(orderList);

			// Get Result
			TypedQuery<CountryTaxSetup> result = em.createQuery(query);
			list = result.getResultList();

			for (CountryTaxSetup data : list) {
				// Response
				DropDownRes res = new DropDownRes();
				res.setCode(data.getTaxId().toString());
				res.setCodeDesc(data.getTaxName());
				res.setStatus(data.getStatus());
				res.setCodeDescLocal(data.getTaxNameLocal());
				resList.add(res);
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Exception is ---> " + e.getMessage());
			return null;
		}
		return resList;
	}

	@Override
	public SuccessRes2 changeStatusOfCountryTax(CountryChangeStatusReq req) {
		DozerBeanMapper mapper = new DozerBeanMapper();
		SuccessRes2 res = new SuccessRes2();
		try {
			
			List<CountryTaxSetup> list = new ArrayList<CountryTaxSetup>();
			// Find Latest Record
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<CountryTaxSetup> query = cb.createQuery(CountryTaxSetup.class);

			// Find All
			Root<CountryTaxSetup> b = query.from(CountryTaxSetup.class);

			// Select
			query.select(b);

			// amendId Max Filter
			Subquery<Long> amendId = query.subquery(Long.class);
			Root<CountryTaxSetup> ocpm1 = amendId.from(CountryTaxSetup.class);
			amendId.select(cb.max(ocpm1.get("amendId")));
			Predicate a1 = cb.equal(ocpm1.get("taxId"), b.get("taxId"));
			Predicate a3 = cb.equal(ocpm1.get("countryId"), b.get("countryId"));
			amendId.where(a1,a3);

			// Order By
			List<Order> orderList = new ArrayList<Order>();
			orderList.add(cb.desc(b.get("taxId")));

			// Where
			Predicate n1 = cb.equal(b.get("amendId"), amendId);
			Predicate n2 = cb.equal(b.get("taxId"), req.getTaxId());
			Predicate n4 = cb.equal(b.get("countryId"), req.getCountryId());
			
			query.where(n1, n2,n4).orderBy(orderList);

			// Get Result
			TypedQuery<CountryTaxSetup> result = em.createQuery(query);
			list = result.getResultList();
			CountryTaxSetup updateRecord = list.get(0);
			updateRecord.setStatus(req.getStatus());
			repo.save(updateRecord);
			
			// perform update
			res.setResponse("Status Changed");
			res.setSuccessId(req.getTaxId());
			
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Exception is ---> " + e.getMessage());
			return null;
		}
		return res;
	}
	
	
}
