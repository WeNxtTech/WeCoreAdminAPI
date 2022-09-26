package com.maan.eway.master.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ProductReferalGetReq {

	@JsonProperty("ReferalId")
    private String referalId     ;
	
	@JsonProperty("ProductId")
    private String     productId     ;
	
	@JsonProperty("InsuranceId")
    private String insuranceId;
}
