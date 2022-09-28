package com.maan.eway.master.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class CountryChangeStatusReq {

	 @JsonProperty("CountryId")
	 private String countryId;
	 
	 @JsonProperty("Status")
	 private String status;
}
