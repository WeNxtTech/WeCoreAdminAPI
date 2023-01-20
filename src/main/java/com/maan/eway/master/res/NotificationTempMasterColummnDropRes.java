package com.maan.eway.master.res;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class NotificationTempMasterColummnDropRes {

	@JsonProperty("CodeDesc")
	private String dispalyName;
//	@JsonProperty("ColumnName")
//	private String columnName;
	@JsonProperty("Code")
	private String fieldName;
	
}
