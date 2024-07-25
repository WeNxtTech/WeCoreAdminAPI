package com.maan.eway.master.res;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ProductSequenceMasterRes  {

   

   
	@JsonProperty("BranchCode")
	private String branchCode;

	@JsonProperty("InsuranceId")
	private String companyId;

	
	@JsonProperty("ProductId")
	private String productId;

	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("EffectiveDateStart")
	private Date effectiveDateStart;

	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("EffectiveDateEnd")
	private Date effectiveDateEnd;
	
	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("EntryDate")
	private Date entryDate;

	@JsonProperty("Status")
	private String status;
	
	@JsonProperty("CreatedBy")
	private String createdBy;

	@JsonProperty("UpdatedBy")
	private String updatedBy;

	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("UpdatedDate")
	private Date updatedDate;
	
	@JsonProperty("SequenceId")
	private String sequenceId;
	
	@JsonProperty("Type")
	private String type;
	
	@JsonProperty("TypeDesc")
	private String typeDesc;
	
	@JsonProperty("SequenceCharacter")
	private String sequenceCharacter;
	
	@JsonProperty("CurrentSequenceNo")
	private String currentSequenceNo;
	
	@JsonProperty("QueryYn")
	private String queryYn;
	
	@JsonProperty("CurrentGeneratedSequence")
	private String currentGeneratedSequence;

	
	@JsonProperty("SequenceNoApplyYn")
	private String sequenceNoApplyYn;
    //--- ENTITY PRIMARY KEY 

   


}
