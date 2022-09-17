package com.maan.eway.master.res;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class BrokerCompanyRes {

	 @JsonProperty("InsuranceId")
	    private String     companyId     ;
		
		@JsonProperty("CompanyName")
	    private String     companyName ;
		
		@JsonProperty("CompanyAddress")
	    private String     companyAddress ;
		
		@JsonProperty("CompanyEmail")
	    private String     companyEmail ;
		
		@JsonProperty("CompanyPhone")
	    private String     companyPhone ;
		
		@JsonProperty("CompanyLogo")
	    private String     companyLogo ;
		
		@JsonProperty("Regards")
	    private String     regards ;
		
		@JsonProperty("CoreAppCode")
		private String coreAppCode;

		@JsonProperty("Remarks")
		private String remarks;
		
		@JsonProperty("BrokerYn")
		private String brokerYn;
		
}
