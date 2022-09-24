package com.maan.eway.common.service;

import java.util.List;

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

}
