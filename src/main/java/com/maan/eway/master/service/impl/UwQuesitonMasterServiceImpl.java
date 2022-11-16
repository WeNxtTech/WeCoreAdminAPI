package com.maan.eway.master.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.maan.eway.bean.MotorMakeMaster;
import com.maan.eway.bean.UWQuestionsMaster;
import com.maan.eway.error.Error;
import com.maan.eway.master.req.UwQuestionChangeStatusReq;
import com.maan.eway.master.req.UwQuestionMasterGetReq;
import com.maan.eway.master.req.UwQuestionMasterSaveReq;
import com.maan.eway.master.req.UwQuestionsMasterGetAllReq;
import com.maan.eway.master.res.MotorMakeGetRes;
import com.maan.eway.master.res.UwQuestionMasterRes;
import com.maan.eway.master.service.UwQuestionMasterService;
import com.maan.eway.repository.UwQuestionMasterRepository;
import com.maan.eway.res.DropDownRes;
import com.maan.eway.res.SuccessRes;

@Service
@Transactional
public class UwQuesitonMasterServiceImpl implements UwQuestionMasterService {

	@Autowired
	private UwQuestionMasterRepository uwRepo;

	@PersistenceContext
	private EntityManager em;

	Gson json = new Gson();

	private Logger log = LogManager.getLogger(UwQuesitonMasterServiceImpl.class);

	@Override
	public List<Error> validateUwQuestions(UwQuestionMasterSaveReq req) {
		List<Error> error = new ArrayList<Error>();

		try {

			if (StringUtils.isBlank(req.getCompanyId())) {
				error.add(new Error("01", "CompanyId", "Please Enter CompanyId"));
			} else if (req.getCompanyId().length() > 20) {
				error.add(new Error("01", "CompanyId", "Please Enter CompanyId within 20 Characters"));
			}

			if (StringUtils.isBlank(req.getProductId())) {
				error.add(new Error("02", "ProductId", "Please Enter ProductId"));
			}
			if (req.getUwQuestionDesc().length() > 100) {
				error.add(new Error("03", "UwQuestionDesc", "Please Enter UwQuestionDesc within 100 Characters"));
			}
			if (req.getQuestionType().length() > 100) {
				error.add(new Error("04", "QuestionType", "Please Enter QuestionType within 100 Characters"));
			}
			if (req.getRemarks().length() > 100) {
				error.add(new Error("05", "Remarks", "Please Enter Remarks within 100 Characters"));
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
				error.add(new Error("06", "EffectiveDateStart", "Please Enter Effective Date Start "));

			} else if (req.getEffectiveDateStart().before(today)) {
				error.add(new Error("06", "EffectiveDateStart", "Please Enter Effective Date Start as Future Date"));
			} else if (req.getEffectiveDateEnd() == null) {
				error.add(new Error("06", "EffectiveDateEnd", "Please Enter Effective Date End "));

			} else if (req.getEffectiveDateEnd().before(req.getEffectiveDateStart())
					|| req.getEffectiveDateEnd().equals(req.getEffectiveDateStart())) {
				error.add(new Error("06", "EffectiveDateStart",
						"Please Enter Effective Date End  is After Effective Date Start"));
			}

			// Status Validation
			if (StringUtils.isBlank(req.getStatus())) {
				error.add(new Error("07", "Status", "Please Enter Status"));
			} else if (req.getStatus().length() > 1) {
				error.add(new Error("07", "Status", " Status 1 Character Only"));
			} else if (!("Y".equals(req.getStatus()) || "N".equals(req.getStatus()))) {
				error.add(new Error("07", "Status", " Status Y or N"));
			}

		} catch (Exception e) {

			log.error(e);
			e.printStackTrace();
			error.add(new Error("10", "Common Error", e.getMessage()));
		}
		return error;
	}

	@Override
	public SuccessRes insertUwQuestions(UwQuestionMasterSaveReq req) {
		SimpleDateFormat sdformat = new SimpleDateFormat("dd/MM/YYYY");
		SuccessRes res = new SuccessRes();
		UWQuestionsMaster saveData = new UWQuestionsMaster();
		List<UWQuestionsMaster> list = new ArrayList<UWQuestionsMaster>();
		DozerBeanMapper dozerMapper = new DozerBeanMapper();

		try {
			Integer amendId = 0;
			Calendar cal = new GregorianCalendar();
			cal.setTime(req.getEffectiveDateStart());
			cal.set(Calendar.HOUR_OF_DAY, 23);
			cal.set(Calendar.MINUTE, 59);
			Date startDate = cal.getTime();
			Date today = new Date();
			cal.setTime(req.getEffectiveDateStart());
			cal.set(Calendar.HOUR_OF_DAY, today.getHours());
			cal.set(Calendar.MINUTE, today.getMinutes());
			Date oldEndDate = cal.getTime();
			cal.setTime(req.getEffectiveDateStart());
			cal.set(Calendar.HOUR_OF_DAY, today.getHours());
			cal.set(Calendar.MINUTE, today.getMinutes());
			Date effDate = cal.getTime();
			Date endDate = req.getEffectiveDateEnd();

			String uwQuestionId = "";
			if (StringUtils.isBlank(req.getUwQuestionId())) {
				// Save
				// Long totalCount = repo.count();
				Long totalCount = getMasterTableCount(req);
				uwQuestionId = Long.valueOf(totalCount + 1).toString();
				res.setResponse("Saved Successfully ");
				res.setSuccessId(uwQuestionId);

			} else {
				// Update
				// Get Less than Equal Today Record
				// Criteria
				uwQuestionId = req.getUwQuestionId().toString();
				CriteriaBuilder cb = em.getCriteriaBuilder();
				CriteriaQuery<UWQuestionsMaster> query = cb.createQuery(UWQuestionsMaster.class);

				// Find All
				Root<UWQuestionsMaster> b = query.from(UWQuestionsMaster.class);

				// Select
				query.select(b);

				// Effective Date Max Filter
				Subquery<Long> effectiveDate = query.subquery(Long.class);
				Root<UWQuestionsMaster> ocpm1 = effectiveDate.from(UWQuestionsMaster.class);
				effectiveDate.select(cb.max(ocpm1.get("effectiveDateStart")));
				Predicate a1 = cb.equal(ocpm1.get("uwQuestionId"), b.get("uwQuestionId"));
				Predicate a2 = cb.lessThanOrEqualTo(ocpm1.get("effectiveDateStart"), startDate);

				effectiveDate.where(a1, a2);

				// Order By
				List<Order> orderList = new ArrayList<Order>();
				orderList.add(cb.desc(b.get("amendId")));

				// Where
				Predicate n1 = cb.equal(b.get("status"), "Y");
				Predicate n2 = cb.equal(b.get("effectiveDateStart"), effectiveDate);
				Predicate n3 = cb.equal(b.get("uwQuestionId"), req.getUwQuestionId());

				query.where(n1, n2, n3);

				// Get Result
				TypedQuery<UWQuestionsMaster> result = em.createQuery(query);
				list = result.getResultList();
				if (list.size() > 0) {
					uwRepo.delete(list.get(0));
					// Amend Id

					if (list.get(0).getEffectiveDateStart().before(startDate)) {
						String startDatewithoutTime = sdformat.format(startDate);
						String oldDatewithoutTime = sdformat.format(list.get(0).getEffectiveDateStart());

						if (startDatewithoutTime.equalsIgnoreCase(oldDatewithoutTime))
							;
						{
							amendId = list.get(0).getAmendId() + 1;
						}
					}
				}
				res.setResponse("Updated Successfully");
				res.setSuccessId(uwQuestionId);

			}
			dozerMapper.map(req, saveData);
			saveData.setUwQuestionId(Integer.valueOf(uwQuestionId));
			saveData.setUwQuestionDesc(req.getUwQuestionDesc());
			saveData.setEffectiveDateStart(req.getEffectiveDateStart());
			saveData.setEffectiveDateEnd(endDate);
			saveData.setEntryDate(new Date());
			saveData.setAmendId(amendId);
			saveData.setQuestionType(req.getQuestionType());
			uwRepo.saveAndFlush(saveData);

			if (list.size() > 0) {
				// Update Old Record
				UWQuestionsMaster lastRecord = list.get(0);
				lastRecord.setEffectiveDateEnd(oldEndDate);
				uwRepo.saveAndFlush(lastRecord);
			}

			log.info("Saved Details is ---> " + json.toJson(saveData));

		} catch (Exception e) {
			e.printStackTrace();
			log.info("Exception is --->" + e.getMessage());
			return null;
		}
		return res;
	}

	public Long getMasterTableCount(UwQuestionMasterSaveReq req) {

		Long data = 0L;
		try {

			List<Long> list = new ArrayList<Long>();
			// Find Latest Record
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Long> query = cb.createQuery(Long.class);

			// Find All
			Root<UWQuestionsMaster> b = query.from(UWQuestionsMaster.class);

			// Select
			query.multiselect(cb.count(b));

			// Effective Date Max Filter
			Subquery<Long> effectiveDate = query.subquery(Long.class);
			Root<UWQuestionsMaster> ocpm1 = effectiveDate.from(UWQuestionsMaster.class);
			effectiveDate.select(cb.max(ocpm1.get("effectiveDateStart")));
			Predicate a1 = cb.equal(ocpm1.get("uwQuestionId"), b.get("uwQuestionId"));
			Predicate a2 = cb.equal(ocpm1.get("productId"), b.get("productId"));
			Predicate a3 = cb.equal(ocpm1.get("companyId"), b.get("companyId"));

			effectiveDate.where(a1, a2, a3);

			Predicate n1 = cb.equal(b.get("effectiveDateStart"), effectiveDate);
			Predicate n2 = cb.equal(b.get("productId"), req.getProductId());
			Predicate n3 = cb.equal(b.get("companyId"), req.getCompanyId());

			query.where(n1, n2, n3);
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

	@Override
	public List<UwQuestionMasterRes> getallUwQuestions(UwQuestionsMasterGetAllReq req) {
		List<UwQuestionMasterRes> resList = new ArrayList<UwQuestionMasterRes>();
		DozerBeanMapper mapper = new DozerBeanMapper();
		try {
			List<UWQuestionsMaster> list = new ArrayList<UWQuestionsMaster>();
			// Pagination
			int limit = StringUtils.isBlank(req.getLimit()) ? 0 : Integer.valueOf(req.getLimit());
			int offset = StringUtils.isBlank(req.getOffset()) ? 100 : Integer.valueOf(req.getOffset());

			// Find Latest Record
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<UWQuestionsMaster> query = cb.createQuery(UWQuestionsMaster.class);

			// Find All
			Root<UWQuestionsMaster> b = query.from(UWQuestionsMaster.class);

			// Select
			query.select(b);

			// Effective Date Max Filter
			Subquery<Long> effectiveDate = query.subquery(Long.class);
			Root<UWQuestionsMaster> ocpm1 = effectiveDate.from(UWQuestionsMaster.class);
			effectiveDate.select(cb.max(ocpm1.get("effectiveDateStart")));
			Predicate a1 = cb.equal(ocpm1.get("productId"), b.get("productId"));
			Predicate a2 = cb.equal(ocpm1.get("companyId"), b.get("companyId"));
			Predicate a3 = cb.equal(ocpm1.get("uwQuestionId"), b.get("uwQuestionId"));

			effectiveDate.where(a1, a2, a3);

			// Order By
			List<Order> orderList = new ArrayList<Order>();
			orderList.add(cb.asc(b.get("uwQuestionId")));

			// Where
			Predicate n1 = cb.equal(b.get("effectiveDateStart"), effectiveDate);
			Predicate n2 = cb.equal(b.get("productId"), req.getProductId());
			Predicate n3 = cb.equal(b.get("companyId"), req.getCompanyId());

			query.where(n1, n2, n3).orderBy(orderList);

			// Get Result
			TypedQuery<UWQuestionsMaster> result = em.createQuery(query);
			result.setFirstResult(limit * offset);
			result.setMaxResults(offset);
			list = result.getResultList();

			// Map
			for (UWQuestionsMaster data : list) {
				UwQuestionMasterRes res = new UwQuestionMasterRes();

				res = mapper.map(data, UwQuestionMasterRes.class);
				res.setUwQuestionId(data.getUwQuestionId().toString());
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
	public List<UwQuestionMasterRes> getActiveUwQuestions(UwQuestionsMasterGetAllReq req) {
		List<UwQuestionMasterRes> resList = new ArrayList<UwQuestionMasterRes>();
		DozerBeanMapper mapper = new DozerBeanMapper();
		try {
			List<UWQuestionsMaster> list = new ArrayList<UWQuestionsMaster>();
			// Pagination
			int limit = StringUtils.isBlank(req.getLimit()) ? 0 : Integer.valueOf(req.getLimit());
			int offset = StringUtils.isBlank(req.getOffset()) ? 100 : Integer.valueOf(req.getOffset());

			// Find Latest Record
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<UWQuestionsMaster> query = cb.createQuery(UWQuestionsMaster.class);

			// Find All
			Root<UWQuestionsMaster> b = query.from(UWQuestionsMaster.class);

			// Select
			query.select(b);

			// Effective Date Max Filter
			Subquery<Long> effectiveDate = query.subquery(Long.class);
			Root<UWQuestionsMaster> ocpm1 = effectiveDate.from(UWQuestionsMaster.class);
			effectiveDate.select(cb.max(ocpm1.get("effectiveDateStart")));
			Predicate a1 = cb.equal(ocpm1.get("uwQuestionId"), b.get("uwQuestionId"));
			Predicate a2 = cb.equal(ocpm1.get("productId"), b.get("productId"));
			Predicate a3 = cb.equal(ocpm1.get("companyId"), b.get("companyId"));

			effectiveDate.where(a1, a2, a3);

			// Order By
			List<Order> orderList = new ArrayList<Order>();
			orderList.add(cb.asc(b.get("uwQuestionId")));

			// Where
			Predicate n1 = cb.equal(b.get("effectiveDateStart"), effectiveDate);
			Predicate n2 = cb.equal(b.get("status"), "Y");
			Predicate n3 = cb.equal(b.get("productId"), req.getProductId());
			Predicate n4 = cb.equal(b.get("companyId"), req.getCompanyId());

			query.where(n1, n2, n3, n4).orderBy(orderList);

			// Get Result
			TypedQuery<UWQuestionsMaster> result = em.createQuery(query);
			result.setFirstResult(limit * offset);
			result.setMaxResults(offset);
			list = result.getResultList();

			// Map
			for (UWQuestionsMaster data : list) {
				UwQuestionMasterRes res = new UwQuestionMasterRes();

				res = mapper.map(data, UwQuestionMasterRes.class);
				res.setUwQuestionId(data.getUwQuestionId().toString());
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
	public UwQuestionMasterRes getByUwQuestionId(UwQuestionMasterGetReq req) {
		UwQuestionMasterRes res = new UwQuestionMasterRes();
		ModelMapper mapper = new ModelMapper();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

		try {
			// Criteria
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<UWQuestionsMaster> query = cb.createQuery(UWQuestionsMaster.class);
			List<UWQuestionsMaster> list = new ArrayList<UWQuestionsMaster>();

			// Find All
			Root<UWQuestionsMaster> c = query.from(UWQuestionsMaster.class);

			// Select
			query.select(c);

			// Effective Date Max Filter
			Subquery<Long> effectiveDate = query.subquery(Long.class);
			Root<UWQuestionsMaster> ocpm1 = effectiveDate.from(UWQuestionsMaster.class);
			effectiveDate.select(cb.max(ocpm1.get("effectiveDateStart")));
			javax.persistence.criteria.Predicate a1 = cb.equal(c.get("uwQuestionId"), ocpm1.get("uwQuestionId"));
			javax.persistence.criteria.Predicate a2 = cb.equal(c.get("productId"), ocpm1.get("productId"));
			javax.persistence.criteria.Predicate a3 = cb.equal(c.get("companyId"), ocpm1.get("companyId"));

			effectiveDate.where(a1, a2, a3);

			// Order By
			List<Order> orderList = new ArrayList<Order>();
			orderList.add(cb.asc(c.get("effectiveDateStart")));

			// Where

			javax.persistence.criteria.Predicate n1 = cb.equal(c.get("effectiveDateStart"), effectiveDate);
			javax.persistence.criteria.Predicate n2 = cb.equal(c.get("uwQuestionId"), req.getUwQuestionId());
			javax.persistence.criteria.Predicate n3 = cb.equal(c.get("productId"), req.getProductId());
			javax.persistence.criteria.Predicate n4 = cb.equal(c.get("companyId"), req.getCompanyId());

			query.where(n1, n2, n3, n4).orderBy(orderList);

			// Get Result
			TypedQuery<UWQuestionsMaster> result = em.createQuery(query);
			list = result.getResultList();
			res = mapper.map(list.get(0), UwQuestionMasterRes.class);
			res.setUwQuestionId(list.get(0).getUwQuestionId().toString());
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

	@Override
	public SuccessRes changeStatusOfUwQuestion(UwQuestionChangeStatusReq req) {
		SuccessRes res = new SuccessRes();
		try {
			Date today = req.getEffectiveDateStart() != null ? req.getEffectiveDateStart() : new Date();
			Calendar cal = new GregorianCalendar();
			UWQuestionsMaster updateRecord = new UWQuestionsMaster();
			cal.setTime(today);
			cal.set(Calendar.HOUR_OF_DAY, 23);
			cal.set(Calendar.MINUTE, 1);
			today = cal.getTime();
			List<UWQuestionsMaster> list = new ArrayList<UWQuestionsMaster>();
			// Find Latest Record
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<UWQuestionsMaster> query = cb.createQuery(UWQuestionsMaster.class);
			// Find all
			Root<UWQuestionsMaster> b = query.from(UWQuestionsMaster.class);
			// Select
			query.select(b);
			// Effective Date Max Filter
			Subquery<Long> effectiveDate = query.subquery(Long.class);
			Root<UWQuestionsMaster> ocpm1 = effectiveDate.from(UWQuestionsMaster.class);
			effectiveDate.select(cb.max(ocpm1.get("effectiveDateStart")));
			Predicate a1 = cb.equal(ocpm1.get("uwQuestionId"), b.get("uwQuestionId"));
			Predicate a2 = cb.lessThanOrEqualTo(ocpm1.get("effectiveDateStart"), today);
			effectiveDate.where(a1, a2);
			// Order By
			List<Order> orderList = new ArrayList<Order>();
			orderList.add(cb.desc(b.get("effectiveDateStart")));
			// where
			Predicate n1 = cb.equal(b.get("effectiveDateStart"), effectiveDate);
			Predicate n2 = cb.equal(b.get("uwQuestionId"), req.getUwQuestionId());
			Predicate n3 = cb.equal(b.get("productId"), req.getProductId());
			Predicate n4 = cb.equal(b.get("companyId"), req.getCompanyId());

			query.where(n1, n2, n3, n4).orderBy(orderList);
			// Get Result
			TypedQuery<UWQuestionsMaster> result = em.createQuery(query);
			list = result.getResultList();
			updateRecord = list.get(0);

			if (req.getStatus().equalsIgnoreCase("N")) {
				// Delete Old Records
				cal.setTime(today);
				cal.set(Calendar.HOUR_OF_DAY, 23);
				cal.set(Calendar.MINUTE, 30);
				today = cal.getTime();
				// Create Update
				CriteriaDelete<UWQuestionsMaster> delete = cb.createCriteriaDelete(UWQuestionsMaster.class);
				Root<UWQuestionsMaster> pm = delete.from(UWQuestionsMaster.class);
				// Where

				Predicate n6 = cb.equal(pm.get("uwQuestionId"), req.getUwQuestionId());
				Predicate n7 = cb.greaterThanOrEqualTo(pm.get("effectiveDateStart"), today);
				Predicate n8 = cb.equal(pm.get("productId"), req.getProductId());
				Predicate n9 = cb.equal(pm.get("companyId"), req.getCompanyId());

				delete.where(n6, n7, n8, n9);
				em.createQuery(delete).executeUpdate();
				// Insert Update Record
				updateRecord.setStatus(req.getStatus());
				uwRepo.save(updateRecord);
			} else if (req.getStatus().equalsIgnoreCase("Y")) {
				// Insert Update Record
				updateRecord.setStatus(req.getStatus());
				uwRepo.save(updateRecord);
			}
			// Perform Update
			res.setResponse("Status Changed");
			res.setSuccessId(req.getUwQuestionId());
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Exception is --> " + e.getMessage());
			return null;
		}
		return res;
	}

	@Override
	public List<DropDownRes> getUwQuestionMasterDropdown(UwQuestionMasterGetReq req) {
		List<DropDownRes> resList = new ArrayList<DropDownRes>();
		try {
			Date today = new Date();
			Calendar cal = new GregorianCalendar();
			cal.setTime(today);
			cal.set(Calendar.HOUR_OF_DAY, 23);
			;
			cal.set(Calendar.MINUTE, 1);
			today = cal.getTime();
			cal.set(Calendar.HOUR_OF_DAY, 1);
			cal.set(Calendar.MINUTE, 1);
			Date todayEnd = cal.getTime();

			// Criteria
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<UWQuestionsMaster> query = cb.createQuery(UWQuestionsMaster.class);
			List<UWQuestionsMaster> list = new ArrayList<UWQuestionsMaster>();
			// Find All
			Root<UWQuestionsMaster> c = query.from(UWQuestionsMaster.class);
			// Select
			query.select(c);
			// Order By
			List<Order> orderList = new ArrayList<Order>();
			orderList.add(cb.asc(c.get("uwQuestionDesc")));

			// Effective Date Start Max Filter
			Subquery<Long> effectiveDate = query.subquery(Long.class);
			Root<UWQuestionsMaster> ocpm1 = effectiveDate.from(UWQuestionsMaster.class);
			effectiveDate.select(cb.max(ocpm1.get("effectiveDateStart")));
			Predicate a1 = cb.equal(c.get("uwQuestionId"), ocpm1.get("uwQuestionId"));
			Predicate a2 = cb.lessThanOrEqualTo(ocpm1.get("effectiveDateStart"), today);
			Predicate a3 = cb.equal(c.get("productId"), ocpm1.get("productId"));

			effectiveDate.where(a1, a2, a3);
			// Effective Date End Max Filter
			Subquery<Long> effectiveDate2 = query.subquery(Long.class);
			Root<UWQuestionsMaster> ocpm2 = effectiveDate2.from(UWQuestionsMaster.class);
			effectiveDate2.select(cb.max(ocpm2.get("effectiveDateEnd")));
			Predicate a6 = cb.equal(c.get("uwQuestionId"), ocpm2.get("uwQuestionId"));
			Predicate a7 = cb.greaterThanOrEqualTo(ocpm2.get("effectiveDateEnd"), todayEnd);
			Predicate a8 = cb.equal(c.get("productId"), ocpm2.get("productId"));

			effectiveDate2.where(a6, a7, a8);
			// Where
			// Where
			javax.persistence.criteria.Predicate n1 = cb.equal(c.get("status"), "Y");
			javax.persistence.criteria.Predicate n2 = cb.equal(c.get("effectiveDateStart"), effectiveDate);
			javax.persistence.criteria.Predicate n3 = cb.equal(c.get("companyId"), req.getCompanyId());
			javax.persistence.criteria.Predicate n4 = cb.equal(c.get("productId"), req.getProductId());
			javax.persistence.criteria.Predicate n5 = cb.equal(c.get("effectiveDateEnd"), effectiveDate2);

			query.where(n1, n2, n3, n4, n5).orderBy(orderList);

			// Get Result
			TypedQuery<UWQuestionsMaster> result = em.createQuery(query);
			list = result.getResultList();
			for (UWQuestionsMaster data : list) {
				// Response
				DropDownRes res = new DropDownRes();
				res.setCode(data.getUwQuestionId().toString());
				res.setCodeDesc(data.getUwQuestionDesc());
				resList.add(res);
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Exception is --->" + e.getMessage());
			return null;
		}
		return resList;
	}

}
