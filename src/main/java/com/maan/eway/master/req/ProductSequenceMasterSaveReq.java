package com.maan.eway.master.req;

import java.util.Date;

import jakarta.persistence.Column;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ProductSequenceMasterSaveReq  {

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
	
	@JsonProperty("Type")
	private String type;
	
	
	@JsonProperty("SequenceCharacter")
	private String sequenceCharacter;
	
	@JsonProperty("CurrentSequenceNo")
	private String currentSequenceNo;
	
	@JsonProperty("QueryYn")
	private String queryYn;
	
	@JsonProperty("SequenceNoApplyYn")
	private String sequenceNoApplyYn;



}
