package com.maan.eway.admin.service.impl;

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
import com.maan.eway.admin.res.LoginCreationRes;
import com.maan.eway.admin.service.LoginReferalService;
import com.maan.eway.bean.LoginReferalMaster;
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

}
