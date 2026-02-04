package com.maan.eway.ttrncloseing.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
@Data
public class HomePositionReq {

	@JsonProperty("QuoteNo")
	private String quoteNo;
	
	@JsonProperty("Date")
	private String date;
	
	@JsonProperty("BranchCode")
	private String branchCode;

	@JsonProperty("ProductId")
    private String productCoreCode;
	
	@JsonProperty("CompanyId")
	private String companyId;
}
