package com.maan.eway.admin.res;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class UserPersonalInfoGetRes {

	// Personal Details
	@JsonProperty("UserName")
    private String     userName     ;
	@JsonProperty("UserMobile")
    private String     userMobile   ;
	@JsonProperty("UserMail")
    private String     userMail     ;

	@JsonProperty("CompanyName")
    private String    companyName ;
    
    @JsonProperty("Address1")
    private String    address1 ;
    
    @JsonProperty("Address2")
    private String    address2 ;
    
    @JsonProperty("Address3")
    private String    address3 ;
    
    @JsonProperty("City")
    private String    city ;
    
    @JsonProperty("State")
    private String    state ;
    
    @JsonProperty("Country")
    private String    country ;
    
    @JsonProperty("Pobox")
    private String    pobox ;
    
    @JsonProperty("Fax")
    private String    fax ;
    
    @JsonProperty("Emirate")
    private String    emirate;
    
    @JsonProperty("Remarks")
    private String    remarks ;
    
    @JsonProperty("CustomerId")
    private String    customerId ;
    
    @JsonProperty("MissippiId")
    private String    missippiId ;
    
    @JsonProperty("ApprovedPreparedBy")
    private String    approvedPreparedBy ;
    
    @JsonProperty("RsaBrokerCode")
    private String    rsaBrokerCode ;
    
    @JsonProperty("AcExecutiveId")
    private String    acExecutiveId ;
    
    @JsonProperty("CustConfirmYn")
    private String    custConfirmYn ;
    
    @JsonProperty("CommissionVatYn")
    private String    commissionVatYn ;
    
    @JsonProperty("VatRegNo")
    private String    vatRegNo ;
    
    @JsonProperty("CheckerYn")
    private String    checkerYn ;
    
    @JsonProperty("MakerYn")
    private String    makerYn ;
}
