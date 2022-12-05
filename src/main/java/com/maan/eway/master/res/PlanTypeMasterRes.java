package com.maan.eway.master.res;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class PlanTypeMasterRes implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("PlanTypeId")
	private String planTypeId;

	@JsonProperty("PlanTypeDescription")
	private String planTypeDescription;

	@JsonProperty("BranchCode")
	private String branchCode;

	@JsonProperty("InsuranceId")
	private String insuranceId;

	@JsonProperty("SectionId")
	private String sectionId;
	
	@JsonProperty("ProductId")
	private String productId;

	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("EffectiveDateStart")
	private Date effectiveDateStart;

	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("EffectiveDateEnd")
	private Date effectiveDateEnd;
	
	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("EntryDate")
	private Date entryDate;

	@JsonProperty("Status")
	private String status;

	@JsonProperty("AmendId")
	private String amendId;

	@JsonProperty("Remarks")
	private String remarks;

	@JsonProperty("RegulatoryCode")
	private String regulatoryCode;


	@JsonProperty("CoreAppCode")
	private String coreAppCode;
	
	@JsonProperty("CreatedBy")
	private String createdBy;

	@JsonProperty("UpdatedBy")
	private String updatedBy;

	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("UpdatedDate")
	private Date updatedDate;

}
