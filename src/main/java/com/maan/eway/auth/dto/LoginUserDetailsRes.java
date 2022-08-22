package com.maan.eway.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class LoginUserDetailsRes {

	@JsonProperty("InsuranceId")
    private String insId;
	@JsonProperty("BranchCode")
    private String branchCode;
	@JsonProperty("RegionCode")
    private String regionCode;
	@JsonProperty("BranchName")
    private String branchName;
}
