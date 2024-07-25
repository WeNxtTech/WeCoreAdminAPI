package com.maan.eway.master.res;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Column;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class InsuranceCompanyMasterRes implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("InsuranceId")
    private String     companyId     ;
	
    @JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("EffectiveDateStart")
	private Date effectiveDateStart;
	
	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("EffectiveDateEnd")
	private Date effectiveDateEnd;

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
	
	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("EntryDate")
    private Date       entryDate;
	
	@JsonProperty("Status")
    private String     status;

	@JsonProperty("CoreAppCode")
	private String coreAppCode;

	@JsonProperty("Remarks")
	private String remarks;
	
	@JsonProperty("BrokerYn")
	private String brokerYn;
	
	@JsonProperty("CreatedBy")
	private String createdBy;
	
	@JsonProperty("RegulatoryCode")
	private String regulatoryCode;
	
	@JsonProperty("AmendId")
	private String amendId;
	
	@JsonProperty("CurrencyId")
	private String currencyId;
	
	@JsonProperty("UpdatedBy")
	private String updatedBy;
	

	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("UpdatedDate")
    private Date   updatedDate;
	
	@JsonProperty("CountryId")
	private String countryId;
	
	@JsonProperty("VrnNumber")
	private String vrnNumber;
	
	@JsonProperty("TinNumber")
	private String tinNumber;
	
	@JsonProperty("PoBox")
	private String poBox;
	
	
	@JsonProperty("Character")
	private String character;
	
	@JsonProperty("NumericDigitsStart")
	private String numericdigitsStart;
	
	@JsonProperty("NumericDigitsEnd")
	private String numericdigitsEnd;
	
	
	@JsonProperty("Symbols")
	private String symbols;
	
	@JsonProperty("TotalLengthMin")
	private String totallengthmin;
	
	@JsonProperty("TotalLengthMax")
	private String TotalLengthMax;
	
	  @Column(name="PATTERN_STATUS", length=1)
	    private String     patternstatus ;
	
}
