package com.maan.eway.master.res;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class CoverMasterGetAllRes {

	@JsonProperty("CoverId")
	private String coverId;
	
	@JsonFormat(pattern="dd/MM/yyyy")
	@JsonProperty("EffectiveDateStart")
	private Date effectiveDateStart;

	@JsonProperty("CoverName")
	private String coverName;
	
	@JsonProperty("CoverDesc")
	private String coverDesc;
	
	@JsonProperty("Status")
	private String status;

	@JsonProperty("RegulatoryCode")
	private String regulatoryCode;

	@JsonProperty("CoverBasedOn")
	private String coverBasedOn;
	
	@JsonProperty("Remarks")
	private String remarks; 
	
	@JsonProperty("CreatedBy")
	private String createdBy; 
	
	@JsonProperty("UpdatedBy")
	private String updatedBy; 
	
	
	@JsonProperty("CoverageTypeDesc")
	private String coverageTypeDesc;
	
	
	@JsonProperty("ToolTip")
	private String toolTip;  

	@JsonFormat(pattern="dd/MM/yyyy")
	@JsonProperty("EffectiveDateEnd")
	private Date effectiveDateEnd;
	
	@JsonFormat(pattern="dd/MM/yyyy")
	@JsonProperty("EntryDate")
	private Date entryDate;
	
	@JsonFormat(pattern="dd/MM/yyyy")
	@JsonProperty("UpdatedDate")
	private Date updatedDate;
	
	// Rating Master Insert 
	@JsonProperty("CoverageType")
	private String coverageType;
	
	@JsonProperty("CoverageLimit")
	private String coverageLimit;
	
	@JsonProperty("Excess")
	private String excess;
	
	@JsonProperty("SubCoverYn")
	private String subCoverYn;
	
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

	@JsonProperty("DiscountCoverId")
	private String discountCoverId;
	
	@JsonProperty("ExcessPercent")
	private String excessPercent;
	
	@JsonProperty("ExcessAmount")
	private String excessAmount;
	
	@JsonProperty("ExcessDesc")
	private String excessDesc;
	
	
	@JsonProperty("ProRataYn")
	private String proRataYn;
	
	@JsonProperty("CodeDescLocal")
	private String codeDescLocal;
}
