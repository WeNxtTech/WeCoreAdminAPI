package com.maan.eway.master.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class SuccessResponse {
	@JsonProperty("Response")
	private String response;

	@JsonProperty("SuccessId")
	private String successId;
}
