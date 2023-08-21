package com.maan.eway.master.req;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;

import lombok.Data;

@Data
public class ProductTaxGetAllReq implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("Limit")
    private String limit;
    
    @JsonProperty("Offset")
    private String offset;
   
    @JsonProperty("ProductId")
	 private String productId;
    
  	@JsonProperty("CountryId")
     private String     countryId     ;
    
  	 @JsonProperty("InsuranceId")
  	 private String companyId;
  	 
  	 @JsonProperty("BranchCode")
  	 private String branchCode;
}
