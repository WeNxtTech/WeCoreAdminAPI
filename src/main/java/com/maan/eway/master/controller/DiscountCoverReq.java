package com.maan.eway.master.controller;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class DiscountCoverReq {

	@JsonProperty("CoverId")
	private String CoverId ;
}
