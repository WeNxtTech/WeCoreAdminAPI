package com.maan.eway.master.res;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PremiaBrokerList {


	@JsonProperty("BrokerCode")
	private String brokerCode;

	@JsonProperty("BrokerName")
	private String brokerName;
	
	@JsonProperty("BrokerAddress")
	private String brokerAddress;
	
	@JsonProperty("BrokerCivilId")
	private String brokerCivilId;
	
	@JsonProperty("VatApplicable")
	private String vatApplicable;
	
	@JsonProperty("TaxApplicable")
	private String taxApplicable;
	
	@JsonProperty("VatReNo")
	private String vatReNo;
	

	@JsonProperty("BrokerPhone")
	private String brokerPhone;
	
	@JsonProperty("BrokerCity")
	private String brokerCity;
	
	@JsonProperty("BrokerFax")
	private String brokerFax;
	
	@JsonProperty("BrokerEmail")
	private String brokerEmail;
	
	@JsonProperty("BrokerMobile")
	private String brokerMobile;

	
}
