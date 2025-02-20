/**
 * @author : Ashok Kumar S 
 * @since  : 13-02-2025
 */
package com.maan.eway.service;

import java.util.List;

import com.maan.eway.error.Error;
import com.maan.eway.req.FlowFieldLOVGetReq;
import com.maan.eway.res.ListOfValuesRes;

public interface FlowFieldLOVService {
	
	public List<Error> validateParametersOfFlowFieldLOVGetRequest(FlowFieldLOVGetReq req);
	
	public List<ListOfValuesRes> dropdownToChooseParentJsonKey(FlowFieldLOVGetReq req);	
	
	public List<ListOfValuesRes> dropdownToChooseDatatypes();

}
