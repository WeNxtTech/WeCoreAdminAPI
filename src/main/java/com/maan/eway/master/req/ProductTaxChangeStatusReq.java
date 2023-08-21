package com.maan.eway.master.req;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ProductTaxChangeStatusReq {

	 @JsonProperty("TaxId")
	 private String taxId;
	 
	 @JsonProperty("InsuranceId")
	 private String companyId;
	 
	 @JsonProperty("ProductId")
	 private String productId;
	 
	 @JsonProperty("BranchCode")
	 private String branchCode;
	 
	 @JsonProperty("Status")
	 private String status;
	 
	 @JsonProperty("CountryId")
	 private String countryId;
	 
	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("EffectiveDateStart")
	private Date effectiveDateStart;
}
