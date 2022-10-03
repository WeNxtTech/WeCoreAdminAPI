package com.maan.eway.admin.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.maan.eway.admin.service.AdminDropDownService;
import com.maan.eway.bean.ListItemValue;
import com.maan.eway.repository.ListItemValueRepository;
import com.maan.eway.req.SubUserTypeReq;
import com.maan.eway.res.DropDownRes;
import com.maan.eway.res.SubUserTypeDropDownRes;

@Service
public class AdminDropDownServiceImpl  implements AdminDropDownService{


	private Logger log = LogManager.getLogger(AdminDropDownServiceImpl.class);

	
	@Autowired
	private ListItemValueRepository listRepo;
	
	
	
	
	// Gender
		@Override
		public List<DropDownRes> getgender() {
			List<DropDownRes> resList = new ArrayList<DropDownRes>();
			try {
				List<ListItemValue> getList = listRepo.findByItemTypeAndStatusOrderByItemCodeAsc("GENDER", "Y");

				for (ListItemValue data : getList) {
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
		public List<DropDownRes> getUserType() {
			List<DropDownRes> resList = new ArrayList<DropDownRes>();
			try {
				List<ListItemValue> getList = listRepo.findByItemTypeAndStatusOrderByItemCodeAsc("USER_TYPE", "Y");

				for (ListItemValue data : getList) {
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
		public List<SubUserTypeDropDownRes> getSubUserType(SubUserTypeReq req) {
			List<SubUserTypeDropDownRes> resList = new ArrayList<SubUserTypeDropDownRes>();
			try {
				List<ListItemValue> getList = listRepo.findByItemTypeAndStatusOrderByItemCodeAsc(req.getUserType(), "Y");

				for (ListItemValue data : getList) {
					SubUserTypeDropDownRes res = new SubUserTypeDropDownRes();
					res.setCode(data.getItemCode());
					res.setCodeDesc(data.getItemValue());
					res.setDisplayName(data.getParam1());
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
		public List<DropDownRes> getConstMaterial() {
			List<DropDownRes> resList = new ArrayList<DropDownRes>();
			try {
				List<ListItemValue> getList = listRepo.findByItemTypeAndStatusOrderByItemCodeAsc("CONST_MATERIAL", "Y");

				for (ListItemValue data : getList) {
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
		public List<DropDownRes> getOutbuildingConst() {
			List<DropDownRes> resList = new ArrayList<DropDownRes>();
			try {
				List<ListItemValue> getList = listRepo.findByItemTypeAndStatusOrderByItemCodeAsc("OUTBUILDING_CONST", "Y");

				for (ListItemValue data : getList) {
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
		public List<DropDownRes> getAboutBuilding() {
			List<DropDownRes> resList = new ArrayList<DropDownRes>();
			try {
				List<ListItemValue> getList = listRepo.findByItemTypeAndStatusOrderByItemCodeAsc("ABOUT_BUILDING", "Y");

				for (ListItemValue data : getList) {
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
		public List<DropDownRes> getStateExtent() {
			List<DropDownRes> resList = new ArrayList<DropDownRes>();
			try {
				List<ListItemValue> getList = listRepo.findByItemTypeAndStatusOrderByItemCodeAsc("STATE_EXTENT", "Y");

				for (ListItemValue data : getList) {
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
		public List<DropDownRes> getContentName() {
			List<DropDownRes> resList = new ArrayList<DropDownRes>();
			try {
				List<ListItemValue> getList = listRepo.findByItemTypeAndStatusOrderByItemCodeAsc("CONTENT_NAME", "Y");

				for (ListItemValue data : getList) {
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
		public List<DropDownRes> getPropertyName() {
			List<DropDownRes> resList = new ArrayList<DropDownRes>();
			try {
				List<ListItemValue> getList = listRepo.findByItemTypeAndStatusOrderByItemCodeAsc("PROPERTY_NAME", "Y");

				for (ListItemValue data : getList) {
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
		public List<DropDownRes> getProductIcons() {
			List<DropDownRes> resList = new ArrayList<DropDownRes>();
			try {
				List<ListItemValue> getList = listRepo.findByItemTypeAndStatusOrderByItemCodeAsc("PRODUCT_ICONS", "Y");
	
				for (ListItemValue data : getList) {
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
		public List<DropDownRes> getCalcTypes() {
			List<DropDownRes> resList = new ArrayList<DropDownRes>();
			try {
				List<ListItemValue> getList = listRepo.findByItemTypeAndStatusOrderByItemIdAsc("CALCULATION_TYPE", "Y");

				for (ListItemValue data : getList) {
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
		public List<DropDownRes> getCoverageTypes() {
			List<DropDownRes> resList = new ArrayList<DropDownRes>();
			try {
				List<ListItemValue> getList = listRepo.findByItemTypeAndStatusOrderByItemCodeAsc("COVERAGE_TYPE", "Y");

				for (ListItemValue data : getList) {
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

}
