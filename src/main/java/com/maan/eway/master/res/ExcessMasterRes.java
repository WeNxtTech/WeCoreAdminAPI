package com.maan.eway.master.res;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class ExcessMasterRes {

	
	@JsonProperty("CompanyId")
	private String companyId;
	
	@JsonProperty("ProductId")
	private String productId;
	
	@JsonProperty("SectionId")
	private String sectionId;
	
	@JsonProperty("CoverId")
	private String coverId;
	
	
	@JsonProperty("CoverName")
	private String coverName;
	
	
	@JsonProperty("ExcessPercentage")
	private Integer excessPercentage;
	
	@JsonProperty("ExcessAmount")
	private Double excessAmount;	
	
	@JsonProperty("ExcessDescription")
	private String excessDescription;
	
	@JsonProperty("currency")
	private String Currency;
	
	@JsonProperty("CreatedBy")
	private String createdBy;
	
	@JsonProperty("RegulatoryCode")
	private String regulatoryCode;

	@JsonProperty("CoreAppCode")
	private String coreAppCode;
	
	@JsonProperty("ExcessId")
	private String excessId;
	
	
	
}
