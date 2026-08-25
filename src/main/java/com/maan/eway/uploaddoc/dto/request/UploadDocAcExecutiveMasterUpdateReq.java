package com.maan.eway.uploaddoc.dto.request;

import java.util.Date;

import lombok.Data;

@Data
public class UploadDocAcExecutiveMasterUpdateReq {

	// business key
	private Integer acExecutiveId;
	private String branchCode;
	private String companyId;
	private String bankCode;

	// amendable fields
	private String acExecutiveName;
	private String oaCode;
	private Double commissionPercent;
	private String status;
	private Date effectiveDateStart;
	private Date effectiveDateEnd;
}
