package com.maan.eway.common.res;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GenerateAuthToken {
	
	@JsonProperty("username")
	private String username;
	
	@JsonProperty("password")
	private String password;
	

}
