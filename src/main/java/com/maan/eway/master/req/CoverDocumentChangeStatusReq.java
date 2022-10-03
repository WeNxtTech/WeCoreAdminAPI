package com.maan.eway.master.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class CoverDocumentChangeStatusReq {

	 @JsonProperty("DocumentId")
	 private String documentId;
	 
	 @JsonProperty("ProductId")
	 private String productId;
	 
	 @JsonProperty("SectionId")
	 private String sectionId;
	 
	 @JsonProperty("CoverId")
	 private String coverId;
	 
	 @JsonProperty("InsuranceId")
	 private String companyId;
	 
	 @JsonProperty("Status")
	 private String status;
}
