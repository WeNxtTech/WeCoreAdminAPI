package com.maan.eway.uploaddoc.service;

import java.util.List;

import com.maan.eway.error.Error;
import com.maan.eway.uploaddoc.dto.request.UploadDocCityMasterGetReq;
import com.maan.eway.uploaddoc.dto.request.UploadDocCityMasterSaveReq;
import com.maan.eway.uploaddoc.dto.request.UploadDocCityMasterUpdateReq;
import com.maan.eway.uploaddoc.dto.response.UploadDocCityMasterRes;

public interface UploadDocCityMasterService {

	List<Error> validateSave(UploadDocCityMasterSaveReq req);

	UploadDocCityMasterRes save(UploadDocCityMasterSaveReq req);

	List<Error> validateUpdate(UploadDocCityMasterUpdateReq req);

	UploadDocCityMasterRes update(UploadDocCityMasterUpdateReq req);

	UploadDocCityMasterRes getLatest(UploadDocCityMasterGetReq req);

	List<UploadDocCityMasterRes> getAll();

	boolean delete(UploadDocCityMasterGetReq req);
}
