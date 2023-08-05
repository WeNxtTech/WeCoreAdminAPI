package com.maan.eway.master.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class InsertPlanBenefits {
	
//	@JsonProperty("CoverId" )
//	private String coverId;

	@JsonProperty("CoverDesc" )
    private String coverDesc ;
    
//    @JsonProperty("SubCoverId" )
//    private String subCoverId ;
    
    @JsonProperty("SubCoverDesc" )
    private String subCoverDesc ;
    
    @JsonProperty("Currency" )
    private String currency ;
    
    @JsonProperty("SumInsured" )
    private String sumInsured ;
    
    @JsonProperty("ExcessAmt" )
    private String excessAmt ;
    
    @JsonProperty("Status")
    private String     status;
    
}
