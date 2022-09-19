package com.maan.eway.master.req;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ProductDocumentMasterGetAllReq {

	@JsonProperty("Limit")
	private String limit;

	@JsonProperty("Offset")
	private String offset;
	
	@JsonProperty("InsuranceId")
	private String companyId;
	

	@JsonProperty("ProductId")
	private Integer productId;
	

	@JsonProperty("SectionId")
	private Integer sectionId;
	
}
