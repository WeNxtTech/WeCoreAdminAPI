package com.maan.eway.master.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class CoverOfsGridSaveReq {

	@JsonProperty("SumInsuredStart")
	private String sumInsuredStart;
	
	@JsonProperty("SumInsuredEnd")
	private String sumInsuredEnd;
	
	@JsonProperty("CalcTypeId")
	private String calcTypeId;
	
	@JsonProperty("MinimumPremium")
	private String minimumPremium;
	
	@JsonProperty("Rate")
	private String rate;
}
