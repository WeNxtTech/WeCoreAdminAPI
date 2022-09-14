package com.maan.eway.admin.service.impl;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.eway.admin.req.MenuServiceReq;
import com.maan.eway.admin.res.AdminListRes;
import com.maan.eway.admin.res.MenuServiceRes;
import com.maan.eway.admin.res.UserMenuListRes;
import com.maan.eway.admin.service.MenuMasterService;
import com.maan.eway.bean.MenuMaster;
import com.maan.eway.repository.MenuMasterRepository;
import com.maan.eway.res.CustomerDetailsSearchRes;

@Service
@Transactional
public class MenuMasterServiceImpl implements MenuMasterService{

	@Autowired
	private MenuMasterRepository menurepo;
	
	@PersistenceContext
	private EntityManager em;
	
	private Logger log = LogManager.getLogger(MenuMasterService.class); 
	
	@Override
	public MenuServiceRes menudisplay(MenuServiceReq req) {
		MenuServiceRes response = new MenuServiceRes();
		ModelMapper mapper = new ModelMapper();
		try {
			CriteriaBuilder cb=	em.getCriteriaBuilder();
			CriteriaQuery<MenuMaster> criteriaQuery = cb.createQuery(MenuMaster.class);
			// Find All
			Root<MenuMaster> m =criteriaQuery.from(MenuMaster.class);
			// Select
			criteriaQuery.select(m);
			
			// Order By
			List<Order> orderList = new ArrayList<Order>();
			orderList.add(cb.asc(m.get("menuId")));

			Predicate  p1 = cb.equal(m.get("displayYn") , "Y");
			Predicate  p2 = cb.equal(m.get("status") , "Y");
			Predicate p3 = cb.like(m.get("usertype"), "%" + req.getUserType() + "%" );
			
			criteriaQuery.where(p1,p2,p3).orderBy(orderList);
			TypedQuery<MenuMaster> query = em.createQuery(criteriaQuery);
			List<MenuMaster> otherMenulist = query.getResultList();
			List<UserMenuListRes> userResList = new ArrayList<UserMenuListRes>();
			Type listType2 = new TypeToken<List<UserMenuListRes>>(){}.getType();
			userResList = mapper.map(otherMenulist ,listType2);
			
			List<AdminListRes> adminReslist = new ArrayList<AdminListRes>();
			if(req.getSubUserType().equalsIgnoreCase("high") ) {
				p3 =  cb.like(m.get("usertype"), "%" + "admin" + "%" );
				criteriaQuery.where(p1,p2,p3).orderBy(orderList);
				query = em.createQuery(criteriaQuery);
				List<MenuMaster> adminMenulist = query.getResultList();
				Type listType = new TypeToken<List<AdminListRes>>(){}.getType();
				adminReslist = mapper.map(adminMenulist ,listType);
				
			} 
			response.setAdminlist(adminReslist);
			response.setUserList(userResList);
		}
		catch(Exception e) {
		e.printStackTrace();
		log.info("Log Details"+e.getMessage());
		return null;
		}
		return response;
	}
	
}
