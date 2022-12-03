package com.maan.eway.fileupload;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FileUploadInputRequest {
	
	@JsonProperty("ProductId")
	private String productId;
	@JsonProperty("InsuranceId")
	private String insuranceId;
	@JsonProperty("CoverId")
	private String coverId;
	@JsonProperty("SubCoverId")
	private String subCoverId;
	@JsonProperty("AgencyCode")
	private String agencyCode;
	@JsonProperty("BranchCode")
	private String branchCode;
	@JsonProperty("CreatedBy")
	private String createdBy ;
	@JsonProperty("SectionId")
	private String sectionId ;
	@JsonProperty("Status")
	private String status ;

}

