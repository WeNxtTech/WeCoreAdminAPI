package com.maan.eway.auth.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ClaimLoginResponse {
	
	@JsonProperty("Token")
    private String token;
	@JsonProperty("LoginId")
    private String loginId;
	@JsonProperty("Email")
    private String email;
	@JsonProperty("MobileNo")
    private String mobileNo;
	@JsonProperty("UserName")
    private String userName;
	
	@JsonProperty("UserType")
    private String userType;
	
	@JsonProperty("AgencyCode")
    private String agencyCode;
	
	@JsonProperty("LoginUserDetails")
    private List<LoginUserDetailsRes> loginUserDetails;


}
