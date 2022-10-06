package com.maan.eway.master.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class FactorTypeGetReq {


	@JsonProperty("ProductId")
    private String productId    ;
	
	@JsonProperty("InsuranceId")
    private String     companyId    ;
	
	@JsonProperty("FactorTypeId")
    private String     factorTypeId;
}
