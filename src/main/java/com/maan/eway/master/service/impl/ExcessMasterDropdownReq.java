package com.maan.eway.master.service.impl;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;


@Data	
public class ExcessMasterDropdownReq {

	@JsonProperty("InsuranceId")
	private String companyId ;
	
	@JsonProperty("BranchCode")
	private String branchCode;
	
	@JsonProperty("ProductId")
	private String productId;
	
	@JsonProperty("SectionId")
	private String sectionId;
	
	@JsonProperty("CoverId")
	private String coverId;
	
	@JsonProperty("ExcessId")
	private String ExcessId;
	
	
	@JsonProperty("ExcessPercentage")
	private String excessPercentage;
	
	@JsonProperty("ExcessAmount")
	private String excessAmount;
	
	@JsonProperty("Currency")
	private String currency;
}
