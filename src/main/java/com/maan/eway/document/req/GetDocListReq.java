package com.maan.eway.document.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GetDocListReq {
	
	@JsonProperty("RequestRefNo")
	private String requestRefNo;
	
    @JsonProperty("InsCompanyId")
    private String insCompanyId ;
	
	@JsonProperty("DocApplicable")
	private String docApplicable;
	
	@JsonProperty("DocApplicableId")
	private String docApplicableId;
}
