package com.maan.eway.uploaddoc.service;

import java.util.List;

import com.maan.eway.error.Error;
import com.maan.eway.uploaddoc.dto.request.UploadDocNotifTemplateMasterGetReq;
import com.maan.eway.uploaddoc.dto.request.UploadDocNotifTemplateMasterSaveReq;
import com.maan.eway.uploaddoc.dto.request.UploadDocNotifTemplateMasterUpdateReq;
import com.maan.eway.uploaddoc.dto.response.UploadDocNotifTemplateMasterRes;

/**
 * Named with an "UploadDoc" prefix to avoid a Spring bean-name collision with
 * the pre-existing {@code com.maan.eway.master.service.NotifTemplateMasterService}.
 */
public interface UploadDocNotifTemplateMasterService {

	List<Error> validateSave(UploadDocNotifTemplateMasterSaveReq req);

	UploadDocNotifTemplateMasterRes save(UploadDocNotifTemplateMasterSaveReq req);

	List<Error> validateUpdate(UploadDocNotifTemplateMasterUpdateReq req);

	UploadDocNotifTemplateMasterRes update(UploadDocNotifTemplateMasterUpdateReq req);

	UploadDocNotifTemplateMasterRes getLatest(UploadDocNotifTemplateMasterGetReq req);

	List<UploadDocNotifTemplateMasterRes> getAll();

	boolean delete(UploadDocNotifTemplateMasterGetReq req);
}
