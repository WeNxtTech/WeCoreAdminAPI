package com.maan.eway.master.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class RegionChangeStatusReq {

	 @JsonProperty("CountryId")
	 private String countryId;
	 
	 @JsonProperty("RegionCode")
	 private String regionCode;
	 
	 @JsonProperty("Status")
	 private String status;
}
