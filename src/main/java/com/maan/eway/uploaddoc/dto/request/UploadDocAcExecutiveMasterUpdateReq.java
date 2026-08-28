package com.maan.eway.uploaddoc.dto.request;

import java.util.Date;

import lombok.Data;

@Data
public class UploadDocAcExecutiveMasterUpdateReq {

	// business key
	private Integer AcExecutiveId;
	private String BranchCode;
	private String CompanyId;
	private String BankCode;

	// amendable fields
	private String AcExecutiveName;
	private String OaCode;
	private Double CommissionPercent;
	private String Status;
	private Date EffectiveDateStart;
	private Date EffectiveDateEnd;
}
