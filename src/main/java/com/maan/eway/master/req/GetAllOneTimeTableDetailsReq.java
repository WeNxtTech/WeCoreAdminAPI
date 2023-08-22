package com.maan.eway.master.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GetAllOneTimeTableDetailsReq {

	   @JsonProperty("CompanyId")
	    private String companyId;
}
