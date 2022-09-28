package com.maan.eway.master.req;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class SectionCoverMasterSaveReq {


    @JsonProperty("CoverId")
    private String    coverId    ;
    
    @JsonProperty("ProductId")
    private String    productId    ;
  
	@JsonProperty("SectionId")
    private String    sectionId    ;

	@JsonProperty("InsuranceId")
	private String companyId;

	@JsonProperty("CoverName")
	private String coverName;
	
	@JsonProperty("CoverDesc")
	private String coverDesc;
	
	
	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("EffectiveDateStart")
	private Date effectiveDateStart;

	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("EffectiveDateEnd")
	private Date effectiveDateEnd;

	@JsonProperty("Status")
	private String status;

	@JsonProperty("CoreAppCode")
	private String coreAppCode;

	@JsonProperty("AmendId")
	private String amendId;

	@JsonProperty("Remarks")
	private String remarks;
	
	@JsonProperty("ToolTip")
	private String toolTip;
	
	@JsonProperty("Excess")
	private String excess;
	
	@JsonProperty("CalcYn")
	private String calcYn;

	@JsonProperty("CoverageType")
	private String coverageType;
	
	@JsonProperty("UploadOption")
	private String uploadOption;
	
	@JsonProperty("CoverageLimit")
	private String coverageLimit;
	
	@JsonProperty("CalcType")
	private String calcType;
	
	@JsonProperty("CalcStatus")
	private String calcStatus;
	
	@JsonProperty("TiraCode")
	private String tiraCode;
	
	@JsonProperty("CreatedBy")
	private String createdBy;
	
	
}
