package com.maan.eway.master.res;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RiskDomesticCriteriaRes {

	@JsonProperty("RiskCount")
	private Long riskCount;
	
	@JsonProperty("ProductId")
	private Integer productId ;
	
	@JsonProperty("ProductName")
	private String productName ;
	
	@JsonProperty("CustomerId")
	private Integer customerId ;
	
	@JsonProperty("CompanyId")
	private String companyId ;
	
	@JsonProperty("RequestReferenceNo")
	private String requestReferenceNo ;
	
	@JsonProperty("CreatedBy")
	private String createdBy;
	
	@JsonProperty("UpdatedBy")
	private String updatedBy ;
	
	@JsonProperty("EntrytDate")
	private Date entryDate;
	
	@JsonProperty("UpdatedDate")
	private Date updatedDate ; 


}
