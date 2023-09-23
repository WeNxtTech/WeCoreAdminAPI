package com.maan.eway.excelconfig;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class UploadTypeGetReq {

	@JsonProperty("CompanyId")
	private String companyId;
	
	@JsonProperty("ProductId")
	private String productId;
	
	@JsonProperty("TypeId")
	private String typeId;
	
	@JsonProperty("SectionId")
	private String sectionId;
	
}
