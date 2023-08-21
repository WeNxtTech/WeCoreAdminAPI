package com.maan.eway.master.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ProductTaxMultiInsertReq {


	@JsonProperty("TaxId")
	private String taxId;
	
	@JsonProperty("TaxCode")
	private String taxCode;
	
	@JsonProperty("CalcType")
	private String calcType;
	
	@JsonProperty("Value")
	private String value;
	
	@JsonProperty("TaxExemptAllowYn")
	private String TaxExemptAllowYn;
	
	@JsonProperty("Status")
	private String status;

//	@JsonProperty("Remarks")
//	private String remarks;

	@JsonProperty("RegulatoryCode")
	private String regulatoryCode;
	
	@JsonProperty("CoreAppCode")
	private String coreAppCode;
	
	@JsonProperty("MinimumAmount")
	private String minimumAmount;
	
//	@JsonProperty("Priority")
//	private String priority;
	
	@JsonProperty("DependentYn")
	private String dependentYn;
}
