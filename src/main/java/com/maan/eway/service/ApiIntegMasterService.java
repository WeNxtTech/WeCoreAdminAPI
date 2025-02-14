/**
 * @author : Ashok Kumar S 
 * @since  : 11-02-2025
 */
package com.maan.eway.service;

import java.util.List;

import com.maan.eway.bean.ApiIntegMaster;
import com.maan.eway.req.ApiIntegMasterGetAllReq;
import com.maan.eway.req.ApiIntegMasterGetReq;
import com.maan.eway.req.ApiIntegMasterSaveUpReq;
import com.maan.eway.res.ApiIntegMasterRes;

public interface ApiIntegMasterService {
	
	public List<ApiIntegMasterRes> getAllApiIntegMasterDetails (ApiIntegMasterGetAllReq req);
	
	public ApiIntegMasterRes getApiIntegMasterDetails(ApiIntegMasterGetReq req);
	
	public ApiIntegMaster saveUpdateApiIntegMasterDetails (ApiIntegMasterSaveUpReq req);
	

}
