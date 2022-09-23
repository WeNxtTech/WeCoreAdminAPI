package com.maan.eway.master.req;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class MotorMakeModelReq {

	@JsonProperty("MakeId")
	private Integer makeId;
	
	@JsonProperty("ModelId")
	private Integer modelId;
	
	@JsonProperty("BodyId")
	private Integer bodyId;
	

	@JsonProperty("VehicleModelCode")
	private Integer vehicleModelCode;
	

	@JsonProperty("Status")
	private String status;

	@JsonProperty("MakeNameEn")
	private String makeNameEn;
	
	@JsonProperty("ModelNameEn")
	private String modelNameEn;
	
	@JsonProperty("BodyNameEn")
	private String bodyNameEn;

	@JsonProperty("VehManfCountry")
	private Integer vehManfCountry;


	@JsonProperty("VehCc")
	private Integer vehCc;
	

	@JsonProperty("VehWeight")
	private Integer vehWeight;
	

	@JsonProperty("VehFueltype")
	private Integer vehFueltype;
	

	@JsonProperty("MakeNameAr")
	private String makeNameAr;
	

	@JsonProperty("ModelNameAr")
	private String modelNameAr;
	

	@JsonProperty("AmendId")
	private Integer amendId;
	

	@JsonProperty("TplRate")
	private Integer tplRate;
	

	@JsonProperty("BaseRate")
	private Integer baseRate;
	

	@JsonProperty("NetRate")
	private Integer netRate;
	

	@JsonProperty("EntryDate")
	private Date entryDate;
	

	@JsonProperty("EffectiveDate")
	private Date effectiveDate;
	

	@JsonProperty("Remarks")
	private String remarks;
	

	@JsonProperty("InsCompanyId")
	private String insCompanyId;
}                                      
