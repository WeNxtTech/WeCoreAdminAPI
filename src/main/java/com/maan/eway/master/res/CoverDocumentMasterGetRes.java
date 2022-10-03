package com.maan.eway.master.res;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class CoverDocumentMasterGetRes implements Serializable {

    private static final long serialVersionUID = 1L;
  
    @JsonProperty("DocumentId")
	private String documentId;
	
	@JsonProperty("DocumentDesc")
	private String documentDesc;
	
	@JsonProperty("DocApplicableId")
	private String docApplicableId;
	

	@JsonProperty("DocApplicable")
	private String docApplicable;
	
	@JsonProperty("InsuranceId")
	private String companyId;
	
	@JsonProperty("ProductId")
	private String productId;
	

	@JsonProperty("SectionId")
	private String sectionId;
	
	@JsonProperty("MandatoryStatus")
	private String mandatoryStatus;
	
	@JsonProperty("Remarks")
	private String remarks;

	
	@JsonFormat(pattern="dd/MM/yyyy")
	@JsonProperty("EffectiveDateStart")
	private Date effectiveDateStart;
	

	@JsonFormat(pattern="dd/MM/yyyy")
	@JsonProperty("EffectiveDateEnd")
	private Date effectiveDateEnd;
	
	@JsonProperty("CoreAppCode")
	private String coreAppCode;
	
	@JsonProperty("AmendId")
	private String amendId;
    
	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("EntryDate")
    private Date       entryDate    ;
	
	@JsonProperty("Status")
    private String     status       ;

	@JsonProperty("TiraCode")
    private String   tiraCode       ;

	@JsonProperty("CreatedBy")
    private String   createdBy       ;

	
}
