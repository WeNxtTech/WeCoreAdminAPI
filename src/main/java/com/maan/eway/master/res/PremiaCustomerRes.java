package com.maan.eway.master.res;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class PremiaCustomerRes {

	@JsonProperty("CustomerCode")
	private String customerCode;
	
	@JsonProperty("CustomerName")
	private String customerName;
	
	@JsonProperty("DivisionCodeFrom")
	private String divisionCodeFrom;
	
	@JsonProperty("DivisionCodeTo")
	private String divisionCodeTo;
}
