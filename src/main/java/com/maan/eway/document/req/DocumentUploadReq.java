package com.maan.eway.document.req;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class DocumentUploadReq {
	

	@JsonProperty("RequestRefNo")
	private String requestRefNo;
	
	@JsonProperty("InsuranceId")
	private String companyId;
	
	@JsonProperty("DocumentId")
	private String documentId;
	
	@JsonProperty("ProductId")
	private String productId;
	
	/*@JsonProperty("CoverId")
	private String coverId;*/
	
	@JsonProperty("SectionId")
	private String sectionId;
	
	@JsonProperty("DocumentReferenceNumber")
	private String documentRef;

	@JsonProperty("DocTypeDescription")
	private String docTypeDescription;

	@JsonProperty("FileName")
	private String fileName;

	@JsonProperty("OrginalFileName")
	private String orginalFileName;

	@JsonProperty("Createdby")
	private String createdby;

	@JsonProperty("DocApplicable")
	private String docApplicable;

	@JsonProperty("DocApplicableId")
	private String docApplicableId;

	@JsonProperty("RequestedBy")
	private String requesteBy;
	
	@JsonProperty("UplodedBy")
	private String uploadedBy;

}
