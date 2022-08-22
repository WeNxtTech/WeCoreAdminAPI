package com.maan.eway.auth.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.eway.error.Error;

import lombok.Data;

@Data
public class CommonLoginResponse {

	@JsonProperty("LoginResponse")
    private ClaimLoginResponse loginResponse;
	@JsonProperty("Errors")
    private List<Error> errors;
	
}
