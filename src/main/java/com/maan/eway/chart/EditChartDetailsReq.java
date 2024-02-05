package com.maan.eway.chart;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EditChartDetailsReq {
	
	@JsonProperty("CompanyId")
	private String companyId;
	
	@JsonProperty("ChartId")
	private String charId;
	
	@JsonProperty("AmendId")
	private String amendId;

}
