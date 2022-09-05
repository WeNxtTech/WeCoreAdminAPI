package com.maan.eway.res;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GetQuoteDetailsRes {
	
	@JsonProperty("BranchCode")
	private String branchCode;
	
	@JsonProperty("ProductId")
	private String productId;
	
	@JsonProperty("CreatedBy")
	private String createdBy;
	
	@JsonProperty("CountryCode")
	private String countryCode;
	
	@JsonProperty("CustomerId")
	private String customerId ;
	
	@JsonProperty("InsuranceId")
	private String insuranceId ;
	
	@JsonProperty("BrokerCode")
	private String brokerCode ;
	
	@JsonProperty("QuoteNo")
	private String quoteNo ;
	
	@JsonProperty("Status")
	private String status ;
	
	@JsonProperty("PolicyNo")
	private String policyNo ;
	
	@JsonProperty("OriginalPolicyNo")
	private String originalPolicyNo ;

	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("EntryDate")
	private Date entryDate;
	
	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("ExpiryDate")
	private Date expiryDate ;
	
	@JsonProperty("Currency")
	private String currency ;
	
	@JsonProperty("Remarks")
	private String  remarks ;
	
	@JsonProperty("Premium")
	private String premium ;
	
/*	@JsonProperty("ExcessSign")
	private String  
	
	@JsonProperty("ExcessPremium")
	@JsonProperty("DiscountPremium")
	@JsonProperty("PolicyFee")
	@JsonProperty("OtherFee")
	@JsonProperty("VatPercent")
	@JsonProperty("VatPremium")
	@JsonProperty("OverallPremium")
	@JsonProperty("CommissionPercentage")
	@JsonProperty("VatCommission")
	@JsonProperty("Commission")
	@JsonProperty("CalcPremium")
	@JsonProperty("AdminRemarks")
    @JsonProperty("ReferralDescription")


	@JsonProperty("")
	private String lapsed_remarks;
	
	@JsonProperty("")
	private String remarks;
	
	@JsonProperty("")
	private String lapsed_date;
	
	@JsonProperty("")
	private String application_no;
	
	@JsonProperty("")
	private String reject_desc;
	
	@JsonProperty("")
	private String overall_premium;
	
	@JsonProperty("")
	private String excess_premium;
	
	@JsonProperty("")
	private String debit_note_no;
	
	@JsonProperty("")
	private String credit_note_no;
	
	@JsonProperty("")
	private String receipt_no;
	
	@JsonProperty("")
	private String credit_no;
	
	@JsonProperty("")
	private String commision;
	
	@JsonProperty("")
	private String policy_start_date;
	
	@JsonProperty("")
	private String policy_end_date;
	
	@JsonProperty("")
	private String org_status;
	
	@JsonProperty("")
	private String oldreferenceno;
	
	@JsonProperty("")
	private String modeofpayment;
	
	/*
	private String aaa_cardno;
	private String rsa_cardno;
	private String integstatus;
	private String integerrordesc;
	private String mobileno;
	*/
}
