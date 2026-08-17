package com.maan.eway.ttrncloseing.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
@Data
public class HomePositionReq {

	@JsonProperty("QuoteNo")
	private String quoteNo;
	
	@JsonProperty("TranId")
	private Integer tranId;
	
	@JsonProperty("Year")
	private String year;
	
	@JsonProperty("CompanyId")
	private String companyId;
	
	@JsonProperty("Date")
	private String date;
	
}
