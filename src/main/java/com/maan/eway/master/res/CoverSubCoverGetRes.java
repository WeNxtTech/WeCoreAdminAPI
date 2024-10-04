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
		
		@JsonProperty("CoverBasedOn")
		private String coverBasedOn;
	    
	    @JsonProperty("SubCoverName")
	    private String subCoverName;
	    
	    @JsonProperty("SubCoverDesc")
	    private String subCoverDesc;
	    

		@JsonProperty("ProRataYn")
		private String proRataYn;
		
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
		
		@JsonProperty("ExcessPercent")
		private String excessPercent;
		
		@JsonProperty("ExcessAmount")
		private String excessAmount;
		
		@JsonProperty("ExcessDesc")
		private String excessDesc;
		
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

		@JsonProperty("UpdatedBy")
	    private String updatedBy;
	    
	    @JsonFormat(pattern = "dd/MM/yyyy")
		@JsonProperty("UpdatedDate")
		private Date updatedDate;
		
		@JsonProperty("Remarks")
		private String remarks;
		
		@JsonProperty("DependentCoverYn")
	    private String dependentCoverYn;
	    
		@JsonProperty("DependentCoverId")
	    private String dependentCoverId;
		
		@JsonProperty("CodeDescLocal")
		private String codeDescLocal;
		
		@JsonProperty("MinimumRateYN")
		private String minimumRateYN;
		
		@JsonProperty("MinimumRate")
		private String minimumRate;
		
		@JsonProperty("IsSelectedYn")
		private String isSelectedYn;
}
