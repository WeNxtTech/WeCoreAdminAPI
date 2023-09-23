package com.maan.eway.batch.res;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class SaveXlConfigReq {

	
	@JsonProperty("CompanyId")
	private String companyId;
	
	@JsonProperty("ProductId")
	private String productId;
	
	@JsonProperty("TypeId")
	private String typeId;
	
	@JsonProperty("Sno")
	private String sno;
	
	@JsonProperty("ExcelHeaderName")
	private String excelHeaderName;
	
	@JsonProperty("RawColumnName")
	private String rawColumnName;
	
	@JsonProperty("MandatoryYn")
	private String mandatoryYn;
	
	@JsonProperty("DataType")
	private String dataType;
	
	@JsonProperty("FieldLength")
	private String fieldLength;
	
	@JsonProperty("DataRange")
	private String dataRange;
	
	@JsonProperty("IsArrayYn")
	private String isArrayYn;
	
	@JsonProperty("ArrayJsonKey")
	private String arrayJsonKey;
	
	@JsonProperty("ArrayTableColumn")
	private String arrayTableColumn;
	
	@JsonProperty("ArrayDefaultValYn")
	private String arrayDefaultValYn;
	
	@JsonProperty("IsObjectYn")
	private String isObjectYn;
	
	@JsonProperty("ObjectJsonKey")
	private String ObjectJsonKey;
	
	@JsonProperty("ObjectTablecolumn")
	private String objectTablecolumn;
	
	@JsonProperty("ObjectDefaultValYn")
	private String objectDefaultValYn;
	
	@JsonProperty("Status")
	private String status;
}
