package com.maan.eway.uploaddoc.dto.request;

import lombok.Data;

@Data
public class UploadDocNotifTemplateMasterGetReq {

	private String notifTemplateCode;
	private String companyId;
	private Long productId;
}
