package com.maan.eway.uploaddoc.service;

import java.util.List;

import com.maan.eway.error.Error;
import com.maan.eway.uploaddoc.dto.request.UploadDocAgricultureMasterGetReq;
import com.maan.eway.uploaddoc.dto.request.UploadDocAgricultureMasterSaveReq;
import com.maan.eway.uploaddoc.dto.request.UploadDocAgricultureMasterUpdateReq;
import com.maan.eway.uploaddoc.dto.response.UploadDocAgricultureMasterRes;

public interface UploadDocAgricultureMasterService {

	List<Error> validateSave(UploadDocAgricultureMasterSaveReq req);

	UploadDocAgricultureMasterRes save(UploadDocAgricultureMasterSaveReq req);

	List<Error> validateUpdate(UploadDocAgricultureMasterUpdateReq req);

	UploadDocAgricultureMasterRes update(UploadDocAgricultureMasterUpdateReq req);

	UploadDocAgricultureMasterRes getLatest(UploadDocAgricultureMasterGetReq req);

	List<UploadDocAgricultureMasterRes> getAll(UploadDocAgricultureMasterGetReq req);

	boolean delete(UploadDocAgricultureMasterGetReq req);

	UploadDocAgricultureMasterRes saveOrUpdate(UploadDocAgricultureMasterSaveReq req);
}
