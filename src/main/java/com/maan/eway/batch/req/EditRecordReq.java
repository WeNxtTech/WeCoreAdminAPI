package com.maan.eway.batch.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class EditRecordReq {
	
	@JsonProperty("RowId")
    private String rowId;
	
	@JsonProperty("ProductId")
    private String productId;
	
	@JsonProperty("RequestRefNo")
    private String requestRefNo;
	
	@JsonProperty("QuoteNo")
    private String quoteNo;
	
	@JsonProperty("CompanyId")
    private String companyId;
	
	@JsonProperty("RiskId")
    private String riskId;


}
