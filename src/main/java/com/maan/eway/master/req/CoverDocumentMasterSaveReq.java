package com.maan.eway.master.req;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class CoverDocumentMasterSaveReq {

	@JsonProperty("DocumentId")
	private List<String> documentId;
	
	@JsonProperty("InsuranceId")
	private String companyId;
	
	@JsonProperty("ProductId")
	private String productId;
	
	@JsonProperty("SectionId")
	private String sectionId;
			
	@JsonProperty("CreatedBy")
	private String createdBy;
	
	@JsonProperty("DocumentType")
	private String documentType;
	
//	@JsonProperty("CoverId")
//	private String coverId;
}
