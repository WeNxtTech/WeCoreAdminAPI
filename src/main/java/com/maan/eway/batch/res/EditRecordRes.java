package com.maan.eway.batch.res;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EditRecordRes {
	
	@JsonProperty("CompanyId")
    private String companyId;
	
	@JsonProperty("ProductId")
    private String productId;
	
	@JsonProperty("RequestRefNo")
    private String requestRefNo;
	
	@JsonProperty("SectionId")
    private String sectionId;
	
	@JsonProperty("VehicleId")
    private String vehicleId;
	
	@JsonProperty("TiraSearchDesc")
    private String tiraSearchDesc;
	
	@JsonProperty("RegistrationNo")
    private String registrationNo;
	
	@JsonProperty("PolicyType")
    private String policyType;
	
	@JsonProperty("BodyType")
    private String bodyType;
	
	
	
	@JsonProperty("VehicleUsage")
    private String vehicleUsageAge;
	
	@JsonProperty("ErrorDesc")
    private String erroDesc;
	
	@JsonProperty("Status")
    private String status;
	
	

}
