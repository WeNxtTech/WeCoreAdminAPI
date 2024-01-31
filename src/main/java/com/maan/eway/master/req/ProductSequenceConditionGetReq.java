package com.maan.eway.master.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ProductSequenceConditionGetReq {

	@JsonProperty("QueryId")
	private String queryId;

    @JsonProperty("BranchCode")
	private String branchCode;

	@JsonProperty("InsuranceId")
	private String insuranceId;

	@JsonProperty("SequenceId")
	private String sequenceId;
	
	@JsonProperty("ProductId")
	private String productId;
    
}
