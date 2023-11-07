package com.maan.eway.common.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class TransactionCheckStatusReq {

	@JsonProperty("InsuranceId")
	private String insuranceId;
}
