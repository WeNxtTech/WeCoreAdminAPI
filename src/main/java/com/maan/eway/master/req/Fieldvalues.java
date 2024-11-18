package com.maan.eway.master.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Fieldvalues {
	
	@JsonProperty("Description")
	private String description;
	
	@JsonProperty("Value")
	private String value;

}
