package com.maan.eway.master.res;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class CoverSubCoverGetRes {

		@JsonProperty("SubCoverId")
	    private String subCoverId;
		
		@JsonProperty("CoverId")
	    private String coverId;
		
		@JsonProperty("SectionId")
	    private String sectionId;
		
		@JsonProperty("ProductId")
	    private String productId;
		
		@JsonProperty("InsuranceId")
	    private String companyId;
	    
	    @JsonProperty("SubCoverName")
	    private String subCoverName;
	    
	    @JsonProperty("SubCoverDesc")
	    private String subCoverDesc;
	    
	    @JsonProperty("CoverName")
	    private String coverName;
	    
	    @JsonFormat(pattern = "dd/MM/yyyy")
		@JsonProperty("EffectiveDateStart")
		private Date effectiveDateStart;
		
		@JsonFormat(pattern = "dd/MM/yyyy")
		@JsonProperty("EffectiveDateEnd")
		private Date effectiveDateEnd;
	    
		@JsonFormat(pattern = "dd/MM/yyyy")
		@JsonProperty("EntryDate")
	    private Date       entryDate    ;
		
		@JsonProperty("Status")
	    private String     status       ;
	
		@JsonProperty("CoreAppCode")
		private String coreAppCode;
		  
		@JsonProperty("TiraCode")
		private String tiraCode;
		    
	    @JsonProperty("CreatedBy")
	    private String createdBy;
	    
		@JsonProperty("AmendId")
		private String amendId;
	
		@JsonProperty("Remarks")
		private String remarks;
		
}
