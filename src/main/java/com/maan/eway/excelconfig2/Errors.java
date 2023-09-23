package com.maan.eway.excelconfig2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Errors {

	@JsonProperty("Code")
	private String code;
	@JsonProperty("Field")
	private String field;
	@JsonProperty("Message")
	private String message;

	
}
	
