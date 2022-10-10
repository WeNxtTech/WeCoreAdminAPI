package com.maan.eway.master.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class SubCoverDropDownReq {

	
	@JsonProperty("CoverId")
	private String coverId;
	
	
}
