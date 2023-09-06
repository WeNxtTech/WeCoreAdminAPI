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
public class PremiaCustomerDetailsRes {

	@JsonProperty("OrderId")
	private Long orderId;
	
	@JsonProperty("Code")
	private String code;

	@JsonProperty("Name")
	private String name;
	
	@JsonProperty("LoginId")
	private String loginId;
	
	@JsonProperty("BrokerBrachCode")
	private String brokerBranchCode;
	
	@JsonProperty("BrokerBrachName")
	private String brokerBranchName;
	
	@JsonProperty("CustomerCode")
	private String customerCode;
	
	@JsonProperty("CustomerName")
	private String customerName;
	
//	@JsonProperty("CustomerTypeDesc")
//	private String customertypedesc;
//	@JsonProperty("Gender")
//	private String gender;
//	@JsonProperty("CustomerCivilId")
//	private String custcivilid;
//	@JsonProperty("DateOfBirth")
//	private String dateofbirth;
//	@JsonProperty("MaritalStatus")
//	private String maritalstatus;
//	@JsonProperty("PlaceOfBirth")
//	private String placeofbirth;
//	@JsonProperty("Nationality")
//	private String nationality;
//	@JsonProperty("Occupation")
//	private String occupation;
//	@JsonProperty("PositionHeld")
//	private String positionheld;
//	@JsonProperty("ResAddress1")
//	private String resaddress1;
//	@JsonProperty("ResAddress2")
//	private String resaddress2;
//	@JsonProperty("ResAddress3")
//	private String resaddress3;
//	@JsonProperty("ResCountry")
//	private String rescountry;
//	@JsonProperty("ResState")
//	private String resstate;
//	@JsonProperty("ResCity")
//	private String rescity;
//	@JsonProperty("ResPhone")
//	private String resphone;
//	@JsonProperty("ResFax")
//	private String resfax;
//	@JsonProperty("EmployersName")
//	private String employersname;
//	@JsonProperty("OfficeAddress1")
//	private String officeaddress1;
//	@JsonProperty("OfficeAddress2")
//	private String officeaddress2;
//	@JsonProperty("OfficeAddress3")
//	private String officeaddress3;
//	@JsonProperty("OfficeCountry")
//	private String officecountry;
//	@JsonProperty("OfficeState")
//	private String officestate;
//	@JsonProperty("OfficeCity")
//	private String officecity;
//	@JsonProperty("OfficePhone")
//	private String officephone;
//	@JsonProperty("OfficeEmailId")
//	private String officeemail;
//	@JsonProperty("EffectiveFrom")
//	private String effectivefrom;
//	@JsonProperty("EffectiveTo")
//	private String effectiveto;
//	
//	
	
}
