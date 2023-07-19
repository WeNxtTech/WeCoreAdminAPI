package com.maan.eway.batch.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GetUploadTransactionReq {
	
	@JsonProperty("CompanyId")
    private String companyId;
	
	@JsonProperty("ProductId")
    private String productId;
	
	@JsonProperty("RequestRefNo")
    private String requestRefNo;
	

}
