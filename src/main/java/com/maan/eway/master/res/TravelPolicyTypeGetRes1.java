package com.maan.eway.master.res;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class TravelPolicyTypeGetRes1 {
	
	@JsonProperty("TotalCount")
	private Integer totalCount;
	
	@JsonProperty("TravelPolicyType")
	private List<TravelPolicyTypeGetRes> travelPolicyType;
	
}
