package com.maan.eway.master.res;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class DocumentMasterGetRes implements Serializable {

    private static final long serialVersionUID = 1L;
    @JsonProperty("DocumentId")
	private String documentId;

    @JsonProperty("DocumentName")
	private String documentName;

	@JsonProperty("DocumentDesc")
	private String documentDesc;

	@JsonProperty("DocApplicableId")
	private String docApplicableId;

	@JsonProperty("DocApplicable")
	private String docApplicable;

	@JsonProperty("MandatoryStatus")
	private String mandatoryStatus;

	@JsonProperty("Remarks")
	private String remarks;

	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("EffectiveDateStart")
	private Date effectiveDateStart;

	@JsonProperty("RegulatoryCode")
	private String regulatoryCode;

	@JsonProperty("CreatedBy")
	private String createdBy;

	@JsonProperty("Status")
	private String status;

	@JsonFormat(pattern="dd/MM/yyyy")
	@JsonProperty("EffectiveDateEnd")
	private Date effectiveDateEnd;
	
	@JsonProperty("AmendId")
	private Integer amendId;
    
	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("EntryDate")
    private Date       entryDate    ;
	
	@JsonProperty("CoreAppCode")
	private String coreAppCode;
	
	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("UpdatedDate")
    private Date       updatedDate    ;
	

	@JsonProperty("UpdatedBy")
	private String updatedBy;


}
