package com.maan.eway.admin.res;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class LoginBrokerDetailsGridRes {

	
	@JsonProperty("LoginId")
	private String loginId ;
	
	@JsonProperty("OaCode")
	private String oaCode ;
	
	@JsonProperty("AgencyCode")
	private String agencyCode ;
	
	@JsonProperty("EntryDate")
	private String entryDate ;
	
	@JsonProperty("CreatedBy")
	private String createdBy ;
	
	@JsonProperty("UpdatedDate")
	private String updatedDate ;
	
	@JsonProperty("UpdatedBy")
	private String updatedBy ;
	
	@JsonProperty("UserName")
	private String userName;
	
	@JsonProperty("UserMobile")
	private String userMobile ;
	
	@JsonProperty("UserMail")
	private String userMail ;
	
	@JsonProperty("BranchCodes")
	private String branchCodes ;
	
	@JsonProperty("BankCode")
	private String bankCode;
	
	@JsonProperty("Status")
	private String status;
	
	@JsonProperty("UserType")
	private String userType;
	
	@JsonProperty("SubUserType")
	private String subUserType;
}
