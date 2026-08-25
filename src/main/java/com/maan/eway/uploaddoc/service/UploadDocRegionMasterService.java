package com.maan.eway.uploaddoc.service;

import java.util.List;

import com.maan.eway.error.Error;
import com.maan.eway.uploaddoc.dto.request.UploadDocRegionMasterGetReq;
import com.maan.eway.uploaddoc.dto.request.UploadDocRegionMasterSaveReq;
import com.maan.eway.uploaddoc.dto.request.UploadDocRegionMasterUpdateReq;
import com.maan.eway.uploaddoc.dto.response.UploadDocRegionMasterRes;

public interface UploadDocRegionMasterService {

	List<Error> validateSave(UploadDocRegionMasterSaveReq req);

	UploadDocRegionMasterRes save(UploadDocRegionMasterSaveReq req);

	List<Error> validateUpdate(UploadDocRegionMasterUpdateReq req);

	UploadDocRegionMasterRes update(UploadDocRegionMasterUpdateReq req);

	UploadDocRegionMasterRes getLatest(UploadDocRegionMasterGetReq req);

	List<UploadDocRegionMasterRes> getAll();

	boolean delete(UploadDocRegionMasterGetReq req);
}
