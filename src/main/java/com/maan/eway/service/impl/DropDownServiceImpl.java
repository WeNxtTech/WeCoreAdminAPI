package com.maan.eway.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.maan.eway.bean.InsuranceCompanyMaster;
import com.maan.eway.bean.ListITemValue;
import com.maan.eway.bean.UsertypeMaster;
import com.maan.eway.repository.InsuranceCompanyMasterRepository;
import com.maan.eway.repository.ListItemValueRepository;
import com.maan.eway.repository.UserTypeMasterRepository;
import com.maan.eway.res.DropDownRes;
import com.maan.eway.service.DropDownService;

@Service
@Transactional
public class DropDownServiceImpl implements DropDownService {
	
	@Autowired
	private ListItemValueRepository listRepo;
	
	@Autowired
	private UserTypeMasterRepository userTypeRepo;
	
	@Autowired
	private InsuranceCompanyMasterRepository insRepo;
	
	Gson json = new Gson();
	
	@PersistenceContext
	private EntityManager em;

	private Logger log = LogManager.getLogger(DropDownServiceImpl.class);

	// Gender
	@Override
	public List<DropDownRes> getgender() {
		List<DropDownRes> resList = new ArrayList<DropDownRes>();
		try {
			List<ListITemValue> getList = listRepo.findByItemTypeAndStatusOrderByItemCodeAsc("GENDER", "Y");

			for (ListITemValue data : getList) {
				DropDownRes res = new DropDownRes();
				res.setCode(data.getItemCode());
				res.setCodeDesc(data.getItemValue());
				resList.add(res);
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Exception is ---> " + e.getMessage());
			return null;
		}
		return resList;
	}

	@Override
	public List<DropDownRes> getUserTypes() {
		List<DropDownRes> resList = new ArrayList<DropDownRes>();
		try {
			List<UsertypeMaster> getList = userTypeRepo.findByStatusOrderByOrderIdAsc("Y");

			for (UsertypeMaster data : getList) {
				DropDownRes res = new DropDownRes();
				res.setCode(data.getUserCode());
				res.setCodeDesc(data.getUsertype());
				resList.add(res);
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Exception is ---> " + e.getMessage());
			return null;
		}
		return resList;
	}
	
	@Override
	public List<DropDownRes> getInsuranceCompanies() {
		List<DropDownRes> resList = new ArrayList<DropDownRes>();
		try {
			List<InsuranceCompanyMaster> getList = insRepo.findByStatusOrderByInsNameAsc("Y");

			for (InsuranceCompanyMaster data : getList) {
				DropDownRes res = new DropDownRes();
				res.setCode(data.getInsId());
				res.setCodeDesc(data.getInsName());
				resList.add(res);
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Exception is ---> " + e.getMessage());
			return null;
		}
		return resList;
	}
	
}
