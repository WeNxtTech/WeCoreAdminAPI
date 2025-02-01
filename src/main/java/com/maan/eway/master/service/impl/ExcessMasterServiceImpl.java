package com.maan.eway.master.service.impl;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.maan.eway.bean.ExcessMaster;
import com.maan.eway.bean.WarrantyMaster;
import com.maan.eway.master.req.ExcessMasterReq;
import com.maan.eway.master.res.ExcessMasterRes;
import com.maan.eway.master.service.ExcessMasterService;
import com.maan.eway.repository.ExcessMasterRepository;
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

@Service
public class ExcessMasterServiceImpl  implements ExcessMasterService{

	private Logger log = LogManager.getLogger(ExcessMasterServiceImpl.class);
	
	@Autowired
	private ExcessMasterRepository excessRepo;
	
	@PersistenceContext
	private EntityManager em;
	

	@Override
	public List<SuccessRes> saveExcess(List<ExcessMasterReq> req) {
		// TODO Auto-generated method stub
		List<SuccessRes> response =null;
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		DozerBeanMapper dozerMapper = new DozerBeanMapper();

		try {
		log.info("Save Starts -->");
       //save
		Integer amendId=0;
		Integer ExcessId=0;
		Date EntryDate=null;
		Date currentDate = formatter.parse(formatter.format(new Date()));
		String CreatedBy=null;
		long MILLS_IN_A_DAY = 1000*60*60*24;
		Calendar cal= Calendar.getInstance();
		cal.setTime(currentDate);
		cal.add(Calendar.YEAR, 5);
		Date EndDate =formatter.parse(formatter.format(cal.getTime()));
		log.info("The StartDate :"+currentDate + "EndDate is :"+EndDate);
		for(ExcessMasterReq data: req)
		{
			ExcessMaster saveReq= new ExcessMaster();
			dozerMapper.map(data, saveReq);
			if(data.getExcessId()==null || data.getExcessId()==0)
			{
		     Integer totalCount = getMasterTableCount(data.getCompanyId(),data.getProductId(),data.getSectionId(),data.getCoverId());
		     ExcessId =totalCount+1;
		     saveReq.setExcessId(ExcessId);	
		     
			}
			else {
				List<ExcessMaster> list = new ArrayList<>();	
				Date oldEndDate = new Date(currentDate.getTime()- MILLS_IN_A_DAY);

				list =(List<ExcessMaster>) updaterecords(data).getResultList();
				if(list.size()>0) {
					Date beforeOneDay = new Date(new Date().getTime()- MILLS_IN_A_DAY);
					if(list.get(0).getEffectiveDateStart().before(beforeOneDay)) {
						amendId = list.get(0).getAmendId()+1;
						EntryDate= new Date();
						CreatedBy= data.getCreatedBy();
						ExcessMaster lastRecord = list.get(0);
						lastRecord.setEffectiveDateEnd(oldEndDate);
						excessRepo.saveAndFlush(lastRecord);
					}
					else {
						saveReq = list.get(0);
						amendId=list.get(0).getAmendId();
						EntryDate=list.get(0).getEntryDate();
						CreatedBy= list.get(0).getCreatedBy();
						if(list.size()>1) {
							ExcessMaster lastRecord = list.get(1);	
							lastRecord.setEffectiveDateEnd(oldEndDate);
							excessRepo.saveAndFlush(lastRecord);
						}
					}
				}
			}
			saveReq.setAmendId(amendId);
			saveReq.setEffectiveDateStart(currentDate);
			saveReq.setEffectiveDateEnd(EndDate);
			saveReq.setEntryDate(currentDate);
			saveReq.setExcessId(ExcessId);
			saveReq.setCreatedBy(CreatedBy);
			excessRepo.saveAndFlush(saveReq);
			log.info("Saved Details is --> " + saveReq);	

		}
		}
		catch(Exception error)
		{
			log.info("Exception Occured -->"+ error.getMessage());
		     error.printStackTrace();
		     return null;
		}
		return response;
	}

	
public Integer getMasterTableCount(String companyId, String productId, String sectionId,String CoverId)	{

	Integer data =0;
	try {
		List<ExcessMaster> list = new ArrayList<ExcessMaster>();
		// Find Latest Record
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<ExcessMaster> query = cb.createQuery(ExcessMaster.class);
		//Find all
		Root<ExcessMaster> b = query.from(ExcessMaster.class);
		// Select
		query.select(b);
		// Effective Date Max Filter
		Subquery<Date> effectiveDate = query.subquery(Date.class);
		Root<ExcessMaster> ocpm1 = effectiveDate.from(ExcessMaster.class);
		effectiveDate.select(cb.greatest(ocpm1.get("effectiveDateStart").as(Date.class)));
		Predicate a1 = cb.equal(ocpm1.get("excessId"),b.get("excessId"));
		Predicate a2 = cb.equal(ocpm1.get("companyId"),b.get("companyId"));
		Predicate a3 = cb.equal(ocpm1.get("branchCode"),b.get("branchCode"));
		Predicate a4 = cb.equal(ocpm1.get("productId"),b.get("productId"));
		Predicate a5 = cb.equal(ocpm1.get("sectionId"),b.get("sectionId"));
		Predicate a6 = cb.equal(ocpm1.get("coverId"),b.get("coverId"));

		
		effectiveDate.where(a1,a2,a3,a4,a5,a6);
	
		//OrderBy
		List<Order> orderList = new ArrayList<Order>();
		orderList.add(cb.desc(b.get("excessId")));
		
		Predicate n1 = cb.equal(b.get("effectiveDateStart"),effectiveDate);
		Predicate n2 = cb.equal(b.get("companyId"),companyId);
		Predicate n6 = cb.equal(b.get("productId"),productId);
		Predicate n9 = cb.equal(b.get("sectionId"),sectionId);
		Predicate n10 = cb.equal(b.get("coverId"),CoverId);
		
		
		query.where(n1,n2,n6,n9,n10).orderBy(orderList);
				
		
		// Get Result
		TypedQuery<ExcessMaster> result = em.createQuery(query);
		int limit = 0 , offset = 1 ;
		result.setFirstResult(limit * offset);
		result.setMaxResults(offset);
		list = result.getResultList();
		data = list.size() > 0 ? list.get(0).getExcessId() : 0 ;
	}
	catch(Exception e) {
		e.printStackTrace();
		log.info(e.getMessage());
	}
	return data;
}

public TypedQuery<ExcessMaster>  updaterecords(ExcessMasterReq req)
{

	
	CriteriaBuilder cb = em.getCriteriaBuilder();
	CriteriaQuery<ExcessMaster> query = cb.createQuery(ExcessMaster.class);
	//Findall
	Root<ExcessMaster> b = query.from(ExcessMaster.class);
	//select
	query.select(b);
	//Orderby
	List<Order> orderList = new ArrayList<Order>();
	orderList.add(cb.desc(b.get("effectiveDateStart")));
	//Where
	Predicate n1 = cb.equal(b.get("excessId"),req.getExcessId());
	Predicate n2 = cb.equal(b.get("companyId"),req.getCompanyId());
	Predicate n3 = cb.equal(b.get("branchCode"),req.getBranchCode());
	Predicate n4 = cb.equal(b.get("productId"),req.getProductId());
	Predicate n5 = cb.equal(b.get("sectionId"),req.getSectionId());
     Predicate n6 = cb.equal(b.get("coverId"),req.getCoverId());
//		
	query.where(n1,n2,n3,n4,n5,n6).orderBy(orderList);
	
	// Get Result
	TypedQuery<ExcessMaster> result = em.createQuery(query);
	int limit=0, offset=2;
	result.setFirstResult(limit * offset);
	result.setMaxResults(offset);
	
return result;
}





@Override
public List<ExcessMasterRes> getExcessMasterDropdown(ExcessMasterDropdownReq req) {

	List<ExcessMasterRes> resList = new ArrayList<ExcessMasterRes>();
	DozerBeanMapper mapper = new DozerBeanMapper();
	try {
		List<ExcessMaster> list = new ArrayList<ExcessMaster>();
	
		// Find Latest Record
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<ExcessMaster> query = cb.createQuery(ExcessMaster.class);

		// Find All
		Root<ExcessMaster> b = query.from(ExcessMaster.class);

		// Select
		query.select(b);

		// Amend ID Max Filter
		Subquery<Long> amendId = query.subquery(Long.class);
		Root<ExcessMaster> ocpm1 = amendId.from(ExcessMaster.class);
		amendId.select(cb.max(ocpm1.get("amendId")));
		Predicate a1 = cb.equal(ocpm1.get("excessId"), b.get("excessId"));
		Predicate a2 = cb.equal(ocpm1.get("companyId"), b.get("companyId"));
		Predicate a3 = cb.equal(ocpm1.get("branchCode"),b.get("branchCode"));
		Predicate a4 = cb.equal(ocpm1.get("productId"),b.get("productId"));
		Predicate a5 = cb.equal(ocpm1.get("sectionId"),b.get("sectionId"));
		Predicate a6 = cb.equal(ocpm1.get("coverId"),b.get("coverId"));

		amendId.where(a1, a2,a3,a4,a5,a6);

		// Order By
		List<Order> orderList = new ArrayList<Order>();
		orderList.add(cb.asc(b.get("sectionId")));

		// Where
		Predicate n1 = cb.equal(b.get("amendId"), amendId);
		Predicate n2 = cb.equal(b.get("companyId"),req.getCompanyId());
		Predicate n3 = cb.equal(b.get("branchCode"), req.getBranchCode());
		Predicate n4 = cb.equal(b.get("branchCode"),"99999");
      	Predicate n5 = cb.or(n3,n4);
		Predicate n6 = cb.equal(b.get("productId"), req.getProductId());
		Predicate n7= cb.equal(b.get("sectionId"), req.getSectionId());
		Predicate n8= cb.equal(b.get("status"), "Y");

		query.where(n1,n2,n5,n6,n7,n8).orderBy(orderList);
		
		// Get Result
		TypedQuery<ExcessMaster> result = em.createQuery(query);
		list =  result.getResultList();
		list.sort(Comparator.comparing(ExcessMaster::getExcessDescription));

		// Map
		for (ExcessMaster data : list) {
			ExcessMasterRes res = new ExcessMasterRes();

			res = mapper.map(data, ExcessMasterRes.class);
			
			resList.add(res);
		}

	} catch (Exception e) {
		e.printStackTrace();
		log.info(e.getMessage());
		return null;

	}
	return resList;

	
}
public List<ExcessMasterRes> getallExcessMaster(ExcessMasterReq  req) {
	List<ExcessMasterRes> resList = new ArrayList<ExcessMasterRes>();
	DozerBeanMapper mapper = new DozerBeanMapper();
	try {
		List<ExcessMaster> list = new ArrayList<ExcessMaster>();
	
		// Find Latest Record
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<ExcessMaster> query = cb.createQuery(ExcessMaster.class);

		// Find All
		Root<ExcessMaster> b = query.from(ExcessMaster.class);

		// Select
		query.select(b);

		// Amend ID Max Filter
		Subquery<Long> amendId = query.subquery(Long.class);
		Root<ExcessMaster> ocpm1 = amendId.from(ExcessMaster.class);
		amendId.select(cb.max(ocpm1.get("amendId")));
		Predicate a1 = cb.equal(ocpm1.get("excessId"), b.get("excessId"));
		Predicate a2 = cb.equal(ocpm1.get("companyId"), b.get("companyId"));
		Predicate a3 = cb.equal(ocpm1.get("branchCode"),b.get("branchCode"));
		Predicate a4 = cb.equal(ocpm1.get("productId"),b.get("productId"));
		Predicate a5 = cb.equal(ocpm1.get("sectionId"),b.get("sectionId"));
		Predicate a6 = cb.equal(ocpm1.get("coverId"),b.get("coverId"));

		amendId.where(a1, a2,a3,a4,a5,a6);

		// Order By
		List<Order> orderList = new ArrayList<Order>();
		orderList.add(cb.asc(b.get("sectionId")));

		// Where
		Predicate n1 = cb.equal(b.get("amendId"), amendId);
		Predicate n2 = cb.equal(b.get("companyId"),req.getCompanyId());
		Predicate n3 = cb.equal(b.get("branchCode"), req.getBranchCode());
		Predicate n4 = cb.equal(b.get("branchCode"),"99999");
      	Predicate n5 = cb.or(n3,n4);
		Predicate n6 = cb.equal(b.get("productId"), req.getProductId());
		
		query.where(n1,n2,n5,n6).orderBy(orderList);
		
		// Get Result
		TypedQuery<ExcessMaster> result = em.createQuery(query);
		list =  result.getResultList();
		list.sort(Comparator.comparing(ExcessMaster::getExcessId));

		// Map
		for (ExcessMaster data : list) {
			ExcessMasterRes res = new ExcessMasterRes();

			res = mapper.map(data, ExcessMasterRes.class);
			
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
public ExcessMasterRes getExcessMasterById(ExcessMasterDropdownReq req) {

	ExcessMasterRes resList = new ExcessMasterRes();
	DozerBeanMapper mapper = new DozerBeanMapper();
	try {
		List<ExcessMaster> list = new ArrayList<ExcessMaster>();
	
		// Find Latest Record
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<ExcessMaster> query = cb.createQuery(ExcessMaster.class);

		// Find All
		Root<ExcessMaster> b = query.from(ExcessMaster.class);

		// Select
		query.select(b);

		// Amend ID Max Filter
		Subquery<Long> amendId = query.subquery(Long.class);
		Root<ExcessMaster> ocpm1 = amendId.from(ExcessMaster.class);
		amendId.select(cb.max(ocpm1.get("amendId")));
		Predicate a1 = cb.equal(ocpm1.get("excessId"), b.get("excessId"));
		Predicate a2 = cb.equal(ocpm1.get("companyId"), b.get("companyId"));
		Predicate a3 = cb.equal(ocpm1.get("branchCode"),b.get("branchCode"));
		Predicate a4 = cb.equal(ocpm1.get("productId"),b.get("productId"));
		Predicate a5 = cb.equal(ocpm1.get("sectionId"), b.get("sectionId"));
		Predicate a6 = cb.equal(ocpm1.get("coverId"),  b.get("coverId"));


		amendId.where(a1, a2,a3,a4,a5,a6);

		// Order By
		List<Order> orderList = new ArrayList<Order>();
		orderList.add(cb.asc(b.get("sectionId")));

		// Where
		Predicate n1 = cb.equal(b.get("amendId"), amendId);
		Predicate n2 = cb.equal(b.get("companyId"),req.getCompanyId());
		Predicate n3 = cb.equal(b.get("branchCode"), req.getBranchCode());
		Predicate n4 = cb.equal(b.get("branchCode"),"99999");
      	Predicate n5 = cb.or(n3,n4);
		Predicate n6 = cb.equal(b.get("productId"), req.getProductId());
		Predicate n7 = cb.equal(b.get("sectionId"), req.getSectionId());
		Predicate n8 = cb.equal(b.get("coverId"), req.getCoverId());
		Predicate n9 = cb.equal(b.get("excessId"),req.getExcessId());


		query.where(n1,n2,n5,n6,n7,n8,n9).orderBy(orderList);
		
		// Get Result
		TypedQuery<ExcessMaster> result = em.createQuery(query);
		list =  result.getResultList();
		
		
		for (Field field : ExcessMaster.class.getDeclaredFields()) {
		    field.setAccessible(true);
		    field.set(resList, field.get(list.get(0)));
		}

	} catch (Exception e) {
		e.printStackTrace();
		log.info(e.getMessage());
		return null;

	}
	return resList;

}





}
