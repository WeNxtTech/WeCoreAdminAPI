package com.maan.eway.master.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GetOneTimeTableDetailsReq {

	   @JsonProperty("CompanyId")
	    private String companyId;
	   @JsonProperty("ParentId")
	    private String parentId;
}
