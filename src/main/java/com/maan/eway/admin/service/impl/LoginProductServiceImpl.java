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
import com.maan.eway.admin.req.AttachCompnayProductRequest;
import com.maan.eway.admin.req.AttachedProductReq;
import com.maan.eway.admin.res.LoginCreationRes;
import com.maan.eway.admin.service.LoginProductService;
import com.maan.eway.bean.LoginProductMaster;
import com.maan.eway.repository.LoginProductMasterRepository;

@Service
public class LoginProductServiceImpl  implements LoginProductService {
	
	@Autowired
	private LoginProductMasterRepository loginProductRepo ;
	
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


}
