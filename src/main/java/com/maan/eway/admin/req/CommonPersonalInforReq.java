package com.maan.eway.admin.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class CommonPersonalInforReq {

	// Personal Details
		@JsonProperty("Username")
	    private String     userName     ;
		@JsonProperty("Usermobile")
	    private String     userMobile   ;
		@JsonProperty("Usermail")
	    private String     userMail     ;
}
