package com.maan.eway.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class LoginBranchDetailsRes {

	@JsonProperty("BranchCode")
    private String branchCode;
	@JsonProperty("BranchName")
    private String branchName;
	@JsonProperty("RegionCode")
    private String regionCode;
	@JsonProperty("InsuranceId")
    private String insuranceId;
	
}
