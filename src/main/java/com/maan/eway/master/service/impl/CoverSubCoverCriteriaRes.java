package com.maan.eway.master.service.impl;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CoverSubCoverCriteriaRes {

    @JsonProperty("SUB_COVER_ID")
    private Integer subCoverId;

    @JsonProperty("COVER_ID")
    private Integer coverId;
    
    @JsonProperty("SECTION_ID")
    private Integer   sectionId;
   
    @JsonProperty("PRODUCT_ID")
    private Integer    productId;
    
    @JsonProperty("COMPANY_ID")
    private String     companyId;

    @JsonFormat(pattern = "dd/MM/yyyy")
    @JsonProperty("EFFECTIVE_DATE_START")
    private Date       effectiveDateStart ;

    @JsonFormat(pattern = "dd/MM/yyyy")
    @JsonProperty("EFFECTIVE_DATE_END")
    private Date       effectiveDateEnd ;
    
    @JsonProperty("SUB_COVER_NAME")
    private String subCoverName;
    
    @JsonProperty("SUB_COVER_DESC")
    private String subCoverDesc;

    @JsonFormat(pattern = "dd/MM/yyyy")
    @JsonProperty("ENTRY_DATE")
    private Date       entryDate ;

    @JsonProperty("STATUS")
    private String     status ;

    @JsonProperty("CORE_APP_CODE")
    private String     coreAppCode ;

    @JsonProperty("AMEND_ID")
    private Integer     amendId ;

    @JsonProperty("REMARKS")
    private String     remarks ;
    
    @JsonProperty("SECTION_NAME")
    private String   sectionName;
    
   	@JsonProperty("COVER_NAME")
    private String   coverName;
   	
  	@JsonProperty("CREATED_BY")
    private String   createdBy;
  	
  	@JsonProperty("TIRA_CODE")
    private String   tiraCode;

}
