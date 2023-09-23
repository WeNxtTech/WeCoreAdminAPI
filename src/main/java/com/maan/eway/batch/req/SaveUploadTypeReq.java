package com.maan.eway.batch.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class SaveUploadTypeReq {
	
	
	@JsonProperty("CompanyId")
	public String companyId;
	
	@JsonProperty("ProductId")
	public String productId;
	
	@JsonProperty("RawTableName")
	public String rawTableName;
	
	@JsonProperty("RawTableId")
	public String rawTableId;
	
	@JsonProperty("SampleXlFilePath")
	public String sampleXlFilePath;
	
	@JsonProperty("Status")
	public String status;
	
	@JsonProperty("FileName")
	public String fileName;
	
	@JsonProperty("ProductDesc")
	public String productDesc;
	
	@JsonProperty("ApiMethod")
	public String apiMethod;
	
	
	

}
