package com.maan.eway.uploaddoc.dto.response;

import java.util.Date;

import lombok.Data;

@Data
public class UploadDocAcExecutiveMasterRes {

	private Integer acExecutiveId;
	private String acExecutiveName;
	private String oaCode;
	private String branchCode;
	private String companyId;
	private Double commissionPercent;
	private String status;
	private Date effectiveDateStart;
	private Date effectiveDateEnd;
	private Integer amendId;
	private Date entryDate;
	private String bankCode;
	private String coreAppCode;
}
