package com.maan.eway.search.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
@Data
public class SearchTiraReq {
	
	@JsonProperty("StickeNo")
	private String stickerno;
	
	@JsonProperty("ChassisNo")
	private String  chassesNo;
	
	@JsonProperty("MobileNo")
	private String  mobileNo;
	
	@JsonProperty("MobileCode")
	private String  mobileCode;
	
	@JsonProperty("RegNo")
	private String  regNo;
	
	@JsonProperty("EngineNo")
	private String  engineNo;
	
	@JsonProperty("CoverNoteNo")
	private String  coverNoteNo;
	
	@JsonProperty("PolicyOrQuote")
	private String  PolicyOrQuote;
	
	@JsonProperty("CompanyId")
	private String  companyId;


}
