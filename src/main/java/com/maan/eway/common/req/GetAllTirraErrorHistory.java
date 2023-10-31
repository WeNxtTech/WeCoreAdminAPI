package com.maan.eway.common.req;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GetAllTirraErrorHistory {

	
	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("EffectiveStartDate")
	private Date effectiveStartDate;
	
	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("EffectiveEndDate")
	private Date effectiveEndDate;
}
