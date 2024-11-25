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
public class PremiaCustomerList {


	@JsonProperty("CustomerCode")
	private String customerCode;

	@JsonProperty("CustomerName")
	private String customerName;
	
	@JsonProperty("CustomerAddress")
	private String customerAddress;
	
	@JsonProperty("CustomerCivilId")
	private String customerCivilId;
	
	@JsonProperty("CustomerEmail")
	private String customerEmail;
	
	@JsonProperty("TaxApplicable")
	private String taxApplicable;
	
	@JsonProperty("CustomerPhone")
	private String customerPhone;
	
	@JsonProperty("CustomerCity")
	private String customerCity;
	
	@JsonProperty("VatReNo")
	private String vatReNo;
	
	@JsonProperty("VatApplicable")
	private String vatApplicable;
	
}
