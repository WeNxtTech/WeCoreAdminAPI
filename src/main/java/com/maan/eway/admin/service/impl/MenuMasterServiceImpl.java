package com.maan.eway.admin.service.impl;

import java.util.ArrayList;
import java.util.List;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.maan.eway.admin.req.MenuServiceReq;
import com.maan.eway.admin.res.AdminListRes;
import com.maan.eway.admin.res.MenuServiceRes;
import com.maan.eway.admin.service.MenuMasterService;
import com.maan.eway.bean.MenuMaster;
import com.maan.eway.repository.MenuMasterRepository;

@Service
@Transactional
public class MenuMasterServiceImpl implements MenuMasterService{

	@Autowired
	private MenuMasterRepository menurepo;
	
	@PersistenceContext
	private EntityManager em;
	
	private Logger log = LogManager.getLogger(MenuMasterService.class); 
	
	@Override
	public List<MenuServiceRes> menudisplay(MenuServiceReq req) {
		List<MenuServiceRes> resList = new ArrayList<MenuServiceRes>();
		DozerBeanMapper mapper = new DozerBeanMapper();
		try {
			CriteriaBuilder builder=	em.getCriteriaBuilder();
			CriteriaQuery<MenuMaster> criteriaQuery = builder.createQuery(MenuMaster.class);
			// Find All
			Root<MenuMaster> root =criteriaQuery.from(MenuMaster.class);
			// Select
			criteriaQuery.select(root);
			
			// Order By
			List<Order> orderList = new ArrayList<Order>();
			orderList.add(builder.asc(root.get("menuId")));

			Predicate n1 = builder.like(root.get("admin"),"%" + req.getUserType() + "%" ) ;
			criteriaQuery.where(n1).orderBy(orderList);

			TypedQuery<MenuMaster> query = em.createQuery(criteriaQuery);
			List<MenuMaster> menulist = query.getResultList();
			for(MenuMaster data : menulist) {
				MenuServiceRes res = new MenuServiceRes();
				res = mapper.map(data, MenuServiceRes.class);
				List<AdminListRes> adminlist = new ArrayList<AdminListRes>();
				if(req.getSubUserType().equalsIgnoreCase("high")) {
					AdminListRes adminres = new AdminListRes();
					adminres=mapper.map(menulist, AdminListRes.class);
					adminlist.add(adminres);
				}
				res.setAdminlist(adminlist);
				resList.add(res);
			}
		}
		catch(Exception e) {
		e.printStackTrace();
		log.info("Log Details"+e.getMessage());
		return null;
		}
		return resList;
	}
	
}
