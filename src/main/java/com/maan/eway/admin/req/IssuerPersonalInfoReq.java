package com.maan.eway.admin.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class IssuerPersonalInfoReq {

	// Personal Details
	@JsonProperty("UserName")
    private String     userName     ;
	@JsonProperty("UserMobile")
    private String     userMobile   ;
	@JsonProperty("UserMail")
    private String     userMail     ;
}
