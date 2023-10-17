package com.maan.eway.embedded;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class EmbeddedDashBoardReq {
	
	@JsonProperty("LoginId")
	private String loginId;
	
	@JsonProperty("CompanyId")
	private String companyId;
	
	@JsonProperty("ProductId")
	private String productId;
	

}
