package com.maan.eway.admin.res;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BrokerProductsGetRes {

	@JsonProperty("AttachedCompanies")
	private List<BrokerCompanyGetRes> attachedCompanies ;
	
	

}
