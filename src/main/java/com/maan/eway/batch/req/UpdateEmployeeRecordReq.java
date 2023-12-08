package com.maan.eway.batch.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class UpdateEmployeeRecordReq {
	
	@JsonProperty("CompanyId")
	private String companyId;
	
	@JsonProperty("ProductId")
	private String productId;
	
	@JsonProperty("RequestReferenceNo")
	private String requestReferenceNo;
	
	@JsonProperty("LocationDesc")
	private String locationDesc;
	
	@JsonProperty("LocationId")
	private String locationId;
	
	@JsonProperty("ContentTypeDesc")
	private String contentTypeDesc;
	
	@JsonProperty("ContentTypeId")
	private String contentTypeId;
	
	@JsonProperty("SumInsured")
	private String sumInsured;
	
	@JsonProperty("Description")
	private String description;
	
	@JsonProperty("RowNum")
	private String rowNum;
	
	@JsonProperty("SerialNumber")
	private String serialNumber;
	


}
