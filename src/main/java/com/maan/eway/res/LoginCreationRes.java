package com.maan.eway.res;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class LoginCreationRes {


	@JsonProperty("Response")
	private String response;

}
