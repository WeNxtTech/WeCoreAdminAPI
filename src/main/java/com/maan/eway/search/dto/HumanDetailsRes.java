package com.maan.eway.search.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
@Data
public class HumanDetailsRes {

	@JsonProperty("RiskId")
	private Integer riskId;
	@JsonProperty("QuoteNo")
	private String quoteNo;
	@JsonProperty("SectionId")
	private String sectionId;
	@JsonProperty("SectionName")
	private String sectionName;
	@JsonProperty("CoverId")
	private String coverId;
	@JsonProperty("LocationId")
	private String locationId;
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

}
