package com.maan.eway.vehicleupload;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class RenewalQuoteReq {
	
	@JsonProperty("CompanyId")
	private String companyId;
	
	
	@JsonProperty("RegistrationNo")
	private String registrationNo;
	

}
