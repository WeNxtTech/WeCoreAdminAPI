package com.maan.eway.excelconfig;

import java.util.List;

import com.maan.eway.res.SuccessRes;

public interface ExcelConfigMasterService {
	
	public List<String> validate(UploadTypeSaveReq req);

	public SuccessRes saveUploadType(UploadTypeSaveReq req);
	
}
