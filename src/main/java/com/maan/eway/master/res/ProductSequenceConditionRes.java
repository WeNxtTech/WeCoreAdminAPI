package com.maan.eway.master.res;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ProductSequenceConditionRes  {


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
  	
   	@JsonProperty("Status")
   	private String status;
   	
   	@JsonProperty("CreatedBy")
   	private String createdBy;
   	
   	@JsonProperty("SequenceId")
   	private String sequenceId;
   	
   	@JsonProperty("Type")
   	private String type;

 	@JsonProperty("TypeDesc")
   	private String typeDesc;
 	
   	@JsonProperty("QueryYn")
   	private String queryYn;

	@JsonProperty("UpdatedBy")
	private String updatedBy;

	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("UpdatedDate")
	private Date updatedDate;


	@JsonProperty("QueryId")
	private String queryId;
	
	@JsonProperty("QueryDesc")
	private String queryDesc;
	
	@JsonProperty("Query")
	private String query;
	
	@JsonProperty("JoinWith")
	private String joinWith;
}
