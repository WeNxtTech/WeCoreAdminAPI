package com.maan.eway.master.req;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class TaxExemptionSaveReq {

	@JsonProperty("InsuranceId")
	private String companyId;
	
	@JsonProperty("CountryId")
	private String countryId;

	@JsonProperty("BranchCode")
	private String branchCode;
	
	@JsonProperty("SectionId")
	private String sectionId;
	
	@JsonProperty("CoverId")
	private String coverId;
	
	@JsonProperty("TaxFor")
	private String taxFor;
//
//
//	@JsonFormat(pattern = "dd/MM/yyyy")
//	@JsonProperty("EffectiveDateStart")
//	private Date effectiveDateStart;
//	
	 @JsonProperty("ProductId")
	 private String productId;
	 
	@JsonProperty("CreatedBy")
	private String createdBy;
	 
	 @JsonProperty("ExemptedTaxIds")
	private List<Integer> exemptedTaxIds;
	 
}
