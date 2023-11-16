package com.maan.eway.embedded;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmbeddedPlanDashBoardRes {
	
	@JsonProperty("LoginId")
	private String loginId;
	
	@JsonProperty("CompanyId")
	private String companyId;
	
	@JsonProperty("ProductId")
	private String productId;
	
	@JsonProperty("PlanId")
	private String planId;
	
	@JsonProperty("PlanType")
	private String planType;
	
	@JsonProperty("Premium")
	private String premium;

	@JsonProperty("TotalPolicy")
	private String totalPolicy;
	
	@JsonProperty("PremiumType")
	private String premiumType;
	
	@JsonProperty("CommissionPremium")
	private String commissionPremium;
	
}
