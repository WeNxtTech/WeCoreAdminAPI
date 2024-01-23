package com.maan.eway.master.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GetAllTaxExcemtedReq {

	@JsonProperty("ProductId")
    private String     productId     ;


	@JsonProperty("SectionId")
   private String     sectionId;
	
	@JsonProperty("CountryId")
	private String     countryId;
	
	@JsonProperty("TaxFor")
    private String     taxFor;
	
 
	@JsonProperty("CoverId")
   private String     coverId;
	
 	 @JsonProperty("InsuranceId")
 	 private String companyId;
 	 
 	 @JsonProperty("BranchCode")
 	 private String branchCode;
 	 

 	
}
