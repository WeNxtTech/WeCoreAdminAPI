package com.maan.eway.res;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class DropDownRes {

	@JsonProperty("Code")
	private String code;
	@JsonProperty("CodeDesc")
	private String codeDesc;
	
	@JsonProperty("IndustryType")
	private String industryType;
	

	@JsonProperty("Status")
	private String status;
	
	@JsonProperty("CodeDescLocal")
	private String codeDescLocal;
	
}
