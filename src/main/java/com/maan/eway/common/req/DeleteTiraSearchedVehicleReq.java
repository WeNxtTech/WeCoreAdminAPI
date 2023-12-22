package com.maan.eway.common.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class DeleteTiraSearchedVehicleReq {


	@JsonProperty("RegisterNumber")
	private String registerNumber;
	
	@JsonProperty("InsuranceId")
	private String insuranceId;
}
