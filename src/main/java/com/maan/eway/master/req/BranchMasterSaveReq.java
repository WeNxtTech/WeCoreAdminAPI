package com.maan.eway.master.req;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class BranchMasterSaveReq implements Serializable {

    private static final long serialVersionUID = 1L;

	@JsonProperty("BranchCode")
	private String branchCode;
	
	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("EffectiveDate")
	private Date effectiveDate;

	@JsonProperty("BranchName")
	private String branchName;
	
	@JsonProperty("Status")
	private String status;
	
	@JsonProperty("RegionCode")
	private String regionCode;
	
	@JsonProperty("InsuranceId")
	private String companyId;
	
	@JsonProperty("CoreAppCode")
	private String coreAppCode;

	@JsonProperty("AmendId")
	private Integer amendId;

	@JsonProperty("Remarks")
	private String remarks;

}
