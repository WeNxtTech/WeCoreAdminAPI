package com.maan.eway.excelconfig2;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class XLConfigGetReq {

	@JsonProperty("CompanyId")
	private String companyId;

	@JsonProperty("ProductId")
	private String productId;

	@JsonProperty("SectionId")
	private String sectionId;

	@JsonProperty("TypeId")
	private String typeId;

	@JsonProperty("FieldId")
	private String fieldId;
	
}
