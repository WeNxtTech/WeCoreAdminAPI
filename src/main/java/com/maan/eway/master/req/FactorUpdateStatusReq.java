package com.maan.eway.master.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class FactorUpdateStatusReq {

	@JsonProperty("ProductId")
    private String productId    ;
	
	@JsonProperty("InsuranceId")
    private String     companyId    ;
	
	@JsonProperty("FactorTypeId")
    private String     factorTypeId;
	
	@JsonProperty("Status")
    private String     status ;
}
