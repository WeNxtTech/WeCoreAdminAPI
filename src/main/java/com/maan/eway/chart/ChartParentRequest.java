package com.maan.eway.chart;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ChartParentRequest {
	
	@JsonProperty("CompanyId")
	private String companyId;
	
	@JsonProperty("ChartId")
	private String chartId;
	
	@JsonProperty("ChartAccountCode")
	private String chartAccountCode;
	
	@JsonProperty("ChartAccountDesc")
	private String chartAccountDesc;
	
	@JsonProperty("NarationDesc")
	private String narationDesc;
	
	@JsonProperty("AccountType")
	private String accountType;
	
	@JsonProperty("CharactersticType")
	private String charactersticType;
	
	@JsonProperty("DisplayOrder")
	private String displayOrder;
	
	@JsonProperty("Status")
	private String status;
	
	@JsonProperty("EffectiveStartDate")
	private String effectiveStartDate;
	
	@JsonProperty("EffectiveEndDate")
	private String effectiveEndDate;
	
	@JsonProperty("UpdatedBy")
	private String updatedBy;

}
