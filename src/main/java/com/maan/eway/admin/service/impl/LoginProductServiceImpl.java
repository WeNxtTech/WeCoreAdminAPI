package com.maan.eway.admin.service.impl;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.maan.eway.admin.req.AttachCompnayProductRequest;
import com.maan.eway.admin.req.AttachedProductReq;
import com.maan.eway.admin.req.BrokerCompanyProductGetReq;
import com.maan.eway.admin.req.BrokerCompanyProductsGetRes;
import com.maan.eway.admin.req.BrokerProductCompaniesRes;
import com.maan.eway.admin.req.BrokerProductGetReq;
import com.maan.eway.admin.res.BranchCriteriaRes;
import com.maan.eway.admin.res.BrokerProductsGetRes;
import com.maan.eway.admin.res.IssuerBranchGetRes;
import com.maan.eway.admin.res.IssuerCompanyGetRes;
import com.maan.eway.admin.res.LoginCreationRes;
import com.maan.eway.admin.res.LoginProductCriteriaRes;
import com.maan.eway.admin.res.ProductCriteriaRes;
import com.maan.eway.admin.service.LoginProductService;
import com.maan.eway.bean.BranchMaster;
import com.maan.eway.bean.InsuranceCompanyMaster;
import com.maan.eway.bean.LoginMaster;
import com.maan.eway.bean.LoginProductMaster;
import com.maan.eway.bean.ProductMaster;
import com.maan.eway.bean.RegionMaster;
import com.maan.eway.repository.LoginMasterRepository;
import com.maan.eway.repository.LoginProductMasterRepository;

@Service
public class LoginProductServiceImpl  implements LoginProductService {
	
	@Autowired
	private LoginProductMasterRepository loginProductRepo ;
	
	@Autowired
	private LoginMasterRepository loginRepo ;
	
	@PersistenceContext
	private EntityManager em;

	Gson json = new Gson();


	private Logger log=LogManager.getLogger(LoginProductServiceImpl.class);
	
//*************************************** Add Products Apis Methods **********************************************************//

	@Transactional
	@Override
	public LoginCreationRes saveBrokerProductDetails(AttachCompnayProductRequest req) {
		LoginCreationRes res = new LoginCreationRes();
		DozerBeanMapper dozerMapper = new  DozerBeanMapper();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy"); 
		try { 
				 
			for ( AttachedProductReq data : req.getAttachedProducts()  ) {
				
				Calendar cal = new GregorianCalendar();
				cal.setTime(data.getEffectiveDate());  cal.set(Calendar.HOUR_OF_DAY, 23); cal.set(Calendar.MINUTE, 59);
				Date startDate = cal.getTime() ;
				cal.setTime(data.getEffectiveDate());  cal.set(Calendar.DAY_OF_MONTH, -1); cal.set(Calendar.HOUR_OF_DAY, 0); cal.set(Calendar.MINUTE, 10);
				Date oldEndDate = cal.getTime() ;
				Date today = new Date();
				cal.setTime(data.getEffectiveDate());  cal.set(Calendar.HOUR_OF_DAY, today.getHours()); cal.set(Calendar.MINUTE, today.getMinutes() );
				Date effDate = cal.getTime();
				
				// Find Old Record 
				CriteriaBuilder cb = em.getCriteriaBuilder();
				CriteriaQuery<LoginProductMaster> query = cb.createQuery(LoginProductMaster.class);
				List<LoginProductMaster> list = new ArrayList<LoginProductMaster>();
				
				// Find All
				Root<LoginProductMaster> lp = query.from(LoginProductMaster.class);

				// Select
				query.select(lp);

				// Effective Date Max Filter
				Subquery<Long> effectiveDate = query.subquery(Long.class);
				Root<LoginProductMaster> ocpm1 = effectiveDate.from(LoginProductMaster.class);
				effectiveDate.select(cb.max(ocpm1.get("effectiveDateStart")));
				Predicate a1 = cb.equal(ocpm1.get("productId"), lp.get("productId"));
				Predicate a2 = cb.equal(ocpm1.get("loginId"), lp.get("loginId"));
				Predicate a3 = cb.equal(ocpm1.get("companyId"), lp.get("companyId"));
				Predicate a4 = cb.lessThanOrEqualTo(ocpm1.get("effectiveDateStart") , startDate);
				effectiveDate.where(a1,a2,a3,a4);

				// Order By
				//List<Order> orderList = new ArrayList<Order>();
				//orderList.add(cb.asc(lp.get("effectiveDateStart")));
				
				// Where
				Predicate n1 = cb.equal(lp.get("productId"), data.getProductId());
				Predicate n2 = cb.equal(lp.get("loginId"), req.getLoginId());
				Predicate n3 = cb.equal(lp.get("companyId"), req.getInsuranceId() );
				Predicate n4 = cb.equal(lp.get("effectiveDateStart") , effectiveDate);

				query.where(n1, n2, n3,n4);//.orderBy(orderList);

				// Get Result
				TypedQuery<LoginProductMaster> result = em.createQuery(query);
				list = result.getResultList();
				
				if(list.size() > 0 ) {
					loginProductRepo.delete(list.get(0));
				}
				Date endDate = sdf.parse("12/12/2050");
				
				LoginProductMaster save = new LoginProductMaster();
				dozerMapper.map(data, save);
				save.setCompanyId(req.getInsuranceId());
				save.setEffectiveDateStart(effDate);	
				save.setEffectiveDateEnd(endDate);
				save.setEntryDate(new Date());
				save.setLoginId(req.getLoginId());
				loginProductRepo.saveAndFlush(save);
				log.info("Saved Details is ---> " + json.toJson(save));
				if(list.size() > 0 ) {
					// Update Old Record
					LoginProductMaster lastRecord = list.get(0) ;
					lastRecord.setEffectiveDateEnd(oldEndDate);
					loginProductRepo.saveAndFlush(lastRecord);
				}
			}		
			
			res.setResponse("Products Added Successfully");
			
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Exception is --->" + e.getMessage());
			return null;
		}
		return res;
	}

//*************************************** Get Products Apis Methods **********************************************************//
	
	@Override
	public List<BrokerProductCompaniesRes> getBrokerProducts(BrokerProductGetReq req) {
		List<BrokerProductCompaniesRes> companyList = new ArrayList<BrokerProductCompaniesRes>();
		try {
			Calendar cal = new GregorianCalendar();
			Date today = new Date();
			cal.setTime(today); cal.set(Calendar.HOUR_OF_DAY, 23);cal.set(Calendar.MINUTE, 50);
			today = cal.getTime() ;
			
			String loginId = req.getLoginId() ;
			LoginMaster loginData = loginRepo.findByLoginId(loginId);
			List<String> companyIds = new ArrayList<>(Arrays.asList(loginData.getAttachedCompanies().split(","))) ;
			
			if(companyIds.size()>0 ) {
				
				List<ProductCriteriaRes> products = getProductDetails( companyIds , today  );
				List<LoginProductCriteriaRes> loginProducts = getBrokerProductDetails (loginId , companyIds , today ) ;
				
				// Grouping
				Map<String ,List<ProductCriteriaRes>> groupByCompany = products.stream().collect(Collectors.groupingBy(ProductCriteriaRes :: getCompanyId )) ;
				for (String company : groupByCompany.keySet()) { 
					BrokerProductCompaniesRes companyRes = new BrokerProductCompaniesRes();
					List<BrokerProductsGetRes> attachedProducts = new ArrayList<BrokerProductsGetRes>();
					
					List<ProductCriteriaRes> filterProduct = groupByCompany.get(company);
					
					for(ProductCriteriaRes data :  filterProduct) {
						BrokerProductsGetRes productRes = new BrokerProductsGetRes();
						
						// Filter Login PRoducts
						List<LoginProductCriteriaRes> filterLoginProduct = loginProducts.stream().filter( o -> o.getCompanyId().equalsIgnoreCase(data.getCompanyId()) && o.getProductId().equals(data.getProductId()) ).collect(Collectors.toList());
						
						productRes.setStatus("N");
						if (filterLoginProduct.size() > 0 ) {
							String pattern = "#####0";
							DecimalFormat df = new DecimalFormat(pattern);
							productRes.setOldProductName(filterLoginProduct.get(0).getOldProductName() );
							productRes.setStartLimit(filterLoginProduct.get(0).getStartLimit()==null?"" : df.format(filterLoginProduct.get(0).getStartLimit()) );
							productRes.setEndLimit(filterLoginProduct.get(0).getEndLimit()==null?"" :df.format(filterLoginProduct.get(0).getEndLimit()) );
							productRes.setStatus(filterLoginProduct.get(0).getStatus());
							productRes.setRemarks(filterLoginProduct.get(0).getRemarks());	;
						}
						
						productRes.setProductId(data.getProductId().toString());
						productRes.setProductName(data.getProductName());
						attachedProducts.add(productRes);
					}
					
					// Response 
					companyRes.setInsuranceId(filterProduct.get(0).getCompanyId() );
					companyRes.setCompanyName(filterProduct.get(0).getCompanyName() );
					companyRes.setAttachedProducts(attachedProducts);
					companyList.add(companyRes);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Exception is --->" + e.getMessage());
			return null;
		}
		return companyList;
	}
	
	public List<ProductCriteriaRes> getProductDetails(List<String> companyIds , Date today ) {
		List<ProductCriteriaRes> list = new ArrayList<ProductCriteriaRes>();  
		try {
			// Product Query 	
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<ProductCriteriaRes> query = cb.createQuery(ProductCriteriaRes.class);
			
			Root<ProductMaster> pm  = query.from(ProductMaster.class);
			
			// Select Company Name SubQuery for Effective Date Max Filter 
			Subquery<Long> insEff = query.subquery(Long.class);
			Root<InsuranceCompanyMaster> i = insEff.from(InsuranceCompanyMaster.class);
			Subquery<Long> company = query.subquery(Long.class);
			Root<InsuranceCompanyMaster> ins = company.from(InsuranceCompanyMaster.class);
			
			insEff.select( cb.max(i.get("effectiveDateStart")) );
			Predicate i1 = cb.equal(ins.get("companyId"), i.get("companyId"));
			Predicate i2 = cb.lessThanOrEqualTo(i.get("effectiveDateStart") , today);
			insEff.where(i1,i2);
			
			company.select( ins.get("companyName")) ;
			Predicate ins1 = cb.equal(ins.get("companyId"), pm.get("companyId"));
			Predicate ins2  = cb.equal(ins.get("effectiveDateStart"),insEff);
			company.where(ins1,ins2);
			
			// Select
			query.multiselect( pm.get("productId").alias("productId") , pm.get("productName").alias("productName") ,pm.get("companyId").alias("companyId") ,
					company.alias("companyName"));

			// Product Effective Date Max Filter
			Subquery<Long> effectiveDate = query.subquery(Long.class);
			Root<ProductMaster> ocpm1 = effectiveDate.from(ProductMaster.class);
			effectiveDate.select(cb.max(ocpm1.get("effectiveDateStart")));
			Predicate a1 = cb.equal(ocpm1.get("productId"), pm.get("productId"));
			Predicate a2 = cb.equal(ocpm1.get("companyId"), pm.get("companyId"));
			Predicate a3 = cb.lessThanOrEqualTo(ocpm1.get("effectiveDateStart") , today);
			effectiveDate.where(a1,a2,a3); 
					
			// Order By
			List<Order> orderList = new ArrayList<Order>();
			orderList.add(cb.asc(pm.get("entryDate")));
			
			//In 
			Expression<String>e0=pm.get("companyId");
			
			// Where
			Predicate n1 = cb.equal(pm.get("status"), "Y");
			Predicate n2 = cb.equal(pm.get("effectiveDateStart"), effectiveDate);
			Predicate n3 = e0.in(companyIds);
			
			query.where(n1, n2, n3).orderBy(orderList);
		
			// Get Result
			TypedQuery<ProductCriteriaRes> result = em.createQuery(query);
			list = result.getResultList();
				
		} catch(Exception e ) {
			e.printStackTrace();
			log.info("Exception is --->" + e.getMessage());
			return null;
		}
		return list  ; 
	}

	public List<LoginProductCriteriaRes> getBrokerProductDetails(String loginId , List<String> companyIds , Date today ) {
		List<LoginProductCriteriaRes> list = new ArrayList<LoginProductCriteriaRes>(); 
		try {
			// Login Product Query	
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<LoginProductCriteriaRes> query = cb.createQuery(LoginProductCriteriaRes.class);

			Root<LoginProductMaster> lm  = query.from(LoginProductMaster.class);
			
			// Select Product Name SubQuery for Effective Date Max Filter 
			Subquery<Long> pmEff = query.subquery(Long.class);
			Root<ProductMaster> pm = pmEff.from(ProductMaster.class);
			Subquery<Long> product = query.subquery(Long.class);
			Root<ProductMaster> p = product.from(ProductMaster.class);
			
			pmEff.select( cb.max(pm.get("effectiveDateStart")) );
			Predicate i1 = cb.equal(p.get("companyId"), pm.get("companyId"));
			Predicate i2 = cb.equal(p.get("productId"), pm.get("productId"));
			Predicate i3 = cb.lessThanOrEqualTo(pm.get("effectiveDateStart") , today);
			pmEff.where(i1,i2,i3);
			
			product.select( p.get("productName")) ;
			Predicate pm1 = cb.equal(p.get("companyId"), lm.get("companyId"));
			Predicate pm2 = cb.equal(p.get("productId"), lm.get("productId"));
			Predicate pm3   = cb.equal(p.get("effectiveDateStart"),pmEff);
			product.where(pm1,pm2,pm3);
			
			// Select
			query.multiselect( lm.get("productId").alias("productId") ,  lm.get("companyId").alias("companyId") , product.alias("productName") ,
					lm.get("productName").alias("oldProductName") ,  lm.get("startLimit").alias("startLimit") , lm.get("endLimit").alias("endLimit") ,
					 lm.get("status").alias("status")  ,  lm.get("remarks").alias("remarks")  
					);

			// Product Effective Date Max Filter
			Subquery<Long> effectiveDate = query.subquery(Long.class);
			Root<LoginProductMaster> ocpm1 = effectiveDate.from(LoginProductMaster.class);
			effectiveDate.select(cb.max(ocpm1.get("effectiveDateStart")));
			Predicate a1 = cb.equal(ocpm1.get("loginId"), lm.get("loginId"));
			Predicate a2 = cb.equal(ocpm1.get("productId"), lm.get("productId"));
			Predicate a3 = cb.equal(ocpm1.get("companyId"), lm.get("companyId"));
			Predicate a4 = cb.lessThanOrEqualTo(ocpm1.get("effectiveDateStart") , today);
			effectiveDate.where(a1,a2,a3,a4); 
					
			// Order By
			List<Order> orderList = new ArrayList<Order>();
			orderList.add(cb.asc(lm.get("entryDate")));
			
			//In 
			Expression<String>e0=lm.get("companyId");
			
			// Where
			Predicate n1 = cb.equal(lm.get("loginId"), loginId );
			Predicate n2 = cb.equal(lm.get("effectiveDateStart"), effectiveDate);
			Predicate n3 = e0.in(companyIds);
			
			query.where(n1, n2, n3).orderBy(orderList);
		
			// Get Result
			TypedQuery<LoginProductCriteriaRes> result = em.createQuery(query);
			list = result.getResultList();
				
		} catch(Exception e ) {
			e.printStackTrace();
			log.info("Exception is --->" + e.getMessage());
			return null;
		}
		return list  ; 
	}

//*************************************** Get Login Company Products Apis Methods **********************************************************//
	
	@Override
	public List<BrokerCompanyProductsGetRes> getBrokerCompanyProducts(BrokerCompanyProductGetReq req) {
		List<BrokerCompanyProductsGetRes> productList = new ArrayList<BrokerCompanyProductsGetRes>();
		try {
			Calendar cal = new GregorianCalendar();
			Date today = new Date();
			cal.setTime(today); cal.set(Calendar.HOUR_OF_DAY, 23);cal.set(Calendar.MINUTE, 50);
			today = cal.getTime() ;
			
			String loginId = req.getLoginId() ;
			List<String> companyIds = new ArrayList<String>() ;
			companyIds.add(req.getInsuranceId());
			
			List<LoginProductCriteriaRes> loginProducts = getBrokerProductDetails (loginId , companyIds , today ) ;
				
			for(LoginProductCriteriaRes data :  loginProducts) {
				BrokerCompanyProductsGetRes productRes = new BrokerCompanyProductsGetRes();
				
				String pattern = "#####0";
				DecimalFormat df = new DecimalFormat(pattern);
				productRes.setProductId(data.getProductId()==null?"" :data.getProductId().toString() );
				productRes.setProductName(data.getProductName());
				productRes.setOldProductName(data.getOldProductName());
				productRes.setStartLimit(data.getStartLimit()==null?"" : df.format(data.getStartLimit()) );
				productRes.setEndLimit(data.getEndLimit()==null?"" :df.format(data.getEndLimit()) );
				productList.add(productRes);
			
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Exception is --->" + e.getMessage());
			return null;
		}
		return productList;
	}
}
