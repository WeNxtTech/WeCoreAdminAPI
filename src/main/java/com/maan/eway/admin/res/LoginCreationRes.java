package com.maan.eway.admin.res;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class LoginCreationRes {


	@JsonProperty("Response")
	private String response;

}
