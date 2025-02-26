package com.maan.eway.master.service;

import java.util.List;

import com.maan.eway.bean.ExcessMaster;
import com.maan.eway.master.req.ExcessMasterGetAllReq;
import com.maan.eway.master.req.ExcessMasterGetReq;
import com.maan.eway.master.req.ExcessMasterSaveUpReq;
import com.maan.eway.master.res.ExcessMasterRes;

public interface ExcessMasterService {
	
	List<ExcessMaster> saveAndUpdateExcessMaster(List<ExcessMasterSaveUpReq> req);

	List<ExcessMasterRes> getallExcessMaster(ExcessMasterGetAllReq req);
	
	List<ExcessMasterRes> getAllActiveExcessMaster(ExcessMasterGetAllReq req);
	
	ExcessMasterRes getExcessMaster (ExcessMasterGetReq req);

}
