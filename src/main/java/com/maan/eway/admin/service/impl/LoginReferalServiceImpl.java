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
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
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
import com.maan.eway.admin.req.AttachIssuerReferalReq;
import com.maan.eway.admin.req.AttachReferalReq;
import com.maan.eway.admin.req.BrokerProductCompaniesRes;
import com.maan.eway.admin.req.BrokerProductGetReq;
import com.maan.eway.admin.req.IssuerCompanyReferalGetReq;
import com.maan.eway.admin.req.IssuerReferalGetReq;
import com.maan.eway.admin.res.BrokerProductsGetRes;
import com.maan.eway.admin.res.IssuerReferalCompanyGetRes;
import com.maan.eway.admin.res.IssuerReferalCompniesRes;
import com.maan.eway.admin.res.IssuerReferalCriteriaRes;
import com.maan.eway.admin.res.IssuerReferalGetRes;
import com.maan.eway.admin.res.LoginCreationRes;
import com.maan.eway.admin.res.ProductCriteriaRes;
import com.maan.eway.admin.res.ReferalCriteriaRes;
import com.maan.eway.admin.service.LoginReferalService;
import com.maan.eway.bean.InsuranceCompanyMaster;
import com.maan.eway.bean.LoginMaster;
import com.maan.eway.bean.LoginProductMaster;
import com.maan.eway.bean.LoginReferalMaster;
import com.maan.eway.bean.ProductMaster;
import com.maan.eway.bean.ReferalMaster;
import com.maan.eway.repository.BranchMasterRepository;
import com.maan.eway.repository.LoginMasterArchRepository;
import com.maan.eway.repository.LoginMasterRepository;
import com.maan.eway.repository.LoginProductMasterRepository;
import com.maan.eway.repository.LoginReferalMasterRepository;
import com.maan.eway.repository.LoginUserInfoArchRepository;
import com.maan.eway.repository.LoginUserInfoRepository;

@Service
public class LoginReferalServiceImpl implements LoginReferalService {

	@Autowired
	private LoginReferalMasterRepository loginReferalRepo ;
	
	@Autowired
	private LoginMasterRepository loginRepo ;
	
	@PersistenceContext
	private EntityManager em;

	Gson json = new Gson();


	private Logger log=LogManager.getLogger(LoginReferalServiceImpl.class);

//*************************************** Add Referal Apis Methods **********************************************************//	


	@Transactional
	@Override
	public LoginCreationRes attachIssuerReferal(AttachIssuerReferalReq req) {
		LoginCreationRes res = new LoginCreationRes();
		DozerBeanMapper dozerMapper = new  DozerBeanMapper();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy"); 
		try { 
				 
			for ( AttachReferalReq data : req.getAttachedReferals() ) {
				
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
				CriteriaQuery<LoginReferalMaster> query = cb.createQuery(LoginReferalMaster.class);
				List<LoginReferalMaster> list = new ArrayList<LoginReferalMaster>();
				
				// Find All
				Root<LoginReferalMaster> lr = query.from(LoginReferalMaster.class);

				// Select
				query.select(lr);

				// Effective Date Max Filter
				Subquery<Long> effectiveDate = query.subquery(Long.class);
				Root<LoginReferalMaster> ocpm1 = effectiveDate.from(LoginReferalMaster.class);
				effectiveDate.select(cb.max(ocpm1.get("effectiveDateStart")));
				Predicate a1 = cb.equal(ocpm1.get("referalId"), lr.get("referalId"));
				Predicate a2 = cb.equal(ocpm1.get("loginId"), lr.get("loginId"));
				Predicate a3 = cb.equal(ocpm1.get("companyId"), lr.get("companyId"));
				Predicate a4 = cb.lessThanOrEqualTo(ocpm1.get("effectiveDateStart") , startDate);
				effectiveDate.where(a1,a2,a3,a4);

				// Order By
				//List<Order> orderList = new ArrayList<Order>();
				//orderList.add(cb.asc(lp.get("effectiveDateStart")));
				
				// Where
				Predicate n1 = cb.equal(lr.get("referalId"), data.getReferalId());
				Predicate n2 = cb.equal(lr.get("loginId"), req.getLoginId());
				Predicate n3 = cb.equal(lr.get("companyId"), req.getInsuranceId() );
				Predicate n4 = cb.equal(lr.get("effectiveDateStart") , effectiveDate);

				query.where(n1, n2, n3,n4);//.orderBy(orderList);

				// Get Result
				TypedQuery<LoginReferalMaster> result = em.createQuery(query);
				list = result.getResultList();
				
				if(list.size() > 0 ) {
					loginReferalRepo.delete(list.get(0));
				}
				Date endDate = sdf.parse("12/12/2050");
				
				LoginReferalMaster save = new LoginReferalMaster();
				dozerMapper.map(data, save);
				save.setCompanyId(req.getInsuranceId());
				save.setEffectiveDateStart(effDate);	
				save.setEffectiveDateEnd(endDate);
				save.setEntryDate(new Date());
				save.setLoginId(req.getLoginId());
				loginReferalRepo.saveAndFlush(save);
				log.info("Saved Details is ---> " + json.toJson(save));
				if(list.size() > 0 ) {
					// Update Old Record
					LoginReferalMaster lastRecord = list.get(0) ;
					lastRecord.setEffectiveDateEnd(oldEndDate);
					loginReferalRepo.saveAndFlush(lastRecord);
				}
			}		
			
			res.setResponse("Referal Added Successfully");
			
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Exception is --->" + e.getMessage());
			return null;
		}
		return res;
	}
	
//*************************************** Get Referal Apis Methods **********************************************************//	

	@Override
	public List<IssuerReferalCompniesRes> getIssuerReferals(IssuerReferalGetReq req) {
		List<IssuerReferalCompniesRes> companyList = new ArrayList<IssuerReferalCompniesRes>();
		try {
			Calendar cal = new GregorianCalendar();
			Date today = new Date();
			cal.setTime(today); cal.set(Calendar.HOUR_OF_DAY, 23);cal.set(Calendar.MINUTE, 50);
			today = cal.getTime() ;
			
			String loginId = req.getLoginId() ;
			LoginMaster loginData = loginRepo.findByLoginId(loginId);
			List<String> companyIds = new ArrayList<>(Arrays.asList(loginData.getAttachedCompanies().split(","))) ;
			
			if(companyIds.size()>0 ) {
				
				List<ReferalCriteriaRes> referals = getReferalDetails( companyIds , today  );
				List<IssuerReferalCriteriaRes> loginReferals = getIssuerReferalDetails (loginId , companyIds , today ) ;
				
				// Grouping
				Map<String ,List<ReferalCriteriaRes>> groupByCompany = referals.stream().collect(Collectors.groupingBy(ReferalCriteriaRes :: getCompanyId )) ;
				for (String company : groupByCompany.keySet()) { 
					IssuerReferalCompniesRes companyRes = new IssuerReferalCompniesRes();
					List<IssuerReferalGetRes> attachedReferals = new ArrayList<IssuerReferalGetRes>();
					
					List<ReferalCriteriaRes> filterReferals = groupByCompany.get(company);
					
					for(ReferalCriteriaRes data :  filterReferals) {
						IssuerReferalGetRes referalRes = new IssuerReferalGetRes();
						
						// Filter Login Referals
						List<IssuerReferalCriteriaRes> filterLoginReferals = loginReferals.stream().filter( o -> o.getCompanyId().equalsIgnoreCase(data.getCompanyId()) && o.getReferalId().equals(data.getReferalId()) ).collect(Collectors.toList());
						
						referalRes.setStatus("N");
						if (filterLoginReferals.size() > 0 ) {
							String pattern = "#####0";
							DecimalFormat df = new DecimalFormat(pattern);
							referalRes.setOldReferalName(filterLoginReferals.get(0).getOldReferalName() );
							referalRes.setSumInsuredStart(filterLoginReferals.get(0).getSumInsuredStart()==null?"" : df.format(filterLoginReferals.get(0).getSumInsuredStart()) );
							referalRes.setSumInsuredEnd(filterLoginReferals.get(0).getSumInsuredEnd()==null?"" :df.format(filterLoginReferals.get(0).getSumInsuredEnd()) );
							referalRes.setStatus(filterLoginReferals.get(0).getStatus());
							referalRes.setRemarks(filterLoginReferals.get(0).getRemarks());	
						;
						}
						
						referalRes.setReferalId(data.getReferalId().toString());
						referalRes.setReferalName(data.getReferalName());
						referalRes.setReferalDesc(data.getReferalDesc() );
						attachedReferals.add(referalRes);
					}
					
					// Response 
					companyRes.setInsuranceId(filterReferals.get(0).getCompanyId() );
					companyRes.setCompanyName(filterReferals.get(0).getCompanyName() );
					companyRes.setAttachedReferals(attachedReferals);
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

	
	public List<ReferalCriteriaRes> getReferalDetails(List<String> companyIds , Date today ) {
		List<ReferalCriteriaRes> list = new ArrayList<ReferalCriteriaRes>();  
		try {
			// Product Query 	
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<ReferalCriteriaRes> query = cb.createQuery(ReferalCriteriaRes.class);
			
			Root<ReferalMaster> rm  = query.from(ReferalMaster.class);
			
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
			Predicate ins1 = cb.equal(ins.get("companyId"), rm  .get("companyId"));
			Predicate ins2  = cb.equal(ins.get("effectiveDateStart"),insEff);
			company.where(ins1,ins2);
			
			// Select
			query.multiselect( rm  .get("referalId").alias("referalId") , rm  .get("referalName").alias("referalName") , rm.get("companyId").alias("companyId") ,
					company.alias("companyName") , rm.get("referalDesc").alias("referalDesc")  );

			// Product Effective Date Max Filter
			Subquery<Long> effectiveDate = query.subquery(Long.class);
			Root<ReferalMaster> ocpm1 = effectiveDate.from(ReferalMaster.class);
			effectiveDate.select(cb.max(ocpm1.get("effectiveDateStart")));
			Predicate a1 = cb.equal(ocpm1.get("referalId"), rm.get("referalId"));
			Predicate a2 = cb.equal(ocpm1.get("companyId"), rm.get("companyId"));
			Predicate a3 = cb.lessThanOrEqualTo(ocpm1.get("effectiveDateStart") , today);
			effectiveDate.where(a1,a2,a3); 
					
			// Order By
			List<Order> orderList = new ArrayList<Order>();
			orderList.add(cb.asc(rm.get("entryDate")));
			
			//In 
			Expression<String>e0=rm.get("companyId");
			
			// Where
			Predicate n1 = cb.equal(rm.get("status"), "Y");
			Predicate n2 = cb.equal(rm.get("effectiveDateStart"), effectiveDate);
			Predicate n3 = e0.in(companyIds);
			
			query.where(n1, n2, n3).orderBy(orderList);
		
			// Get Result
			TypedQuery<ReferalCriteriaRes> result = em.createQuery(query);
			list = result.getResultList();
				
		} catch(Exception e ) {
			e.printStackTrace();
			log.info("Exception is --->" + e.getMessage());
			return null;
		}
		return list  ; 
	}

	public List<IssuerReferalCriteriaRes> getIssuerReferalDetails(String loginId , List<String> companyIds , Date today ) {
		List<IssuerReferalCriteriaRes> list = new ArrayList<IssuerReferalCriteriaRes>(); 
		try {
			// Login Product Query	
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<IssuerReferalCriteriaRes> query = cb.createQuery(IssuerReferalCriteriaRes.class);

			Root<LoginReferalMaster> lm  = query.from(LoginReferalMaster.class);
			
			// Select Referal Name SubQuery for Effective Date Max Filter 
			Subquery<Long> rmEff = query.subquery(Long.class);
			Root<ReferalMaster> rm = rmEff .from(ReferalMaster.class);
			Subquery<Long> referal = query.subquery(Long.class);
			Root<ReferalMaster> r = referal.from(ReferalMaster.class);
			
			rmEff .select( cb.max(rm.get("effectiveDateStart")) );
			Predicate i1 = cb.equal(r.get("companyId"), rm.get("companyId"));
			Predicate i2 = cb.equal(r.get("referalId"), rm.get("referalId"));
			Predicate i3 = cb.lessThanOrEqualTo(rm.get("effectiveDateStart") , today);
			rmEff .where(i1,i2,i3);
			
			referal.select( r.get("referalName")) ;
			Predicate rm1 = cb.equal(r.get("companyId"), lm.get("companyId"));
			Predicate rm2 = cb.equal(r.get("referalId"), lm.get("referalId"));
			Predicate rm3   = cb.equal(r.get("effectiveDateStart"),rmEff);
			referal.where(rm1,rm2,rm3);
			
			// Select
			query.multiselect( lm.get("referalId").alias("referalId"), lm.get("companyId").alias("companyId") ,
					referal.alias("referalName")  , lm.get("referalName").alias("oldReferalName") , 
					lm.get("sumInsuredStart").alias("sumInsuredStart")  , lm.get("sumInsuredEnd").alias("sumInsuredEnd")  ,
					 lm.get("status").alias("status") , lm.get("remarks").alias("remarks"));

			// Product Effective Date Max Filter
			Subquery<Long> effectiveDate = query.subquery(Long.class);
			Root<LoginReferalMaster> ocpm1 = effectiveDate.from(LoginReferalMaster.class);
			effectiveDate.select(cb.max(ocpm1.get("effectiveDateStart")));
			Predicate a1 = cb.equal(ocpm1.get("loginId"), lm.get("loginId"));
			Predicate a2 = cb.equal(ocpm1.get("referalId"), lm.get("referalId"));
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
			TypedQuery<IssuerReferalCriteriaRes> result = em.createQuery(query);
			list = result.getResultList();
				
		} catch(Exception e ) {
			e.printStackTrace();
			log.info("Exception is --->" + e.getMessage());
			return null;
		}
		return list  ; 
	}

	@Override
	public List<IssuerReferalCompanyGetRes> getIssuerCompanyReferal(IssuerCompanyReferalGetReq req) {
		List<IssuerReferalCompanyGetRes> referalList = new ArrayList<IssuerReferalCompanyGetRes>();
		try {
			Calendar cal = new GregorianCalendar();
			Date today = new Date();
			cal.setTime(today); cal.set(Calendar.HOUR_OF_DAY, 23);cal.set(Calendar.MINUTE, 50);
			today = cal.getTime() ;
			
			String loginId = req.getLoginId() ;
			List<String> companyIds = new ArrayList<String>() ;
			companyIds.add(req.getInsuranceId());
			
			List<IssuerReferalCriteriaRes> loginReferals = getIssuerReferalDetails (loginId , companyIds , today ) ;
				
			for(IssuerReferalCriteriaRes data :  loginReferals) {
				IssuerReferalCompanyGetRes referalRes = new IssuerReferalCompanyGetRes();
				
				String pattern = "#####0";
				DecimalFormat df = new DecimalFormat(pattern);
				referalRes.setOldReferalName(data.getOldReferalName());
				referalRes.setSumInsuredStart(data.getSumInsuredStart()==null?"" : df.format(data.getSumInsuredStart()) );
				referalRes.setSumInsuredEnd(data.getSumInsuredEnd()==null?"" :df.format(data.getSumInsuredEnd()) );
				referalRes.setReferalId(data.getReferalId().toString());
				referalRes.setReferalName(data.getReferalName());
				referalList.add(referalRes);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Exception is --->" + e.getMessage());
			return null;
		}
		return referalList ;
	}
}
