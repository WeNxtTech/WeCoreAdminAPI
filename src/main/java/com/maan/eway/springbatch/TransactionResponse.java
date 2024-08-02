package com.maan.eway.springbatch;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TransactionResponse {
	
	@JsonProperty("TranId")
	private Object tranId;
	@JsonProperty("Status")
	private Object status;
	@JsonProperty("Description")
	private Object description;
	@JsonProperty("TotalRecord")
	private Object totalRecord;
	@JsonProperty("ValidRecord")
	private Object validRecord;
	@JsonProperty("ErrorRecord")
	private Object errorRecord;
	@JsonProperty("ExcelFilePath")
	private Object excelFilePath;
	@JsonProperty("ExcelFileName")
	private String excelFileName;

}
