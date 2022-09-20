package com.maan.eway.master.req;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class OfsGridSaveReq implements Serializable {

    private static final long serialVersionUID = 1L;
	
	@JsonProperty("CalcType")
	private String calcType;
	
	@JsonProperty("MaxSuminsured")
	private String maxSuminsured;
	
	@JsonProperty("MinSuminsured")
	private String minSuminsured;
	
	@JsonProperty("BaseRate")
	private String baseRate;
	
	@JsonProperty("MinPremium")
	private String minPremium;

	@JsonProperty("ToolTip")
	private String toolTip;
	
	/*@JsonProperty("CoreCode")
	private String coreCode;

	@JsonProperty("RatingId")
	private String    ratingId ;

	@JsonProperty("RatingSetupName")
	private String ratingSetupName;
	
	@JsonProperty("RatingSetupShort")
	private String ratingSetupShort;
	
	@JsonFormat(pattern="dd/MM/yyyy")
	@JsonProperty("RatingEffectDate")
	private Date ratingEffectDate;*/





}
