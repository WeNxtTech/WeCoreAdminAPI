package com.maan.eway.uploaddoc.service;

import java.util.List;

import com.maan.eway.error.Error;
import com.maan.eway.req.StateMasterRequest;
import com.maan.eway.uploaddoc.dto.request.UploadDocStateMasterGetReq;
import com.maan.eway.uploaddoc.dto.request.UploadDocStateMasterSaveReq;
import com.maan.eway.uploaddoc.dto.request.UploadDocStateMasterUpdateReq;
import com.maan.eway.uploaddoc.dto.response.UploadDocStateMasterRes;

public interface UploadDocStateMasterService {

	List<Error> validateSave(UploadDocStateMasterSaveReq req);

	UploadDocStateMasterRes save(UploadDocStateMasterSaveReq req);

	List<Error> validateUpdate(UploadDocStateMasterUpdateReq req);

	UploadDocStateMasterRes update(UploadDocStateMasterUpdateReq req);

	UploadDocStateMasterRes getLatest(UploadDocStateMasterGetReq req);

	List<UploadDocStateMasterRes> getAll(StateMasterRequest req);

	boolean delete(UploadDocStateMasterGetReq req);

	UploadDocStateMasterRes saveOrUpdate(UploadDocStateMasterSaveReq req);
}
