package com.maan.eway.master.service;

import java.util.List;

import com.maan.eway.error.Error;
import com.maan.eway.master.req.PolicyTypeMasterChangeStatusReq;
import com.maan.eway.master.req.PolicyTypeMasterGetAllReq;
import com.maan.eway.master.req.PolicyTypeMasterGetReq;
import com.maan.eway.master.req.PolicyTypeMasterSaveReq;
import com.maan.eway.master.res.PolicyTypeMasterGetRes;
import com.maan.eway.res.DropDownRes;
import com.maan.eway.res.SuccessRes;

public interface PolicyTypeMasterService {

	List<String> validatePolicyType(PolicyTypeMasterSaveReq req);

	SuccessRes insertPolicyType(PolicyTypeMasterSaveReq req);

	PolicyTypeMasterGetRes getPolicyType(PolicyTypeMasterGetReq req);

	List<PolicyTypeMasterGetRes> getallPolicyType(PolicyTypeMasterGetAllReq req);

	List<PolicyTypeMasterGetRes> getallactivePolicyType(PolicyTypeMasterGetAllReq req);

	List<DropDownRes> getPolicyTypeMasterDropdown( PolicyTypeMasterGetAllReq req );

	SuccessRes changeStatusOfPolicyType(PolicyTypeMasterChangeStatusReq req);

}
