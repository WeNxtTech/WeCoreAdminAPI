package com.maan.eway.uploaddoc.dto.request;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

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
	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date effectiveDateStart;
	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date effectiveDateEnd;
	private String bankCode;
	private String coreAppCode;
}
