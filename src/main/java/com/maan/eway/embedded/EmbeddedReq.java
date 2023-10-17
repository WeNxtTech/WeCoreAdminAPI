package com.maan.eway.embedded;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EmbeddedReq {
	
	
	@JsonProperty("LoginId")
	private String loginId;
	
	@JsonProperty("CompanyId")
	private String companyId;
	
	@JsonProperty("ProductId")
	private String productId;
	
	@JsonProperty("StartDate")
	private String startDate;
	
	@JsonProperty("EndDate")
	private String endDate;
	
	@JsonProperty("SearchType")
	private String searchType;
	
	@JsonProperty("SearchValue")
	private String searchValue;

}
