package com.maan.eway.uploaddoc.dto.request;

import lombok.Data;

@Data
public class UploadDocAcExecutiveMasterGetReq {

	private Integer AcExecutiveId;
	private String branchCode;
	private String CompanyId;
	private String BankCode;
}
