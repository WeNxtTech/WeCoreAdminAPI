package com.maan.eway.excelconfig2;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class DropDownRes {

	@JsonProperty("Code")
	private String code;
	
	@JsonProperty("CodeDesc")
	private String codeDesc;
	
	@JsonProperty("Status")
	private String status;

}
