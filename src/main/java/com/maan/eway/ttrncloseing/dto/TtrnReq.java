package com.maan.eway.ttrncloseing.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class TtrnReq {

	@JsonProperty("TranCode")
	private String tranCode;

	@JsonProperty("DateClosed")
	private LocalDate cloDateClosed;

	@JsonProperty("MonthEnddate")
	private LocalDate monthEnddate;

	@JsonProperty("Remarks")
	private String remarks;

	@JsonProperty("PreparedBy")
	private String preparedBy;

	@JsonProperty("PreparedDt")
	private LocalDate preparedDt;

	@JsonProperty("ModifiedBy")
	private String modifiedBy;

	@JsonProperty("DateOpened")
	private LocalDate dateOpened;

	@JsonProperty("CompanyId")
	private String companyId;

	@JsonProperty("BranchCode")
	private String branchCode;

	@JsonProperty("ProductId")
	private String productCoreCode;
}
