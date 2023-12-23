package com.maan.eway.fileupload;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PremiaFileDownloadRequest {
	
	@JsonProperty("CompanyId")
	private String companyId;
	@JsonProperty("ProductId")
	private String productId;
	@JsonProperty("PolicyNo")
	private String policyNo;
	@JsonProperty("CustomerId")
	private String customerId;
	

	
}
