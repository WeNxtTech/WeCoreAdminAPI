package com.maan.eway.master.service;

import java.util.List;

import com.maan.eway.error.Error;
import com.maan.eway.master.req.TravelPolicyTypeGetReq;
import com.maan.eway.master.req.TravelPolicyTypeSaveReq;
import com.maan.eway.master.res.TravelPolicyTypeGetRes1;
import com.maan.eway.res.SuccessRes;

public interface TravelPolicyTypeService {
	
	List<Error> validateTravelPolicyType(TravelPolicyTypeSaveReq req);

	SuccessRes insertTravelPolicyType(TravelPolicyTypeSaveReq req);

	TravelPolicyTypeGetRes1 getalltravelpolicytype(TravelPolicyTypeGetReq req);

}
