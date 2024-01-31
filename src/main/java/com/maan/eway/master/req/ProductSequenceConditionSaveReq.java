package com.maan.eway.master.req;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ProductSequenceConditionSaveReq {

	@JsonProperty("BranchCode")
	private String branchCode;

	@JsonProperty("InsuranceId")
	private String insuranceId;

	@JsonProperty("ProductId")
	private String productId;

	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("EffectiveDateStart")
	private Date effectiveDateStart;
	
	@JsonProperty("Status")
	private String status;
	
	@JsonProperty("CreatedBy")
	private String createdBy;
	
	@JsonProperty("SequenceId")
	private String sequenceId;
	
	
	@JsonProperty("QueryId")
	private String queryId;
	
	@JsonProperty("QueryDesc")
	private String queryDesc;
	
	@JsonProperty("Query")
	private String query;
	
	@JsonProperty("JoinWith")
	private String joinWith;

}
