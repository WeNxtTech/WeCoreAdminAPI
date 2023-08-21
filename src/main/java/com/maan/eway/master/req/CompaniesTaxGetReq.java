package com.maan.eway.master.req;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class CompaniesTaxGetReq implements Serializable {

    private static final long serialVersionUID = 1L;

    
  	@JsonProperty("CountryId")
     private String     countryId     ;
  	
  	@JsonProperty("TaxId")
    private String     taxId     ;
    
  	 @JsonProperty("InsuranceId")
  	 private String companyId;
  	 
  	 @JsonProperty("BranchCode")
  	 private String branchCode;

    
}
