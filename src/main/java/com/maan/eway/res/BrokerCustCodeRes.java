package com.maan.eway.res;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class BrokerCustCodeRes {


	@JsonProperty("Code")
	private String Code;
	@JsonProperty("CodeDesc")
	private String codeDesc;

	@JsonProperty("CustomerCode")
	private String customerCode;

	@JsonProperty("Status")
	private String status;
}
