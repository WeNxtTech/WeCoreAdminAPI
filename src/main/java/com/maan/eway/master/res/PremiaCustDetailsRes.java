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
public class PremiaCustDetailsRes {

	@JsonProperty("HEAD")
	private String head;
	
	@JsonProperty("CUSTOMER_ADDRESS")
	private String customerAddress;
	
	@JsonProperty("CC_TYPE")
	private String ccType;

	@JsonProperty("CUSTOMER_PHONE")
	private String customerPhone;
	
	@JsonProperty("TAX_APPLICABLE")
	private String taxApplicable;
	
	@JsonProperty("CREATED_ID")
	private String createdId;
	
	@JsonProperty("CUST_MAST_DEF_CODE")
	private String customerMastDefCode;

	@JsonProperty("COMMERCIAL_YN")
	private String commercialYN;
	
	@JsonProperty("CUST_CC_PREFIX")
	private String customerCcPrefix;

	@JsonProperty("CIVIL_ID")
	private String civilId;
	
	@JsonProperty("CUSTOMER_CITY")
	private String customerCity;
	
	@JsonProperty("CUST_MC_CODE")
	private String customerMcCode;
	
	@JsonProperty("VAT_APPLICABLE")
	private String vatApplicable;
	
	@JsonProperty("CUSTOMER_CODE")
	private String customerCode;
	
	@JsonProperty("CUSTOMER_NAME")
	private String customerName;
	
	@JsonProperty("CUSTOMER_EMAIL")
	private String customerEmail;
	
	@JsonProperty("CREDIT_CHECK")
	private String creditCheck;
	
	@JsonProperty("MARITAL STATUS")
	private String maritalStatus;
	
	@JsonProperty("VAT_EG_NO")
	private String vatRegNo;
	
	@JsonProperty("MAIL_ADDRESS")
	private String mailAddress;
	
	@JsonProperty("GENDER")
	private String gender;
	
	
	
}
