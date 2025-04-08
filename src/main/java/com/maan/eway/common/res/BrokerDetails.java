package com.maan.eway.common.res;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class BrokerDetails {
	
	@JsonProperty("CREATED_ID")
	private String createdId;


	@JsonProperty("BROKER_NAME")
	private String brokerName;
	
	@JsonProperty("BROKER_CITY")
	private String brokerCity;


	@JsonProperty("BROKER_CODE")
	private String brokerCode;
	
	@JsonProperty("MAIL_ADDRESS")
	private String mailAddress;
	
	@JsonProperty("CIVIL_ID")
	private String civilId;
	
	@JsonProperty("BROKER_EMAIL")
	private String broEmail;
	

	@JsonProperty("BROK_CC_PREFIX")
	private String broCCPrefix;
	
	@JsonProperty("BROKER_PHONE")
	private String brokPhone;
	
	@JsonProperty("VAT APPLICABLE")
	private String vatApplicable;
	
	@JsonProperty("COMMERCIAL_YN")
	private String commercialYN;
	
	@JsonProperty("BROKER_MOBILE")
	private String broMobile;
	
	@JsonProperty("BROKER_ADDRESS")
	private String customerAddress;
	
	@JsonProperty("BROK_MAST_DEF_CODE")
	private String brokMastDefCode;
	
	@JsonProperty("CREDIT_CHECK")
	private String creditCheck;
	
	@JsonProperty("TAX_APPLICABLE")
	private String taxApplicable;
	
	@JsonProperty("CC_TYPE")
	private String ccType;
	
	@JsonProperty("BROK_MC_CODE")
	private String brokMcCode;

}
