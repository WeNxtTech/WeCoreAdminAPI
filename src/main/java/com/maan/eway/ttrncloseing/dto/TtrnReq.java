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
	
	 private Date cloDateClosed;

	@JsonProperty("DateClosed")
	
	 private Date monthEnddate;
	
	@JsonProperty("Remarks")
    private String remarks;

	@JsonProperty("PreparedBy")
    private String preparedBy;

	@JsonProperty("PreparedDt")
	
    private Date  preparedDt;

	@JsonProperty("ModifiedBy")
    private String modifiedBy;

	@JsonProperty("DateOpened")
	
    private Date dateOpened;

	@JsonProperty("CompanyId")
	private String companyId;
   
	@JsonProperty("BranchCode")
	private String branchCode;

	@JsonProperty("ProductId")
    private String productCoreCode;
}
