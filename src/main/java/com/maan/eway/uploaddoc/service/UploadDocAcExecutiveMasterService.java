package com.maan.eway.uploaddoc.service;

import java.util.List;

import com.maan.eway.error.Error;
import com.maan.eway.uploaddoc.dto.request.UploadDocAcExecutiveMasterGetReq;
import com.maan.eway.uploaddoc.dto.request.UploadDocAcExecutiveMasterSaveReq;
import com.maan.eway.uploaddoc.dto.request.UploadDocAcExecutiveMasterUpdateReq;
import com.maan.eway.uploaddoc.dto.response.AcExtDropDownRes;
import com.maan.eway.uploaddoc.dto.response.UploadDocAcExecutiveMasterRes;

public interface UploadDocAcExecutiveMasterService {

	List<Error> validateSave(UploadDocAcExecutiveMasterSaveReq req);

	UploadDocAcExecutiveMasterRes save(UploadDocAcExecutiveMasterSaveReq req);

	List<Error> validateUpdate(UploadDocAcExecutiveMasterUpdateReq req);

	UploadDocAcExecutiveMasterRes update(UploadDocAcExecutiveMasterUpdateReq req);

	UploadDocAcExecutiveMasterRes getLatest(UploadDocAcExecutiveMasterGetReq req);

	List<UploadDocAcExecutiveMasterRes> getAll(UploadDocAcExecutiveMasterGetReq req);

	boolean delete(UploadDocAcExecutiveMasterGetReq req);

	UploadDocAcExecutiveMasterRes saveOrUpdate(UploadDocAcExecutiveMasterSaveReq req);

	List<AcExtDropDownRes> dropDownList(UploadDocAcExecutiveMasterGetReq req);
}
