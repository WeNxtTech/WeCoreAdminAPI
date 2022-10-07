package com.maan.eway.master.res;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class FactorTypeDropDownRes {

	@JsonProperty("FactorTypeId")
	private String factorTypeId;
	
	@JsonProperty("RatingFieldId")
	private String ratingFieldId;
}
