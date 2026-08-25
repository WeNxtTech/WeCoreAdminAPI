package com.maan.eway.uploaddoc.dto.request;

import lombok.Data;

@Data
public class UploadDocAcExecutiveMasterGetReq {

	private Integer acExecutiveId;
	private String branchCode;
	private String companyId;
	private String bankCode;
}
