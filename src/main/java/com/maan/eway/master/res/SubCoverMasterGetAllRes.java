package com.maan.eway.master.res;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class SubCoverMasterGetAllRes implements Serializable {

    private static final long serialVersionUID = 1L;
  

    @JsonProperty("CoverId")
    private String coverId;
    
    @JsonProperty("SubCoverId")
    private String subCoverId;
  
    @JsonProperty("SubCoverName")
    private String subCoverName;
    

    @JsonProperty("SubCoverDesc")
    private String subCoverDesc;

	
	@JsonProperty("CoverageTypeDesc")
	private String coverageTypeDesc;
	
	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("EffectiveDateStart")
	private Date effectiveDateStart;
	
	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("EffectiveDateEnd")
	private Date effectiveDateEnd;
   
	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("EntryDate")
    private Date       entryDate    ;
	
	@JsonProperty("Status")
    private String     status       ;

	@JsonProperty("CoverBasedOn")
	private String coverBasedOn;

	@JsonProperty("AmendId")
	private Integer amendId;

	@JsonProperty("Remarks")
	private String remarks;
	
	@JsonProperty("RegulatoryCode")
	private String regulatoryCode;
	    
    @JsonProperty("CreatedBy")
    private String createdBy;
    @JsonProperty("IsTaxExcempted")
	private String isTaxExcempted;
	
	@JsonProperty("TaxAmount")
	private String taxAmount;
	
	@JsonProperty("TaxCode")
	private String taxCode;
	
	@JsonProperty("TaxExcemptionReference")
	private String taxExcemptionReference;
	
	@JsonProperty("TaxExcemptionType")
	private String taxExcemptionType;

	@JsonProperty("ExcessPercent")
	private String excessPercent;
	
	@JsonProperty("ExcessAmount")
	private String excessAmount;
	
	@JsonProperty("ExcessDesc")
	private String excessDesc;
	

	@JsonProperty("ProRataYn")
	private String proRataYn;
	
	@JsonProperty("CodeDescLocal")
	private String codeDescLocal;
}
