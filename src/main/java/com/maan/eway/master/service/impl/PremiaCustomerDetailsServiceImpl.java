package com.maan.eway.master.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.maan.eway.bean.BranchMaster;
import com.maan.eway.bean.CompanyProductMaster;
import com.maan.eway.bean.CustomerDetails;
import com.maan.eway.bean.HomePositionMaster;
import com.maan.eway.bean.InsuranceCompanyMaster;
import com.maan.eway.bean.LoginBranchMaster;
import com.maan.eway.bean.LoginMaster;
import com.maan.eway.bean.LoginProductMaster;
import com.maan.eway.bean.LoginUserInfo;
import com.maan.eway.bean.OccupationMaster;
import com.maan.eway.bean.PremiaCustomerDetails;
import com.maan.eway.error.Error;
import com.maan.eway.master.req.PremiaDropDownReq;
import com.maan.eway.master.res.PremiaCustomerDetailsCriteriaRes;
import com.maan.eway.master.res.PremiaCustomerDetailsRes;
import com.maan.eway.master.service.PremiaCustomerDetailsService;
import com.maan.eway.repository.LoginBranchMasterRepository;
import com.maan.eway.repository.PremiaCustomerDetailsRepository;
import com.maan.eway.res.CustomerDetailsCriteriaRes;
import com.maan.eway.res.DropDownRes;
import com.maan.eway.res.SuccessRes;
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
							resList.setCustomercode(data.getCustomerCode());
							resList.setCustomername(data.getCustomerName());
							resList.setBrokerBranchCode(data.getBranchCode());
							resList.setOrderId(orderId);
							res.add(resList);
						}
					}else {
						res = new ArrayList<PremiaCustomerDetailsRes>();
						PremiaCustomerDetailsRes errRes = new PremiaCustomerDetailsRes();
						errRes.setCustomername("No Record Found");
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
				if(req.getSourcetype().equalsIgnoreCase("Direct") ){
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
//					    Predicate n1 = cb.equal(c.get("customerAttachedTo"),agencyCode);
//						Predicate n2 = cb.equal(c.get("customerAttachedTo"), null);
//						Predicate n3 = cb.or(n1,n2);
					    Predicate n3 = cb.equal(c.get("companyId"),req.getCompanyId());
						Predicate n4 = cb.like(c.get("customerCode"),"%" + req.getSearchvalue() + "%" ) ;
						Predicate n5 = cb.like(cb.lower(c.get("customerName")),"%" + req.getSearchvalue().toLowerCase() + "%" ) ;
						Predicate n6 = cb.or(n4,n5);
						Predicate n7 = cb.equal(c.get("status"),"Y");
						Predicate n8 = cb.notEqual(c.get("branchCode"),req.getBranchCode());
						Predicate n9 =  cb.notEqual(c.get("customerType"),"001");
						query.where(n3,n6,n7,n8,n9).orderBy(orderList);
						
						// Get Result
						TypedQuery<PremiaCustomerDetails> result = em.createQuery(query);
						customerDetailsList = result.getResultList();
						
					//	customerDetailsList=whatsappRepo.customerDetailsList(req.getDivisioncode(),req.getSearchvalue()+"%",req.getLoginid());
						if(customerDetailsList.size()>0 && customerDetailsList!=null) {
							for (PremiaCustomerDetails data : customerDetailsList) {
								orderId = orderId + 1 ;
								// Response 
								PremiaCustomerDetailsRes resList = new PremiaCustomerDetailsRes();
								resList.setCustomercode(data.getCustomerCode());
								resList.setCustomername(data.getCustomerName());
								resList.setBrokerBranchCode(data.getBranchCode() );
								resList.setOrderId(orderId);
								res.add(resList);
							}
						}else {
							res = new ArrayList<PremiaCustomerDetailsRes>();
							PremiaCustomerDetailsRes errRes = new PremiaCustomerDetailsRes();
							errRes.setCustomername("No Record Found");
							res.add(errRes);
						}
					
				} else if (req.getSourcetype().equalsIgnoreCase("Broker") ) {
					String productId = req.getProductid() ;
					String  companyId = req.getCompanyId() ;
					String branchCode = req.getBranchCode() ;
					List<String> usertypes = new ArrayList<String>();
					usertypes.add("Broker");
					usertypes.add("User");
					List<String> subUsertypes = new ArrayList<String>();
					subUsertypes.add("b2b");
					subUsertypes.add("b2c");
					Date today = new Date() ;
					
					
					List<Tuple> loginList = getBrokerProducts ( productId ,companyId ,branchCode , usertypes , subUsertypes , today ) ;
					for (Tuple data : loginList) {
					//	LoginProductMaster product = (LoginProductMaster) data.get("loginProduct")  ;
						LoginMaster  login = (LoginMaster)   data.get("loginMaster") ;
						orderId = orderId + 1 ;
						// Response 
						PremiaCustomerDetailsRes resList = new PremiaCustomerDetailsRes();
						resList.setCustomercode(login.getAgencyCode().toString());
						resList.setCustomername(login.getLoginId());
						resList.setLoginId(login.getLoginId());
					//	resList.setBrokerBranchCode(branch.getBrokerBranchCode());
					//	resList.setBrokerBranchName(branch.getBrokerBranchName());
						resList.setOrderId(orderId);
						res.add(resList);
					}
					
				} else if (req.getSourcetype().equalsIgnoreCase("Agent") ) {
					String productId = req.getProductid() ;
					String  companyId = req.getCompanyId() ;
					String branchCode = req.getBranchCode() ;
					List<String> usertypes = new ArrayList<String>();
					usertypes.add("Broker");
					usertypes.add("User");
					List<String> subUsertypes = new ArrayList<String>();
					subUsertypes.add("bank");
					Date today = new Date() ;
					
					List<Tuple> loginList = getBrokerProducts ( productId ,companyId ,branchCode , usertypes , subUsertypes , today ) ;
					for (Tuple data : loginList) {
				//		LoginProductMaster product = (LoginProductMaster) data.get("loginProduct")  ;
						LoginMaster  login = (LoginMaster)   data.get("loginMaster") ;
						orderId = orderId + 1 ;
						// Response 
						PremiaCustomerDetailsRes resList = new PremiaCustomerDetailsRes();
						resList.setCustomercode(login.getAgencyCode().toString() );
						resList.setCustomername(login.getLoginId());
						resList.setLoginId(login.getLoginId());
					//	resList.setBrokerBranchCode(branch.getBrokerBranchCode());
					//	resList.setBrokerBranchName(branch.getBrokerBranchName());
						resList.setOrderId(orderId);
						res.add(resList);
					}
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
			
			
			// Select
			query.multiselect(l.alias("loginMaster")  ).distinct(true) ;
			
			// Order By
			List<Order> orderList = new ArrayList<Order>();
			orderList.add(cb.desc(l.get("updatedDate")));
			
			
			Subquery<Long> loginId = query.subquery(Long.class);
			Root<LoginBranchMaster> ocpm6 = loginId.from(LoginBranchMaster.class);
			loginId.select(ocpm6.get("loginId"));
			Predicate a19 = cb.equal(ocpm6.get("companyId"),b.get("companyId") );
			Predicate a20 = cb.equal(ocpm6.get("branchCode"),b.get("branchCode") );
			
			
			loginId.where(a19,a20);
			
			Subquery<Long> effectiveDate3 = query.subquery(Long.class);
			Root<LoginProductMaster> ocpm4 = effectiveDate3.from(LoginProductMaster.class);
			effectiveDate3.select(cb.max(ocpm4.get("effectiveDateStart")));
			Predicate a9 = cb.equal(c.get("productId"),ocpm4.get("productId") );
			Predicate a10 = cb.equal(c.get("companyId"),ocpm4.get("companyId") );
			Predicate a11 = cb.lessThanOrEqualTo(ocpm4.get("effectiveDateStart"), today);
			Predicate a15 = cb.equal(c.get("loginId"),ocpm4.get("loginId") );
			effectiveDate3.where(a9,a10,a11,a15);
			
			Subquery<Long> effectiveDate4 = query.subquery(Long.class);
			Root<LoginProductMaster> ocpm5 = effectiveDate4.from(LoginProductMaster.class);
			effectiveDate4.select(cb.max(ocpm5.get("effectiveDateEnd")));
			Predicate a12 = cb.equal(c.get("productId"),ocpm5.get("productId") );
			Predicate a13 = cb.equal(c.get("companyId"),ocpm5.get("companyId") );
			Predicate a14 = cb.greaterThanOrEqualTo(ocpm5.get("effectiveDateEnd"), todayEnd);
			Predicate a16 = cb.equal(c.get("loginId"),ocpm5.get("loginId") );
			effectiveDate4.where(a12,a13,a14,a16);
			//In 
			Expression<String>e0=c.get("loginId");
			Expression<String>e1=b.get("userType");
			Expression<String>e2=b.get("subUserType");
			
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
			query.where(n1,n2,n4,n5,n6,n7,n8,n9,n10,n11).orderBy(orderList);
			
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
	public List<DropDownRes> getBrokerBranches(PremiaDropDownReq req) {
		List<DropDownRes> resList = new ArrayList<DropDownRes>();
			try {
				List<LoginBranchMaster> lb = lbRepo.findByAgencyCodeAndStatusAndBranchCode(Integer.valueOf(req.getBrokerCode()) , "Y" ,req.getBranchCode());
					for (LoginBranchMaster data : lb ) {
						// Response 
						DropDownRes res = new DropDownRes(); 
						res.setCode(data.getBrokerBranchCode());
						res.setCodeDesc(data.getBrokerBranchName());
						resList.add(res);
						
					}
						
			}catch(Exception e) {
					e.printStackTrace();
					log.info("Exception is --->"+e.getMessage());
					return null;
					}
	 		return resList;
		}
		
}
