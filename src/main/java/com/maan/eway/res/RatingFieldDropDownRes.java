package com.maan.eway.res;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class RatingFieldDropDownRes {


	@JsonProperty("Code")
	private String code;
	@JsonProperty("CodeDesc")
	private String codeDesc;

	@JsonProperty("Status")
	private String status;
	
	@JsonProperty("ApiUrl")
	private String apiUrl;
	
	@JsonProperty("MasterYn")
	private String masterYn;
}
