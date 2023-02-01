package com.maan.eway.springbatch;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EwayBatchUploadResponse {
	
	@JsonProperty("FileName")
	private String filename;
	@JsonProperty("FileDownloadUri")
	private String filedownloaduri;
	@JsonProperty("FilePath")
	private String filepath;
	@JsonProperty("FileType")
	private String filetype;
	@JsonProperty("CsvFilePath")
	private String csvfilepath;
	@JsonProperty("CsvFileName")
	private String csvfilename;
	@JsonProperty("CsvFileDownloadUri")
	private String csvfiledownloaduri;
	@JsonProperty("ExcelFileName")
	private String excelfilename;
	@JsonProperty("ExcelFilePath")
	private String excelfilepath;
	@JsonProperty("ExcelFileType")
	private String excelfiletype;
	@JsonProperty("ExcelFileSize")
	private String excelfilesize;
	@JsonProperty("UploadedTranId")
	private String uploadedtranid;
	@JsonProperty("ProductId")
	private String productid;
	@JsonProperty("FileUploadType")
	private String FileUploadType;
	@JsonProperty("FileUploadTypeId")
	private String FileUploadTypeid;
	@JsonProperty("ExcelRawTableName")
	private String excelrawtablename;
	@JsonProperty("ExcelRawTableFields")
	private String excelrawtablefields;
	@JsonProperty("RawTableSno")
	private String rawtablesno;
	@JsonProperty("ProgressDesc")
	private String progressdesc;
	@JsonProperty("ProgressStatus")
	private String progressstatus;
	@JsonProperty("TotalRecords")
	private String totalrecords;
	@JsonProperty("ValidRecords")
	private String validrecords;
	@JsonProperty("ErrorRecords")
	private String errorrecords;
	@JsonProperty("duplicaterecords")
	private String duplicaterecords;
	@JsonProperty("DeletedRecords")
	private String deletedrecords;
	@JsonProperty("InvalidRecords")
	private String invalidrecords;
	@JsonProperty("ProgressErrorDesc")
	private String progresserrordesc;
	@JsonProperty("ExcelMandatoryList")
	private String excelmandatorylist;
	@JsonProperty("ExcelDataTypeList")
	private String exceldatatypelist;
	@JsonProperty("ExcelDateformatList")
	private String exceldateformatlist;
	@JsonProperty("ExcelHeaderList")
	private String excelheaderlist;
	@JsonProperty("RequestRefrenceNo")
	private String requestreferenceno;
	@JsonProperty("DuplicateCheckColumns")
	private String duplicatecheckcolumns;
	@JsonProperty("DuplicateCheckExcelColumns")
	private String duplicatecheckexcelcolumns;
	@JsonProperty("PolicyNo")
	private String policyNo;
	@JsonProperty("EndorsementNo")
	private String endorsementNo;
	@JsonProperty("ErrorDesc")
	private String errorDesc;
	@JsonProperty("Uploadedby")
	private String uploadedby;
	@JsonProperty("BranchCode")
	private String branchCode;
	@JsonProperty("LoadingPercentage")
	private String loadingPercentage;

}
