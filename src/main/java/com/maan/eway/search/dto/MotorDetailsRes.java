package com.maan.eway.search.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class MotorDetailsRes {
	
	@JsonProperty("QuoteNo")
	private String quoteNo;
	@JsonProperty("CoverId")
	private String coverId;
	@JsonProperty("ChassisNumber")
	private String chassisNumber;
	@JsonProperty("RegistrationNumber")
	private String registrationNumber;
	@JsonProperty("VehicleId")
	private String vehicleId;
	@JsonProperty("ProductId")
	private Integer productId;
	@JsonProperty("SectionId")
	private Integer sectionId;
	@JsonProperty("SalePointCode")
	 private String    salePointCode;
	@JsonProperty("BrokerTiraCode")
	 private String brokerTiraCode;

}
