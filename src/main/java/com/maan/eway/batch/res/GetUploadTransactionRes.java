package com.maan.eway.batch.res;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetUploadTransactionRes {
	
	@JsonProperty("CompanyId")
    private String companyId;
	
	@JsonProperty("ProductId")
    private String productId;
	
	@JsonProperty("RequestRefNo")
    private String requestRefNo;
	
	@JsonProperty("TotalRecords")
    private String totalRecords;
	
	@JsonProperty("ErrorRecords")
    private String errorRecords;
	
	@JsonProperty("ValidRecords")
    private String validRecords;
	
	@JsonProperty("MovedRecords")
    private String movedRecords;
	
	@JsonProperty("UploadDate")
    private String uploadDate;
	
	@JsonProperty("SectionId")
    private String sectionId;
	
}
