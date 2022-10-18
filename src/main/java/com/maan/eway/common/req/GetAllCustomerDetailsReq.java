package com.maan.eway.common.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GetAllCustomerDetailsReq {


    @JsonProperty("Limit")
    private String   limit;
	
	@JsonProperty("Offset")
    private String    offset   ;
}
