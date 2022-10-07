package com.maan.eway.master.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class FactorTypeDropDownReq {

	@JsonProperty("ProductId")
	private String productId;
	
	@JsonProperty("InsuranceId")
	private String CompanyId;
	
	@JsonProperty("Limit")
	private String limit;
	
	@JsonProperty("Offset")
	private String offset;
}
