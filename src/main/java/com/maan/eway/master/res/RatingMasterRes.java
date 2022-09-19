package com.maan.eway.master.res;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class RatingMasterRes implements Serializable {

    private static final long serialVersionUID = 1L;

	@JsonProperty("RatingId")
	 private String    ratingId ;
	
	@JsonProperty("RatingSetupName")
	private String ratingSetupName;
	
	@JsonProperty("RatingSetupShort")
	private String ratingSetupShort;
	
	@JsonProperty("RatingStatus")
	private String ratingStatus;
	
	@JsonFormat(pattern="dd/MM/yyyy")
	@JsonProperty("RatingEffectDate")
	private Date ratingEffectDate;
	
	@JsonProperty("CoverageType")
	private String coverageType;
	
	@JsonProperty("UploadOption")
	private String uploadOption;
	
	@JsonProperty("MaxSuminsured")
	private String maxSuminsured;
	
	@JsonProperty("CoverageLimit")
	private String coverageLimit;
	
	@JsonProperty("CalcType")
	private String calcType;
	
	@JsonProperty("BaseRate")
	private String baseRate;
	
	@JsonProperty("Excess")
	private String excess;
	
	@JsonProperty("CalcYn")
	private String calcYn;
	
	@JsonProperty("MinPremium")
	private String minPremium;
	
	@JsonProperty("coverShortDesc")
	private String coverShortDesc;

	@JsonProperty("CoverName")
	private String coverName;
	
	@JsonProperty("CalcStatus")
	private String calcStatus;
	
	@JsonProperty("ToolTip")
	private String toolTip;
	
	@JsonProperty("CoreCode")
	private String coreCode;
	
	@JsonProperty("MinSuminsured")
	private String minSuminsured;
	
	@JsonProperty("CoreAppCode")
	private String coreAppCode;
	
	@JsonProperty("AmendId")
	private String amendId;

	@JsonProperty("Remarks")
	private String remarks;

	@JsonProperty("Status")
	private String status;

	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("EntryDate")
	private Date entryDate;
	
	


}
