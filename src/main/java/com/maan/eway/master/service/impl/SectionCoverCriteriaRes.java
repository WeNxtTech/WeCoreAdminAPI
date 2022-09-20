package com.maan.eway.master.service.impl;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class SectionCoverCriteriaRes {

	  //--- ENTITY PRIMARY KEY 
		@JsonProperty("COVER_ID")
		private Integer    coverId ;

	    @JsonProperty("SECTION_ID")
	    private Integer    sectionId ;
	 
	    @JsonProperty("PRODUCT_ID")
	    private Integer    productId ;

	    @JsonProperty("COMPANY_ID")
	    private String     companyId ;

	    @JsonFormat(pattern = "dd/MM/yyyy")
	    @JsonProperty("EFFECTIVE_DATE_START")
	    private Date       effectiveDateStart ;

	    @JsonFormat(pattern = "dd/MM/yyyy")
	    @JsonProperty("EFFECTIVE_DATE_END")
	    private Date       effectiveDateEnd ;

	    //--- ENTITY DATA FIELDS 
	    @JsonProperty("COVER_NAME")
	    private String     coverName ;

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
	    private String     sectionName ; 
}
