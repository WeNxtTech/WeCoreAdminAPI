package com.maan.eway.master.req;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ReferalMasterSaveReq implements Serializable {

    private static final long serialVersionUID = 1L;

	@JsonProperty("ReferalId")
	private String referalId;

	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("EffectiveDate")
	private Date effectiveDate;

	@JsonProperty("InsuranceId")
	private String companyId;

	@JsonProperty("ReferalName")
	private String referalName;

	@JsonProperty("ReferalDesc")
	private String referalDesc;

	@JsonProperty("CoreAppCode")
	private String coreAppCode;

	@JsonProperty("AmendId")
	private Integer amendId;

	@JsonProperty("Remarks")
	private String remarks;

	@JsonProperty("Status")
	private String status;

	@JsonProperty("BranchCode")
	private String branchCode;

}
