package com.maan.eway.excelconfig;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ItemDropDown {
	
	@JsonProperty("ItemCode")
	private String itemCode;
	
	@JsonProperty("ItemDesc")
	private String itemValue;
	
}
