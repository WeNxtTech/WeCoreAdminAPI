package com.maan.eway.uploaddoc.dto.request;

import lombok.Data;

@Data
public class UploadDocDeductibleMasterGetReq {

	private Integer deductId;
	private String companyId;
	private Integer productId;
	private Integer sectionId;
	private String branchCode;
}
