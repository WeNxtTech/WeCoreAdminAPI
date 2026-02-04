package com.maan.eway.ttrncloseing.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
@Data
public class HomePositionReq {

	@JsonProperty("QuoteNo")
	private String quoteNo;
	
	@JsonProperty("Status")
	private String status;
	
	@JsonProperty("BranchCode")
	private String branchCode;

	@JsonProperty("ProductCode")
    private String productCoreCode;
	
	@JsonProperty("CompanyId")
	private String companyId;
}
