package com.maan.eway.master.req;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class CoverSubCoverMasterSaveReq {


    @JsonProperty("SubCoverId")
    private String    subCoverId    ;

    @JsonProperty("CoverId")
    private String    coverId    ;
    
    @JsonProperty("ProductId")
    private String    productId    ;
  
	@JsonProperty("SectionId")
    private String    sectionId    ;

	@JsonProperty("InsuranceId")
	private String companyId;

	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("EffectiveDate")
	private Date effectiveDate;

	@JsonProperty("SubCoverName")
	private String subCoverName;
	
	@JsonProperty("CoverName")
	private String CoverName;

	@JsonProperty("SubCoverDesc")
	private String subCoverDesc;
	
	@JsonProperty("Status")
	private String status;

	@JsonProperty("CoreAppCode")
	private String coreAppCode;

	@JsonProperty("Remarks")
	private String remarks;
	
	@JsonProperty("CreatedBy")
	private String createdBy;
	
	@JsonProperty("TiraCode")
	private String tiraCode;
}
