package com.maan.eway.master.service.impl;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.maan.eway.bean.ListItemValue;
import com.maan.eway.bean.ProductTaxSetup;
import com.maan.eway.bean.TaxExemptionSetup;
import com.maan.eway.error.Error;
import com.maan.eway.master.req.GetAllTaxExcemtedReq;
import com.maan.eway.master.req.TaxExemptionSaveReq;
import com.maan.eway.master.req.TaxExemtedGetRes;
import com.maan.eway.master.service.TaxExcemptionSetupService;
import com.maan.eway.repository.TaxExemptedSetupRepository;
import com.maan.eway.res.SuccessRes2;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.CriteriaUpdate;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;

@Service
@Transactional
public class TaxExemptionSetupServiceImpl implements TaxExcemptionSetupService {
 
	@PersistenceContext
	private EntityManager em;

	@Autowired
	private TaxExemptedSetupRepository repo;

	Gson json = new Gson();

	private Logger log = LogManager.getLogger(TaxExemptionSetupServiceImpl.class);

	

	
	
	
	public Integer upadateOldTaxes(TaxExemptionSaveReq req) {
		List<TaxExemptionSetup> list = new ArrayList<TaxExemptionSetup>();
		Integer amendId = 0 ;
		try {
			long MILLIS_IN_A_DAY = 1000 * 60 * 60 * 24;
			Date oldEndDate = new Date(new Date().getTime() - MILLIS_IN_A_DAY);
			Date entryDate = null ;
			entryDate = new Date();
			
			// Get Sno Record For Amend ID
			// FInd Old Record
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<TaxExemptionSetup> query = cb.createQuery(TaxExemptionSetup.class);
			//Find all
			Root<TaxExemptionSetup> b = query.from(TaxExemptionSetup.class);
			//Select 
			query.select(b);
			
			// Max AmendId
			Subquery<Long> maxAmendId = query.subquery(Long.class);
			Root<TaxExemptionSetup> ocpm1 = maxAmendId.from(TaxExemptionSetup.class);
			maxAmendId.select(cb.max(ocpm1.get("amendId")));
			Predicate a1 = cb.equal(ocpm1.get("companyId"), b.get("companyId"));
			Predicate a2 = cb.equal(ocpm1.get("countryId"), b.get("countryId"));
			Predicate a3 = cb.equal(ocpm1.get("branchCode"), b.get("branchCode"));
			Predicate a4 = cb.equal(ocpm1.get("taxFor"), b.get("taxFor"));
			Predicate a5 = cb.equal(ocpm1.get("taxId"), b.get("taxId"));
			Predicate a6 = cb.equal(ocpm1.get("productId"), b.get("productId"));
			Predicate a7 = cb.equal(ocpm1.get("sectionId"), b.get("sectionId"));
			Predicate a8 = cb.equal(ocpm1.get("coverId"), b.get("coverId"));
			
			
			
			maxAmendId.where(a1,a2,a3,a4,a5,a6,a7,a8);

			// Order By
			List<Order> orderList = new ArrayList<Order>();
			orderList.add(cb.desc(b.get("amendId")));
			
			// Where
			Predicate n1 = cb.equal(b.get("companyId"), req.getCompanyId());
			Predicate n2 = cb.equal(b.get("countryId"), req.getCountryId());
			Predicate n3 = cb.equal(b.get("branchCode"), req.getBranchCode());
			Predicate n4 = cb.equal(b.get("taxFor"), req.getTaxFor());
			Predicate n5 = cb.equal(b.get("amendId"), maxAmendId);
			Predicate n6 = cb.equal(b.get("productId"), req.getProductId());
			Predicate n7 = cb.equal(b.get("sectionId"), req.getSectionId());
			Predicate n8 = cb.equal(b.get("coverId"), req.getCoverId());
		
					
					
			query.where(n1,n2,n3,n4,n5,n6,n7,n8).orderBy(orderList);
			
			// Get Result 
			TypedQuery<TaxExemptionSetup> result = em.createQuery(query);
			list = result.getResultList();
			
			if(list.size()>0) {
				Date beforeOneDay = new Date(new Date().getTime() - MILLIS_IN_A_DAY);
			
				if ( list.get(0).getEffectiveDateStart().before(beforeOneDay)  ) {
					amendId = list.get(0).getAmendId() + 1 ;
					entryDate = new Date() ;
					
					//UPDATE
					CriteriaBuilder cb2 = em.getCriteriaBuilder();
					// create update
					CriteriaUpdate<TaxExemptionSetup> update = cb2.createCriteriaUpdate(TaxExemptionSetup.class);
					// set the root class
					Root<TaxExemptionSetup> m = update.from(TaxExemptionSetup.class);
					// set update and where clause
					
					update.set("updatedBy", req.getCreatedBy());
					update.set("updatedDate", entryDate);
					update.set("effectiveDateEnd", oldEndDate);
					
					n1 = cb.equal(m.get("companyId"), req.getCompanyId());
					n2 = cb.equal(m.get("countryId"), req.getCountryId());
					n3 = cb.equal(m.get("branchCode"), req.getBranchCode());
					n4 = cb.equal(m.get("taxFor"), req.getTaxFor() );
					n5 = cb.equal(m.get("amendId"), list.get(0).getAmendId());
					n6 = cb.equal(m.get("productId"), req.getProductId());
					n7 = cb.equal(m.get("sectionId"), req.getSectionId());
					n8 = cb.equal(m.get("coverId"), req.getCoverId());
					
					update.where(n1,n2,n3,n4,n5,n6,n7,n8);
					// perform update
					em.createQuery(update).executeUpdate();
					
				} else {
					amendId = list.get(0).getAmendId() ;
					repo.deleteAll(list);
			    }
			}
			
		} catch (Exception e) {
		e.printStackTrace();
		log.info("Exception is --->" + e.getMessage());
		return null;
	}
	return amendId;
	}
	
	public List<ProductTaxSetup> productTaxList(TaxExemptionSaveReq req) {
		List<ProductTaxSetup> taxList = new ArrayList<ProductTaxSetup>();
		try {
			// Tax List
			
			{
				// Find Latest Record
				CriteriaBuilder cb2 = em.getCriteriaBuilder();
				CriteriaQuery<ProductTaxSetup> query2 = cb2.createQuery(ProductTaxSetup.class);
	
				// Find All
				Root<ProductTaxSetup> b2 = query2.from(ProductTaxSetup.class);
	
				// Effective Date Max Filter
				Subquery<Timestamp> effectiveDate2 = query2.subquery(Timestamp.class);
				Root<ProductTaxSetup> ocpm2 = effectiveDate2.from(ProductTaxSetup.class);
				effectiveDate2.select(cb2.greatest(ocpm2.get("effectiveDateStart")));
				Predicate a1 = cb2.equal(ocpm2.get("companyId"), b2.get("companyId"));
				Predicate a2 = cb2.equal(ocpm2.get("countryId"), b2.get("countryId"));
				Predicate a3 = cb2.equal(ocpm2.get("branchCode"), b2.get("branchCode"));
				Predicate a4 = cb2.equal(ocpm2.get("taxFor"), b2.get("taxFor"));
				Predicate a5 = cb2.equal(ocpm2.get("taxId"), b2.get("taxId"));
				Predicate a6 = cb2.equal(ocpm2.get("productId"), b2.get("productId"));
				effectiveDate2.where(a1,a2,a3,a4,a5,a6);
				

			
				// Select
				query2.select(b2);
	
				// Order By
				List<Order> orderList2 = new ArrayList<Order>();
				orderList2.add(cb2.desc(b2.get("effectiveDateStart")));
	
				List<Integer> taxIds = req.getExemptedTaxIds() ;
				
				// Where
				Predicate n1 =  cb2.equal(b2.get("effectiveDateStart"),effectiveDate2);
				Predicate n2 =  cb2.equal(b2.get("countryId"), req.getCountryId());
				Predicate n4 = cb2.equal(b2.get("taxFor"), req.getTaxFor());
				Predicate n5 = cb2.equal(b2.get("branchCode"), req.getBranchCode());
				Predicate n6 = cb2.equal(b2.get("productId"), req.getProductId());
				Predicate n9 = cb2.equal(b2.get("companyId"), req.getCompanyId());
				
				//In 
				Expression<String>e0=b2.get("taxId");
				Predicate n3 = e0.in(taxIds);
				
				query2.where(n1,n2,n3,n4,n5,n6,n9).orderBy(orderList2);
				// Get Result
				TypedQuery<ProductTaxSetup> result2 = em.createQuery(query2);
				taxList  = result2.getResultList();
			}
			
		} catch (Exception e) {
		e.printStackTrace();
		log.info("Exception is --->" + e.getMessage());
		return null;
	}
	return taxList;
	}
	
	public SuccessRes2 insertNewTaxes(TaxExemptionSaveReq req , Integer amendId , List<ProductTaxSetup> productTaxes ) {
		SimpleDateFormat sdformat = new SimpleDateFormat("dd/MM/yyyy");
		sdformat.setTimeZone(TimeZone.getTimeZone("UTC"));
		
		SuccessRes2 res = new SuccessRes2();
	//	DozerBeanMapper dozerMapper = new  DozerBeanMapper();
		try {
			String end = "31/12/2050";
			
			Date endDate = sdformat.parse(end);
			
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(endDate);
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
			
			res.setResponse("Updated Successfully");
			res.setSuccessId(req.getTaxFor());
		
			// Decimal Digit
			
			List<TaxExemptionSetup> saveList = new ArrayList<TaxExemptionSetup>();
			for ( Integer taxId :  req.getExemptedTaxIds() ) {
				TaxExemptionSetup saveData = new TaxExemptionSetup();
				// Save New Records
			//	saveData = dozerMapper.map(req, CompaniesTaxSetup.class );
				saveData.setAmendId(amendId);
				saveData.setBranchCode(req.getBranchCode());
				saveData.setCompanyId(req.getCompanyId());
				saveData.setCountryId(req.getCountryId());
				saveData.setCreatedBy(req.getCreatedBy());
				saveData.setEffectiveDateStart(new Date());
				saveData.setEffectiveDateEnd(calendar.getTime());
				saveData.setEntryDate(new Date());
			//	saveData.setPriority(StringUtils.isBlank(data.getPriority()) ? null :Integer.valueOf(data.getPriority())  );
				saveData.setStatus("Y");
				//saveData.setTaxCode(data.getTaxCode());
				//saveData.setTaxExemptAllowYn(StringUtils.isBlank(data.getTaxExemptAllowYn()) ? "N" : data.getTaxExemptAllowYn());
				saveData.setTaxFor(req.getTaxFor());
				saveData.setTaxId(taxId);
				List<ProductTaxSetup> filterTax = productTaxes.stream().filter( o -> o.getTaxId().equals(saveData.getTaxId()) ).collect(Collectors.toList());
				saveData.setUpdatedBy(req.getCreatedBy());
				saveData.setUpdatedDate(new Date());
				saveData.setProductId(Integer.valueOf(req.getProductId()));
				saveData.setTaxForDesc(filterTax.size() > 0 ? filterTax.get(0).getTaxForDesc() : "" );
				saveData.setSectionId(Integer.valueOf(req.getSectionId()) );
				saveData.setCoverId(Integer.valueOf(req.getCoverId()));
				saveList.add(saveData);
			}
			repo.saveAllAndFlush(saveList);
			
		} catch (Exception e) {
		e.printStackTrace();
		log.info("Exception is --->" + e.getMessage());
		return null;
	}
	return res;
	}
	
	public synchronized List<ListItemValue> getListItem(String insuranceId , String branchCode, String itemType ) {
		List<ListItemValue> list = new ArrayList<ListItemValue>();
		try {
			Date today = new Date();
			Calendar cal = new GregorianCalendar();
			cal.setTime(today);
			today = cal.getTime();
			Date todayEnd = cal.getTime();
			
			// Criteria
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<ListItemValue> query=  cb.createQuery(ListItemValue.class);
			// Find All
			Root<ListItemValue> c = query.from(ListItemValue.class);
			
			//Select
			query.select(c);
			// Order By
			List<Order> orderList = new ArrayList<Order>();
			orderList.add(cb.asc(c.get("branchCode")));
			
			
			// Effective Date Start Max Filter
			Subquery<Timestamp> effectiveDate = query.subquery(Timestamp.class);
			Root<ListItemValue> ocpm1 = effectiveDate.from(ListItemValue.class);
			effectiveDate.select(cb.greatest(ocpm1.get("effectiveDateStart")));
			Predicate a1 = cb.equal(c.get("itemId"),ocpm1.get("itemId"));
			Predicate a2 = cb.lessThanOrEqualTo(ocpm1.get("effectiveDateStart"), today);
			effectiveDate.where(a1,a2);
			// Effective Date End Max Filter
			Subquery<Timestamp> effectiveDate2 = query.subquery(Timestamp.class);
			Root<ListItemValue> ocpm2 = effectiveDate2.from(ListItemValue.class);
			effectiveDate2.select(cb.greatest(ocpm2.get("effectiveDateEnd")));
			Predicate a3 = cb.equal(c.get("itemId"),ocpm2.get("itemId"));
			Predicate a4 = cb.greaterThanOrEqualTo(ocpm2.get("effectiveDateEnd"), todayEnd);
			effectiveDate2.where(a3,a4);
						
			// Where
			Predicate n1 = cb.equal(c.get("status"),"Y");
			Predicate n11 = cb.equal(c.get("status"),"R");
			Predicate n12 = cb.or(n1,n11);
			Predicate n2 = cb.equal(c.get("effectiveDateStart"),effectiveDate);
			Predicate n3 = cb.equal(c.get("effectiveDateEnd"),effectiveDate2);	
			Predicate n4 = cb.equal(c.get("companyId"),insuranceId);
			Predicate n6 = cb.equal(c.get("branchCode"),branchCode);
			Predicate n7 = cb.equal(c.get("branchCode"), "99999");
			Predicate n9 = cb.or(n6,n7);
			Predicate n10 = cb.equal(c.get("itemType"),itemType);
			query.where(n12,n2,n3,n4,n9,n10).orderBy(orderList);
			// Get Result
			TypedQuery<ListItemValue> result = em.createQuery(query);
			list = result.getResultList();
			
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Exception is ---> " + e.getMessage());
			return null;
		}
		return list ;
	}
	
	public synchronized String getTaxForDesc(String insuranceId , String branchCode, String itemType , String itemCode) {
		String taxForDesc = "" ;
		List<ListItemValue> list = new ArrayList<ListItemValue>();
		try {
			Date today = new Date();
			Calendar cal = new GregorianCalendar();
			cal.setTime(today);
			today = cal.getTime();
			Date todayEnd = cal.getTime();
			
			// Criteria
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<ListItemValue> query=  cb.createQuery(ListItemValue.class);
			// Find All
			Root<ListItemValue> c = query.from(ListItemValue.class);
			
			//Select
			query.select(c);
			// Order By
			List<Order> orderList = new ArrayList<Order>();
			orderList.add(cb.asc(c.get("branchCode")));
			
			
			// Effective Date Start Max Filter
			Subquery<Timestamp> effectiveDate = query.subquery(Timestamp.class);
			Root<ListItemValue> ocpm1 = effectiveDate.from(ListItemValue.class);
			effectiveDate.select(cb.greatest(ocpm1.get("effectiveDateStart")));
			Predicate a1 = cb.equal(c.get("itemId"),ocpm1.get("itemId"));
			Predicate a2 = cb.lessThanOrEqualTo(ocpm1.get("effectiveDateStart"), today);
			effectiveDate.where(a1,a2);
			// Effective Date End Max Filter
			Subquery<Timestamp> effectiveDate2 = query.subquery(Timestamp.class);
			Root<ListItemValue> ocpm2 = effectiveDate2.from(ListItemValue.class);
			effectiveDate2.select(cb.greatest(ocpm2.get("effectiveDateEnd")));
			Predicate a3 = cb.equal(c.get("itemId"),ocpm2.get("itemId"));
			Predicate a4 = cb.greaterThanOrEqualTo(ocpm2.get("effectiveDateEnd"), todayEnd);
			effectiveDate2.where(a3,a4);
						
			// Where
			Predicate n1 = cb.equal(c.get("status"),"Y");
			Predicate n11 = cb.equal(c.get("status"),"R");
			Predicate n12 = cb.or(n1,n11);
			Predicate n2 = cb.equal(c.get("effectiveDateStart"),effectiveDate);
			Predicate n3 = cb.equal(c.get("effectiveDateEnd"),effectiveDate2);	
			Predicate n4 = cb.equal(c.get("companyId"),insuranceId);
			Predicate n6 = cb.equal(c.get("branchCode"),branchCode);
			Predicate n7 = cb.equal(c.get("branchCode"), "99999");
			Predicate n9 = cb.or(n6,n7);
			Predicate n10 = cb.equal(c.get("itemType"),itemType);
			Predicate n13 = cb.equal(c.get("itemCode"),itemCode);
			query.where(n12,n2,n3,n4,n9,n10,n13).orderBy(orderList);
			// Get Result
			TypedQuery<ListItemValue> result = em.createQuery(query);
			list = result.getResultList();
			taxForDesc = list.size() > 0 ? list.get(0).getItemValue() : "" ; 
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Exception is ---> " + e.getMessage());
			return null;
		}
		return taxForDesc ;
	}






	@Override
	public List<Error> validateTaxesExemption(TaxExemptionSaveReq req) {
		List<Error> errorList = new ArrayList<Error>();

		try {	
//				
			if (StringUtils.isBlank(req.getCountryId()) ) {
				errorList.add(new Error("03", "CountryId", "Please Select Country Id "));
			}
			else if(req.getCountryId().length()>20) {
				errorList.add(new Error("03","CountryId","Pleaser enter the country between 20 characters"));
				
			}
			
			if (StringUtils.isBlank(req.getCreatedBy())) {
				errorList.add(new Error("07", "CreatedBy", "Please Enter CreatedBy"));
			} else if (req.getCreatedBy().length() > 100) {
				errorList.add(new Error("07", "CreatedBy", "Please Enter CreatedBy within 100 Characters"));
			}

			if (StringUtils.isBlank(req.getBranchCode())) {
				errorList.add(new Error("07", "BranchCode", "Please Select BranchCode"));
			}
			
			if (StringUtils.isBlank(req.getTaxFor())) {
				errorList.add(new Error("07", "Tax For", "Please Select Tax For"));
			}
			
			if (StringUtils.isBlank(req.getCompanyId())) {
				errorList.add(new Error("07", "CompanyId", "Please Select Insurance Id"));
			}
			
			if (StringUtils.isBlank(req.getProductId())) {
				errorList.add(new Error("07", "ProductId", "Please Select ProductId"));
			}
			
			if (StringUtils.isBlank(req.getProductId())) {
				errorList.add(new Error("07", "ProductId", "Please Select ProductId"));
			}
			
			if (StringUtils.isBlank(req.getCreatedBy())) {
				errorList.add(new Error("07", "CreatedBy", "Please Enter CreatedBy"));
			} else if (req.getCreatedBy().length() > 100) {
				errorList.add(new Error("07", "CreatedBy", "Please Enter CreatedBy within 100 Characters"));
			}
			if (StringUtils.isBlank(req.getSectionId())) {
				errorList.add(new Error("07", "SectionId", "Please Select Section"));
			}
			if (StringUtils.isBlank(req.getCoverId())) {
				errorList.add(new Error("07", "CoverId", "Please Select Cover"));
			}
			

//			// Date Validation
//			Calendar cal = new GregorianCalendar();
//			Date today = new Date();
//			cal.setTime(today);
//			cal.add(Calendar.DAY_OF_MONTH, -1);
//			cal.set(Calendar.HOUR_OF_DAY, 23);
//			cal.set(Calendar.MINUTE, 50);
//			today = cal.getTime();
//			if (req.getEffectiveDateStart() == null) {
//				errorList.add(new Error("04", "EffectiveDateStart", "Please Enter Effective Date Start "));
//
//			} else if (req.getEffectiveDateStart().before(today)) {
//				errorList.add(new Error("04", "EffectiveDateStart", "Please Enter Effective Date Start as Future Date"));
//			}
			
//			List<String> taxIds = new ArrayList<String>(); 
//			List<String> taxCodes = new ArrayList<String>(); 
//			List<String> coreAppCodes = new ArrayList<String>(); 
//			List<String> regulatoryCodes = new ArrayList<String>(); 
		//	List<String> priorities = new ArrayList<String>(); 
			
//			if(req.getExemptedTaxIds()==null || req.getExemptedTaxIds().size() <= 0 ) {
//				errorList.add(new Error("01", "TaxDetails", "Please Excempt Atleast One Tax "));
//			}
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
		}
		return errorList;
	}

	@Override
	public SuccessRes2 saveTaxesExemption(TaxExemptionSaveReq req) {
		SuccessRes2 res = new SuccessRes2();
		try {
			// Update Old Records
			Integer amendId = upadateOldTaxes(req ) ;
			
			// Calc Type
			//List<ListItemValue> calcTypes =  getListItem("99999" , req.getBranchCode()   ,"CALCULATION_TYPE"); 
		
			List<ProductTaxSetup> productTaxes = productTaxList(req );
			
			// Insert New Records
			res = insertNewTaxes(req , amendId , productTaxes ) ;
		
		} catch (Exception e) {
		e.printStackTrace();
		log.info("Exception is --->" + e.getMessage());
		return null;
	}
	return res;
	}

	@Override
	public TaxExemtedGetRes getallTaxesExempted(GetAllTaxExcemtedReq req) {
		TaxExemtedGetRes res = new TaxExemtedGetRes();
	//	DozerBeanMapper dozerMapper = new DozerBeanMapper();
		try {
			List<TaxExemptionSetup> exemptedlist = new ArrayList<TaxExemptionSetup>(); 
			{
				// Find Latest Record
				CriteriaBuilder cb = em.getCriteriaBuilder();
				CriteriaQuery<TaxExemptionSetup> query = cb.createQuery(TaxExemptionSetup.class);

				// Find All
				Root<TaxExemptionSetup> b = query.from(TaxExemptionSetup.class);

				// Select
				query.select(b);

				// Amend ID Max Filter
				Subquery<Long> amendId = query.subquery(Long.class);
				Root<TaxExemptionSetup> ocpm1 = amendId.from(TaxExemptionSetup.class);
				amendId.select(cb.max(ocpm1.get("amendId")));
				Predicate a1 = cb.equal(ocpm1.get("countryId"), b.get("countryId"));
				Predicate a2 = cb.equal(ocpm1.get("companyId"), b.get("companyId"));
				Predicate a3 = cb.equal(ocpm1.get("taxId"), b.get("taxId"));
				Predicate a4 = cb.equal(ocpm1.get("taxFor"), b.get("taxFor"));
				Predicate a5 = cb.equal(ocpm1.get("branchCode"), b.get("branchCode"));
				Predicate a6 = cb.equal(ocpm1.get("productId"), b.get("productId"));
				Predicate a7 = cb.equal(ocpm1.get("sectionId"), b.get("sectionId"));
				Predicate a8 = cb.equal(ocpm1.get("coverId"), b.get("coverId"));
				amendId.where(a1,a2,a3,a4,a5,a6,a7,a8);

				Predicate n1 = cb.equal(b.get("amendId"),amendId);
				Predicate n2 = cb.equal(b.get("countryId"), req.getCountryId() );	
				Predicate n3 = cb.equal(b.get("companyId"), req.getCompanyId() );		
				Predicate n4 = cb.equal(b.get("branchCode"), req.getBranchCode());
				Predicate n5 = cb.equal(b.get("taxFor"), req.getTaxFor());
				Predicate n6 = cb.equal(b.get("productId"), req.getProductId());
				Predicate n7 = cb.equal(b.get("sectionId"), req.getSectionId());
				Predicate n8 = cb.equal(b.get("coverId"), req.getCoverId());
				// Order By
//				List<Order> orderList = new ArrayList<Order>();
//				orderList.add(cb.asc(b.get("taxName")));
				query.where( n1, n2, n3,n4,n5,n6,n7,n8);//.orderBy(orderList);
				
				// Get Result
				TypedQuery<TaxExemptionSetup> result = em.createQuery(query);
				exemptedlist = result.getResultList();
			}
		
			// Get Product Taxes 
			
			res.setBranchCode(req.getBranchCode());
			res.setCompanyId(req.getCompanyId());
			res.setCountryId(req.getCountryId());
			res.setTaxFor(req.getTaxFor());
			res.setProductId(req.getProductId());
			res.setSectionId(req.getSectionId());
			res.setCoverId(req.getProductId());
			
			List<Integer> exemptedTaxIds = new ArrayList<Integer>();
			if(exemptedlist.size() > 0) {
				exemptedTaxIds = exemptedlist.stream().map( TaxExemptionSetup :: getTaxId ).collect(Collectors.toList());
				
			}
			res.setExemptedTaxIds(exemptedTaxIds);
			

		} catch (Exception e) {
			e.printStackTrace();
			log.info(e.getMessage());
			return null;
	
		}
		return res;
	}
	
}
