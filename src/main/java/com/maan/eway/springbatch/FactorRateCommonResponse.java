package com.maan.eway.springbatch;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class FactorRateCommonResponse {
	
	@JsonProperty("EndDate")
	private Date endate;
	@JsonProperty("AmendId")
	private Integer amendId;

}
