package com.maan.eway.master.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ProductReferalsGetReq {

	
	@JsonProperty("ProductId")
    private String     productId     ;
	
	@JsonProperty("InsuranceId")
    private String companyId;
}
