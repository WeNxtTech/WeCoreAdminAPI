package com.maan.eway.master.res;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class RiskDomesticDetailsListRes {

	@JsonProperty("ProductId")
    private String     productId ;
	
	@JsonProperty("ProductName")
    private String     productName ;
	
	@JsonProperty("CustomerId")
    private String     customerId ;
	
	@JsonProperty("RequestReferenceNo")
    private String     requestReferenceNo ;
	
	@JsonProperty("InsuranceId")
    private String     companyId ;
	
	@JsonProperty("CreatedBy")
    private String     createdBy;
	
	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("EntryDate")
    private Date entryDate;
	
	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("UpdatedDate")
    private Date updatedDate;
	
	@JsonProperty("UpdatedBy")
    private String updatedBy;
	
	@JsonProperty("RiskCount")
    private String riskCount;
	
	
}
