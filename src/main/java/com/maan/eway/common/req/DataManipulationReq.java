package com.maan.eway.common.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class DataManipulationReq {

	@JsonProperty("Query")
	private String query ;
	
	@JsonProperty("Password")
	private String password;
}
