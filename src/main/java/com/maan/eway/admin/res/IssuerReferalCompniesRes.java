package com.maan.eway.admin.res;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.eway.admin.res.BrokerProductsGetRes;

import lombok.Data;

@Data
public class IssuerReferalCompniesRes {
	
	@JsonProperty("InsuranceId")
	private String  insuranceId;  
	@JsonProperty("CompanyName")
	private String  companyName;  
	
	
	@JsonProperty("AttachedReferals")
	private List<IssuerReferalGetRes> attachedReferals ;

}
