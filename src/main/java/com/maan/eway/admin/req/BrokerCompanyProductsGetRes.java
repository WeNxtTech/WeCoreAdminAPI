package com.maan.eway.admin.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class BrokerCompanyProductsGetRes {

	
	@JsonProperty("ProductId")
	private String productId ;
	
	@JsonProperty("ProductName")
	private String productName ;
	
	@JsonProperty("OldProductName")
	private String oldProductName ;
	
	@JsonProperty("StartLimit")
	private String startLimit ;
	
	@JsonProperty("EndLimit")
	private String endLimit ;
	
	
}
