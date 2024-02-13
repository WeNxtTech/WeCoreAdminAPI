package com.maan.eway.chart;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ChildCoverList {
	
	@JsonProperty("CoverId")
	private String coverId;
	
	@JsonProperty("Status")
	private String status;
	
	
}
