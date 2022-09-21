package com.maan.eway.auth.service.impl;

import java.text.DecimalFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.maan.eway.auth.dto.BrokerProductCompaniesRes;
import com.maan.eway.auth.dto.BrokerProductsGetRes;
import com.maan.eway.auth.dto.ChangePasswordReq;
import com.maan.eway.auth.dto.ClaimLoginResponse;
import com.maan.eway.auth.dto.CommonLoginRes;
import com.maan.eway.auth.dto.LoginBranchDetailsRes;
import com.maan.eway.auth.dto.LoginProductCriteriaRes;
import com.maan.eway.auth.dto.LoginRequest;
import com.maan.eway.auth.service.AuthendicationService;
import com.maan.eway.auth.token.EncryDecryService;
import com.maan.eway.auth.token.JwtTokenUtil;
import com.maan.eway.auth.token.passwordEnc;
import com.maan.eway.bean.BranchMaster;
import com.maan.eway.bean.LoginMaster;
import com.maan.eway.bean.LoginMasterId;
import com.maan.eway.bean.LoginProductMaster;
import com.maan.eway.bean.LoginUserInfo;
import com.maan.eway.bean.ProductMaster;
import com.maan.eway.bean.SessionMaster;
import com.maan.eway.repository.BranchMasterRepository;
import com.maan.eway.repository.LoginMasterRepository;
import com.maan.eway.repository.LoginUserInfoRepository;
import com.maan.eway.repository.SessionMasterRepository;


@Lazy
@Service
public class AuthendicationServiceImpl implements AuthendicationService, UserDetailsService {

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	@Autowired
	private LoginMasterRepository loginRepo;
	@Autowired
	private SessionMasterRepository sessionRep;
	@Autowired
	private EncryDecryService endecryService;
	@Autowired
	private BranchMasterRepository branchRepo;
	
	@Autowired
	private LoginUserInfoRepository loginUserRepo ;
	

	
	
	@PersistenceContext
	private EntityManager em;
	
	private Logger log = LogManager.getLogger(AuthendicationServiceImpl.class);
	
	@Override
	public CommonLoginRes checkUserLogin(LoginRequest mslogin, HttpServletRequest http) {
		CommonLoginRes res = new CommonLoginRes();
		try {
			passwordEnc passEnc = new passwordEnc();
			String epass = passEnc.crypt(mslogin.getPassword().trim());
			log.info("Encrpted password "+epass);
			LoginMaster login =loginRepo.findByLoginIdAndPassword(mslogin.getLoginId(),epass);
			if (login != null ) {
				http.getSession().removeAttribute(mslogin.getLoginId());
				String token = jwtTokenUtil.doGenerateToken(mslogin.getLoginId());
				log.info("-----token------" + token);
				SessionMaster session = new SessionMaster();
				session.setLoginId(mslogin.getLoginId());
				session.setTokenId(token);
				session.setStatus("ACTIVE");
				String temptoken = bCryptPasswordEncoder.encode("CommercialClaim");
				session.setTempTokenid(temptoken);
				session.setUserType(login.getUserType());
				session.setSubUserType(login.getSubUserType());
				Date today = new Date(); 
				session.setEntryDate(today);
				session.setStartTime(today);
				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.MINUTE, 15);
				Date endTime = cal.getTime();
				session.setEndTime(endTime );
				session =sessionRep.save(session);
				ClaimLoginResponse loginRes = setTokenResponse(session,login,mslogin);
				
				//Response 
				res.setCommonResponse(loginRes);
				res.setIsError(false);
				res.setErrorMessage(Collections.emptyList());
				res.setMessage("Success");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}
	
	private ClaimLoginResponse setTokenResponse(SessionMaster session, LoginMaster login, LoginRequest mslogin) {
		ClaimLoginResponse r = new ClaimLoginResponse();
		try {
			LoginUserInfo userInfo = loginUserRepo.findByLoginId(login.getLoginId());
			
			r.setToken(session.getTempTokenid());
			r.setLoginId(login.getLoginId());
			r.setUserName(userInfo.getUserName());
			r.setUserMail(StringUtils.isBlank(userInfo.getUserMail())?"":userInfo.getUserMail());
			r.setUserMobile(StringUtils.isBlank(userInfo.getUserMobile())?"":userInfo.getUserMobile());
			r.setUserType(login.getUserType());
			r.setSubUserType(login.getSubUserType());
			r.setOaCode(login.getOaCode());
			r.setBankCode(login.getBankCode());
			
			// Branch Res
			List<String> branches = new ArrayList<>(Arrays.asList(login.getAttachedBranches().split(",")));
			List<LoginBranchDetailsRes> loginBranchDetails = getBranchDetails(branches);
			r.setLoginBranchDetails(loginBranchDetails);
			
			// Products
			r.setCompanyProducts( getBrokerProducts(login.getLoginId() , login.getAttachedCompanies()));
			
			// Menu Ids
		  if(login.getMenuIds()!=null && login.getMenuIds().indexOf(",")!=-1) {
			  String[] split = login.getMenuIds().split(",");
			  List<String> asList = Arrays.asList(split);
			//  r.setMenuList(getMenuList( asList));
		  }				
			
		}catch (Exception e) {
			e.printStackTrace();
			log.info("Exception is ---> " + e.getMessage());
		}
		return r;
		
	}
	
	

	public List<BrokerProductCompaniesRes> getBrokerProducts(String loginId , String companies  ) {
		List<BrokerProductCompaniesRes> companyList = new ArrayList<BrokerProductCompaniesRes>();
		try {
			Calendar cal = new GregorianCalendar();
			Date today = new Date();
			cal.setTime(today); cal.set(Calendar.HOUR_OF_DAY, 23);cal.set(Calendar.MINUTE, 50);
			today = cal.getTime() ;
			
			List<String> companyIds = new ArrayList<>(Arrays.asList(companies.split(","))) ;
			
			List<LoginProductCriteriaRes> loginProducts = getBrokerProductDetails(loginId , companyIds , today ) ;
				
			// Grouping
			Map<String ,List<LoginProductCriteriaRes>> groupByCompany = loginProducts.stream().collect(Collectors.groupingBy(LoginProductCriteriaRes :: getCompanyId )) ;
			for (String company : groupByCompany.keySet()) { 
				BrokerProductCompaniesRes companyRes = new BrokerProductCompaniesRes();
				List<BrokerProductsGetRes> attachedProducts = new ArrayList<BrokerProductsGetRes>();
				
				List<LoginProductCriteriaRes> filterProduct = groupByCompany.get(company);
				
				for(LoginProductCriteriaRes data :  filterProduct) {
					BrokerProductsGetRes productRes = new BrokerProductsGetRes();
					
					if(StringUtils.isNotBlank(data.getStatus()) && data.getStatus().equalsIgnoreCase("Y")  ) {
						String pattern = "#####0";
						DecimalFormat df = new DecimalFormat(pattern);
						productRes.setOldProductName(data.getOldProductName() );
						productRes.setStartLimit(data.getStartLimit()==null?"" : df.format(data.getStartLimit()) );
						productRes.setEndLimit(data.getEndLimit()==null?"" :df.format(data.getEndLimit()) );
						productRes.setStatus(data.getStatus());
						productRes.setRemarks(data.getRemarks());

						productRes.setProductId(data.getProductId().toString());
						productRes.setProductName(data.getProductName());
						attachedProducts.add(productRes);
					}
				}
				
				// Response 
				companyRes.setInsuranceId(filterProduct.get(0).getCompanyId() );
				companyRes.setAttachedProducts(attachedProducts);
				companyList.add(companyRes);
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Exception is --->" + e.getMessage());
			return null;
		}
		return companyList;
	}
	
	private List<LoginBranchDetailsRes> getBranchDetails(List<String> branches) {
		List<LoginBranchDetailsRes> loginBranchDetails = new ArrayList<LoginBranchDetailsRes>();
		try {
			Date today = new Date();
			Calendar cal = new GregorianCalendar();
			cal.setTime(today);
			cal.set(Calendar.HOUR_OF_DAY, 23);
			cal.set(Calendar.MINUTE, 59);
			today = cal.getTime();

			
			// Criteria
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<BranchMaster> query = cb.createQuery(BranchMaster.class);
			List<BranchMaster> list = new ArrayList<BranchMaster>();

			// Find All
			Root<BranchMaster> b = query.from(BranchMaster.class);

			// Select
			query.select(b);

			// Effective Date Max Filter
			Subquery<Long> effectiveDate = query.subquery(Long.class);
			Root<BranchMaster> ocpm1 = effectiveDate.from(BranchMaster.class);
			effectiveDate.select(cb.max(ocpm1.get("effectiveDateStart")));
			Predicate a1 = cb.equal(ocpm1.get("branchCode"), b.get("branchCode"));
			Predicate a2 = cb.lessThanOrEqualTo(ocpm1.get("effectiveDateStart") , today);
			effectiveDate.where(a1,a2);

			// Order By
			List<Order> orderList = new ArrayList<Order>();
			orderList.add(cb.asc(b.get("branchName")));
			//In 
			Expression<String>e0=b.get("branchCode");
			
			// Where
			Predicate n1 = cb.equal(b.get("status"), "Y");
			Predicate n2 = cb.equal(b.get("effectiveDateStart"), effectiveDate);
			Predicate n3 = e0.in(branches) ;

			query.where(n1, n2, n3).orderBy(orderList);

			// Get Result
			TypedQuery<BranchMaster> result = em.createQuery(query);
			list = result.getResultList();
			
			for(BranchMaster data :  list) {
				LoginBranchDetailsRes branchRes = new LoginBranchDetailsRes();
				branchRes.setInsuranceId(data.getCompanyId());
				branchRes.setBranchCode(data.getBranchCode());
				branchRes.setRegionCode(data.getRegionCode());
				branchRes.setBranchName(data.getBranchName());;
				loginBranchDetails.add(branchRes);
			}
			
		}catch (Exception e) {
			e.printStackTrace();
			log.info("Exception is ---> " + e.getMessage());
		}
		return loginBranchDetails;
		
	}
	
	@SuppressWarnings("static-access")
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		LoginMaster userList = new LoginMaster ();
		try {
			log.info("loadUserByUsername==>" + username);
			
			String[] split = username.split(":");
			
			LoginMasterId id = new LoginMasterId();
			id.setLoginId(split[0]);
			
			LoginMaster  userListopt = loginRepo.findByLoginId(split[0]);
			 if(userListopt!=null) {
				 userList = userListopt;
			 }
			if (userList!=null) {
				//user = userList.get(0);
				String pass = bCryptPasswordEncoder.encode(endecryService.decrypt("zQYgCo7GMZeX1tBQyzAi8Q=="));
				userList.setPassword(pass);
				Set<GrantedAuthority> grantedAuthorities = new HashSet<GrantedAuthority>();
				grantedAuthorities.add(new SimpleGrantedAuthority("ADMIN"));
				log.info("loadUserByTokenEncrypt==>" + userList.getPassword());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new org.springframework.security.core.userdetails.User(userList.getLoginId(), userList.getPassword(),getAuthority());
	}
	
	private List<SimpleGrantedAuthority> getAuthority() {
		return Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN"));
	}
	
	
	// Change Passowrd
	@Override
	public String LoginChangePassword(ChangePasswordReq req) {
		String res = new String();
		try {
		passwordEnc passEnc = new passwordEnc();
		String epass = passEnc.crypt(req.getOldpassword().trim());
		String newpass = passEnc.crypt(req.getNewPassword().trim());
		LoginMaster master = new LoginMaster();  
		log.info("EncryptPassword-->" + epass);
		LoginMaster model = loginRepo.findByLoginId(req.getLoginId());
		if(model !=null ) {
			master = model ;
			
			String pass1 = master.getPassword();
			String pass2 = master.getLpass1();
			String pass3 = master.getLpass2();
			String pass4 = master.getLpass4();
			String pass5 = master.getLpass5();
			
			master.setLpass1(pass1);
			master.setLpass2(pass2);
			master.setLpass3(pass3);
			master.setLpass4(pass4);
			master.setLpass5(pass5);
			master.setPassword(newpass);
			master.setPwdCount(master.getPwdCount()+1);
			
			Instant now = Instant.now();
			Instant after = now.plus(Duration.ofDays(45));
			Date dateAfter = Date.from(after);
			master.setLpassDate(dateAfter);
			LoginMaster table = loginRepo.save(master);
			
			if(table!=null) {
				res  = "Password Changed Successfully";
			}
			else {
				res  = "FAILED" ;
				
			}
		}
		
		
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Error-->" + e.getMessage());
		}
		return res;

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
			Predicate pm4  = cb.equal(p.get("status"),"Y");
			product.where(pm1,pm2,pm3,pm4);
			
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
	
}


