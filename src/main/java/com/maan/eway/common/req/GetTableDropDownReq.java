package com.maan.eway.common.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GetTableDropDownReq {


	@JsonProperty("InsuranceId")
	private String insuranceId;
	@JsonProperty("BranchCode")
	private String branchCode;
	@JsonProperty("TableName")
	private String tableName;
	@JsonProperty("ProductId")
	private String productId;
	@JsonProperty("SectionId")
	private String sectionId;

}
