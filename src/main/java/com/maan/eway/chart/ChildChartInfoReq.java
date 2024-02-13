package com.maan.eway.chart;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ChildChartInfoReq {
	
	@JsonProperty("CompanyId")
	private String companyId;
	
	@JsonProperty("ProductId")
	private String productId;
	
	@JsonProperty("SectionId")
	private String sectionId;
	
	@JsonProperty("ChartId")
	private String chartId;
	
	@JsonProperty("AccountType")
	private String accountType;
	
	@JsonProperty("EffectiveStartDate")
	private String effectiveStartDate;
	
	@JsonProperty("UpdatedBy")
	private String updatedBy;
	
	@JsonProperty("CoverList")
	private List<ChildCoverList> coverList;

}
