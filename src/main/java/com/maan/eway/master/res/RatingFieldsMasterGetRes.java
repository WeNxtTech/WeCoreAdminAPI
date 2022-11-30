package com.maan.eway.master.res;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
@Data

public class RatingFieldsMasterGetRes {

	@JsonProperty("RatingId")
	private String ratingId;

	@JsonProperty("RatingField")
	private String ratingField;

	@JsonProperty("RatingDesc")
	private String ratingDesc;

	@JsonProperty("InputTable")
	private String inputTable;

	@JsonProperty("InputColumn")
	private String inputColumn;
	
	@JsonProperty("Status")
	private String status;
	
	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("EffectiveDateStart")
	private Date effectiveDateStart;

	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("EffectiveDateEnd")
	private Date effectiveDateEnd;
	
	@JsonProperty("Remarks")
	private String remarks;
	
	@JsonProperty("ProductId")
	private String productId;
	
	@JsonProperty("CreatedBy")
	private String createdBy;

	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("EntryDate")
	private Date entryDate;

	@JsonProperty("AmendId")
	private String amendId;

	
	@JsonProperty("InputTableName")
	private String inputTableName;

	@JsonProperty("InputColumnName")
	private String inputColumnName;
	
	@JsonProperty("MasterYn")
	private String masterYn;
	
	@JsonProperty("ApiUrl")
	private String apiUrl;
	
}
