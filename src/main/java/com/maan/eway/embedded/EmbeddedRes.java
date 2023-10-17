package com.maan.eway.embedded;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmbeddedRes {
	
	
	@JsonProperty("CompanyId")
	private String companyId;
	
	@JsonProperty("ProductId")
	private String productId;
	
	@JsonProperty("TransactionNo")
	private String transactionNo;
	
	@JsonProperty("NidaNo")
	private String nidaNo;
	
	@JsonProperty("LoginId")
	private String loginId;
	
	@JsonProperty("CustomerName")
	private String customerName;
	
	@JsonProperty("MobileNo")
	private String mobileNo;
	
	@JsonProperty("PolicyNo")
	private String policyNo;
	
	@JsonProperty("RequestReferenceNo")
	private String requestReferenceNo;
	
	@JsonProperty("PolicyStartDate")
	private String policyStartDate;
	
	@JsonProperty("PolicyEndDate")
	private String policyEndDate;
	
	@JsonProperty("Premium")
	private String premium;
	
	@JsonProperty("OverAllPremium")
	private String overAllPremium;
	
	@JsonProperty("TaxPremium")
	private String taxPremium;
	
	@JsonProperty("AmountPaid")
	private String amountPaid;
	
	@JsonProperty("PlanType")
	private String planType;
	
	@JsonProperty("FilePath")
	private String filePath;
		
	@JsonProperty("ResponsePeriod")
	private String responsePeriod;
	
	@JsonProperty("CommissionPercentage")
	private String commissionPercentage;
	
	@JsonProperty("CommissionAmt")
	private String commissionAmt;

	@JsonProperty("TaxPercentage")
	private String taxPercentage;
	
	@JsonProperty("MobileCode")
	private String mobileCode;
	
	
}
