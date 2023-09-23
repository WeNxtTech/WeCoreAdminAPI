package com.maan.eway.excelconfig2;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class CommonResponse {

	@JsonProperty("IsError")
	private boolean isError;
	
	@JsonProperty("ErrorMessage")
	private List<Errors> errorMessage;
	
	@JsonProperty("ErrorCode")
	private int errorCode;
	
	@JsonProperty("Message")
	private String message;
	
	@JsonProperty("Result")
	private Object result;
	
}
