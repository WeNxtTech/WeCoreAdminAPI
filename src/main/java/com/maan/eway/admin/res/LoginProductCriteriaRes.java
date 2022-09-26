package com.maan.eway.admin.res;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginProductCriteriaRes {

	@JsonProperty("ProductId")
	private Integer productId ;
	
	@JsonProperty("InsuranceId")
	private String companyId ;
	
	@JsonProperty("ProductName")
	private String productName ;
	
	
	@JsonProperty("OldProductName")
	private String oldProductName ;
	
	@JsonProperty("SumInsuredStart")
	private Double sumInsuredStart;
	
	@JsonProperty("SumInsuredEnd")
	private Double sumInsuredEnd;
	
	@JsonProperty("Status")
	private String status;
	
	@JsonProperty("Remarks")
	private String remarks ;
	
	@JsonProperty("CompanyName")
	private String companyName ;
	
}
