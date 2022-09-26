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

	@JsonProperty("DocumentDescr")
	private String documentDescr;

	@JsonProperty("DocApplicableId")
	private String docApplicableId;

	@JsonProperty("DocApplicable")
	private String docApplicable;

	@JsonProperty("MandatoryStatus")
	private String mandatoryStatus;

	@JsonProperty("Remarks")
	private String remarks;

	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("EffectiveDate")
	private Date effectiveDateStart;

	@JsonProperty("CoreAppCode")
	private String coreAppCode;

	@JsonProperty("TiraCode")
	private String tiraCode;

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
	
	
}
