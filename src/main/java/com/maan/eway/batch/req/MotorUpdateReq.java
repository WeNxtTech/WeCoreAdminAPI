package com.maan.eway.batch.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MotorUpdateReq {
	
	@JsonProperty("RowNum")
	private String rowNum;
	
	@JsonProperty("SearchByData")
	private String SearchByData;
	
	@JsonProperty("InsuranceType")
	private String insuranceType;
	
	@JsonProperty("InsuranceTypeId")
	private String insuranceTypeId;
	
	@JsonProperty("InsuranceClass")
	private String insuranceClass;

	@JsonProperty("InsuranceClassId")
	private String insuranceClassId;

	@JsonProperty("BodyType")
	private String bodyType;
	
	@JsonProperty("BodyTypeId")
	private String bodyTypeId;
	
	@JsonProperty("MotorUsage")
	private String motorUsage;
	
	@JsonProperty("MotorUsageId")
	private String motorUsageId;
	
	@JsonProperty("ClaimYn")
	private String ClaimYn;
	
	@JsonProperty("GPSYn")
	private String gpsYn;
	
	@JsonProperty("VehcileSuminsured")
	private String vehcileSuminsured;
	
	@JsonProperty("AccessoriesSuminured")
	private String accessoriesSuminured;
	
	@JsonProperty("WinShieldSuminsured")
	private String winShieldSuminsured;

	@JsonProperty("ExtendedTPPDSuminsured")
	private String extendedTPPDSuminsured;
	
	@JsonProperty("CollateralYn")
	private String collateralYn;
	
	@JsonProperty("BorrowType")
	private String borrowType;
	
	@JsonProperty("FirstLossPayee")
	private String FirstLossPayee;

}
