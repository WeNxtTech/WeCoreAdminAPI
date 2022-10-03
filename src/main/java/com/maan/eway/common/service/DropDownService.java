package com.maan.eway.common.service;

import java.util.List;

import com.maan.eway.master.req.CityDropDownReq;
import com.maan.eway.master.req.RegionDropDownReq;
import com.maan.eway.master.req.StateDropDownReq;
import com.maan.eway.req.SubUserTypeReq;
import com.maan.eway.res.DropDownRes;

public interface DropDownService {

	
	
	List<DropDownRes> coverNoteType();

	List<DropDownRes> paymentmode();

	List<DropDownRes> endorsementtype();

	List<DropDownRes> discounttypeoffered();

	List<DropDownRes> taxexcempted();

	List<DropDownRes> taxexcemptiontype();

	List<DropDownRes> policyholdertype();

	List<DropDownRes> policyholderidtype();

	List<DropDownRes> policyholdergender();

	List<DropDownRes> nametitle();

	List<DropDownRes> notificationtype();

	List<DropDownRes> getCountryDropdown();

	List<DropDownRes> getRegionDropdown(RegionDropDownReq req);

	List<DropDownRes> getStateDropdown(StateDropDownReq req);

	List<DropDownRes> getCityDropdown(CityDropDownReq req);

}
