package com.maan.eway.uploaddoc.dto.request;

import java.util.Date;

import lombok.Data;

@Data
public class UploadDocAcExecutiveMasterSaveReq {

	private Integer acExecutiveId;
	private String acExecutiveName;
	private String oaCode;
	private String branchCode;
	private String companyId;
	private Double commissionPercent;
	private String status;
	private Date effectiveDateStart;
	private Date effectiveDateEnd;
	private String bankCode;
}
