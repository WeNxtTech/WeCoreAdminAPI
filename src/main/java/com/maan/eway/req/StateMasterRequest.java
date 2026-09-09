package com.maan.eway.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StateMasterRequest {
	
	@JsonProperty("CountryId")
	private String countryId;
	
	@JsonProperty("RegionId")
	private String regionCode;

}
