package com.maan.eway.search.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PolicyDetailsRes {
	
	@JsonProperty("quoteNo")
	private String quoteNo;
	@JsonProperty("ProductId")
	private String productId;
	@JsonProperty("ProductName")
	private String productName;
	@JsonProperty("CustomerId")
	private String customerId;
	@JsonProperty("CompanyId")
	private String companyId;
	@JsonProperty("NoOfVehicles")
	private Integer noOfVehicles;
	@JsonProperty("CustomerName")
	private String customerName;
	@JsonProperty("PolicyNo")
	private String policyNo;
	@JsonProperty("OriginalPolicyNo")
	private String originalPolicyNo;
	@JsonProperty("Status")
	private String status;
	@JsonProperty("PolicyStartDate")
	private Date inceptionDate;
	@JsonProperty("ExpiryDate")
	private Date expiryDate;
	@JsonProperty("EffectiveDate")
	private Date effectiveDate;
	@JsonProperty("OverallPremiumLc")
	private BigDecimal overallPremiumLc;
	@JsonProperty("OverallPremiumFc")
	private BigDecimal overallPremiumFc;
	@JsonProperty("BranchName")
	private String branchName;
	@JsonProperty("PaymentMode")
	private String paymentMode;
	@JsonProperty("PaymentStatus")
	private String paymentStatus;
	@JsonProperty("BdmName")
	private String bdmName;
	@JsonProperty("Bdmcode")
	private String bdmcode;

}
