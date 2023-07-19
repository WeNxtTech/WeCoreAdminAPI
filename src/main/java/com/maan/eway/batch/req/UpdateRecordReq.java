package com.maan.eway.batch.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateRecordReq {
	
	@JsonProperty("ProductId")
    private String productId;
	
	@JsonProperty("RequestRefNo")
    private String requestRefNo;
	
	@JsonProperty("QuoteNo")
    private String quoteNo;
	
	@JsonProperty("CompanyId")
    private String companyId;
	
	@JsonProperty("RiskId")
    private String riskId;
	
	@JsonProperty("EmployeeRequest")
	private EmployeeUpdateReq employeeUpdateReq;
	
	@JsonProperty("MotorRequest")
	private MotorUpdateReq motorRequest;
	
	@JsonProperty("TravelRequest")
	private TravelUpdateReq travelRequest;
	
}
