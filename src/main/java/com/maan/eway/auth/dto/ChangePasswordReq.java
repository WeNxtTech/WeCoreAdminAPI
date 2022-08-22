package com.maan.eway.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordReq {

	@JsonProperty("OldPassword")
	private String oldpassword;
	
	@JsonProperty("UserId")
	private String userId;

	@JsonProperty("NewPassword")
	private String newPassword;
	
	@JsonProperty("UserType")
	private String userType;
	
	@JsonProperty("CompanyId")
	private String companyId;
	
}
