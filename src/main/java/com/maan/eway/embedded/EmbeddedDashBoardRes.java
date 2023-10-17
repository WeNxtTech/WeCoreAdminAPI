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
	
	@JsonProperty("TotalPolicy")
	private String totalPolicy;
	
	@JsonProperty("OverAllPremium")
	private String overAllPremium;
	
	@JsonProperty("OverAllTaxPremium")
	private String overAllTaxPremium;
	
	@JsonProperty("OverAllComiPremium")
	private String overAllComiPremium;

	@JsonProperty("ActivePremium")
	private String activePremium;
}
