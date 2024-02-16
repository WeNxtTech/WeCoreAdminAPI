package com.maan.eway.master.service;

import java.util.List;

import com.maan.eway.error.Error;
import com.maan.eway.master.req.GetPolicyTypesubcoverReq;
import com.maan.eway.master.req.PolicyTypeMasterSubCoverSaveReq;
import com.maan.eway.master.req.PolicyTypeMasterSubCoverSingleSaveReq;
import com.maan.eway.master.req.PolicyTypeSubCoverMasterGetAllReq;
import com.maan.eway.master.res.PolicyTypeSubCoverMasterGetRes;
import com.maan.eway.res.SuccessRes2;

public interface PolicyTypeMasterSubCoverService {

	//List<Error> validatePolicyTypeSubCover(PolicyTypeMasterSubCoverSaveReq req);

//	SuccessRes2 insertPolicyTypeSubCover(PolicyTypeMasterSubCoverSaveReq req);

	List<PolicyTypeSubCoverMasterGetRes> getallPolicyTypesubcover(PolicyTypeSubCoverMasterGetAllReq req);

	PolicyTypeSubCoverMasterGetRes getPolicyTypesubcover(GetPolicyTypesubcoverReq req);

	List<String> validatePolicyTypeSubCover(PolicyTypeMasterSubCoverSingleSaveReq req);

	SuccessRes2 insertPolicyTypeSubCover(PolicyTypeMasterSubCoverSingleSaveReq req);

}
