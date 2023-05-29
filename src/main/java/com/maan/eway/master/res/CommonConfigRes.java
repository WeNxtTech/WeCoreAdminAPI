package com.maan.eway.master.res;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class CommonConfigRes {
	
	@JsonProperty("Id")
	private String id;
	
	@JsonProperty("Name")
	private String name ;

}
