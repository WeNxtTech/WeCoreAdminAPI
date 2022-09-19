package com.maan.eway.master.res;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class SubCoverMasterRes {

	@JsonProperty("SubCoverId")
	private Integer subCoverId;
	
	@JsonProperty("CoverId")
	private Integer coverId;
	
	@JsonProperty("InsuranceId")
	private String companyId;
	
	@JsonFormat(pattern="dd/MM/yyyy")
	@JsonProperty("EffectiveDateStart")
	private Date effectiveDateStart;
	
	@JsonFormat(pattern="dd/MM/yyyy")
	@JsonProperty("EffectiveDateEnd")
	private Date effectiveDateEnd;

	@JsonProperty("SubCoverName")
	private String subCoverName;
	
	@JsonProperty("SubCoverDesc")
	private String subCoverDesc;
	
	@JsonFormat(pattern="dd/MM/yyyy")
	@JsonProperty("EntryDate")
	private Date entryDate;
	
	@JsonProperty("Status")
	private String status;

	@JsonProperty("CoreAppCode")
	private String coreAppCode;

	@JsonProperty("AmendId")
	private Integer amendId;

	@JsonProperty("Remarks")
	private String remarks;
	
	@JsonProperty("SectionId")
	private Integer sectionId;
	
	@JsonProperty("ProductId")
	private Integer productId;
	
	@JsonProperty("SectionName")
	private String sectionName;
	
   	@JsonProperty("CoverName")
    private String   coverName;
}
