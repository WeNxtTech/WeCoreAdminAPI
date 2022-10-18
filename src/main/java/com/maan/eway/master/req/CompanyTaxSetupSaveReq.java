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

    
 /*   @JsonProperty("TAX_ID")
    private Integer     taxId ;

    
    @JsonProperty("PRODUCT_ID")
    private Integer productId ;

    
    @JsonProperty("COMPANY_ID", nullable=false, length=20)
    private String     companyId;
    
    
    @Temporal(TemporalType.TIMESTAMP)
    @JsonProperty("EFFECTIVE_DATE_START", nullable=false)
    private Date       effectiveDateStart ;

    
    @Temporal(TemporalType.TIMESTAMP)
    @JsonProperty("EFFECTIVE_DATE_END", nullable=false)
    private Date       effectiveDateEnd ;
    
    //--- ENTITY DATA FIELDS 
    @JsonProperty("TAX_NAME", length=100)
    private String     taxName ;
    
    @JsonProperty("TAX_DESC", length=20)
    private String     taxDesc ;

    @JsonProperty("CALC_TYPE")
    private String     calcType ;

    @JsonProperty("CALC_TYPE_DESC", length=100)
    private String     calcTypeDesc ;
    
    @JsonProperty("VALUE", length=20)
    private Double    value ;

    @JsonProperty("CREATED_BY", length=20)
    private String   createdBy ;
    
    @Temporal(TemporalType.TIMESTAMP)
    @JsonProperty("ENTRY_DATE")
    private Date       entryDate ;

    @JsonProperty("STATUS", length=1)
    private String     status ; */

}
