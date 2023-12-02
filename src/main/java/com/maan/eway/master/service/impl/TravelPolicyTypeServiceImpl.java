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
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.apache.commons.lang3.StringUtils;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.maan.eway.bean.LmTariffDet;
import com.maan.eway.bean.TravelPolicyType;
import com.maan.eway.error.Error;
import com.maan.eway.master.req.TravelPolicyTypeGetReq;
import com.maan.eway.master.req.TravelPolicyTypeSaveReq;
import com.maan.eway.master.res.TravelPolicyTypeGetRes;
import com.maan.eway.master.res.TravelPolicyTypeGetRes1;
import com.maan.eway.master.service.TravelPolicyTypeService;
import com.maan.eway.repository.TravelPolicyTypeRepository;
import com.maan.eway.res.SuccessRes;

@Service
public class TravelPolicyTypeServiceImpl implements TravelPolicyTypeService {
	
	@Autowired
	private TravelPolicyTypeRepository repo;
	
	@PersistenceContext
	private EntityManager em;

	@Override
	public List<Error> validateTravelPolicyType(TravelPolicyTypeSaveReq req) {
		List<Error> error = new ArrayList<Error>();
		
		if(req.getPolicyTypeId()==null)
		{
			error.add(new Error("O1","Policy Type Id","Please Enter Policy Type Id"));
		}
		
		if(req.getPlanTypeId()==null)
		{
			error.add(new Error("O2","Plan Type Id","Please Enter Plan Type Id"));
		}
		
		if(req.getCoverId()==null)
		{
			error.add(new Error("O3","Cover Id","Please Enter Cover Id"));
		}
		
		if(req.getSubCoverId()==null)
		{
			error.add(new Error("O4","SubCover Id","Please Enter SubCover Id"));
		}
		
//		if(req.getAmendId()==null)
//		{
//			error.add(new Error("O5","Amend Id","Please Enter Amend Id"));
//		}
//		
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
		
		if(StringUtils.isBlank(req.getSubCoverDesc()))
		{
			error.add(new Error("12","SubCover Description","Please Enter SubCover Description"));
		}
		else if(req.getSubCoverDesc().length()>100)
		{
			error.add(new Error("12","SubCover Description","Please Enter SubCover Description below 100 character"));
		}
		
		if(req.getCurrency()==null)
		{
			error.add(new Error("13","Currency","Please Enter Currency"));
		}
		
		if(req.getSumInsured()==null)
		{
			error.add(new Error("14","Sum Insured","Please Enter Sum Insured"));
		}
		
		if(req.getExcessAmt()==null)
		{
			error.add(new Error("15","Excess Amount","Please Enter Excess Amount"));
		}
		
		Calendar cal = new GregorianCalendar();
		Date today = new Date();
		cal.setTime(today);
		cal.add(Calendar.DAY_OF_MONTH, -1);
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 50);
		today = cal.getTime();
		
		if (req.getEffectiveStartdate() == null) {
			error.add(new Error("16", "EffectiveStartDate", "Please Enter Effective Start Date"));

		} else if (req.getEffectiveStartdate().before(today)) {
			error.add(new Error("16", "EffectiveStartDate", "Please Enter Effective Start Date as Future Date"));
		}
		
		if (req.getEffectiveEnddate()==null) {
			error.add(new Error("17", "EffectiveEnddate", "Please Enter EffectiveEnddate"));
		}else if (req.getEffectiveStartdate()!=null && req.getEffectiveEnddate()!=null) {
			if( req.getEffectiveStartdate().after(req.getEffectiveEnddate())) {
				error.add(new Error("17", "EffectiveEnddate", "EffectiveStartdate After EffectiveEnddate Not Allwoed"));	
			}
		}
		
		if (req.getEntryDate() == null) {
			error.add(new Error("18", "Entry Date", "Please Enter Entry Date"));

		}
		
		if (StringUtils.isBlank(req.getRemarks())) {
			error.add(new Error("19", "Remarks", "Please Enter Remarks"));
		} else if (req.getRemarks().length() > 100) {
			error.add(new Error("19", "Remarks", "Enter Remarks  within 100 Characters Only"));
		}
		
		if (StringUtils.isBlank(req.getStatus())) {
			error.add(new Error("20", "Status", "Please Enter Status"));
			} else if (req.getStatus().length() > 1) {
				error.add(new Error("20", "Status", "Enter Status in One Character Only"));
			} else if(!("Y".equalsIgnoreCase(req.getStatus())||"N".equalsIgnoreCase(req.getStatus())||"R".equalsIgnoreCase(req.getStatus())|| "P".equalsIgnoreCase(req.getStatus()))) {
				error.add(new Error("20", "Status", "Please Select Valid Status - Active or Deactive or Pending or Referral "));
			}
		
		if (StringUtils.isBlank(req.getCoverStatus())) {
			error.add(new Error("21", "Cover Status", "Please Enter Cover Status"));
		} else if (req.getCoverStatus().length() > 100) {
			error.add(new Error("21", "Cover Status", "Enter Cover Status within 100 Characters Only"));
		}
		
		if (StringUtils.isBlank(req.getUpdatedBy())) {
			error.add(new Error("22", "Updated By", "Please Enter Updated By"));
		} else if (req.getUpdatedBy().length() > 50) {
			error.add(new Error("22", "Updated By", "Enter Updated By within 50 Characters Only"));
		}
		return error;
	}

	@Override
	public SuccessRes insertTravelPolicyType(TravelPolicyTypeSaveReq req) {
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/YYYY");
		SuccessRes data = new SuccessRes();

		TravelPolicyType pt = new TravelPolicyType();
		List<TravelPolicyType> list = new ArrayList<TravelPolicyType>();

		try
		{
		int amendId = 0;
		Date startdate = req.getEffectiveStartdate();
		String enddate = "31/12/2050";
		Date end = format.parse(enddate);
		long MILLIS_IN_A_DAY = 1000 * 60 * 60 * 24;
		Date oldEndDate = new Date(req.getEffectiveStartdate().getTime() - MILLIS_IN_A_DAY);
		Date entrydate = null;

		Integer subCoverId = 0;
		{
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<TravelPolicyType> query = cb.createQuery(TravelPolicyType.class);
		Root<TravelPolicyType> root = query.from(TravelPolicyType.class);
		query.select(root);

		Predicate p1 = cb.equal(root.get("subCoverId"), req.getSubCoverId());

		query.where(p1);

		TypedQuery<TravelPolicyType> tq = em.createQuery(query);
		int limit = 0 , offset = 1 ;
		tq.setFirstResult(limit * offset);
		tq.setMaxResults(offset);

		list = tq.getResultList();

		}

		if(list.size() > 0)
		{
		subCoverId= req.getSubCoverId();
		CriteriaBuilder cb1 = em.getCriteriaBuilder();
		CriteriaQuery<TravelPolicyType> query1 = cb1.createQuery(TravelPolicyType.class);

		Root<TravelPolicyType> b = query1.from(TravelPolicyType.class);

		Subquery<Long> maxAmendId = query1.subquery(Long.class);
		Root<TravelPolicyType> ocpm1 = maxAmendId.from(TravelPolicyType.class);
		maxAmendId.select(cb1.max(ocpm1.get("amendId")));
		Predicate a1 = cb1.equal(ocpm1.get("companyId"), b.get("companyId"));
		Predicate a2 = cb1.equal(ocpm1.get("productId"), b.get("productId"));
		Predicate a3 = cb1.equal(ocpm1.get("policyTypeId"), b.get("policyTypeId"));
		Predicate a4 = cb1.equal(ocpm1.get("planTypeId"), b.get("planTypeId"));
		Predicate a5 = cb1.equal(ocpm1.get("branchCode"), b.get("branchCode"));
		maxAmendId.where(a1,a2,a3,a4,a5);

		// Order By
		List<Order> orderList = new ArrayList<Order>();
		orderList.add(cb1.desc(b.get("amendId")));

		// Where
		Predicate n1 = cb1.equal(b.get("companyId"), req.getCompanyId());
		Predicate n2 = cb1.equal(b.get("productId"), req.getProductId());
		Predicate n3 = cb1.equal(b.get("policyTypeId"), req.getPolicyTypeId());
		Predicate n4 = cb1.equal(b.get("planTypeId"), req.getPlanTypeId());
		Predicate n5 = cb1.equal(b.get("branchCode"), req.getBranchCode());

		query1.where(n1, n2, n3, n4, n5).orderBy(orderList);

		TypedQuery<TravelPolicyType> result = em.createQuery(query1);
		int limit1 = 0 , offset1 = 2 ;
		result.setFirstResult(limit1 * offset1);
		result.setMaxResults(offset1);
		list = result.getResultList();

		if (list.size() > 0) {
		Date beforeOneDay = new Date(new Date().getTime() - MILLIS_IN_A_DAY);

		if ( list.get(0).getEffectiveStartdate().before(beforeOneDay)  ) {
		amendId = list.get(0).getAmendId() + 1 ;
		entrydate = new Date() ;
		TravelPolicyType lastRecord = list.get(0);
		lastRecord.setEffectiveEnddate(oldEndDate);
		savedetails(data,pt,req,entrydate,amendId);
		repo.saveAndFlush(lastRecord);

		} else {
		amendId = list.get(0).getAmendId() ;
		entrydate = list.get(0).getEntryDate() ;
		pt = list.get(0) ;
		if (list.size()>1 ) {
		TravelPolicyType lastRecord = list.get(1);
		lastRecord.setEffectiveEnddate(oldEndDate);
		savedetails(data,pt,req,entrydate,amendId);
		repo.saveAndFlush(lastRecord);
		}
	}
}
		savedetails(data,pt,req,entrydate,amendId);
		repo.save(pt);
		data.setResponse("Updated Successfully ");
		data.setSuccessId(subCoverId.toString());

		}
		else
		{
		CriteriaBuilder cb2 = em.getCriteriaBuilder();
		CriteriaQuery<TravelPolicyType> query2 = cb2.createQuery(TravelPolicyType.class);
		Root<TravelPolicyType> root2 = query2.from(TravelPolicyType.class);
		query2.select(root2);

		Predicate p1 = cb2.equal(root2.get("subCoverDesc"), req.getSubCoverId());

		query2.where(p1);

		TypedQuery<TravelPolicyType> tq2 = em.createQuery(query2);
		int limit = 0 , offset = 1 ;
		tq2.setFirstResult(limit * offset);
		tq2.setMaxResults(offset);

		list = tq2.getResultList();

		    if(list.size() > 0)
		   {
		    data.setResponse("Sub Cover Description is already Exist");
		    data.setSuccessId(req.getSubCoverId().toString());
		   }
		   else
		  {
		data.setResponse("Saved Successfully ");
		data.setSuccessId(req.getSubCoverId().toString());
		savedetails(data,pt,req,entrydate,amendId);
		repo.saveAndFlush(pt);
		   }
		}
		} 
		
		catch (Exception e) {
			e.printStackTrace();
		}
		return data;
	}

	private static TravelPolicyType savedetails(SuccessRes data, TravelPolicyType pt, TravelPolicyTypeSaveReq req, Date entrydate,int amendId) {
		DozerBeanMapper mapper = new DozerBeanMapper();			
		mapper.map(req, pt);
		pt.setAmendId(amendId);
		pt.setPolicyTypeId(Integer.valueOf(req.getPolicyTypeId()));
		pt.setPlanTypeId(Integer.valueOf(req.getPlanTypeId()));
		pt.setPolicyTypeDesc(req.getPolicyTypeDesc());
		pt.setPlanTypeDesc(req.getPlanTypeDesc());
		pt.setCoverId(Integer.valueOf(req.getCoverId()));
		pt.setSubCoverId(Integer.valueOf(req.getSubCoverId()));
		pt.setCompanyId(req.getCompanyId());
		pt.setProductId(Integer.valueOf(req.getProductId()));
		pt.setBranchCode(req.getBranchCode());
		pt.setSubCoverDesc(req.getSubCoverDesc());
		pt.setCurrency(req.getCurrency());
		pt.setSumInsured(req.getSumInsured());
		pt.setExcessAmt(req.getExcessAmt());
		pt.setEntryDate(entrydate);
		pt.setStatus(req.getStatus());
		pt.setRemarks(req.getRemarks());
		pt.setEffectiveStartdate(req.getEffectiveStartdate());
		pt.setEffectiveEnddate(req.getEffectiveEnddate());
		pt.setUpdatedBy(req.getUpdatedBy());
		pt.setUpdatedDate(req.getUpdatedDate());
		pt.setCoverStatus(req.getCoverStatus());
		pt.setSubCoverId(req.getSubCoverId());
		return pt;
	}

	@Override
	public TravelPolicyTypeGetRes1 getalltravelpolicytype(TravelPolicyTypeGetReq req) {
		TravelPolicyTypeGetRes1 res1 = new TravelPolicyTypeGetRes1();

		List<TravelPolicyTypeGetRes> resList = new ArrayList<TravelPolicyTypeGetRes>();

		DozerBeanMapper mapper = new DozerBeanMapper();
		
		try {
		List<TravelPolicyType> list = new ArrayList<TravelPolicyType>();

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<TravelPolicyType> query = cb.createQuery(TravelPolicyType.class);

		Root<TravelPolicyType> b = query.from(TravelPolicyType.class);


		query.select(b);


		Subquery<Long> maxAmendId = query.subquery(Long.class);
		Root<TravelPolicyType> ocpm1 = maxAmendId.from(TravelPolicyType.class);
		maxAmendId.select(cb.max(ocpm1.get("amendId")));
		Predicate a1 = cb.equal(ocpm1.get("companyId"), b.get("companyId"));
		Predicate a2 = cb.equal(ocpm1.get("productId"), b.get("productId"));
		Predicate a3 = cb.equal(ocpm1.get("policyTypeId"), b.get("policyTypeId"));
		Predicate a4 = cb.equal(ocpm1.get("planTypeId"), b.get("planTypeId"));
		Predicate a5 = cb.equal(ocpm1.get("branchCode"), b.get("branchCode"));
		maxAmendId.where(a1,a2,a3,a4,a5);

		// Order By
		List<Order> orderList = new ArrayList<Order>();
		orderList.add(cb.desc(b.get("amendId")));

		// Where
		Predicate n1 = cb.equal(b.get("companyId"), req.getCompanyId());
		Predicate n2 = cb.equal(b.get("productId"), req.getProductId());
		Predicate n3 = cb.equal(b.get("policyTypeId"), req.getPolicyTypeId());
		Predicate n4 = cb.equal(b.get("planTypeId"), req.getPlanTypeId());
		Predicate n5 = cb.equal(b.get("branchCode"), req.getBranchCode());

		query.where(n1, n2, n3, n4, n5).orderBy(orderList);

		TypedQuery<TravelPolicyType> result = em.createQuery(query);
		result.setFirstResult(req.getLimit() * req.getOffset());
		result.setMaxResults( req.getOffset());
		list = result.getResultList();
		
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
		res.setSubCoverDesc(data.getSubCoverDesc());
		res.setCurrency(data.getCurrency());
		res.setSumInsured(data.getSumInsured());
		res.setExcessAmt(data.getExcessAmt());
		res.setEntryDate(data.getEntryDate());
		res.setStatus(data.getStatus());
		res.setRemarks(data.getRemarks()==null?"":data.getRemarks());
		res.setEffectiveStartdate(data.getEffectiveStartdate());
		res.setEffectiveEnddate(data.getEffectiveEnddate());
		res.setUpdatedDate(data.getUpdatedDate());
		res.setCoverStatus(data.getCoverStatus()==null?"":data.getCoverStatus());
		resList.add(res);
		}		
		res1.setTotalCount(list.size());
		res1.setTravelPolicyType(resList);
		
		} catch (Exception e) {
		e.printStackTrace();
		return null;

		}
		return res1;

	
	}
	
	

}
