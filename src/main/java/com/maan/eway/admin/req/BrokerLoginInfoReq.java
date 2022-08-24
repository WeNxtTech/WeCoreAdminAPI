package com.maan.eway.admin.req;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class BrokerLoginInfoReq {

	// Login Details
		@JsonProperty("LoginId")
	    private String     loginId      ;
		@JsonProperty("UserType")
	    private String     userType     ;
		@JsonProperty("SubUserType")
	    private String     subUserType  ;
		@JsonProperty("OaCode")
	    private String     oaCode       ;
		
		@JsonProperty("Attachedbranches")
	    private List<String>     attachedBranches ;
		
		@JsonProperty("Password")
	    private String     password     ;
		@JsonProperty("Createdby")
	    private String     createdBy    ;
		@JsonProperty("Status")
	    private String     status       ;
}
