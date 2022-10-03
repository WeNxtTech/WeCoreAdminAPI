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
		@JsonProperty("CoverId")
		private Integer coverId ;

	    @JsonProperty("SectionId")
	    private Integer sectionId ;
	 
	    @JsonProperty("ProductId")
	    private Integer   productId ;

	    @JsonProperty("InsuranceId")
	    private String     companyId ;

	    @JsonProperty("EffectiveDateStart")
	    private Date       effectiveDateStart ;

	    @JsonProperty("EffectiveDateEnd")
	    private Date       effectiveDateEnd ;

	    @JsonProperty("CoverName")
	    private String     coverName ;

	    @JsonProperty("EntryDate")
	    private Date       entryDate ;

	    
	    @JsonProperty("Status")
	    private String     status ;
	    
	    @JsonProperty("CoreAppCode")
	    private String     coreAppCode ;

	    @JsonProperty("AmendId")
	    private Integer amendId ;

	    @JsonProperty("Remarks")
	    private String     remarks ;
	    
	    
	  
	    @JsonProperty("CoverDesc")
	    private String     coverDesc ;

	  @JsonProperty("ToolTip")
	    private String   toolTip;
	    
	    @JsonProperty("TiraCode")
	    private String   tiraCode;

	    @JsonProperty("CreatedBy")
	    private String  createdBy;
	    
	    @JsonProperty("SectionName")
	    private String  sectionName;

}
