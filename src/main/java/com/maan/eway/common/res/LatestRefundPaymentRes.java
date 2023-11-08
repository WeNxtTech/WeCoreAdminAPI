package com.maan.eway.common.res;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class LatestRefundPaymentRes {

//	@JsonProperty("PolicyNo")
//    private String     policyNo;

	@JsonProperty("QuoteNo")
    private String     quoteNo ;
	
	@JsonProperty("PaymentId")
    private String     paymentId  ;
	
	@JsonProperty("MerchantReference")
    private String     merchantReference  ;
	
	@JsonProperty("PaymentTypedesc")
    private String     paymentTypedesc ;
	
	@JsonProperty("PaymentStatus")
    private String     paymentStatus ;
	
	
	@JsonProperty("TinyUrl")
	private String shorternUrl;
	
	@JsonProperty("CustomerEmail")
	private String customerEmail;
	
	@JsonProperty("CustomerName")
	private String customerName;
	
	@JsonProperty("ReqBillToSurname")
	private String reqBillToSurname;
	
	@JsonProperty("ReqBillToAddressCity")
	private String reqBillToAddressCity;
	
	@JsonProperty("ReqBillToAddrPostalCode")
	private String reqBillToAddrPostalCode;
	
	@JsonProperty("ReqBillToPhone")
	private String reqBillToPhone;
	
	@JsonProperty("ReqBillToEmail")
	private String reqBillToEmail;
	
	@JsonProperty("CustomerId")
	private String customerId;
	
	@JsonProperty("BankName")
	private String bankName;
	
	@JsonProperty("BranchCode")
	private String branchCode;
	
	@JsonProperty("BranchName")
	private String branchName;
	
	@JsonFormat(pattern = "dd/MM/yyyy hh:mm:ss")
	@JsonProperty("EntryDate")
    private Date     entryDate  ;
	
	@JsonProperty("PremiumFc")
	private String premiumFc;
	
	@JsonProperty("PremiumLc")
	private String premiumLc;
	
	@JsonProperty("CurrencyId")
	private String currencyId;
	
	@JsonProperty("ExchangeRate")
	private String exchangeRate;
	
	@JsonProperty("Payments")
	private String       payments ;
	
//	@JsonProperty("PayeeName")
//	private String payeeName;
	
	@JsonProperty("ReqBillToCountry")
	private String reqBillToCountry;
	
	
	@JsonFormat(pattern = "dd/MM/yyyy hh:mm:ss")
	@JsonProperty("UpdatedDate")
    private Date     updatedDate  ;
	
//	@JsonProperty("ChequeNo")
//	private String chequeNo;
//	
//	@JsonFormat(pattern = "dd/MM/yyyy")
//	@JsonProperty("ChequeDate")
//	private Date chequeDate;
//	
//	
	@JsonProperty("AccountNumber")
	private String       accountNumber ;
	
	@JsonProperty("IbanNumber")
	private String       ibanNumber ;
//	
//	@JsonProperty("MicrNo")
//	private String micrNo;
}
