package com.maan.eway.master.req;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class CompanyTaxSetupSaveReq implements Serializable {

    private static final long serialVersionUID = 1L;

    
    @JsonProperty("TaxId")
    private String     taxId ;

    
    @JsonProperty("ProductId")
    private String productId ;

    
    @JsonProperty("InsuranceId")
    private String     companyId;
    
    
    @JsonFormat(pattern = "dd/MM/yyyy")
    @JsonProperty("EffectiveDateStart")
    private Date       effectiveDateStart ;

    
    @JsonFormat(pattern = "dd/MM/yyyy")
    @JsonProperty("EffectiveDateEnd")
    private Date       effectiveDateEnd ;
    
    //--- ENTITY DATA FIELDS 
    @JsonProperty("TaxName")
    private String     taxName ;
    
    @JsonProperty("TaxDesc")
    private String     taxDesc ;

    @JsonProperty("CalcType")
    private String     calcType ;

    
    @JsonProperty("Value")
    private String    value ;

    @JsonProperty("CreatedBy")
    private String   createdBy ;
    
    @JsonProperty("Status")
    private String     status ; 
    
    @JsonProperty("Remarks")
    private String remarks; 

}
