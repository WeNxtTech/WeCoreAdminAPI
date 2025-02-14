/**
 * @author : Ashok Kumar S 
 * @since  : 11-02-2025
 */
package com.maan.eway.service;

import java.util.List;

import com.maan.eway.bean.FlowFieldDetails;
import com.maan.eway.error.Error;
import com.maan.eway.req.FlowFieldDetailsGetAllReq;
import com.maan.eway.req.FlowFieldDetailsGetReq;
import com.maan.eway.req.FlowFieldDetailsSaveUpReq;
import com.maan.eway.res.FlowFieldDetailsRes;

public interface FlowFieldDetailsService {
	
	public List<Error> validateFlowFieldDetailsGetAllRequest(FlowFieldDetailsGetAllReq req);
	
	public List<Error> validateFlowFieldDetailsGetRequest(FlowFieldDetailsGetReq req);
	
	public List<Error> validateFlowFieldDetailsSaveAndUpdateRequest(FlowFieldDetailsSaveUpReq req);
	
	public List<FlowFieldDetailsRes> getAllFlowFieldDetails(FlowFieldDetailsGetAllReq req);
	
	public FlowFieldDetailsRes getFlowFieldDetails(FlowFieldDetailsGetReq req);
	
	public FlowFieldDetails saveAndUpdateFlowFieldDetails(FlowFieldDetailsSaveUpReq req);
}
