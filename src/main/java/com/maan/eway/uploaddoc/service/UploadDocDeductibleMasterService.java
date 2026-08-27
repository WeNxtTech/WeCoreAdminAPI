package com.maan.eway.uploaddoc.service;

import java.util.List;

import com.maan.eway.error.Error;
import com.maan.eway.uploaddoc.dto.request.UploadDocDeductibleMasterGetReq;
import com.maan.eway.uploaddoc.dto.request.UploadDocDeductibleMasterSaveReq;
import com.maan.eway.uploaddoc.dto.request.UploadDocDeductibleMasterUpdateReq;
import com.maan.eway.uploaddoc.dto.response.UploadDocDeductibleMasterRes;

public interface UploadDocDeductibleMasterService {

	List<Error> validateSave(UploadDocDeductibleMasterSaveReq req);

	UploadDocDeductibleMasterRes save(UploadDocDeductibleMasterSaveReq req);

	List<Error> validateUpdate(UploadDocDeductibleMasterUpdateReq req);

	UploadDocDeductibleMasterRes update(UploadDocDeductibleMasterUpdateReq req);

	UploadDocDeductibleMasterRes getLatest(UploadDocDeductibleMasterGetReq req);

	List<UploadDocDeductibleMasterRes> getAll(UploadDocDeductibleMasterGetReq req);

	boolean delete(UploadDocDeductibleMasterGetReq req);
}
