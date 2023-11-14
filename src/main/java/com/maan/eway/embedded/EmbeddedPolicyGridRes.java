package com.maan.eway.embedded;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmbeddedPolicyGridRes {
	
	@JsonProperty("")
	private String planName;
	
	@JsonProperty("")
	private String planOpted;
	
	@JsonProperty("")
	private String requestReferenceNumber;
	
	@JsonProperty("")
	private String companyId;
	
	@JsonProperty("")
	private String loginId;
	
	@JsonProperty("")
	private String policyNo;
	
	@JsonProperty("")
	private String mobileCode;
	
	@JsonProperty("")
	private String mobileNo;
	
	@JsonProperty("")
	private String sectionId;
	
	@JsonProperty("")
	private String clientTransactionNo;
	
	@JsonProperty("")
	private String customerName;
	
	@JsonProperty("")
	private String productId;
	
	@JsonProperty("")
	private String amountPaid;
	
	@JsonProperty("")
	private String premium;

	@JsonProperty("")
	private String commissionPercentage;

	@JsonProperty("")
	private String commissionAmount;

	
	@JsonProperty("")
	private String taxPercentage;

	
	@JsonProperty("")
	private String taxPremium;

	@JsonProperty("")
	private String overAllPremium;

	@JsonProperty("")
	private String totalPolicy;

	@JsonProperty("")
	private String overAllComiPremium;

	@JsonProperty("")
	private String overAllTaxPremium;

	@JsonProperty("")
	private String activePolicyCount;
	
	@JsonProperty("")
	private String expiryPolicyPremium;
	
	@JsonProperty("")
	private String expiryPolicyCount;
	
	@JsonProperty("")
	private String activePremium;

}
