package com.maan.eway.master.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class CompanyRegionGetReq {

	@JsonProperty("InsuranceId")
    private String companyId;
	
	@JsonProperty("RegionCode")
    private String regionCode;
	
}
