package com.maan.eway.ttrncloseing.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
@Data
public class TtrnReq {

	@JsonProperty("TranCode")
    private String tranCode;
    
	@JsonProperty("DateClosed")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	 private Date cloDateClosed;

	@JsonProperty("Remarks")
    private String remarks;

	@JsonProperty("PreparedBy")
    private String preparedBy;

	@JsonProperty("PreparedDt")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date  preparedDt;

	@JsonProperty("ModifiedBy")
    private String modifiedBy;

	@JsonProperty("DateOpened")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date dateOpened;

	@JsonProperty("CompanyId")
	private String companyId;
   
	@JsonProperty("BranchCode")
	private String branchCode;

	@JsonProperty("ProductCode")
    private String productCoreCode;
}
