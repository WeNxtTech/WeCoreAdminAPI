package com.maan.eway.common.req;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class TiraPushedDetailsReq {

	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("StartDate")
	private Date startDate;
	
	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("EndDate")
	private Date endDate;
	
	@JsonProperty("Limit")
	private String limit;
	
	@JsonProperty("Offset")
	private String offset;
}
