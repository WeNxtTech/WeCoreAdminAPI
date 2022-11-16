package com.maan.eway.document.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class DocumentDeleteReq {


	@JsonProperty("RequestRefNo")
	private String requestRefNo;

	@JsonProperty("DocTypeDescription")
	private String docTypeDescription;
	
	@JsonProperty("DocumentId")
	private String documentId;
	
	@JsonProperty("DocumentRef")
	private String documentRef;
	
	@JsonProperty("DocApplicable")
	private String docApplicable;

	@JsonProperty("DocApplicableId")
	private String docApplicableId;

	@JsonProperty("InsuranceId")
	private String CompanyId;

	@JsonProperty("ProductId")
	private String productId;

	@JsonProperty("SectionId")
	private String sectionId;

	@JsonProperty("CoverId")
	private String coverId;
	
	
}
