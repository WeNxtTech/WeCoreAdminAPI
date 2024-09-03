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
	@JsonProperty("ExcelHeaderColumns")
	private String excelHeaderColumns ;
	@JsonProperty("Columns")
	private String columns ;
	@JsonProperty("Remarks")
	private String remarks ;
	@JsonProperty("FactorTypeId")
	private String factorTypeId ;
	@JsonProperty("FileName")
	private String fileName ;
	@JsonProperty("EffectiveDate")
	private String effectiveDate ;
	@JsonProperty("TotalRecordsCount")
	private String totalRecordsCount ;
	@JsonProperty("RangeColumns")
	private String rangeColumns ;
	@JsonProperty("DiscreteColumn")
	private String discreteColumn ;
	@JsonProperty("TranId")
	private String tranId ;
	@JsonProperty("CsvFilePath")
	private String csvFilePath ;
	@JsonProperty("XlFilePath")
	private String xlFilePath ;
	@JsonProperty("Authorization")
	private String authorization ;
	
	@JsonProperty("ProgressDesc")
	private String progressDesc ;
	@JsonProperty("ProgressErrorDesc")
	private String progressErrorDesc ;
	@JsonProperty("ProgressStatus")
	private String progressStatus ;
	@JsonProperty("MinimumRateYn")
	private String minimumRateYn ;



}

