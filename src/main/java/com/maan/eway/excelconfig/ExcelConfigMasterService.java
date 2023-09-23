package com.maan.eway.excelconfig;

import java.util.List;

public interface ExcelConfigMasterService {
	
	public List<Errors> validate(UploadTypeSaveReq req);

	public SuccessResponse saveUploadType(UploadTypeSaveReq req);
	
}
