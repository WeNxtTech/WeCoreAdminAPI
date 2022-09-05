package com.maan.eway.master.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GetQuoteDetailsReq {

	@JsonProperty("CutomerId")
	private String customerId ;
	
	@JsonProperty("ProductId")
	private String productId ;

}
