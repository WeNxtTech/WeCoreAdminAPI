package com.maan.eway.master.res;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PremiaBrokerDetailsRes {

	@JsonProperty("HEAD")
	private String head;
	
	@JsonProperty("BROK_MC_CODE")
	private String brokerMcCode;
	
	@JsonProperty("CC_TYPE")
	private String ccType;

	@JsonProperty("TAX_APPLICABLE")
	private String taxApplicable;
	
	@JsonProperty("CREDIT_CHECK")
	private String creditCheck;
	
	@JsonProperty("BROK_MAST_DEF_CODE")
	private String brokerMastDefCode;
	
	@JsonProperty("BROKER_ADDRESS")
	private String brokerAddress;
	
	@JsonProperty("MARITAL STATUS")
	private String maritalStatus;
	
	@JsonProperty("COMMERCIAL_YN")
	private String commercialYN;
	
	@JsonProperty("BROK_CC_PREFIX")
	private String brokerCcPrefix;
	
	@JsonProperty("VAT_EG_NO")
	private String vatRegNo;
	
	@JsonProperty("CIVIL_ID")
	private String civilId;
	
	@JsonProperty("MAIL_ADDRESS")
	private String mailAddress;
	
	@JsonProperty("GENDER")
	private String gender;
	
	@JsonProperty("VAT_APPLICABLE")
	private String vatApplicable;
	
	@JsonProperty("BROKER_CODE")
	private String brokerCode;
	
	@JsonProperty("BROKER_NAME")
	private String brokerName;
	
	@JsonProperty("CREATED_ID")
	private String CreatedId;
	
	@JsonProperty("BROKER_PHONE")
	private String brokerPhone;
	
	@JsonProperty("BROKER_CITY")
	private String brokerCity;
	
	@JsonProperty("BROKER_FAX")
	private String brokerFax;
	
	@JsonProperty("BROKER_EMAIL")
	private String brokerEmail;
	
	@JsonProperty("BROKER_MOBILE")
	private String brokerMobile;
	
	
}
