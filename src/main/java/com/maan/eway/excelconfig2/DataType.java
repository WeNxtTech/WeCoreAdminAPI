package com.maan.eway.excelconfig2;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class DataType {

	@JsonProperty("ItemCode")
	private String itemCode;
	
	@JsonProperty("ItemValue")
	private String ItemValue;
	
}
