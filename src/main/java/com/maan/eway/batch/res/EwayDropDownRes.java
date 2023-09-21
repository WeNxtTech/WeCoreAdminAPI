package com.maan.eway.batch.res;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EwayDropDownRes {
	
	@JsonProperty("Code")
	private String code;
	
	@JsonProperty("Description")
	private String description;

}
