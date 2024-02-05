package com.maan.eway.chart;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ChartChildRequest {
	
	@JsonProperty("CompanyId")
	private String companyId;
	
	@JsonProperty("ProductId")
	private String productId;
	
	@JsonProperty("SectionId")
	private String sectionId;
	
	@JsonProperty("ChartId")
	private String chartId;
	
	@JsonProperty("CoverId")
	private String coverId;
	
	@JsonProperty("AccountType")
	private String accountType;
	
	@JsonProperty("Status")
	private String status;
	
	@JsonProperty("EffectiveStartDate")
	private String effectiveStartDate;
	
	@JsonProperty("UpdatedBy")
	private String updatedBy;
	
	@JsonProperty("EffectiveEndDate")
	private String effectiveEndDate;

}
