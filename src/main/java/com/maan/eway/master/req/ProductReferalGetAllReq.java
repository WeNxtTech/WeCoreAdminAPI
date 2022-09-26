package com.maan.eway.master.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ProductReferalGetAllReq {

	@JsonProperty("Limit")
    private String limit;
    
    @JsonProperty("InsuranceId")
    private String insuranceId;
    
    @JsonProperty("ProductId")
    private String productId;
    
    @JsonProperty("Offset")
    private String offset;
}
