package com.maan.eway.master.service.impl;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class AcExecutiveDropDownReq {

	@JsonProperty("OaCode")
	private String oaCode ;
}
