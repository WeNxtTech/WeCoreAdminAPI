package com.maan.eway.master.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class CompanyStateMasterChangeStatusReq {
	 
	 @JsonProperty("StateId")
	 private String stateId;
	 
	 @JsonProperty("InsuranceId")
	 private String companyId;
	 
	 @JsonProperty("Status")
	 private String status;
}
