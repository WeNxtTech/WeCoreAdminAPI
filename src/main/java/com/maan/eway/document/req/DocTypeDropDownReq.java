package com.maan.eway.document.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class DocTypeDropDownReq {
	
	@JsonProperty("ProductId")
	private String productId;
	
	/*@JsonProperty("CoverId")
	private String coverId;
	*/
	@JsonProperty("SectionId")
	private String sectionId;
	
	@JsonProperty("InsuranceId")
	private String companyId ;
	
	@JsonProperty("DocApplicableId")
	private String docApplicableId ;
	
	@JsonProperty("DocApplicable")
	private String docApplicable ;
}
