package com.maan.eway.master.res;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import jakarta.persistence.Column;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.eway.master.req.ProrataMultiInsertReq;
import com.maan.eway.master.req.TaxMultiInsertReq;

import lombok.Data;

@Data
public class CompanyProrataGetRes implements Serializable {

    private static final long serialVersionUID = 1L;

   
    @JsonProperty("ProductId")
    private String productid ;

    
    @JsonProperty("InsuranceId")
    private String     insuranceid;
    
    
    @JsonFormat(pattern = "dd/MM/yyyy")
    @JsonProperty("EffectiveDateStart")
    private Date       effectiveDateStart ;

    
    @JsonFormat(pattern = "dd/MM/yyyy")
    @JsonProperty("EffectiveDateEnd")
    private Date       effectiveDateEnd ;
    
    @JsonProperty("CreatedBy")
    private String   createdBy ;
    
    @JsonProperty("PolicyTypeId")
	 private String   policyTypeId;
    
    @JsonProperty("Status")
    private String     status ; 
    
	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("EntryDate")
	private Date entryDate;

	@JsonProperty("AmendId")
    private String    amendId;

	 @JsonProperty("ProrataDetails")
	 private List<ProrataMultiInsertReq> prorataMultiInsertReq ; 

}
