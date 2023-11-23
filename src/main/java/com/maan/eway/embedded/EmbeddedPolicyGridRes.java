package com.maan.eway.embedded;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmbeddedPolicyGridRes {
	
	private String planName;
	
	private String planOpted;
	
	private String requestReferenceNumber;
	
	private String companyId;
	
	private String loginId;
	
	private String policyNo;
	
	private String mobileCode;
	
	private String mobileNo;
	
	private String sectionId;
	
	private String clientTransactionNo;
	
	private String customerName;
	
	private String productId;
	
	private String amountPaid;
	
	private String premium;

	private String commissionPercentage;

	private String commissionAmount;

	
	private String taxPercentage;

	
	private String taxPremium;

	private String overAllPremium;

	private String totalPolicy;

	private String overAllComiPremium;

	private String overAllTaxPremium;

	private String activePolicyCount;
	
	private String expiryPolicyPremium;
	
	private String expiryPolicyCount;
	
	private String activePremium;
	
	
	private String startDate;
	
	
	private String endDate;
	
	private long noOfDays;
	
	@JsonProperty("FilePath")
	private String filePath;
	
}
