package com.maan.eway.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class LoginRequest {

	@JsonProperty("UserId")
	private String userId;
	
	@JsonProperty("Password")
	private String password;
	
	@JsonProperty("UserType")
	private String userType;
	
	@JsonProperty("ReLoginKey")
	private String reLoginKey;
	
/*	@JsonProperty("CompanyId")
	private String companyId; */
	


}
