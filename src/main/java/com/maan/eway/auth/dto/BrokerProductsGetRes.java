package com.maan.eway.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class BrokerProductsGetRes {

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
	
	@JsonProperty("Status")
	private String status ;
	
	@JsonProperty("Remarks")
	private String remarks;
	
}
