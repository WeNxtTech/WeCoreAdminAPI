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
public class EmbeddedDashBoardRes {
	
	@JsonProperty("LoginId")
	private String loginId;
	
	@JsonProperty("CompanyId")
	private String companyId;
	
	@JsonProperty("ProductId")
	private String productId;
	
	@JsonProperty("OverAllPremium")
	private String overAllPremium;

	@JsonProperty("ActivePremium")
	private String activePremium;
	
	@JsonProperty("ExpiryPolicyPremium")
	private String expiryPolicyPremium;

	@JsonProperty("TotalPolicy")
	private String totalPolicy;
	
	@JsonProperty("ActivePolicyCount")
	private String activePolicyCount;
	
	@JsonProperty("ExpiryPolicyCount")
	private String expiryPolicyCount;
	
	
	
}
