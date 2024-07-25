package com.maan.eway.master.service.impl;

import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.maan.eway.bean.BranchMaster;
import com.maan.eway.bean.LoginBranchMaster;
import com.maan.eway.bean.LoginMaster;
import com.maan.eway.bean.LoginProductMaster;
import com.maan.eway.bean.LoginUserInfo;
import com.maan.eway.bean.PremiaCustomerDetails;
import com.maan.eway.master.req.PremiaDropDownReq;
import com.maan.eway.master.res.PremiaCustomerDetailsRes;
import com.maan.eway.master.res.PremiaCustomerRes;
import com.maan.eway.master.service.PremiaCustomerDetailsService;
import com.maan.eway.repository.LoginBranchMasterRepository;
import com.maan.eway.repository.PremiaCustomerDetailsRepository;
import com.maan.eway.res.BrokerCustCodeRes;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Tuple;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import jakarta.transaction.Transactional;
@Service
@Transactional
public class PremiaCustomerDetailsServiceImpl implements PremiaCustomerDetailsService {

	@Autowired
	private PremiaCustomerDetailsRepository repo;
	
	@PersistenceContext
	private EntityManager em;
	
	@Autowired
	private LoginBranchMasterRepository lbRepo ;
	
	Gson json = new Gson();
	
	@Value(value = "${premia.customer.api}")
	private String PremiaCustomerApiCall;
	
	@Value(value = "${ClaimBasicAuthPass}")
	private String ClaimBasicAuthPass;
	
	@Value(value = "${ClaimBasicAuthName}")
	private String ClaimBasicAuthName;

	
	private Logger log = LogManager.getLogger(PremiaCustomerDetailsServiceImpl.class);

	@Override
	public List<PremiaCustomerDetailsRes> searchPremiacustDetails(PremiaDropDownReq req) {
		List<PremiaCustomerDetailsRes> res = new ArrayList<PremiaCustomerDetailsRes>();
//	List<PremiaCustomerDetailsCriteriaRes> resList=new ArrayList<PremiaCustomerDetailsCriteriaRes>();
		Long orderId = 0L ;
		try {
			if(StringUtils.isNotBlank(req.getBranchCode()) ){
					List<PremiaCustomerDetails> customerDetailsList=new ArrayList<PremiaCustomerDetails>(); 
					
					CriteriaBuilder cb = em.getCriteriaBuilder();
					CriteriaQuery<PremiaCustomerDetails> query = cb.createQuery(PremiaCustomerDetails.class);
					
					Root<PremiaCustomerDetails> c = query.from(PremiaCustomerDetails.class);
					
					query.select( c
							);
				
					// Order By
					List<Order> orderList = new ArrayList<Order>();
				    orderList.add(cb.asc(c.get("customerCode")));
				    
					// Where 
	//			    Predicate n1 = cb.equal(c.get("customerAttachedTo"),agencyCode);
	//				Predicate n2 = cb.equal(c.get("customerAttachedTo"), null);
	//				Predicate n3 = cb.or(n1,n2);
				    Predicate n3 = cb.equal(c.get("companyId"),req.getCompanyId());
					Predicate n4 = cb.like(c.get("customerCode"),"%" + req.getSearchvalue() + "%" ) ;
					Predicate n5 = cb.like(cb.lower(c.get("customerName")),"%" + req.getSearchvalue().toLowerCase() + "%" ) ;
					Predicate n6 = cb.or(n4,n5);
					Predicate n7 = cb.equal(c.get("status"),"Y");
					Predicate n8 = cb.notEqual(c.get("branchCode"),req.getBranchCode());
					Predicate n9 = c.get("customerType").in("001");
					query.where(n3,n6,n7,n8,n9).orderBy(orderList);
					
					// Get Result
					TypedQuery<PremiaCustomerDetails> result = em.createQuery(query);
					customerDetailsList = result.getResultList();
					
				//	customerDetailsList=whatsappRepo.customerDetailsList(req.getDivisioncode(),req.getSearchvalue()+"%",req.getLoginid());
					if(customerDetailsList.size()>0 && customerDetailsList!=null) {
						for (PremiaCustomerDetails data : customerDetailsList) {
							// Response 
							orderId = orderId + 1 ;
							PremiaCustomerDetailsRes resList = new PremiaCustomerDetailsRes();
							resList.setCode(data.getCustomerCode());
							resList.setName(data.getCustomerName());
							resList.setBrokerBranchCode(data.getBranchCode());
							resList.setOrderId(orderId);
							res.add(resList);
						}
					}else {
						res = new ArrayList<PremiaCustomerDetailsRes>();
						PremiaCustomerDetailsRes errRes = new PremiaCustomerDetailsRes();
						errRes.setName("No Record Found");
						res.add(errRes);
					}
				
			}
		}catch(Exception e) {
				e.printStackTrace();
				log.info("Exception is --->"+e.getMessage());
				return null;
				}
 		return res;
	}

	@Override
	public List<PremiaCustomerDetailsRes> searchPremiaSourceCode(PremiaDropDownReq req) {
		List<PremiaCustomerDetailsRes> res = new ArrayList<PremiaCustomerDetailsRes>();
//		List<PremiaCustomerDetailsCriteriaRes> resList=new ArrayList<PremiaCustomerDetailsCriteriaRes>();
		Long orderId = 0L ;
			try {
				String productId = req.getProductid() ;
				String  companyId = req.getCompanyId() ;
				String branchCode = req.getBranchCode() ;
				List<String> usertypes = new ArrayList<String>();
				usertypes.add("Broker");
			//	usertypes.add("User");
				List<String> subUsertypes = new ArrayList<String>();
				subUsertypes.add(req.getSourcetype() );
				Date today = new Date() ;
				
				
				List<Tuple> loginList = getBrokerProducts ( productId ,companyId ,branchCode , usertypes , subUsertypes , today ) ;
				for (Tuple data : loginList) {
				//	LoginProductMaster product = (LoginProductMaster) data.get("loginProduct")  ;
					LoginMaster  login = (LoginMaster)   data.get("loginMaster") ;
					LoginUserInfo  loginUser = (LoginUserInfo)   data.get("loginUserInfo") ;
					orderId = orderId + 1 ;
					// Response 
					PremiaCustomerDetailsRes resList = new PremiaCustomerDetailsRes();
					resList.setCode(login.getAgencyCode().toString());
					resList.setName(login.getLoginId());
					resList.setLoginId(login.getLoginId());
					resList.setCustomerCode(loginUser.getCustomerCode());
					resList.setCustomerName(loginUser.getCustomerName());
				//	resList.setBrokerBranchCode(branch.getBrokerBranchCode());
				//	resList.setBrokerBranchName(branch.getBrokerBranchName());
					resList.setOrderId(orderId);
					res.add(resList);
				}
				
				
			}catch(Exception e) {
					e.printStackTrace();
					log.info("Exception is --->"+e.getMessage());
					return null;
					}
	 		return res;
		}
	
	public List<Tuple> getBrokerProducts(String productId ,String  companyId ,String branchCode ,List<String> usertypes , List<String> subUsertypes ,  Date today ) {
		 List<Tuple> list = new  ArrayList<Tuple>();
		try {
			Calendar cal = new GregorianCalendar(); 
			cal.setTime(today);
			cal.set(Calendar.HOUR_OF_DAY, 23);
			cal.set(Calendar.MINUTE, 1);
			today   = cal.getTime();
			cal.setTime(today);
			cal.set(Calendar.HOUR_OF_DAY, 1);
			cal.set(Calendar.MINUTE, 1);
			Date todayEnd   = cal.getTime();
			
			// Criteria
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class);
		
			// Find All
			Root<LoginProductMaster>    c = query.from(LoginProductMaster.class);		
			Root<LoginBranchMaster>    b = query.from(LoginBranchMaster.class);
			Root<LoginMaster>    l = query.from(LoginMaster.class);	
			Root<LoginUserInfo>    lu = query.from(LoginUserInfo.class);	
			
			// Select
			query.multiselect(l.alias("loginMaster") , lu.alias("loginUserInfo")  ).distinct(true) ;
			
			// Order By
			List<Order> orderList = new ArrayList<Order>();
			orderList.add(cb.desc(l.get("updatedDate")));
			
			
			Subquery<String> loginId = query.subquery(String.class);
			Root<LoginBranchMaster> ocpm6 = loginId.from(LoginBranchMaster.class);
			loginId.select(ocpm6.get("loginId"));
			Predicate a19 = cb.equal(ocpm6.get("companyId"),b.get("companyId") );
			Predicate a20 = cb.equal(ocpm6.get("branchCode"),b.get("branchCode") );
			
			
			loginId.where(a19,a20);
			
			Subquery<Timestamp> effectiveDate3 = query.subquery(Timestamp.class);
			Root<LoginProductMaster> ocpm4 = effectiveDate3.from(LoginProductMaster.class);
			effectiveDate3.select(cb.greatest(ocpm4.get("effectiveDateStart")));
			Predicate a9 = cb.equal(c.get("productId"),ocpm4.get("productId") );
			Predicate a10 = cb.equal(c.get("companyId"),ocpm4.get("companyId") );
			Predicate a11 = cb.lessThanOrEqualTo(ocpm4.get("effectiveDateStart"), today);
			Predicate a15 = cb.equal(c.get("loginId"),ocpm4.get("loginId") );
			effectiveDate3.where(a9,a10,a11,a15);
			
			Subquery<Timestamp> effectiveDate4 = query.subquery(Timestamp.class);
			Root<LoginProductMaster> ocpm5 = effectiveDate4.from(LoginProductMaster.class);
			effectiveDate4.select(cb.greatest(ocpm5.get("effectiveDateEnd")));
			Predicate a12 = cb.equal(c.get("productId"),ocpm5.get("productId") );
			Predicate a13 = cb.equal(c.get("companyId"),ocpm5.get("companyId") );
			Predicate a14 = cb.greaterThanOrEqualTo(ocpm5.get("effectiveDateEnd"), todayEnd);
			Predicate a16 = cb.equal(c.get("loginId"),ocpm5.get("loginId") );
			effectiveDate4.where(a12,a13,a14,a16);
			//In 
			Expression<String>e0=c.get("loginId");
			Expression<String>e1=l.get("userType");
			Expression<String>e2=l.get("subUserType");
			
		    // Where	
			Predicate n1 = cb.equal(c.get("effectiveDateStart"), effectiveDate3);
			Predicate n2 = cb.equal(c.get("effectiveDateEnd"), effectiveDate4);
			Predicate n4 = cb.equal(c.get("companyId"), companyId);
			Predicate n5 =e0.in(loginId);
			Predicate n6 =cb.equal(c.get("productId"), productId);
			Predicate n7 =e1.in(usertypes); 
			Predicate n8 =e2.in(subUsertypes);
			Predicate n9 = cb.equal(c.get("loginId"), b.get("loginId"));
			Predicate n10 = cb.equal(b.get("branchCode"),branchCode);
			Predicate n11 = cb.equal(c.get("loginId"), l.get("loginId"));
			Predicate n12 = cb.notEqual(c.get("status"), "D");
			Predicate n13 = cb.notEqual(c.get("status"), "N");
			Predicate n14 = cb.notEqual(l.get("status"), "N");
			Predicate n15 = cb.notEqual(b.get("status"), "N");
			Predicate n16 = cb.lessThanOrEqualTo(l.get("effectiveDateStart"), new Date());
			Predicate n17 = cb.equal(l.get("loginId"), lu.get("loginId"));
			query.where(n1,n2,n4,n5,n6,n7,n8,n9,n10,n11,n12,n13,n14,n15,n16,n17).orderBy(orderList);
			
			// Get Result
			TypedQuery<Tuple> result = em.createQuery(query);			
			list =  result.getResultList(); 
			
		} catch(Exception e ) {
			e.printStackTrace();
			log.info("Exception is --->" + e.getMessage());
			return null;
		}
		return list  ; 
	}

	@Override
	public List<BrokerCustCodeRes> getBrokerBranches(PremiaDropDownReq req) {
		List<BrokerCustCodeRes> resList = new ArrayList<BrokerCustCodeRes>();
			try { 
				if (StringUtils.isNotBlank(req.getBrokerCode())) {
					List<LoginBranchMaster> lb = lbRepo.findByAgencyCodeAndStatusAndBranchCodeAndEffectiveDateStartLessThanEqual(Integer.valueOf(req.getBrokerCode()) , "Y" ,req.getBranchCode(), new Date());
					for (LoginBranchMaster data : lb ) {
						// Response 
						BrokerCustCodeRes res = new BrokerCustCodeRes(); 
						res.setCode(data.getBrokerBranchCode());
						res.setCodeDesc(data.getBrokerBranchName());
						resList.add(res);
						
					}
				}
				
						
			}catch(Exception e) {
					e.printStackTrace();
					log.info("Exception is --->"+e.getMessage());
					return null;
					}
	 		return resList;
		}

	@Override
	public List<PremiaCustomerDetailsRes> searchPremiaBrokerCustomerCode(PremiaDropDownReq req) {
		List<PremiaCustomerDetailsRes> resList = new ArrayList<PremiaCustomerDetailsRes>();
//		List<PremiaCustomerDetailsCriteriaRes> resList=new ArrayList<PremiaCustomerDetailsCriteriaRes>();
			Long orderId = 0L ;
			try {
				
				String url = PremiaCustomerApiCall ;
				String auth = ClaimBasicAuthName +":"+ ClaimBasicAuthPass;
				//Branch Core App Code
				if(StringUtils.isNotBlank(req.getBranchCode())) {
				String coreAppCode= getByBranchCode(req.getBranchCode());
					req.setBranchCoreAppCode(coreAppCode);
				}else
				{
					req.setBranchCoreAppCode("");
				}
				
				byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes(StandardCharsets.US_ASCII));
		        String authHeader = "Basic " + new String( encodedAuth );
		     	RestTemplate restTemplate = new RestTemplate();
				HttpHeaders headers = new HttpHeaders();
				headers.setAccept(Arrays.asList(new MediaType[] { MediaType.APPLICATION_JSON }));
				headers.setContentType(MediaType.APPLICATION_JSON);
				headers.set("Authorization",authHeader);
				HttpEntity<Object> entityReq = new HttpEntity<Object>(req, headers);

				log.info("Api Url -----------> " +  url );
			    log.info("Request -----------> " + json.toJson(req) );
				ResponseEntity<Object> response = restTemplate.postForEntity(url, entityReq, Object.class);
				System.out.println(response.getBody());
				log.info("Response -----------> " + json.toJson(response.getBody()) );
				
				ObjectMapper mapper = new ObjectMapper();
				List<PremiaCustomerRes> premiaResList = mapper.convertValue(response.getBody() ,new TypeReference<List<PremiaCustomerRes>>(){});
				
				if(premiaResList.size()>0 && premiaResList!=null) {
					for(PremiaCustomerRes premia :  premiaResList) {
						PremiaCustomerDetailsRes res = new PremiaCustomerDetailsRes();
						res.setCode(premia.getCustomerCode());
						res.setName(premia.getCustomerName());
						res.setCustomerCode(premia.getCustomerCode());
						res.setCustomerName(premia.getCustomerName());
						res.setOrderId(orderId);
						resList.add(res);
					}
				}else {
					resList = new ArrayList<PremiaCustomerDetailsRes>();
					PremiaCustomerDetailsRes errRes = new PremiaCustomerDetailsRes();
					errRes.setName("No Record Found");
					resList.add(errRes);
					
				}
				
			}catch(Exception e) {
					e.printStackTrace();
					log.info("Exception is --->"+e.getMessage());
					return null;
					}
	 		return resList;
		}
	
	public String getByBranchCode(String branchCode) {
		String res = "";
		try {
			Date today = new Date();
			Calendar cal = new GregorianCalendar();
			cal.setTime(today);
			cal.set(Calendar.HOUR_OF_DAY, 23);
			cal.set(Calendar.MINUTE, 1);
			today = cal.getTime();
			// Criteria
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<BranchMaster> query = cb.createQuery(BranchMaster.class);
			List<BranchMaster> list = new ArrayList<BranchMaster>();
			
			// Find All
			Root<BranchMaster>    c = query.from(BranchMaster.class);		
			
			// Select
			query.select(c );
			
			// amendId Max Filter
			Subquery<Long> amendId = query.subquery(Long.class);
			Root<BranchMaster> ocpm1 = amendId.from(BranchMaster.class);
			amendId.select(cb.max(ocpm1.get("amendId")));
			Predicate a1 = cb.equal(c.get("branchCode"),ocpm1.get("branchCode") );
			
			amendId.where(a1);
			
			// Order By
			List<Order> orderList = new ArrayList<Order>();
			orderList.add(cb.asc(c.get("branchCode")));
			
			// Where
			Predicate n1 = cb.equal(c.get("amendId"), amendId);
			Predicate n4 = cb.equal(c.get("branchCode"), branchCode);
			query.where(n1,n4).orderBy(orderList);
			
			// Get Result
			TypedQuery<BranchMaster> result = em.createQuery(query);			
			list =  result.getResultList();  
			list = list.stream().filter(distinctByKey(o -> Arrays.asList(o.getBranchCode()))).collect(Collectors.toList());
			list.sort(Comparator.comparing(BranchMaster :: getBranchName ));
			res=list.get(0).getCoreAppCode();
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
}
