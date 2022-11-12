package com.maan.eway.admin.controller;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class BelogingBrokerDropDwonReq {

	@JsonProperty("SubUserType")
	private String subUserType ;
	
}
