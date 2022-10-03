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

	@JsonProperty("CoreAppCode")
	private String coreAppCode;

	@JsonProperty("Remarks")
	private String remarks; 
	
	@JsonProperty("CreatedBy")
	private String createdBy; 
	
	@JsonProperty("ToolTip")
	private String toolTip;  

	@JsonProperty("TiraCode")
	private String tiraCode;

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

}
