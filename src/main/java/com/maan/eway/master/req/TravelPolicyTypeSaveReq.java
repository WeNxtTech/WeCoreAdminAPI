package com.maan.eway.master.req;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class TravelPolicyTypeSaveReq {

	private static final long serialVersionUID = 1L;

	@JsonProperty("PolicyTypeId")
	private String policyTypeId;

	@JsonProperty("PlanTypeId")
	private String planTypeId;

	@JsonProperty("CoverId")
	private String coverId;

	@JsonProperty("CompanyId")
	private String companyId;

	@JsonProperty("ProductId")
	private String productId;

	@JsonProperty("BranchCode")
	private String branchCode;

	@JsonProperty("PolicyTypeDesc")
	private String policyTypeDesc;

	@JsonProperty("PlanTypeDesc")
	private String planTypeDesc;

	@JsonProperty("CoverDesc")
	private String coverDesc;

	@JsonProperty("Remarks")
	private String remarks;

	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("EffectiveDateStart")
	private Date effectiveDateStart;

	@JsonProperty("CoverStatus")
	private String coverStatus;

	@JsonProperty("CreatedBy")
	private String createdBy;

}
