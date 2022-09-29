package com.maan.eway.master.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class CityChangeStatusReq {

	 @JsonProperty("CityId")
	 private String cityId;
	 
	 @JsonProperty("Status")
	 private String status;
}
