package com.maan.eway.master.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ProductSequenceConditionGetAllReq {

    @JsonProperty("BranchCode")
	private String branchCode;

	@JsonProperty("InsuranceId")
	private String insuranceId;

	@JsonProperty("SequenceId")
	private String sequenceId;
	
	@JsonProperty("ProductId")
	private String productId;
}
