package com.maan.eway.master.req;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class FactorRateGetRes {

	@JsonProperty("FactorTypeId")
    private String factorTypeId ;

    @JsonProperty("InsuranceId")
    private String    companyId ;
  
    @JsonProperty("ProductId")
    private String    productId ;
  
    @JsonProperty("BranchCode")
    private String    branchCode ;
    
    @JsonProperty("AgencyCode")
    private String    agencyCode ;
    
    @JsonProperty("CoverId")
    private String    coverId ;
    
    @JsonProperty("SubCoverId")
    private String    subCoverId ;
    
    @JsonFormat(pattern = "dd/MM/yyyy")
    @JsonProperty("EntryDate")
    private Date       entryDate;

    @JsonFormat(pattern = "dd/MM/yyyy")
    @JsonProperty("EffectiveDateStart")
    private Date       effectiveDateStart ;

    @JsonFormat(pattern = "dd/MM/yyyy")
    @JsonProperty("EffectiveDateEnd")
    private Date       effectiveDateEnd ;


    @JsonFormat(pattern = "dd/MM/yyyy")
    @JsonProperty("UpdatedDate")
    private Date   updatedDate;

    
    @JsonProperty("Status")
    private String     status ;

    @JsonProperty("CreatedBy")
    private String     createdBy ;
    
    @JsonProperty("UpdatedBy")
    private String     updatedBy ;
    
    @JsonProperty("CoreAppCode")
    private String   coreAppCode ;
    
    @JsonProperty("RegulatoryCode")
    private String   regulatoryCode ;
    
    
    @JsonProperty("FactorParams")
    private List<FactorParamsInsert>     factorParams;
}
