package com.maan.eway.ttrncloseing.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
@Data
public class TtrnRes {

	@JsonProperty("TranCode")
	private Integer tranCode;

	@JsonProperty("DateClosed")
	 @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	private LocalDate dateClosed;

	@JsonProperty("Remarks")
	private String remarks;

//	@JsonProperty("PreparedBy")
//	private Integer preparedBy;

	@JsonProperty("PreparedDt")
	 @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	private LocalDate preparedDt;

//	@JsonProperty("ModifiedBy")
//	private String modifiedBy;

	@JsonProperty("MonthendDt")
	 @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy" , timezone = "UTC")
	private LocalDate monthendDt;

	@JsonProperty("DateOpened")
	 @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	private LocalDate dateOpened;

	@JsonProperty("setUpMonth")
	 @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	private LocalDate setUpMonth;
	
	@JsonProperty("BranchCode")
	private String branchCode;

	@JsonProperty("ProductId")
	private String productCoreCode;

	@JsonProperty("CompanyId")
	private String companyid;
	
	@JsonProperty("Year")
	private String year;

}
