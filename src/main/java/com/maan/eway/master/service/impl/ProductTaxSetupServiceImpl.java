package com.maan.eway.master.service.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.maan.eway.bean.CompaniesTaxSetup;
import com.maan.eway.bean.CountryTaxSetup;
import com.maan.eway.bean.ListItemValue;
import com.maan.eway.bean.ProductTaxSetup;
import com.maan.eway.master.req.GetAllProductTaxReq;
import com.maan.eway.master.req.ProductTaxGetRes;
import com.maan.eway.master.req.ProductTaxMultiInsertReq;
import com.maan.eway.master.req.ProductTaxSaveReq;
import com.maan.eway.master.service.ProductTaxSetupService;
import com.maan.eway.repository.ProductTaxSetupRepository;
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
public class ProductTaxSetupServiceImpl implements ProductTaxSetupService {
 
	@PersistenceContext
	private EntityManager em;

	@Autowired
	private ProductTaxSetupRepository repo;

	Gson json = new Gson();

	private Logger log = LogManager.getLogger(ProductTaxSetupServiceImpl.class);

	

	
	
	
	public Integer upadateOldTaxes(ProductTaxSaveReq req) {
		List<ProductTaxSetup> list = new ArrayList<ProductTaxSetup>();
		Integer amendId = 0 ;
		try {
			long MILLIS_IN_A_DAY = 1000 * 60 * 60 * 24;
			Date oldEndDate = new Date(req.getEffectiveDateStart().getTime() - MILLIS_IN_A_DAY);
			Date entryDate = null ;
			entryDate = new Date();
			
			// Get Sno Record For Amend ID
			// FInd Old Record
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<ProductTaxSetup> query = cb.createQuery(ProductTaxSetup.class);
			//Find all
			Root<ProductTaxSetup> b = query.from(ProductTaxSetup.class);
			//Select 
			query.select(b);
			
			// Max AmendId
			Subquery<Long> maxAmendId = query.subquery(Long.class);
			Root<ProductTaxSetup> ocpm1 = maxAmendId.from(ProductTaxSetup.class);
			maxAmendId.select(cb.max(ocpm1.get("amendId")));
			Predicate a1 = cb.equal(ocpm1.get("companyId"), b.get("companyId"));
			Predicate a2 = cb.equal(ocpm1.get("countryId"), b.get("countryId"));
			Predicate a3 = cb.equal(ocpm1.get("branchCode"), b.get("branchCode"));
			Predicate a4 = cb.equal(ocpm1.get("taxFor"), b.get("taxFor"));
			Predicate a5 = cb.equal(ocpm1.get("taxId"), b.get("taxId"));
			Predicate a6 = cb.equal(ocpm1.get("productId"), b.get("productId"));
			maxAmendId.where(a1,a2,a3,a4,a5,a6);

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
			query.where(n1,n2,n3,n4,n5,n6).orderBy(orderList);
			
			// Get Result 
			TypedQuery<ProductTaxSetup> result = em.createQuery(query);
			list = result.getResultList();
			
			if(list.size()>0) {
				Date beforeOneDay = new Date(new Date().getTime() - MILLIS_IN_A_DAY);
			
				if ( list.get(0).getEffectiveDateStart().before(beforeOneDay)  ) {
					amendId = list.get(0).getAmendId() + 1 ;
					entryDate = new Date() ;
					
					//UPDATE
					CriteriaBuilder cb2 = em.getCriteriaBuilder();
					// create update
					CriteriaUpdate<ProductTaxSetup> update = cb2.createCriteriaUpdate(ProductTaxSetup.class);
					// set the root class
					Root<ProductTaxSetup> m = update.from(ProductTaxSetup.class);
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
					
					update.where(n1,n2,n3,n4,n5,n6);
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
	
	public List<CountryTaxSetup> countryTaxList(ProductTaxSaveReq req) {
		List<CountryTaxSetup> taxList = new ArrayList<CountryTaxSetup>();
		try {
			// Tax List
			
			{
				// Find Latest Record
				CriteriaBuilder cb2 = em.getCriteriaBuilder();
				CriteriaQuery<CountryTaxSetup> query2 = cb2.createQuery(CountryTaxSetup.class);
	
				// Find All
				Root<CountryTaxSetup> b2 = query2.from(CountryTaxSetup.class);
	
				// Effective Date Max Filter
				Subquery<Timestamp> effectiveDate2 = query2.subquery(Timestamp.class);
				Root<CountryTaxSetup> ocpm2 = effectiveDate2.from(CountryTaxSetup.class);
				effectiveDate2.select(cb2.greatest(ocpm2.get("effectiveDateStart")));
				Predicate a10 = cb2.equal(ocpm2.get("countryId"), b2.get("countryId"));
				Predicate a11 = cb2.equal(ocpm2.get("taxId"), b2.get("taxId"));
				effectiveDate2.where(a10,a11);
	
				// Select
				query2.select(b2);
	
				// Order By
				List<Order> orderList2 = new ArrayList<Order>();
				orderList2.add(cb2.desc(b2.get("effectiveDateStart")));
	
				List<String> taxIds = req.getTaxList().stream().map(ProductTaxMultiInsertReq :: getTaxId ).collect(Collectors.toList()) ;
				
				// Where
				Predicate n1 =  cb2.equal(b2.get("effectiveDateStart"),effectiveDate2);
				Predicate n2 =  cb2.equal(b2.get("countryId"), req.getCountryId());
				//In 
				Expression<String>e0=b2.get("taxId");
				Predicate n3 = e0.in(taxIds);
				
				query2.where(n1,n2,n3).orderBy(orderList2);
				// Get Result
				TypedQuery<CountryTaxSetup> result2 = em.createQuery(query2);
				taxList  = result2.getResultList();
			}
			
		} catch (Exception e) {
		e.printStackTrace();
		log.info("Exception is --->" + e.getMessage());
		return null;
	}
	return taxList;
	}
	
	public SuccessRes2 insertNewTaxes(ProductTaxSaveReq req , Integer amendId , List<ListItemValue> calcTypes ,List<CountryTaxSetup> countryTaxes ) {
		SimpleDateFormat sdformat = new SimpleDateFormat("dd/MM/YYYY");
		SuccessRes2 res = new SuccessRes2();
		DozerBeanMapper dozerMapper = new  DozerBeanMapper();
		try {
			String end = "31/12/2050";
			Date endDate = sdformat.parse(end);
			res.setResponse("Updated Successfully");
			res.setSuccessId(req.getTaxFor());
			
			String taxForDesc =  getTaxForDesc("99999" , req.getBranchCode()   ,"TAX_FOR_DESC" , req.getTaxFor()); 
		
			// Decimal Digit
			
			List<ProductTaxSetup> saveList = new ArrayList<ProductTaxSetup>();
			for ( ProductTaxMultiInsertReq data :  req.getTaxList() ) {
				ProductTaxSetup saveData = new ProductTaxSetup();
				// Save New Records
			//	saveData = dozerMapper.map(req, CompaniesTaxSetup.class );
				saveData.setAmendId(amendId);
				saveData.setBranchCode(req.getBranchCode());
				saveData.setCompanyId(req.getCompanyId());
				saveData.setCalcType(data.getCalcType() );
				saveData.setCalcTypeDesc(calcTypes.stream().filter( o -> o.getItemCode().equalsIgnoreCase(data.getCalcType()) ).collect(Collectors.toList()).get(0).getItemValue());
				saveData.setCoreAppCode(data.getCoreAppCode());
				saveData.setCountryId(req.getCountryId());
				saveData.setCreatedBy(req.getCreatedBy());
				saveData.setEffectiveDateStart(req.getEffectiveDateStart());
				saveData.setEffectiveDateEnd(endDate);
				saveData.setEntryDate(new Date());
			//	saveData.setPriority(StringUtils.isBlank(data.getPriority()) ? null :Integer.valueOf(data.getPriority())  );
				saveData.setRemarks("");
				saveData.setRegulatoryCode(data.getRegulatoryCode());
				saveData.setStatus(data.getStatus());
				saveData.setTaxCode(data.getTaxCode());
				saveData.setTaxExemptAllowYn(StringUtils.isBlank(data.getTaxExemptAllowYn()) ? "N" : data.getTaxExemptAllowYn());
				saveData.setTaxFor(req.getTaxFor());
				saveData.setTaxForDesc(taxForDesc);
				saveData.setTaxId(Integer.valueOf(data.getTaxId()));
				List<CountryTaxSetup> filterTax = countryTaxes.stream().filter( o -> o.getTaxId().equals(saveData.getTaxId()) ).collect(Collectors.toList());
				saveData.setTaxDesc(filterTax.size() > 0 ? filterTax.get(0).getTaxDesc() : "" );
				saveData.setTaxName(filterTax.size() > 0 ? filterTax.get(0).getTaxName() : "" );
				saveData.setUpdatedBy(req.getCreatedBy());
				saveData.setUpdatedDate(new Date());
				saveData.setValue(StringUtils.isBlank(data.getValue()) ? null : Double.valueOf(data.getValue()) ) ;
				saveData.setDependentYn(StringUtils.isBlank(data.getDependentYn())?"N":data.getDependentYn());
				saveData.setMinimumAmount(StringUtils.isBlank(data.getMinimumAmount())?BigDecimal.ZERO  :new BigDecimal(data.getMinimumAmount()) );
				saveData.setProductId(Integer.valueOf(req.getProductId()));
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
	public List<String> validateProductTaxes(ProductTaxSaveReq req) {
		List<String> errorList = new ArrayList<String>();

		try {	
//				
			if (StringUtils.isBlank(req.getCountryId()) ) {
		//		errorList.add(new Error("03", "CountryId", "Please Select Country Id "));
				errorList.add("1613");
			}
			else if(req.getCountryId().length()>20) {
			//	errorList.add(new Error("03","CountryId","Pleaser enter the country between 20 characters"));
				errorList.add("1614");
				
			}
			
			if (StringUtils.isBlank(req.getCreatedBy())) {
			//	errorList.add(new Error("07", "CreatedBy", "Please Enter CreatedBy"));
				errorList.add("1270");
			} else if (req.getCreatedBy().length() > 100) {
			//	errorList.add(new Error("07", "CreatedBy", "Please Enter CreatedBy within 100 Characters"));
				errorList.add("1271");
			}

			if (StringUtils.isBlank(req.getBranchCode())) {
			//	errorList.add(new Error("07", "BranchCode", "Please Select BranchCode"));
				errorList.add("1256");
			}
			
			if (StringUtils.isBlank(req.getTaxFor())) {
			//	errorList.add(new Error("07", "Tax For", "Please Select Tax For"));
				errorList.add("1615");
			}
			
			if (StringUtils.isBlank(req.getCompanyId())) {
			//	errorList.add(new Error("07", "CompanyId", "Please Select Insurance Id"));
				errorList.add("1255");
			}
			
			if (StringUtils.isBlank(req.getProductId())) {
			//	errorList.add(new Error("07", "ProductId", "Please Select ProductId"));
				errorList.add("1313");
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
		//		errorList.add(new Error("04", "EffectiveDateStart", "Please Enter Effective Date Start "));
				errorList.add("1261");

			} else if (req.getEffectiveDateStart().before(today)) {
			//	errorList.add(new Error("04", "EffectiveDateStart", "Please Enter Effective Date Start as Future Date"));
				errorList.add("1262");
			}
			
			List<String> taxIds = new ArrayList<String>(); 
			List<String> taxCodes = new ArrayList<String>(); 
			List<String> coreAppCodes = new ArrayList<String>(); 
			List<String> regulatoryCodes = new ArrayList<String>(); 
		//	List<String> priorities = new ArrayList<String>(); 
			
			if(req.getTaxList()==null || req.getTaxList().size() <= 0 ) {
			//	errorList.add(new Error("01", "TaxDetails", "Please Enter Atleast One Tax Details"));
				errorList.add("1616");
			} else {
				List<ProductTaxMultiInsertReq> taxList =req.getTaxList() ;
				Integer row = 0 ;
				
				for (ProductTaxMultiInsertReq data :  taxList) {
					row = row + 1 ;
										
					if (StringUtils.isBlank(data.getTaxId())) {
				//		errorList.add(new Error("01", "Tax", "Please Select Tax Name In Row No : " + row ));
						errorList.add("1617" + "," + row);
						
					}else {
						List<String> filterTaxIds = taxIds.stream().filter( o -> o.equalsIgnoreCase(data.getTaxId())).collect(Collectors.toList());
						if(filterTaxIds.size() > 0 ) {
				//			errorList.add(new Error("01", "Tax", "Duplicate Tax Name Available In Row No : " + row ));
							errorList.add("1618" + "," + row);
						} else {
							taxIds.add(data.getTaxId());
						}
						
					} 
					
					
					if (StringUtils.isBlank(data.getValue())) {
					//	errorList.add(new Error("05", "Value", "Please Enter Tax Value In Row No : " + row ));
						errorList.add("1619" + "," + row);
					}else if(! data.getValue().matches("[0-9.]+")) {
					//	errorList.add(new Error("05","Value", "Please Enter Valid Tax Value in Row No : " + row ));
						errorList.add("1620" + "," + row);
					}
					
					if (StringUtils.isBlank(data.getMinimumAmount())) {
					//	errorList.add(new Error("05", "MinimumAmount", "Please Enter Minimum Amount In Row No : " + row ));
						errorList.add("1621" + "," + row);
					}else if(! data.getMinimumAmount().matches("[0-9.]+")) {
					//	errorList.add(new Error("05","MinimumAmount", "Please Enter Valid Minimum Amount in Row No : " + row ));
						errorList.add("1622" + "," + row);
					}
					
//					if (StringUtils.isBlank(data.getPriority())) {
//						errorList.add(new Error("05", "Priority", "Please Enter Priority Number In Row No : " + row ));
//					}else if(! data.getPriority().matches("[0-9]+")) {
//						errorList.add(new Error("05","Priority", "Please Enter Valid Priority Number in Row No : " + row ));
//					} else  {
//						List<String> filterPriorities = priorities.stream().filter( o -> o.equalsIgnoreCase(data.getPriority())).collect(Collectors.toList());
//						if(filterPriorities.size() > 0 ) {
//							errorList.add(new Error("01", "Priority", "Duplicate Priority Available In Row No : " + row ));
//						}
//						
//					}
					
					if (StringUtils.isBlank(data.getCalcType())) {
					//	errorList.add(new Error("05", "CalcType", "Please Enter CalcType In Row No : " + row ));
						errorList.add("1623" + "," + row);
					} else if (data.getCalcType().length() > 1) {
					//	errorList.add(new Error("05", "CalcType", "Enter CalcType 1 Character Only In Row No : " + row ));
						errorList.add("1625" + "," + row);
					} else if (data.getCalcType().equalsIgnoreCase("P")) {
						if(StringUtils.isNotBlank(data.getValue()) &&  data.getValue().matches("[0-9.]+") && Double.valueOf(data.getValue())>1000 ) {
					//		errorList.add(new Error("05", "Tax Value ", "Please Enter Valid Tax Value Percent In Row No : " + row ));
							errorList.add("1624" + "," + row);
						}
					}
					
					//Status Validation
					if (StringUtils.isBlank(data.getStatus())) {
				//		errorList.add(new Error("05", "Status", "Please Enter Status In Row No : " + row ));
						errorList.add("1483" + "," + row);
						
					} else if (data.getStatus().length() > 1) {
					//	errorList.add(new Error("05", "Status", "Enter Status in One Character Only In Row No : " + row ));
						errorList.add("1484" + "," + row);
						
					} else if(!("Y".equalsIgnoreCase(data.getStatus())||"N".equalsIgnoreCase(data.getStatus())||"R".equalsIgnoreCase(data.getStatus())|| "P".equalsIgnoreCase(data.getStatus()))) {
				//		errorList.add(new Error("05", "Status", "Please Select Valid Status - Active or Deactive or Pending or Referral In Row No : " + row ));
						errorList.add("1485" + "," + row);
					}
					
					
					
					if (StringUtils.isBlank(data.getRegulatoryCode())) {
					//	errorList.add(new Error("06", "RegulatoryCode", "Please Enter RegulatoryCode In Row No : " + row ));
						errorList.add("1486" + "," + row);
					} else if(data.getRegulatoryCode().length()>20) {
				//		errorList.add(new Error("06", "RegulatoryCode", "Please Enter RegulatoryCode within 20 Characters In Row No : " + row ));
						errorList.add("1487" + "," + row);
						
					} else if(! ( "N/A".equalsIgnoreCase(data.getRegulatoryCode()) || "99999".equalsIgnoreCase(data.getRegulatoryCode()) 
							|| "NA".equalsIgnoreCase(data.getRegulatoryCode())) ) {
						List<String> filterRegulatoryCodes = regulatoryCodes.stream().filter( o -> o.equalsIgnoreCase(data.getRegulatoryCode())).collect(Collectors.toList());
						if(filterRegulatoryCodes.size() > 0 ) {
					//		errorList.add(new Error("01", "RegulatoryCode", "Duplicate Regulatory Available In Row No : " + row ));
							errorList.add("1488" + "," + row);
						} else {
							regulatoryCodes.add(data.getRegulatoryCode());
						}
						
						
					} 
					
					if (StringUtils.isBlank(data.getCoreAppCode())) {
				//		errorList.add(new Error("06", "CoreAppCode", "Please Enter CoreAppCode In Row No : " + row ));
						errorList.add("1489" + "," + row);
					} else if(data.getCoreAppCode().length()>20) {
					//	errorList.add(new Error("06", "CoreAppCode", "Please Enter CoreAppCode within 20 Characters In Row No : " + row ));
						errorList.add("1490" + "," + row);
						
					} else if(! ( "N/A".equalsIgnoreCase(data.getCoreAppCode()) || "99999".equalsIgnoreCase(data.getCoreAppCode()) 
							|| "NA".equalsIgnoreCase(data.getCoreAppCode())) ) {
						List<String> filterCoreAppCodes = coreAppCodes.stream().filter( o -> o.equalsIgnoreCase(data.getCoreAppCode())).collect(Collectors.toList());
						if(filterCoreAppCodes.size() > 0 ) {
					//		errorList.add(new Error("01", "Core App Code", "Duplicate Core App Code Available In Row No : " + row ));
							errorList.add("1491" + "," + row);
						} else {
							coreAppCodes.add(data.getCoreAppCode());
						}
						
					}
					
					if (StringUtils.isBlank(data.getTaxCode())) {
				//		errorList.add(new Error("06", "TaxCode", "Please Enter Tax Code In Row No : " + row ));
						errorList.add("1626" + "," + row);
					} else if(data.getTaxCode().length()>100) {
					//	errorList.add(new Error("06", "TaxCode", "Please Enter Tax Code within 100 Characters In Row No : " + row ));
						errorList.add("1627" + "," + row);
						
					} else if(! ( "N/A".equalsIgnoreCase(data.getTaxCode()) || "99999".equalsIgnoreCase(data.getTaxCode()) 
							|| "NA".equalsIgnoreCase(data.getTaxCode())) ) {
						List<String> filterTaxCodes = taxCodes.stream().filter( o -> o.equalsIgnoreCase(data.getTaxCode())).collect(Collectors.toList());
						if(filterTaxCodes.size() > 0 ) {
					//		errorList.add(new Error("01", "TaxCode", "Duplicate Tax Code Available In Row No : " + row ));
							errorList.add("1628" + "," + row);
						} else {
							taxCodes.add(data.getTaxCode());
						}
						
					}
					
				} 
				
			}
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
		}
		return errorList;
	}


	@Override
	public SuccessRes2 saveProductTaxes(ProductTaxSaveReq req) {
		SuccessRes2 res = new SuccessRes2();
		try {
			// Update Old Records
			Integer amendId = upadateOldTaxes(req ) ;
			
			// Calc Type
			List<ListItemValue> calcTypes =  getListItem("99999" , req.getBranchCode()   ,"CALCULATION_TYPE"); 
		
			// Calc Type
			List<CountryTaxSetup> countryTaxes = countryTaxList(req );
			
			// Insert New Records
			res = insertNewTaxes(req , amendId , calcTypes , countryTaxes ) ;
		
		} catch (Exception e) {
		e.printStackTrace();
		log.info("Exception is --->" + e.getMessage());
		return null;
	}
	return res;
	}


	@Override
	public ProductTaxGetRes getallProductTaxes(GetAllProductTaxReq req) {
		ProductTaxGetRes res = new ProductTaxGetRes();
		DozerBeanMapper dozerMapper = new DozerBeanMapper();
		try {
			List<ProductTaxSetup> productlist = new ArrayList<ProductTaxSetup>(); 
			{
				// Find Latest Record
				CriteriaBuilder cb = em.getCriteriaBuilder();
				CriteriaQuery<ProductTaxSetup> query = cb.createQuery(ProductTaxSetup.class);

				// Find All
				Root<ProductTaxSetup> b = query.from(ProductTaxSetup.class);

				// Select
				query.select(b);

				// Amend ID Max Filter
				Subquery<Long> amendId = query.subquery(Long.class);
				Root<ProductTaxSetup> ocpm1 = amendId.from(ProductTaxSetup.class);
				amendId.select(cb.max(ocpm1.get("amendId")));
				Predicate a1 = cb.equal(ocpm1.get("countryId"), b.get("countryId"));
				Predicate a2 = cb.equal(ocpm1.get("companyId"), b.get("companyId"));
				Predicate a3 = cb.equal(ocpm1.get("taxId"), b.get("taxId"));
				Predicate a4 = cb.equal(ocpm1.get("taxFor"), b.get("taxFor"));
				Predicate a5 = cb.equal(ocpm1.get("branchCode"), b.get("branchCode"));
				Predicate a6 = cb.equal(ocpm1.get("productId"), b.get("productId"));
				amendId.where(a1,a2,a3,a4,a5,a6);

				Predicate n1 = cb.equal(b.get("amendId"),amendId);
				Predicate n2 = cb.equal(b.get("countryId"), req.getCountryId() );	
				Predicate n3 = cb.equal(b.get("companyId"), req.getCompanyId() );		
				Predicate n4 = cb.equal(b.get("branchCode"), req.getBranchCode());
				Predicate n5 = cb.equal(b.get("taxFor"), req.getTaxFor());
				Predicate n6 = cb.equal(b.get("productId"), req.getProductId());
				// Order By
				List<Order> orderList = new ArrayList<Order>();
				orderList.add(cb.asc(b.get("taxName")));
				query.where( n1, n2, n3,n4,n5,n6).orderBy(orderList);
				
				// Get Result
				TypedQuery<ProductTaxSetup> result = em.createQuery(query);
				productlist = result.getResultList();
			}
		
			// Get Product Taxes 
			
			if(productlist.size() > 0 ) {
				ProductTaxSetup firstData = productlist.get(0);
				res.setBranchCode(firstData.getBranchCode());
				res.setCompanyId(firstData.getCompanyId());
				res.setCountryId(firstData.getCountryId());
				res.setCreatedBy(firstData.getCreatedBy());
				res.setEffectiveDateStart(firstData.getEffectiveDateStart());
				res.setEffectiveDateEnd(firstData.getEffectiveDateEnd());
				res.setEntryDate(firstData.getEntryDate());
				res.setTaxFor(firstData.getTaxFor());
				res.setTaxForDesc(firstData.getTaxForDesc());
				res.setProductId(firstData.getProductId().toString());
				
				List<ProductTaxMultiInsertReq> taxList = new ArrayList<ProductTaxMultiInsertReq>();
				for (ProductTaxSetup  data : productlist) {
					ProductTaxMultiInsertReq taxRes = new ProductTaxMultiInsertReq();
					taxRes.setCalcType(data.getCalcType());
					taxRes.setCoreAppCode(data.getCoreAppCode());
					taxRes.setCalcType(data.getCalcType() );
					//taxRes.setCalcTypeDesc(data.getCalcTypeDesc());
					taxRes.setCoreAppCode(data.getCoreAppCode());
				//	taxRes.setPriority(data.getPriority()== null ? "" :data.getPriority().toString()  );
					taxRes.setRegulatoryCode(data.getRegulatoryCode());
					taxRes.setStatus(data.getStatus());
					taxRes.setTaxCode(data.getTaxCode());
					taxRes.setTaxExemptAllowYn(data.getTaxExemptAllowYn());
					taxRes.setTaxId(data.getTaxId()==null ? "" : data.getTaxId().toString());
//					taxRes.setTaxDesc(filterTax.size() > 0 ? filterTax.get(0).getTaxDesc() : "" );
//					taxRes.setTaxName(filterTax.size() > 0 ? filterTax.get(0).getTaxName() : "" );
//					taxRes.setUpdatedBy(data.getCreatedBy());
//					taxRes.setUpdatedDate(new Date());
					taxRes.setValue(data.getValue()== null ? "" : data.getValue().toString() ) ;
					taxRes.setDependentYn(data.getDependentYn()==null? "" :data.getDependentYn());
					taxRes.setMinimumAmount(data.getMinimumAmount()==null?"" :data.getMinimumAmount().toPlainString() );
					taxList.add(taxRes);
				}
				res.setTaxList(taxList);
				
			} else {

				List<CompaniesTaxSetup> list = new ArrayList<CompaniesTaxSetup>();
				// Find Latest Record
				CriteriaBuilder cb = em.getCriteriaBuilder();
				CriteriaQuery<CompaniesTaxSetup> query = cb.createQuery(CompaniesTaxSetup.class);

				// Find All
				Root<CompaniesTaxSetup> b = query.from(CompaniesTaxSetup.class);

				// Select
				query.select(b);

				// Amend ID Max Filter
				Subquery<Long> amendId = query.subquery(Long.class);
				Root<CompaniesTaxSetup> ocpm1 = amendId.from(CompaniesTaxSetup.class);
				amendId.select(cb.max(ocpm1.get("amendId")));
				Predicate a1 = cb.equal(ocpm1.get("countryId"), b.get("countryId"));
				Predicate a2 = cb.equal(ocpm1.get("companyId"), b.get("companyId"));
				Predicate a3 = cb.equal(ocpm1.get("taxId"), b.get("taxId"));
				Predicate a4 = cb.equal(ocpm1.get("taxFor"), b.get("taxFor"));
				Predicate a5 = cb.equal(ocpm1.get("branchCode"), b.get("branchCode"));
				amendId.where(a1,a2,a3,a4,a5);

				Predicate n1 = cb.equal(b.get("amendId"),amendId);
				Predicate n2 = cb.equal(b.get("countryId"), req.getCountryId() );	
				Predicate n3 = cb.equal(b.get("companyId"), req.getCompanyId() );		
				Predicate n4 = cb.equal(b.get("branchCode"), req.getBranchCode());
				Predicate n5 = cb.equal(b.get("taxFor"), req.getTaxFor());
				// Order By
				List<Order> orderList = new ArrayList<Order>();
				orderList.add(cb.asc(b.get("taxName")));
				query.where( n1, n2, n3,n4,n5).orderBy(orderList);
				
				// Get Result
				TypedQuery<CompaniesTaxSetup> result = em.createQuery(query);
				list = result.getResultList();
				if(list.size() > 0 ) {
					CompaniesTaxSetup firstData = list.get(0);
					res.setBranchCode(firstData.getBranchCode());
					res.setCompanyId(firstData.getCompanyId());
					res.setCountryId(firstData.getCountryId());
					res.setCreatedBy(firstData.getCreatedBy());
					res.setEffectiveDateStart(firstData.getEffectiveDateStart());
					res.setEffectiveDateEnd(firstData.getEffectiveDateEnd());
					res.setEntryDate(firstData.getEntryDate());
					res.setTaxFor(firstData.getTaxFor());
					res.setTaxForDesc(firstData.getTaxForDesc());
					
					List<ProductTaxMultiInsertReq> taxList = new ArrayList<ProductTaxMultiInsertReq>();
					for (CompaniesTaxSetup  data : list) {
						ProductTaxMultiInsertReq taxRes = new ProductTaxMultiInsertReq();
						taxRes.setCalcType(data.getCalcType());
						taxRes.setCoreAppCode(data.getCoreAppCode());
						taxRes.setCalcType(data.getCalcType() );
						//taxRes.setCalcTypeDesc(data.getCalcTypeDesc());
						taxRes.setCoreAppCode(data.getCoreAppCode());
					//	taxRes.setPriority(data.getPriority()== null ? "" :data.getPriority().toString()  );
						taxRes.setRegulatoryCode(data.getRegulatoryCode());
						taxRes.setStatus(data.getStatus());
						taxRes.setTaxCode(data.getTaxCode());
						taxRes.setTaxExemptAllowYn(data.getTaxExemptAllowYn());
						taxRes.setTaxId(data.getTaxId()==null ? "" : data.getTaxId().toString());
//						taxRes.setTaxDesc(filterTax.size() > 0 ? filterTax.get(0).getTaxDesc() : "" );
//						taxRes.setTaxName(filterTax.size() > 0 ? filterTax.get(0).getTaxName() : "" );
//						taxRes.setUpdatedBy(data.getCreatedBy());
//						taxRes.setUpdatedDate(new Date());
						taxRes.setValue(data.getValue()== null ? "" : new BigDecimal(data.getValue()).toPlainString() ) ;
						taxRes.setDependentYn(data.getDependentYn()==null? "" :data.getDependentYn());
						taxRes.setMinimumAmount(data.getMinimumAmount()==null?"" :data.getMinimumAmount().toPlainString() );
						taxList.add(taxRes);
					}
					res.setTaxList(taxList);
				} else {
					res = null ;
				}
			}
			

		} catch (Exception e) {
			e.printStackTrace();
			log.info(e.getMessage());
			return null;
	
		}
		return res;
	}

	@Override
	public ProductTaxGetRes getactiveProductTaxes(GetAllProductTaxReq req) {
		ProductTaxGetRes res = new ProductTaxGetRes();
		DozerBeanMapper dozerMapper = new DozerBeanMapper();
		try {
			List<ProductTaxSetup> productlist = new ArrayList<ProductTaxSetup>(); 
			{
				// Find Latest Record
				CriteriaBuilder cb = em.getCriteriaBuilder();
				CriteriaQuery<ProductTaxSetup> query = cb.createQuery(ProductTaxSetup.class);

				// Find All
				Root<ProductTaxSetup> b = query.from(ProductTaxSetup.class);

				// Select
				query.select(b);

				// Amend ID Max Filter
				Subquery<Long> amendId = query.subquery(Long.class);
				Root<ProductTaxSetup> ocpm1 = amendId.from(ProductTaxSetup.class);
				amendId.select(cb.max(ocpm1.get("amendId")));
				Predicate a1 = cb.equal(ocpm1.get("countryId"), b.get("countryId"));
				Predicate a2 = cb.equal(ocpm1.get("companyId"), b.get("companyId"));
				Predicate a3 = cb.equal(ocpm1.get("taxId"), b.get("taxId"));
				Predicate a4 = cb.equal(ocpm1.get("taxFor"), b.get("taxFor"));
				Predicate a5 = cb.equal(ocpm1.get("branchCode"), b.get("branchCode"));
				Predicate a6 = cb.equal(ocpm1.get("productId"), b.get("productId"));
				amendId.where(a1,a2,a3,a4,a5,a6);

				Predicate n1 = cb.equal(b.get("amendId"),amendId);
				Predicate n2 = cb.equal(b.get("countryId"), req.getCountryId() );	
				Predicate n3 = cb.equal(b.get("companyId"), req.getCompanyId() );		
				Predicate n4 = cb.equal(b.get("branchCode"), req.getBranchCode());
				Predicate n5 = cb.equal(b.get("taxFor"), req.getTaxFor());
				Predicate n6 = cb.equal(b.get("productId"), req.getProductId());
				Predicate n7 = cb.equal(b.get("status"), "Y");
				// Order By
				List<Order> orderList = new ArrayList<Order>();
				orderList.add(cb.asc(b.get("taxName")));
				query.where( n1, n2, n3,n4,n5,n6,n7).orderBy(orderList);
				
				// Get Result
				TypedQuery<ProductTaxSetup> result = em.createQuery(query);
				productlist = result.getResultList();
			}
		
			// Get Product Taxes 
			
			if(productlist.size() > 0 ) {
				ProductTaxSetup firstData = productlist.get(0);
				res.setBranchCode(firstData.getBranchCode());
				res.setCompanyId(firstData.getCompanyId());
				res.setCountryId(firstData.getCountryId());
				res.setCreatedBy(firstData.getCreatedBy());
				res.setEffectiveDateStart(firstData.getEffectiveDateStart());
				res.setEffectiveDateEnd(firstData.getEffectiveDateEnd());
				res.setEntryDate(firstData.getEntryDate());
				res.setTaxFor(firstData.getTaxFor());
				res.setTaxForDesc(firstData.getTaxForDesc());
				res.setProductId(firstData.getProductId().toString());
				
				List<ProductTaxMultiInsertReq> taxList = new ArrayList<ProductTaxMultiInsertReq>();
				for (ProductTaxSetup  data : productlist) {
					ProductTaxMultiInsertReq taxRes = new ProductTaxMultiInsertReq();
					taxRes.setCalcType(data.getCalcType());
					taxRes.setCoreAppCode(data.getCoreAppCode());
					taxRes.setCalcType(data.getCalcType() );
					//taxRes.setCalcTypeDesc(data.getCalcTypeDesc());
					taxRes.setCoreAppCode(data.getCoreAppCode());
				//	taxRes.setPriority(data.getPriority()== null ? "" :data.getPriority().toString()  );
					taxRes.setRegulatoryCode(data.getRegulatoryCode());
					taxRes.setStatus(data.getStatus());
					taxRes.setTaxCode(data.getTaxCode());
					taxRes.setTaxExemptAllowYn(data.getTaxExemptAllowYn());
					taxRes.setTaxId(data.getTaxId()==null ? "" : data.getTaxId().toString());
//					taxRes.setTaxDesc(filterTax.size() > 0 ? filterTax.get(0).getTaxDesc() : "" );
//					taxRes.setTaxName(filterTax.size() > 0 ? filterTax.get(0).getTaxName() : "" );
//					taxRes.setUpdatedBy(data.getCreatedBy());
//					taxRes.setUpdatedDate(new Date());
					taxRes.setValue(data.getValue()== null ? "" : data.getValue().toString() ) ;
					taxRes.setDependentYn(data.getDependentYn()==null? "" :data.getDependentYn());
					taxRes.setMinimumAmount(data.getMinimumAmount()==null?"" :data.getMinimumAmount().toPlainString() );
					taxList.add(taxRes);
				}
				res.setTaxList(taxList);
				
			} 
			

		} catch (Exception e) {
			e.printStackTrace();
			log.info(e.getMessage());
			return null;
	
		}
		return res;
	}
	
}
