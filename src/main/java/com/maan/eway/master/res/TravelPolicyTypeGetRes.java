package com.maan.eway.master.res;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class TravelPolicyTypeGetRes {
	
	private static final long serialVersionUID = 1L;

	@JsonProperty("PolicyTypeId")
	private String policyTypeId; //plan africa, asia...

	@JsonProperty("PlanTypeId")
	private String planTypeId;  //indivual, family, group

	@JsonProperty("CoverId")
	private String coverId; 

	@JsonProperty("SubCoverId")
	private String subCoverId;
	
	@JsonProperty("AmendId")
	private String amendId;

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

//	@JsonProperty("subCoverDesc")
//	private String subCoverDesc;
//
//	@JsonProperty("Currency")
//	private String currency;
//
//	@JsonProperty("SumInsured")
//	private String sumInsured;
//
//	@JsonProperty("ExcessAmt")
//	private String excessAmt;
//
//	@JsonFormat(pattern = "dd/MM/yyyy")
//	@JsonProperty("EntryDate")
//	private Date entryDate;
//
//	@JsonProperty("Status")
//	private String status;

	@JsonProperty("Remarks")
	private String remarks;

	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("EffectiveStartdate")
	private Date effectiveStartdate;

	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("EffectiveEnddate")
	private Date effectiveEnddate;

	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("UpdatedDate")
	private Date updatedDate;

	@JsonProperty("UpdatedBy")
	private String updatedBy;

	@JsonProperty("CoverStatus")
	private String coverStatus;

}
