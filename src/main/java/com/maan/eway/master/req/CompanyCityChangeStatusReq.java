package com.maan.eway.master.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class CompanyCityChangeStatusReq {

	 @JsonProperty("CityId")
	 private String cityId;
	 
	 @JsonProperty("Status")
	 private String status;
	 
	 @JsonProperty("InsuranceId")
	 private String companyId;
	
}
