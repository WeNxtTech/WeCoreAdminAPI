package com.maan.eway.master.res;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.eway.master.req.OfsGridGetRes;

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
	    
	    @JsonProperty("RegulatoryCode")
		private String regulatoryCode;

		@JsonFormat(pattern="dd/MM/yyyy")
		@JsonProperty("EffectiveDateEnd")
		private Date effectiveDateEnd;
		
		@JsonFormat(pattern="dd/MM/yyyy")
		@JsonProperty("EntryDate")
		private Date entryDate;
		
		// Rating Master Insert 
		@JsonProperty("CoverageType")
		private String coverageType;
		
		@JsonProperty("CoverageLimit")
		private String coverageLimit;
		
		@JsonProperty("Excess")
		private String excess;
		
		@JsonProperty("CalcType")
		private String calcType;
		
		@JsonProperty("BaseRate")
		private String baseRate;
		
		@JsonProperty("SumInsuredEnd")
		private String sumInsuredEnd;
		
		@JsonProperty("MinimumPremium")
		private String minimumPremium;
		
		@JsonProperty("FactorTypeId")
		private String factorTypeId; 

		@JsonProperty("SubCoverYn")
		private String subCoverYn;
		
		@JsonProperty("AmendId")
		private String amendId;
		@JsonProperty("SumInsuredStart")
		private String sumInsuredStart;
		
		@JsonProperty("GridDetails")
		private List<OfsGridGetRes> gridDetails;
		
		@JsonProperty("DiscountCoverId")
		private String discountCoverId;
		
		@JsonProperty("IsTaxExcempted")
		private String isTaxExcempted;
		
		@JsonProperty("TaxAmount")
		private String taxAmount;
		
		@JsonProperty("TaxCode")
		private String taxCode;
		
		@JsonProperty("TaxExcemptionReference")
		private String taxExcemptionReference;
		
		@JsonProperty("TaxExcemptionType")
		private String taxExcemptionType;
	    	
	    @JsonProperty("CoverName")
	    private String coverName;
	    
	    @JsonFormat(pattern = "dd/MM/yyyy")
		@JsonProperty("EffectiveDateStart")
		private Date effectiveDateStart;
		
		@JsonProperty("Status")
	    private String     status       ;
	
		@JsonProperty("CoreAppCode")
		private String coreAppCode;
		  
		@JsonProperty("CreatedBy")
	    private String createdBy;
	    
		@JsonProperty("Remarks")
		private String remarks;
		
}
