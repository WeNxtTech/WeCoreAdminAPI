package com.maan.eway.excelconfig2;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class XLConfigMasterResponse {


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

	@JsonProperty("ExcelHeaderName")
	private String excelHeaderName;

	@JsonProperty("MandatoryYn")
	private String mandatoryYn;
	
	@JsonProperty("DataType")
	private String dataType;
	
	@JsonProperty("Status")
	private String status;
	
	@JsonProperty("FieldNameRaw")
	private String fieldNameRaw;
	
	@JsonProperty("FieldLength")
	private String fieldLength;
	
	@JsonProperty("DataRange")
	private String dataRange;

	@JsonProperty("IsMainDefauVal")
	private String isMainDefauVal;

	@JsonProperty("ApiJsonKey")
	private String apiJsonKey;

	@JsonProperty("SelColName")
	private String selColName;

	@JsonProperty("IsObject")
	private String isObject;

	@JsonProperty("IsArray")
	private String isArray;

	@JsonProperty("ObjApijsonKey")	
	private String objApijsonKey;

	@JsonProperty("ObjSelcolKey")
	private String objSelcolKey;

	@JsonProperty("ObjDefaulVal")
	private String objDefaulVal;
	
}
