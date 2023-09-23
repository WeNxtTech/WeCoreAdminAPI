package com.maan.eway.excelconfig;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UploadTypeResponse {

	@JsonProperty("CompanyId")
	private String companyId;
	
	@JsonProperty("ProductId")
	private String productId;
	
	@JsonProperty("TypeId")
	private String typeId;
	
	@JsonProperty("SectionId")
	private String sectionId;
	
	@JsonProperty("TypeName")
	private String typeName;
	
	@JsonProperty("Status")
	private String status;
	
	@JsonProperty("RawTableName")
	private String rawTableName;
	
	@JsonProperty("ApiName")
	private String apiName;
	
	@JsonProperty("ProductDescription")
	private String productDesc;
	
	@JsonProperty("FilePath")
	private String filePath;
	
	@JsonProperty("RawTableId")
	private String rawTableId;
	
	@JsonProperty("IsMainStatus")
	private String isMainStatus;
	
	@JsonProperty("ApiMethod")
	private String apiMethod;

}
