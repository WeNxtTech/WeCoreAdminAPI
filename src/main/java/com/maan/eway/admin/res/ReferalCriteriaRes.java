package com.maan.eway.admin.res;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReferalCriteriaRes {

	@JsonProperty("ReferalId")
	private Integer referalId ;
	
	@JsonProperty("ReferalName")
	private String referalName ;
	
	@JsonProperty("CompanyId")
	private String companyId ;
	
	@JsonProperty("CompanyName")
	private String companyName ;
	
	@JsonProperty("ReferalDesc")
	private String referalDesc ;
}
