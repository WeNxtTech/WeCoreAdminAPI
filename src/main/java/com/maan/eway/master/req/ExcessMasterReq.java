package com.maan.eway.master.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class ExcessMasterReq {

	
	@JsonProperty("ExcessId")
	private Integer ExcessId;
	
	@JsonProperty("InsuranceId")
	private String companyId;
	
	@JsonProperty("ProductId")
	private String productId;
	
	@JsonProperty("SectionId")
	private String sectionId;
	
	@JsonProperty("CoverId")
	private String coverId;
	
	@JsonProperty("ExcessPercentage")
	private Integer excessPercentage;
	
	@JsonProperty("ExcessAmount")
	private Double excessAmount;	
	
	@JsonProperty("ExcessDescription")
	private String excessDescription;
	
	@JsonProperty("Currency")
	private String currency;
	
	@JsonProperty("CreatedBy")
	private String createdBy;
	
	@JsonProperty("RegulatoryCode")
	private String regulatoryCode;

	@JsonProperty("CoreAppCode")
	private String coreAppCode;
	
	@JsonProperty( "BranchCode")
	private String branchCode;
	
	
	@JsonProperty( "Status")
	private String status;
	
}
