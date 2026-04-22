package com.maan.eway.search.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BuildingRiskRes {
	
	@JsonProperty("RiskId")
	private Integer riskId;
	@JsonProperty("QuoteNo")
	private String quoteNo;
	@JsonProperty("CoverId")
	private String coverId;
	@JsonProperty("SectionId")
	private String sectionId;
	@JsonProperty("SectionId")
	private String sectionname;
	@JsonProperty("LocationId")
	private Integer locationId;
	@JsonProperty("LocationName")
	private String locationName;
	@JsonProperty("BranchCode")
	private String branchCode;
	@JsonProperty("IndustryId")
	private Integer industryId;
	@JsonProperty("BrokerTiraCode")
	private String brokerTiraCode;
	@JsonProperty("SalePointCode")
	private String salePointCode;
	@JsonProperty("Productid")
	private String productid;
	@JsonProperty("ProductName")
	private String productName;
	
}

