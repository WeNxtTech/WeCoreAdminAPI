package com.maan.eway.master.req;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ProductDocumentMasterSaveReq {

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
	
	@JsonProperty("ProductId")
	private Integer productId;
	
	@JsonProperty("SectionId")
	private Integer sectionId;
	
	
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
	
	@JsonProperty("Status")
	private String status;
}
