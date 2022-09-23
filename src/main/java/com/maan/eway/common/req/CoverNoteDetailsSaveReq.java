package com.maan.eway.common.req;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class CoverNoteDetailsSaveReq {

	@JsonProperty("RequestId")
    private String     requestId    ;
	@JsonProperty("CompanyCode")
    private String     companyCode  ;
	@JsonProperty("SystemCode")
    private String     systemCode   ;
	@JsonProperty("CallBackUrl")
    private String     callBackUrl  ;
	@JsonProperty("InsuranceCompanyCode")
    private String     insuranceCompanyCode ;
	@JsonProperty("TranCompanyCode")
    private String     tranCompanyCode ;
	@JsonProperty("CoverNoteTypeId")
    private String    coverNoteTypeId ;
	@JsonProperty("CoverNoteTypeDesc")
    private String     coverNoteTypeDesc ;
	@JsonProperty("CoverNoteNumber")
    private String     coverNoteNumber ;
	@JsonProperty("PrevConverNoteReferenceNumber")
    private String     prevConverNoteReferenceNumber ;
	@JsonProperty("SalePointCode")
    private String     salePointCode ;
	
	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("CoverNoteStartDate")
    private Date       coverNoteStartDate ;
	
	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("CoverNoteEndDate")
    private Date       coverNoteEndDate ;
	
	@JsonProperty("CoverNoteDesc")
    private String     coverNoteDesc ;
	@JsonProperty("Operativeclause")
    private String     operativeClause ;
	@JsonProperty("Paymentmode")
    private String    paymentMode  ;
	
	@JsonProperty("Currenycode")
    private String     currenyCode  ;
	@JsonProperty("Exchangerate")
    private String     exchangeRate ;
	@JsonProperty("Totalpremiumexcludingtax")
    private String     totalPremiumExcludingTax ;
	@JsonProperty("Totalpremiumincludingtax")
    private String     totalPremiumIncludingTax ;
	@JsonProperty("Commissionpaid")
    private String     commissionPaid ;
	@JsonProperty("Commissionrate")
    private String     commissionRate ;
	@JsonProperty("Officername")
    private String     officerName  ;
	@JsonProperty("Officertitle")
    private String     officerTitle ;
	@JsonProperty("Productcode")
    private String     productCode  ;
	@JsonProperty("Endorsementtype")
    private String    endorsementType ;
	@JsonProperty("Endorsementreason")
    private String     endorsementReason ;
	@JsonProperty("Endoresementpremiumearned")
    private String     endoresementPremiumEarned ;
}
