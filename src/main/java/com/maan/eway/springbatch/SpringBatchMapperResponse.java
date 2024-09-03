package com.maan.eway.springbatch;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class SpringBatchMapperResponse {

	private String productId;
	private String insuranceId;
	private String coverId;
	private String subCoverId;
	private String agencyCode;
	private String branchCode;
	private String createdBy;
	private String sectionId;
	private String status;
	private String csvFilePath;
	private String xlFilePath;
	private String factorTypeId;
	private String remarks;
	private String columns;
	private String effectiveDate;
	private String fileName;
	private String tranId;
	private String excelHeaderColumns;
	private String discreteColumn;
	private String totalRecordsCount;
	private String authorization;
	private String rangeColumns;
	private String minimumRateYn;

	
}

