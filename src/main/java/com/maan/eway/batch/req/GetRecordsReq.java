package com.maan.eway.batch.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GetRecordsReq {
	
	@JsonProperty("CompanyId")
    private String companyId;
	
	@JsonProperty("ProductId")
    private String productId;
	
	@JsonProperty("RequestRefNo")
    private String requestRefNo;
	
	@JsonProperty("QuoteNo")
    private String quoteNo;
	
	@JsonProperty("RiskId")
    private String riskId;
	
	@JsonProperty("Status")
    private String status;
	
}
