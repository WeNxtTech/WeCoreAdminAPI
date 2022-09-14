package com.maan.eway.admin.req;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.eway.admin.res.BrokerProductsGetRes;

import lombok.Data;

@Data
public class BrokerProductCompaniesRes {

	@JsonProperty("InsuranceId")
	private String  insuranceId;  
	@JsonProperty("CompanyName")
	private String  companyName;   
	
	
	@JsonProperty("AttachedProducts")
	private List<BrokerProductsGetRes> attachedProducts ;
}
