package com.maan.eway.master.service;

import java.util.List;

import com.maan.eway.master.req.ExcessMasterReq;
import com.maan.eway.master.res.ExcessMasterRes;
import com.maan.eway.master.service.impl.ExcessMasterDropdownReq;
import com.maan.eway.res.DropDownRes;
import com.maan.eway.res.SuccessRes;

public interface ExcessMasterService {
	
	List<SuccessRes> saveExcess(List<ExcessMasterReq> req);

    List<ExcessMasterRes> getallExcessMaster(ExcessMasterReq req);  
	

	List<ExcessMasterRes> getExcessMasterDropdown(ExcessMasterDropdownReq req);
	
	ExcessMasterRes getExcessMasterById(ExcessMasterDropdownReq req);

}
