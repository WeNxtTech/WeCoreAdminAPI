package com.maan.eway.fileupload;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FileDownloadRequest {
	
	@JsonProperty("CompanyId")
	private String companyId;
	@JsonProperty("ProductId")
	private String productId;
	@JsonProperty("CoverId")
	private String coverId;
	@JsonProperty("SectionId")
	private String sectionId;
	@JsonProperty("AgencyCode")
	private String agencyCode;
	@JsonProperty("BranchCode")
	private String branchCode;
	@JsonProperty("SubCoverId")
	private String subCoverId;
	
}
