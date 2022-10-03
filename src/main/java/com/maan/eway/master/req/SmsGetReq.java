package com.maan.eway.master.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class SmsGetReq {

	@JsonProperty("SNo")
	private String sNo;
	
	
	@JsonProperty("InsuranceId")
	private String companyId;
}
