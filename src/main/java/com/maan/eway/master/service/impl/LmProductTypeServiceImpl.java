/**
 * 
 */
package com.maan.eway.master.service.impl;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.maan.eway.bean.LmProductType;
import com.maan.eway.error.Error;
import com.maan.eway.master.req.LmProductTypeChangeStatus;
import com.maan.eway.master.req.LmProductTypeDropDownReq;
import com.maan.eway.master.req.LmProductTypeGetDetailsReq;
import com.maan.eway.master.req.LmProductTypeSaveReq;
import com.maan.eway.master.res.LmProductTypeResponse;
import com.maan.eway.master.service.LmProductTypeService;
import com.maan.eway.repository.LmProductTypeRepository;
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

/**
 * @author Shanishg
 *
 */
@Service
public class LmProductTypeServiceImpl implements LmProductTypeService {

	@Autowired
	private LmProductTypeRepository repo;

	@PersistenceContext
	private EntityManager em;

	
	@Override
	public List<Error> validation(LmProductTypeSaveReq req) {
		List<Error> error = new ArrayList<Error>();
		
			if(StringUtils.isBlank(req.getPT_TYPE()))
			{
				error.add(new Error("O1","Pt Type","Please Enter Pt Type"));
			}
			else if(req.getPT_TYPE().length()>20)
			{
				error.add(new Error("O2","Pt Type","Please Enter Pt Type below 20 character"));
			}
			
			if(StringUtils.isBlank(req.getPT_TYPE_DESC()))
			{
				error.add(new Error("O3","Pt Type Desc","Please Enter Pt Type Desc"));
			}
			else if(req.getPT_TYPE_DESC().length()>100)
			{
				error.add(new Error("O4","Pt Type Desc","Please Enter Pt Type Desc below 100 character"));
			}
			
			if(StringUtils.isBlank(req.getCREATED_BY()))
			{
				error.add(new Error("05","Created By","Please Enter Created By"));
			}
			else if(req.getCREATED_BY().length()>100)
			{
				error.add(new Error("O6","Created By","Please Enter Created By below 100 character"));
			}
			
			if(StringUtils.isBlank(req.getUPDATED_BY()))
			{
				error.add(new Error("O7","Updated By","Please Enter Updated By"));
			}
			else if(req.getUPDATED_BY().length()>100)
			{
				error.add(new Error("O8","Updated By","Please Enter Updated By below 100 character"));
			}
			
			if (StringUtils.isBlank(req.getSTATUS())) {
				error.add(new Error("09", "Status", "Please Enter Status"));
				} else if (req.getSTATUS().length() > 1) {
					error.add(new Error("09", "Status", "Enter Status in One Character Only"));
				} else if(!("Y".equalsIgnoreCase(req.getSTATUS())||"N".equalsIgnoreCase(req.getSTATUS())||"R".equalsIgnoreCase(req.getSTATUS())|| "P".equalsIgnoreCase(req.getSTATUS()))) {
					error.add(new Error("09", "Status", "Please Select Valid Status - Active or Deactive or Pending or Referral "));
				}
		return error;
	}

	@Override
	public SuccessRes saveProductTypes(LmProductTypeSaveReq req) {
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/YYYY");
		SuccessRes data = new SuccessRes();
		
		
		LmProductType productType = new LmProductType();
		List<LmProductType> list = new ArrayList<LmProductType>();
		
		try
		{
			int amendid = 0;
			Date startdate = req.getEFFECTIVE_DATE_START();
			String enddate = "31/12/2050";
			Date end = format.parse(enddate);
			long MILLIS_IN_A_DAY = 1000 * 60 * 60 * 24;
			Date oldEndDate = new Date(req.getEFFECTIVE_DATE_START().getTime() - MILLIS_IN_A_DAY);
			Date entrydate = null;
			
			String ptType = "";
			{
				CriteriaBuilder cb = em.getCriteriaBuilder();
				CriteriaQuery<LmProductType> query = cb.createQuery(LmProductType.class);
				Root<LmProductType> root = query.from(LmProductType.class);
				query.select(root);
				
				
				
				jakarta.persistence.criteria.Predicate p1 = cb.equal(root.get("ptType"), req.getPT_TYPE());
				
				query.where(p1);
				
				TypedQuery<LmProductType> tq = em.createQuery(query);
				int limit = 0 , offset = 1 ;
				tq.setFirstResult(limit * offset);
				tq.setMaxResults(offset);
				
				list = tq.getResultList();
				
			}
			
			
			
			if(list.size() > 0)
			{
				
									
				ptType = req.getPT_TYPE();
				CriteriaBuilder cb1 = em.getCriteriaBuilder();
				CriteriaQuery<LmProductType> query1 = cb1.createQuery(LmProductType.class);


				Root<LmProductType> b = query1.from(LmProductType.class);

				query1.select(b);

				List<Order> orderList = new ArrayList<Order>();

		       jakarta.persistence.criteria.Predicate p2 = cb1.equal(b.get("ptType"), req.getPT_TYPE());
		       jakarta.persistence.criteria.Predicate n1 = cb1.equal(b.get("status"), "Y");

				query1.where(p2,n1).orderBy(orderList);

				TypedQuery<LmProductType> result = em.createQuery(query1);
				int limit1 = 0 , offset1 = 2 ;
				result.setFirstResult(limit1 * offset1);
				result.setMaxResults(offset1);
				list = result.getResultList();

				if (list.size() > 0) {
					Date beforeOneDay = new Date(new Date().getTime() - MILLIS_IN_A_DAY);
					
					if ( list.get(0).getEffectiveDateStart().before(beforeOneDay)  ) {
						amendid = list.get(0).getAmendId() + 1 ;
						entrydate = new Date() ;
						LmProductType lastRecord = list.get(0);
							lastRecord.setEffectiveDateEnd(oldEndDate);
							repo.saveAndFlush(lastRecord);
						
					} else {
						amendid = list.get(0).getAmendId() ;
						entrydate = list.get(0).getEntryDate() ;
						productType = list.get(0) ;
						if (list.size()>1 ) {
							LmProductType lastRecord = list.get(1);
							lastRecord.setEffectiveDateEnd(oldEndDate);
							savedetails(data,productType,req,entrydate,amendid);
							repo.saveAndFlush(lastRecord);
						}
					
				    }
				}
				savedetails(data,productType,req,entrydate,amendid);
				repo.saveAndFlush(productType);
				data.setResponse("Updated Successfully ");
				data.setSuccessId(ptType);

				}
			
			
			else
			{
				CriteriaBuilder cb2 = em.getCriteriaBuilder();
				CriteriaQuery<LmProductType> query2 = cb2.createQuery(LmProductType.class);
				Root<LmProductType> root2 = query2.from(LmProductType.class);
				query2.select(root2);
				
				
				
				jakarta.persistence.criteria.Predicate p1 = cb2.equal(root2.get("ptTypeDesc"), req.getPT_TYPE_DESC());
				
				query2.where(p1);
				
				TypedQuery<LmProductType> tq2 = em.createQuery(query2);
				int limit = 0 , offset = 1 ;
				tq2.setFirstResult(limit * offset);
				tq2.setMaxResults(offset);
				
				list = tq2.getResultList();
			
			

			    if(list.size() > 0)
			   {
				    data.setResponse("Pt Type Description is already Exist");
				    data.setSuccessId(req.getPT_TYPE_DESC());
			   }
			   else
			  {
				   
				data.setResponse("Saved Successfully ");
				data.setSuccessId(req.getPT_TYPE());
				savedetails(data,productType,req,entrydate,amendid);
				repo.saveAndFlush(productType);
			   }
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	    return data;

	}

	private static LmProductType savedetails(SuccessRes data, LmProductType productType, LmProductTypeSaveReq req, Date entrydate,int amendid) {
		DozerBeanMapper mapper = new DozerBeanMapper();			
        
		mapper.map(req, productType);
		productType.setPtType(req.getPT_TYPE());
		productType.setPtTypeDesc(req.getPT_TYPE_DESC());
		productType.setEffectiveDateStart(req.getEFFECTIVE_DATE_START());;
		productType.setEffectiveDateEnd(req.getEFFECTIVE_DATE_END());
		productType.setEntryDate(entrydate);
		productType.setCreatedBy(req.getCREATED_BY());
		productType.setUpdatedBy(req.getUPDATED_BY());
		productType.setAmendId(amendid);
		productType.setStatus(req.getSTATUS());
		return productType;
	}

	@Override
	public List<LmProductTypeResponse> getallproducttypedetails(LmProductTypeGetDetailsReq req) {
		List<LmProductTypeResponse> resList = new ArrayList<LmProductTypeResponse>();
		
		DozerBeanMapper mapper = new DozerBeanMapper();
		try {
			List<LmProductType> list = new ArrayList<LmProductType>();

			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<LmProductType> query = cb.createQuery(LmProductType.class);
			
			Root<LmProductType> b = query.from(LmProductType.class);

			
			query.select(b);

			
			Subquery<Long> amendId = query.subquery(Long.class);
			Root<LmProductType> ocpm1 = amendId.from(LmProductType.class);
			amendId.select(cb.max(ocpm1.get("amendId")));
			Predicate a1 = cb.equal(ocpm1.get("ptType"), b.get("ptType"));
		//	Predicate a2 = cb.equal(ocpm1.get("companyId"), b.get("companyId"));
			

			amendId.where(a1);

		
			List<Order> orderList = new ArrayList<Order>();
			orderList.add(cb.asc(b.get("ptType")));

			
			Predicate n1 = cb.equal(b.get("amendId"), amendId);
			Predicate n2 = cb.equal(b.get("ptType"), req.getPT_TYPE());


			query.where(n1, n2).orderBy(orderList);

			TypedQuery<LmProductType> result = em.createQuery(query);
			list = result.getResultList();
			
			for (LmProductType data : list) {
				LmProductTypeResponse res = new LmProductTypeResponse();

				res = mapper.map(data, LmProductTypeResponse.class);
				res.setPT_TYPE(data.getPtType().toString());
				res.setPT_TYPE_DESC(data.getPtTypeDesc());
				res.setEFFECTIVE_DATE_START(data.getEffectiveDateStart());
				res.setEFFECTIVE_DATE_END(data.getEffectiveDateEnd());
				res.setCREATED_BY(data.getCreatedBy());
				res.setAMEND_ID(data.getAmendId());
				res.setSTATUS(data.getStatus());;
				res.setENTRY_DATE(data.getEntryDate());
				res.setUPDATED_BY(data.getUpdatedBy());
				res.setUPDATED_DATE(data.getUpdatedDate());
				resList.add(res);
			}

		} catch (Exception e) {
			e.printStackTrace();
			return null;

		}
		return resList;
	}

	@Override
	public List<LmProductTypeResponse> getproductTypeDetails(LmProductTypeGetDetailsReq req) {
		List<LmProductTypeResponse> res = new ArrayList<LmProductTypeResponse>();
		
		DozerBeanMapper mapper = new DozerBeanMapper();
		
		try
		{
			List<LmProductType> productType = new ArrayList<LmProductType>();
			
			CriteriaBuilder b = em.getCriteriaBuilder();
			CriteriaQuery<LmProductType> query = b.createQuery(LmProductType.class);
			
			Root<LmProductType> root = query.from(LmProductType.class);
			
			query.select(root);
			
			Subquery<Long> amendId = query.subquery(Long.class);
			Root<LmProductType> ocpm1 = amendId.from(LmProductType.class);
			amendId.select(b.max(ocpm1.get("amendId")));
			Predicate a1 = b.equal(ocpm1.get("ptType"), root.get("ptType"));
		
			amendId.where(a1);

		
			List<Order> orderList = new ArrayList<Order>();
			orderList.add(b.asc(root.get("ptType")));

			
			Predicate n1 = b.equal(root.get("amendId"), amendId);
			Predicate n2 = b.equal(root.get("ptType"), req.getPT_TYPE());
			Predicate n3 = b.equal(root.get("status"), "Y");


			query.where(n1, n2, n3).orderBy(orderList);

			TypedQuery<LmProductType> result = em.createQuery(query);
			productType = result.getResultList();
			
			for (LmProductType data : productType) {
				LmProductTypeResponse list = new LmProductTypeResponse();

				list = mapper.map(data, LmProductTypeResponse.class);
				list.setPT_TYPE(data.getPtType().toString());
				list.setENTRY_DATE(data.getEntryDate());
				list.setAMEND_ID(data.getAmendId());
				list.setCREATED_BY(data.getCreatedBy());
				list.setEFFECTIVE_DATE_START(data.getEffectiveDateStart());	
				list.setEFFECTIVE_DATE_END(data.getEffectiveDateEnd());
				list.setPT_TYPE_DESC(data.getPtTypeDesc());	
				list.setSTATUS(data.getStatus());
				list.setUPDATED_BY(data.getUpdatedBy());
				list.setUPDATED_DATE(data.getUpdatedDate());
				res.add(list);
			}

		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return res;
	}

	@Override
	public List<LmProductTypeResponse> getProductTypeById(LmProductTypeGetDetailsReq req) {
		 List<LmProductTypeResponse> res = new ArrayList<LmProductTypeResponse>();
			
			DozerBeanMapper mapper = new DozerBeanMapper();
			
			try
			{
				List<LmProductType> productType = new ArrayList<LmProductType>();
				
				CriteriaBuilder b = em.getCriteriaBuilder();
				CriteriaQuery<LmProductType> query = b.createQuery(LmProductType.class);
				
				Root<LmProductType> root = query.from(LmProductType.class);
				
				query.select(root);
				
				Subquery<Long> amendId = query.subquery(Long.class);
				Root<LmProductType> ocpm1 = amendId.from(LmProductType.class);
				amendId.select(b.max(ocpm1.get("amendId")));
				Predicate a1 = b.equal(ocpm1.get("ptType"), root.get("ptType"));
			

				amendId.where(a1);

			
				List<Order> orderList = new ArrayList<Order>();
				orderList.add(b.asc(root.get("ptType")));

				
				Predicate n1 = b.equal(root.get("amendId"), amendId);
				Predicate n2 = b.equal(root.get("ptType"), req.getPT_TYPE());


				query.where(n1, n2).orderBy(orderList);

				TypedQuery<LmProductType> result = em.createQuery(query);
				productType = result.getResultList();
				
				for (LmProductType data : productType) {
					LmProductTypeResponse list = new LmProductTypeResponse();

					list = mapper.map(data, LmProductTypeResponse.class);
					list.setPT_TYPE(data.getPtType().toString());
					list.setENTRY_DATE(data.getEntryDate());
					list.setAMEND_ID(data.getAmendId());
					list.setCREATED_BY(data.getCreatedBy());
					list.setEFFECTIVE_DATE_START(data.getEffectiveDateStart());	
					list.setEFFECTIVE_DATE_END(data.getEffectiveDateEnd());
					list.setPT_TYPE_DESC(data.getPtTypeDesc());	
					list.setSTATUS(data.getStatus());
					list.setUPDATED_BY(data.getUpdatedBy());
					list.setUPDATED_DATE(data.getUpdatedDate());
					res.add(list);
				}

			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			return res;
	}

	@Override
	public SuccessRes changeStatusOfProductType(LmProductTypeChangeStatus req) {
		DozerBeanMapper mapper = new DozerBeanMapper();
		SuccessRes res = new SuccessRes();
		try {

			List<LmProductType> list = new ArrayList<LmProductType>();
			
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<LmProductType> query = cb.createQuery(LmProductType.class);

			Root<LmProductType> b = query.from(LmProductType.class);

			
			query.select(b);

			Subquery<Long> amendId = query.subquery(Long.class);
			Root<LmProductType> ocpm1 = amendId.from(LmProductType.class);
			amendId.select(cb.max(ocpm1.get("amendId")));
			Predicate a1 = cb.equal(ocpm1.get("ptType"), b.get("ptType"));
			amendId.where(a1);

			
			List<Order> orderList = new ArrayList<Order>();
			orderList.add(cb.desc(b.get("ptType")));

			
			Predicate n1 = cb.equal(b.get("amendId"), amendId);
			Predicate n2 = cb.equal(b.get("ptType"), req.getPT_TYPE());
		
			query.where(n1, n2).orderBy(orderList);

			
			TypedQuery<LmProductType> result = em.createQuery(query);
			list = result.getResultList();
			LmProductType updateRecord = list.get(0);

			if (req.getPT_TYPE().equalsIgnoreCase(updateRecord.getPtType().toString())) {
				updateRecord.setStatus(req.getSTATUS());
				repo.save(updateRecord);
			} else {
				LmProductType saveNew = new LmProductType();
				mapper.map(updateRecord, saveNew);
				saveNew.setPtType(req.getPT_TYPE());
				saveNew.setStatus(req.getSTATUS());
				repo.save(saveNew);
			}

			res.setResponse("Status Changed");
			res.setSuccessId(req.getPT_TYPE());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return res;

	}

	@Override
	public List<DropDownRes> getProductTypeDropDown(LmProductTypeDropDownReq req) {
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

			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<LmProductType> query = cb.createQuery(LmProductType.class);
			List<LmProductType> list = new ArrayList<LmProductType>();

			Root<LmProductType> c = query.from(LmProductType.class);

			query.select(c);

			Subquery<Timestamp> effectiveDate = query.subquery(Timestamp.class);
			Root<LmProductType> ocpm1 = effectiveDate.from(LmProductType.class);
			effectiveDate.select(cb.greatest(ocpm1.get("effectiveDateStart")));
			jakarta.persistence.criteria.Predicate a1 = cb.equal(c.get("ptType"), ocpm1.get("ptType"));
			jakarta.persistence.criteria.Predicate a2 = cb.lessThanOrEqualTo(ocpm1.get("effectiveDateStart"), today);
			
			effectiveDate.where(a1, a2);
			
			Subquery<Timestamp> effectiveDate2 = query.subquery(Timestamp.class);
			Root<LmProductType> ocpm2 = effectiveDate2.from(LmProductType.class);
			effectiveDate2.select(cb.greatest(ocpm2.get("effectiveDateEnd")));

			jakarta.persistence.criteria.Predicate a10 = cb.greaterThanOrEqualTo(ocpm2.get("effectiveDateEnd"), todayEnd);
			effectiveDate2.where( a10);
		

			Predicate n1 = cb.equal(c.get("status"), "Y");
			Predicate n11 = cb.equal(c.get("status"), "R");
			Predicate n12 = cb.or(n1, n11);
			jakarta.persistence.criteria.Predicate n2 = cb.equal(c.get("effectiveDateStart"), effectiveDate);
			jakarta.persistence.criteria.Predicate n3 = cb.equal(c.get("ptType"), req.getPT_TYPE());;
			jakarta.persistence.criteria.Predicate n6 = cb.equal(c.get("effectiveDateEnd"), effectiveDate2);

			query.where(n12, n2, n3, n6);

			
			TypedQuery<LmProductType> result = em.createQuery(query);
			list = result.getResultList();

			for (LmProductType data : list) {
				
				DropDownRes res = new DropDownRes();
				res.setCode(data.getPtType().toString());
				res.setCodeDesc(data.getPtTypeDesc());
				res.setStatus(data.getStatus());
				resList.add(res);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return resList;

	}

}


