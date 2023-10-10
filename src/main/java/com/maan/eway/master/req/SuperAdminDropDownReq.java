package com.maan.eway.master.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class SuperAdminDropDownReq {

	@JsonProperty("LoginId")
	private String loginId;
}
