package com.maan.eway.common.dropdownservice.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.maan.eway.bean.ListItemValue;
import com.maan.eway.common.dropdownservice.CommonDropDownService;
import com.maan.eway.repository.ListItemValueRepository;
import com.maan.eway.res.DropDownRes;

@Service
public class CommonDropDownServiceImpl  implements CommonDropDownService{


	private Logger log = LogManager.getLogger(CommonDropDownServiceImpl.class);

	@Autowired 
	private ListItemValueRepository repo;
	
	
	// Cover Note Type Drop Down

	@Override
	public List<DropDownRes> coverNoteType() {
		List<DropDownRes> resList = new ArrayList<DropDownRes>();
		try {
			List<ListItemValue> getList = repo.findByItemTypeAndStatusOrderByItemCodeAsc("COVER_NOTE_TYPE", "Y");

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
	public List<DropDownRes> paymentmode() {
		List<DropDownRes> resList = new ArrayList<DropDownRes>();
		try {
			List<ListItemValue> getList = repo.findByItemTypeAndStatusOrderByItemCodeAsc("PAYMENT_MODE", "Y");

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
	public List<DropDownRes> endorsementtype() {
		List<DropDownRes> resList = new ArrayList<DropDownRes>();
		try {
			List<ListItemValue> getList = repo.findByItemTypeAndStatusOrderByItemCodeAsc("ENDROSEMENT_TYPE", "Y");

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
	public List<DropDownRes> discounttypeoffered() {
		List<DropDownRes> resList = new ArrayList<DropDownRes>();
		try {
			List<ListItemValue> getList = repo.findByItemTypeAndStatusOrderByItemCodeAsc("DISCOUNT_TYPE_OFFERED", "Y");

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
	public List<DropDownRes> taxexcempted() {
		List<DropDownRes> resList = new ArrayList<DropDownRes>();
		try {
			List<ListItemValue> getList = repo.findByItemTypeAndStatusOrderByItemCodeAsc("IS_TAX_EXEMPTED", "Y");

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
	public List<DropDownRes> taxexcemptiontype() {
		List<DropDownRes> resList = new ArrayList<DropDownRes>();
		try {
			List<ListItemValue> getList = repo.findByItemTypeAndStatusOrderByItemCodeAsc("TAX_EXEMPTION_TYPE", "Y");

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
	public List<DropDownRes> policyholdertype() {
		List<DropDownRes> resList = new ArrayList<DropDownRes>();
		try {
			List<ListItemValue> getList = repo.findByItemTypeAndStatusOrderByItemCodeAsc("POLICY_HOLDER_TYPE", "Y");

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
	public List<DropDownRes> policyholderidtype() {
		List<DropDownRes> resList = new ArrayList<DropDownRes>();
		try {
			List<ListItemValue> getList = repo.findByItemTypeAndStatusOrderByItemCodeAsc("POLICY_HOLDER_ID_TYPE", "Y");

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
	public List<DropDownRes> policyholdergender() {
		List<DropDownRes> resList = new ArrayList<DropDownRes>();
		try {
			List<ListItemValue> getList = repo.findByItemTypeAndStatusOrderByItemCodeAsc("POLICY_HOLDER_GENDER", "Y");

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
