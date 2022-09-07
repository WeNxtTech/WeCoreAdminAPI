package com.maan.eway.service;

import java.util.List;

import com.maan.eway.req.SubUserTypeReq;
import com.maan.eway.res.DropDownRes;

public interface DropDownService {

	List<DropDownRes> getgender();

	List<DropDownRes> getUserType();

	List<DropDownRes> getSubUserType(SubUserTypeReq req);

	List<DropDownRes> getConstMaterial();

	List<DropDownRes> getOutbuildingConst();

	List<DropDownRes> getAboutBuilding();

	List<DropDownRes> getStateExtent();

}
