package com.maan.eway.master.req;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class PolicyTypeMasterSubCoverSingleSaveReq {
	
	@JsonProperty("PlanTypeId")
    private String planTypeId ;
	
	@JsonProperty("PlanTypeDesc")
    private String planTypeDesc ;
	
	@JsonProperty("PolicyTypeId")
    private String PolicyTypeId ;
	
	@JsonProperty("PolicyTypeDesc")
    private String PolicyTypeDesc ;
	
	@JsonProperty("CoverId")
    private String    coverId ;
	
	@JsonProperty("CoverDesc")
	private String    coverDesc;
	
	@JsonProperty("InsuranceId")
	private String companyId;
	
	@JsonProperty("ProductId")
	private String productId;
	
	@JsonProperty("BranchCode")
	private String branchCode;
	
	@JsonProperty("Remarks")
	private String remarks;
	
	@JsonProperty("CreatedBy")
	private String createdBy;
	
	@JsonProperty("CoverStatus")
	private String coverStatus;
	 
	@JsonFormat(pattern="dd/MM/yyyy")
	@JsonProperty("EffectiveDateStart")
	private Date effectiveDateStart;
	
	//subcover
	@JsonProperty("SubCoverId")
	private String subCoverId;
	
	@JsonProperty("SubCoverDesc")
	private String subCoverDesc;
	
	@JsonProperty("SumInsured")
	private String sumInsured; //Excess Limits
	
	@JsonProperty("Currency")
	private String currency;
	
	@JsonProperty("ExcessAmt")
	private String excessAmt;
	
	@JsonProperty("Status")
	private String status;

}
