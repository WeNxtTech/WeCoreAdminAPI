package com.maan.eway.common.res;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class MotorProductDetailsRes {
	
	@JsonProperty("VehicleDetails")
	private  VehicleDetailsRes vehicleDetails   ;


	@JsonProperty("Covers")
	private  List<Cover> covers ;

}
