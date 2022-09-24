package com.maan.eway.admin.service;

import java.util.List;

import com.maan.eway.req.SubUserTypeReq;
import com.maan.eway.res.DropDownRes;

public interface AdminDropDownService {

	List<DropDownRes> getgender();

	List<DropDownRes> getUserType();

	List<DropDownRes> getSubUserType(SubUserTypeReq req);

	List<DropDownRes> getConstMaterial();

	List<DropDownRes> getOutbuildingConst();

	List<DropDownRes> getAboutBuilding();

	List<DropDownRes> getStateExtent();

	List<DropDownRes> getContentName();

	List<DropDownRes> getPropertyName();

}
