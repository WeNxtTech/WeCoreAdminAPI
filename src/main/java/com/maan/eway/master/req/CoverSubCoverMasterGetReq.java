package com.maan.eway.master.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class CoverSubCoverMasterGetReq {

	   @JsonProperty("InsuranceId")
	    private String companyId;
	    
	    @JsonProperty("ProductId")
	    private String productId;
	    
	    @JsonProperty("SectionId")
	    private String sectionId;
	    
	    @JsonProperty("CoverId")
	    private String CoverId;
	    
	    @JsonProperty("SubCoverId")
	    private String subCoverId;
}
