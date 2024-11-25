package com.maan.eway.master.res;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PremiaCustRes {

	@JsonProperty("Heading")
	private String heading;
	
	@JsonProperty("Data")
	private List<PremiaCustDetailsRes> data;

	@JsonProperty("status")
	private String status;
	
	
	
	
	
}
