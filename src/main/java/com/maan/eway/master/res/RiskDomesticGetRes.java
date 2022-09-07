package com.maan.eway.master.res;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class RiskDomesticGetRes {

	@JsonProperty("ProductId")
    private String     productId ;
	
	@JsonProperty("ProductName")
    private String     productName ;
	
	@JsonProperty("OldProductName")
    private String     oldProductName ;
	
	@JsonProperty("CustomerId")
    private String     customerId ;
	
	@JsonProperty("RequestReferenceNo")
    private String     requestReferenceNo ;
	
	@JsonProperty("InsuranceId")
    private String     companyId ;
	
	@JsonProperty("BranchCode")
    private String     branchCode;
	
	@JsonProperty("UpdatedBy")
    private String     updatedBy;
	
	@JsonFormat(pattern ="dd/MM/yyyy")
	@JsonProperty("EntryDate")
    private Date entryDate;
	
	@JsonFormat(pattern ="dd/MM/yyyy")
	@JsonProperty("UpdatedDate")
    private Date updatedDate;
	
	@JsonProperty("CreatedBy")
    private String     createdBy;
	
	
	
	@JsonProperty("RiskList")
    private List<RisksListRes>     riskList ;
}
