package com.maan.eway.notif.service.impl;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.maan.eway.bean.SmsConfigMaster;
import com.maan.eway.notif.req.SmsConfigInsertReq;
import com.maan.eway.notif.req.SmsGetReq;
import com.maan.eway.notif.res.SmsMasterGetRes;
import com.maan.eway.notif.service.SmsConfigMasterService;
import com.maan.eway.repository.SmsMasterRepository;
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

@Service
@Transactional
public class SmsConfigMasterServiceImpl implements SmsConfigMasterService{

	@Autowired
	private SmsMasterRepository smsrepo; 
	
	@PersistenceContext
	private EntityManager em;
	
	Gson json = new Gson();
	
	private Logger log = LogManager.getLogger(SmsConfigMasterServiceImpl.class);
	
	@Override
	public List<String> validatesmsmaster(SmsConfigInsertReq req) {
		List<String> errorList = new ArrayList<String>();

		try {

			if (StringUtils.isBlank(req.getCompanyId())) {
			//	errorList.add(new Error("01", "Company Id", "Please Enter Company Id"));
				errorList.add("1255");
			} else if (req.getCompanyId().length() > 20) {
			//	errorList.add(new Error("01", "Company Id", "Please Enter Company Id within 20 Characters"));
				errorList.add("1448");
			} else if (StringUtils.isBlank(req.getSNo())) {
//				List<SmsConfigMaster> smsList = getSnoDetails(req.getCompanyId(),req.getBranchCode());
//				if (smsList.size() > 0) {
//					errorList.add(new Error("01", "S No", "Please Enter Your Sno"));
//				}
			} else {
//				List<SmsConfigMaster> smsList = getCompanyIdExistDetails(req.getCompanyId(),req.getBranchCode());
//				if (smsList.size() > 0 && (!req.getSNo().equalsIgnoreCase(smsList.get(0).getSNo().toString()))) {
//					errorList.add(new Error("01", "Company Id", "This Company Id Already Exist "));
//				}

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
			//	errorList.add(new Error("02", "EffectiveDateStart", "Please Enter Effective Date Start "));
				errorList.add("1261");

			} else if (req.getEffectiveDateStart().before(today)) {
			//	errorList.add(new Error("02", "EffectiveDateStart", "Please Enter Effective Date Start as Future Date"));
				errorList.add("1262");
			} 
			// Status Validation
			if (StringUtils.isBlank(req.getStatus())) {
			//	errorList.add(new Error("04", "Status", "Please Enter Status"));
				errorList.add("1263");
			} else if (req.getStatus().length() > 1) {
			//	errorList.add(new Error("04", "Status", "Status 1 Character Only"));
				errorList.add("1264");
			} else if (!("Y".equals(req.getStatus()) || "N".equals(req.getStatus()))) {
			//	errorList.add(new Error("04", "Status", "Enter Status Y or N Only"));
				errorList.add("1265");
			}
			if (StringUtils.isBlank(req.getCreatedBy())) {
			//	errorList.add(new Error("05", "CreatedBy", "Please Enter CreatedBy"));
				errorList.add("1270");
			} else if (req.getCreatedBy().length() > 100) {
			//	errorList.add(new Error("05", "CreatedBy", "Please Enter CreatedBy within 100 Characters"));
				errorList.add("1271");
			}
			if (StringUtils.isBlank(req.getRemarks())) {
			//	errorList.add(new Error("06", "Remarks", "Please Enter Remarks"));
				errorList.add("1259");
			} else if (req.getRemarks().length() > 300) {
			//	errorList.add(new Error("06", "Remarks", "Please Enter Remarks within 100 Characters"));
				errorList.add("1260");
			}
			if (StringUtils.isBlank(req.getSmsUserPass())) {
			//	errorList.add(new Error("07", "SmsUserPass", "Please Enter SmsUserPass"));
				errorList.add("1460");
			} else if (req.getSmsUserPass().length() > 150) {
			//	errorList.add(new Error("07", "SmsUserPass", "Please Enter SmsUserPass within 150 Characters"));
				errorList.add("1461");
			}
			if (StringUtils.isBlank(req.getSmsUserName())) {
			//	errorList.add(new Error("08", "SmsUserName", "Please Enter SmsUserName"));
				errorList.add("1462");
			} else if (req.getSmsUserName().length() > 150) {
			//	errorList.add(new Error("08", "SmsUserName", "Please Enter SmsUserName within 150 Characters"));
				errorList.add("1463");
			}
			if (StringUtils.isBlank(req.getSmsPartyUrl())) {
				//errorList.add(new Error("09", "SmsPartyUrl", "Please Enter SmsPartyUrl"));
				errorList.add("1464");
			} else if (req.getSmsPartyUrl().length() > 300) {
			//	errorList.add(new Error("09", "SmsPartyUrl", "Please Enter SmsPartyUrl within 300 Characters"));
				errorList.add("1465");
			}
			if (StringUtils.isBlank(req.getSenderId())) {
			//	errorList.add(new Error("10", "SenderId", "Please Enter SenderId"));
				errorList.add("1466");
			}
			 else if (req.getSenderId().length() > 60) {
			//	errorList.add(new Error("10", "Sender ID", "Please Enter Sender ID within 60 Characters"));
				errorList.add("1467");
			}
			if (StringUtils.isBlank(req.getSecureYn())) {
			//	errorList.add(new Error("11", "SecureYn", "Please Enter SecureYn"));
				errorList.add("1468");
			} else if (req.getSecureYn().length() > 60) {
			//	errorList.add(new Error("11", "SecureYn", "Please Enter SecureYn within 60 Characters"));
				errorList.add("1469");
			}
			if (StringUtils.isBlank(req.getCoreAppCode())) {
			//	errorList.add(new Error("12", "Core App Code", "Please Enter Core App Code"));
				errorList.add("1266");
			} else if (req.getCoreAppCode().length() > 20) {
			//	errorList.add(new Error("12", "Core App Code", "Please Enter CreatedBy within 20 Characters"));
				errorList.add("1267");
			}
			if (StringUtils.isBlank(req.getRegulatoryCode())) {
			//	errorList.add(new Error("13", "Regulatory Code", "Please Enter Regulatory Code"));
				errorList.add("1268");
			} else if (req.getRegulatoryCode().length() > 20) {
			//	errorList.add(new Error("13", "Regulatory Code", "Please Enter Regulatory Code within 20 Characters"));
				errorList.add("1269");
			}
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
		}
		return errorList;
	}

	@Override
	public SuccessRes insertsmsmaster(SmsConfigInsertReq req) {
		SimpleDateFormat sdformat = new SimpleDateFormat("dd/MM/yyyy");
		SuccessRes res = new SuccessRes();
		SmsConfigMaster saveData = new SmsConfigMaster();
		List<SmsConfigMaster> list = new ArrayList<SmsConfigMaster>();
		DozerBeanMapper dozermapper = new DozerBeanMapper();
		try {
			Integer amendId = 0;
			Date startDate = req.getEffectiveDateStart() ;
			String end = "31/12/2050";
			Date endDate = sdformat.parse(end);
			long MILLIS_IN_A_DAY = 1000 * 60 * 60 * 24;
			Date oldEndDate = new Date(req.getEffectiveDateStart().getTime() - MILLIS_IN_A_DAY);
			Date entryDate = null ;
			String sno = "";
			String createdBy = "" ;	
			if(StringUtils.isBlank(req.getSNo())) {
				// Save 
				Long totalcount = getMasterTableCount();
				sno = Long.valueOf(totalcount+1).toString();
				res.setResponse("Saved Successfully");
				res.setSuccessId(sno);
				res.setFactorTypeId("");
				}
			else {
				// Update
				sno= req.getSNo();
				CriteriaBuilder cb = em.getCriteriaBuilder();
				CriteriaQuery<SmsConfigMaster> query = cb.createQuery(SmsConfigMaster.class);
				// Find all
				Root<SmsConfigMaster> b = query.from(SmsConfigMaster.class);
				// Select
				query.select(b);
				// Effective Date Max Filter
				Subquery<Timestamp> effectiveDate = query.subquery(Timestamp.class);
				Root<SmsConfigMaster> ocpm1= effectiveDate.from(SmsConfigMaster.class);
				effectiveDate.select(cb.greatest(ocpm1.get("effectiveDateStart")));
				Predicate a1 = cb.lessThanOrEqualTo(ocpm1.get("effectiveDateStart"), startDate);
				effectiveDate.where(a1);
				//where
				Predicate n1 = cb.equal(b.get("status"),"Y");
				Predicate n2 = cb.equal(b.get("effectiveDateStart"),effectiveDate);
				Predicate n3 = cb.equal(b.get("sNo"),req.getSNo());
				query.where(n1,n2,n3);
				// Get Result
				TypedQuery<SmsConfigMaster> result = em.createQuery(query);
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
						SmsConfigMaster lastRecord = list.get(0);
							lastRecord.setEffectiveDateEnd(oldEndDate);
							smsrepo.saveAndFlush(lastRecord);
						
					} else {
						amendId = list.get(0).getAmendId() ;
						entryDate = list.get(0).getEntryDate() ;
						createdBy = list.get(0).getCreatedBy();
						saveData = list.get(0) ;
						if (list.size()>1 ) {
							SmsConfigMaster lastRecord = list.get(1);
							lastRecord.setEffectiveDateEnd(oldEndDate);
							smsrepo.saveAndFlush(lastRecord);
						}
					
				    }
				}
				res.setResponse("Updated Successfully");
				res.setSuccessId(sno);
				res.setFactorTypeId("");
				
			}
			dozermapper.map(req, saveData);
			saveData.setSNo(Integer.valueOf(sno));
			saveData.setEffectiveDateStart(req.getEffectiveDateStart());
			saveData.setEffectiveDateEnd(endDate);
			saveData.setEntryDate(new Date());
			saveData.setAmendId(amendId);
			saveData.setUpdatedBy(req.getCreatedBy());
			saveData.setUpdatedDate(new Date());
			smsrepo.saveAndFlush(saveData);

		
			log.info("Saved Details is --> " + json.toJson(saveData));
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Exception is -->" + e.getMessage());
			return null;
		}
		return res;
	}

	
	public Long getMasterTableCount() {
		Long data = 0L;
		try {
			List<Long> list = new ArrayList<Long>();
			// Find Latest Record

			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Long> query = cb.createQuery(Long.class);
			// Find All
			Root<SmsConfigMaster> b = query.from(SmsConfigMaster.class);
			// Select
			query.multiselect(cb.count(b));
			// Effective Date Max Filter
			Subquery<Timestamp> effectiveDate = query.subquery(Timestamp.class);
			Root<SmsConfigMaster> ocpm1 = effectiveDate.from(SmsConfigMaster.class);
			effectiveDate.select(cb.greatest(ocpm1.get("effectiveDateStart")));
			Predicate a1 = cb.equal(ocpm1.get("companyId"), b.get("companyId"));

			effectiveDate.where(a1);
			Predicate n1 = cb.equal(b.get("effectiveDateStart"), effectiveDate);

			query.where(n1);
			// Get Result
			TypedQuery<Long> result = em.createQuery(query);
			list = result.getResultList();
			data = list.get(0);
		} catch (Exception e) {
			e.printStackTrace();
			log.info(e.getMessage());
		}
		return data;

	}

	// Company Id Exist Details validation
	public List<SmsConfigMaster> getSnoDetails(String companyId , String branchCode) {
		List<SmsConfigMaster> list = new ArrayList<SmsConfigMaster>();
		try {
			// Find Latest Record
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<SmsConfigMaster> query = cb.createQuery(SmsConfigMaster.class);

			// Find All
			Root<SmsConfigMaster> b = query.from(SmsConfigMaster.class);

			// Select
			query.select(b);

			// Effective Date Max Filter
			Subquery<Timestamp> effectiveDate = query.subquery(Timestamp.class);
			Root<SmsConfigMaster> ocpm1 = effectiveDate.from(SmsConfigMaster.class);
			effectiveDate.select(cb.greatest(ocpm1.get("effectiveDateStart")));
			Predicate a1 = cb.equal(ocpm1.get("companyId"), b.get("companyId"));
			Predicate a2 = cb.equal(ocpm1.get("branchCode"), b.get("branchCode"));

			effectiveDate.where(a1,a2);

			Predicate n1 = cb.equal(b.get("effectiveDateStart"), effectiveDate);
			Predicate n2 = cb.equal(b.get("companyId"), companyId);
			Predicate n3 = cb.equal(b.get("branchCode"), branchCode);
			
			query.where(n1, n2,n3);
			// Get Result
			TypedQuery<SmsConfigMaster> result = em.createQuery(query);
			list = result.getResultList();

		} catch (Exception e) {
			e.printStackTrace();
			log.info(e.getMessage());

		}
		return list;
	}

	@Override
	public SmsMasterGetRes getbysmsid(SmsGetReq req) {
		SmsMasterGetRes res = new SmsMasterGetRes();
		DozerBeanMapper mapper = new DozerBeanMapper();
		try {
			Date today = new Date();
			Calendar cal = new GregorianCalendar();
			cal.setTime(today);
			cal.set(Calendar.HOUR_OF_DAY, 23);
			cal.set(Calendar.MINUTE, 1);
			today = cal.getTime();

			List<SmsConfigMaster> list = new ArrayList<SmsConfigMaster>();
			// Find Latest Record
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<SmsConfigMaster> query = cb.createQuery(SmsConfigMaster.class);
			// Find all
			Root<SmsConfigMaster> b = query.from(SmsConfigMaster.class);
			// Select
			query.select(b);
			// Amend ID Max Filter
			Subquery<Long> amendId = query.subquery(Long.class);
			Root<SmsConfigMaster> ocpm1 = amendId.from(SmsConfigMaster.class);
			amendId.select(cb.max(ocpm1.get("amendId")));
			Predicate a1 = cb.equal(ocpm1.get("sNo"), b.get("sNo"));
			Predicate a2 = cb.equal(ocpm1.get("companyId"), b.get("companyId"));
			Predicate a3 = cb.equal(ocpm1.get("branchCode"),b.get("branchCode"));

			amendId.where(a1, a2,a3);

			// Order By
			List<Order> orderList = new ArrayList<Order>();
			orderList.add(cb.asc(b.get("branchCode")));

			// Where
			Predicate n1 = cb.equal(b.get("amendId"), amendId);
			Predicate n2 = cb.equal(b.get("companyId"), req.getCompanyId());
			Predicate n3 = cb.equal(b.get("branchCode"), req.getBranchCode());
			Predicate n4 = cb.equal(b.get("sNo"), req.getSNo());
			Predicate n5 = cb.equal(b.get("branchCode"), "99999");
			Predicate n6 = cb.or(n3,n5);
			
			query.where(n1,n2,n4,n6).orderBy(orderList);

			// Get Result
			TypedQuery<SmsConfigMaster> result = em.createQuery(query);
			list = result.getResultList();

			// Map
			for (SmsConfigMaster data : list) {

				res = mapper.map(data, SmsMasterGetRes.class);
				res.setSNo(data.getSNo().toString());
			}

		} catch (Exception e) {
			e.printStackTrace();
			log.info(e.getMessage());
			return null;

		}
		return res;
	}

	// Company Id Exist Details validation

		private List<SmsConfigMaster> getCompanyIdExistDetails(String companyId, String branchCode) {
			List<SmsConfigMaster> list = new ArrayList<SmsConfigMaster>();
			try {
				// Find Latest Record
				CriteriaBuilder cb = em.getCriteriaBuilder();
				CriteriaQuery<SmsConfigMaster> query = cb.createQuery(SmsConfigMaster.class);

				// Find All
				Root<SmsConfigMaster> b = query.from(SmsConfigMaster.class);

				// Select
				query.select(b);

				// Effective Date Max Filter
				Subquery<Timestamp> effectiveDate = query.subquery(Timestamp.class);
				Root<SmsConfigMaster> ocpm1 = effectiveDate.from(SmsConfigMaster.class);
				effectiveDate.select(cb.greatest(ocpm1.get("effectiveDateStart")));
				Predicate a1 = cb.equal(ocpm1.get("sNo"), b.get("sNo"));
				Predicate a2 = cb.equal(ocpm1.get("companyId"), b.get("companyId"));
				Predicate a3 = cb.equal(ocpm1.get("branchCode"), b.get("branchCode"));

				effectiveDate.where(a1, a2,a3);

				Predicate n1 = cb.equal(b.get("effectiveDateStart"), effectiveDate);
				Predicate n2 = cb.equal(b.get("companyId"), companyId);
				Predicate n3 = cb.equal(b.get("branchCode"), branchCode);
				
				query.where(n1, n2,n3);
				// Get Result
				TypedQuery<SmsConfigMaster> result = em.createQuery(query);
				list = result.getResultList();

			} catch (Exception e) {
				e.printStackTrace();
				log.info(e.getMessage());

			}
			return list;

		}
}
