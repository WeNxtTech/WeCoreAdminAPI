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
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
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
import com.maan.eway.bean.CustomerDetails;
import com.maan.eway.bean.HomePositionMaster;
import com.maan.eway.bean.InsuranceCompanyMaster;
import com.maan.eway.bean.LoginMaster;
import com.maan.eway.bean.LoginUserInfo;
import com.maan.eway.bean.OccupationMaster;
import com.maan.eway.bean.PremiaCustomerDetails;
import com.maan.eway.error.Error;
import com.maan.eway.master.req.PremiaDropDownReq;
import com.maan.eway.master.res.PremiaCustomerDetailsCriteriaRes;
import com.maan.eway.master.res.PremiaCustomerDetailsRes;
import com.maan.eway.master.service.PremiaCustomerDetailsService;
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
	
	
	Gson json = new Gson();
	
	private Logger log = LogManager.getLogger(PremiaCustomerDetailsServiceImpl.class);

	@Override
	public List<PremiaCustomerDetailsRes> searchPremiacustDetails(PremiaDropDownReq req) {
		List<PremiaCustomerDetailsRes> res = new ArrayList<PremiaCustomerDetailsRes>();
//	List<PremiaCustomerDetailsCriteriaRes> resList=new ArrayList<PremiaCustomerDetailsCriteriaRes>();
		try {
			if(StringUtils.isNotBlank(req.getBranchCode())&& StringUtils.isNotBlank(req.getSearchvalue())){
				if(req.getSearchvalue().length()<3) {
					res = new ArrayList<PremiaCustomerDetailsRes>();
					PremiaCustomerDetailsRes errRes = new PremiaCustomerDetailsRes();
					errRes.setCustomername("No Record Found");
					res.add(errRes);
				}else {
					
					List<PremiaCustomerDetailsCriteriaRes> customerDetailsList=new ArrayList<PremiaCustomerDetailsCriteriaRes>(); 
				
					CriteriaBuilder cb = em.getCriteriaBuilder();
					CriteriaQuery<PremiaCustomerDetailsCriteriaRes> query = cb.createQuery(PremiaCustomerDetailsCriteriaRes.class);
					
					Root<PremiaCustomerDetails> c = query.from(PremiaCustomerDetails.class);
					
					// Select Core_App_Code 
					Subquery<Long> coreAppCode = query.subquery(Long.class);
					Root<BranchMaster> core = coreAppCode.from(BranchMaster.class);
					coreAppCode.select( core.get("coreAppCode")) ;
					Predicate i1 = cb.equal(core.get("branchCode"), req.getBranchCode());
					Predicate i2 = cb.between(i1, c.get("divisionFrom"),c.get("divisionTo"));
					coreAppCode.where(i1,i2);
					
//					// Get OACode from Login Master
//					Subquery<Long> agencyCode = query.subquery(Long.class);
//					Root<LoginMaster> lm2 = agencyCode.from(LoginMaster.class);
//					agencyCode.select(lm2.get("agencyCode"));
//					Predicate lp2 = cb.equal(lm2.get("loginId"), req.getLoginid());
//					agencyCode.where(lp2);
					
					// Select Core_App_Code With Status Y From Branch Master
					Subquery<Long> coreAppCode1 = query.subquery(Long.class);
					Root<BranchMaster> core1 = coreAppCode1.from(BranchMaster.class);
					coreAppCode1.select( core1.get("coreAppCode")) ;
					Predicate co1 = cb.equal(core1.get("branchCode"), req.getBranchCode());
					Predicate co2 = cb.equal(core1.get("status"), "Y");
					coreAppCode1.where(co1,co2);
					
					query.multiselect(
							 c.get("customerCode").alias("customerCode")
							,c.get("customerName").alias("customerName"));
				
					// Order By
					List<Order> orderList = new ArrayList<Order>();
				    orderList.add(cb.asc(c.get("customerCode")));
				
					// Where 
//				    Predicate n1 = cb.equal(c.get("customerAttachedTo"),agencyCode);
//					Predicate n2 = cb.equal(c.get("customerAttachedTo"), null);
//					Predicate n3 = cb.or(n1,n2);
				    Predicate n3 = cb.equal(c.get("companyId"),req.getCompanyId());
					Predicate n4 = cb.like(c.get("customerCode"),"%" + req.getSearchvalue() + "%" ) ;
					Predicate n5 = cb.like(cb.lower(c.get("customerName")),"%" + req.getSearchvalue().toUpperCase() + "%" ) ;
					Predicate n6 = cb.or(n4,n5);
					Predicate n7 = cb.equal(c.get("status"),"Y");
					Predicate n8 = cb.notEqual(c.get("customerCategory"),coreAppCode1);
					Predicate n9 = c.get("customerType").in("001", "005", "009", "013" , "Individual");
					query.where(n3,n6,n7,n8,n9).orderBy(orderList);
					
					// Get Result
					TypedQuery<PremiaCustomerDetailsCriteriaRes> result = em.createQuery(query);
					customerDetailsList = result.getResultList();
					
				//	customerDetailsList=whatsappRepo.customerDetailsList(req.getDivisioncode(),req.getSearchvalue()+"%",req.getLoginid());
					if(customerDetailsList.size()>0 && customerDetailsList!=null) {
						for (PremiaCustomerDetailsCriteriaRes data : customerDetailsList) {
							// Response 
							PremiaCustomerDetailsRes resList = new PremiaCustomerDetailsRes();
							resList.setCustomercode(data.getCustomercode());
							resList.setCustomername(data.getCustomername());
							res.add(resList);
						}
					}else {
						res = new ArrayList<PremiaCustomerDetailsRes>();
						PremiaCustomerDetailsRes errRes = new PremiaCustomerDetailsRes();
						errRes.setCustomername("No Record Found");
						res.add(errRes);
					}
				}
			}
		}catch(Exception e) {
				e.printStackTrace();
				log.info("Exception is --->"+e.getMessage());
				return null;
				}
 		return res;
	}
	
		
}
