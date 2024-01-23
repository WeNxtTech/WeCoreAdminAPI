package com.maan.eway.master.req;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class TaxExemtedGetRes {

	@JsonProperty("InsuranceId")
	private String companyId;
	

	@JsonProperty("BranchCode")
	private String branchCode;
	
	@JsonProperty("SectionId")
	private String sectionId;
	
	@JsonProperty("CoverId")
	private String coverId;
	
	@JsonProperty("TaxFor")
	private String taxFor;
	
	@JsonProperty("CountryId")
	private String     countryId;
	
//
//
//	@JsonFormat(pattern = "dd/MM/yyyy")
//	@JsonProperty("EffectiveDateStart")
//	private Date effectiveDateStart;
//	
	 @JsonProperty("ProductId")
	 private String productId;
	 

	@JsonProperty("ExemptedTaxIds")
	private List<Integer> exemptedTaxIds;
}
