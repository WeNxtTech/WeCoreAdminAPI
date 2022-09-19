package com.maan.eway.master.req;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class DocumentMasterSaveReq {

	@JsonProperty("DocumentId")
	private String documentId;
	
	@JsonProperty("DocumentDesc")
	private String documentDesc;
	
	@JsonProperty("DocApplicableId")
	private Integer docApplicableId;
	

	@JsonProperty("DocApplicable")
	private String docApplicable;
	
	@JsonProperty("InsuranceId")
	private String companyId;
	
	@JsonProperty("MandatoryStatus")
	private String mandatoryStatus;
	
	@JsonProperty("Remarks")
	private String remarks;

	@JsonProperty("DisplayOrder")
	private Integer displayOrder;
	
	@JsonFormat(pattern="dd/MM/yyyy")
	@JsonProperty("EffectiveDateStart")
	private Date effectiveDateStart;
	

	@JsonFormat(pattern="dd/MM/yyyy")
	@JsonProperty("EffectiveDateEnd")
	private Date effectiveDateEnd;
	
	@JsonProperty("CoreAppCode")
	private String coreAppCode;
	
	@JsonProperty("AmendId")
	private Integer amendId;
}
