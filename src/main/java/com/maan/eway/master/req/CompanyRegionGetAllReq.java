package com.maan.eway.master.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class CompanyRegionGetAllReq {

	    @JsonProperty("InsuranceId")
	    private String companyId;
	    
	    @JsonProperty("Limit")
	    private String limit;
	    
	    @JsonProperty("Offset")
	    private String offset;
}
