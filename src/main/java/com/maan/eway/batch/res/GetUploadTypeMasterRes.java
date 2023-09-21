package com.maan.eway.batch.res;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetUploadTypeMasterRes {
	
	@JsonProperty("CompanyId")
	private String companyId;
	
	@JsonProperty("ProductId")
	private String productId;
	
	@JsonProperty("TypeId")
	private String typeId;
	
	@JsonProperty("Status")
	private String status;
	
	@JsonProperty("EntryDate")
	private String entryDate;
	
	@JsonProperty("ProductDesc")
	private String productDesc;
	
	@JsonProperty("RawTableName")
	private String rawTableName;
	
	@JsonProperty("RawTableId")
	private String rawTableId;
	
	@JsonProperty("FileName")
	private String fileName;

	@JsonProperty("SampleExcelFilePath")
	private String sampleExcelFilePath;

}
