package com.maan.eway.master.res;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class SubCoverMasterGetRes implements Serializable {

    private static final long serialVersionUID = 1L;
  
    @JsonProperty("SubCoverId")
    private Integer subCoverId;
    
    @JsonProperty("InsuranceId")
    private String companyId;
    
    @JsonProperty("SubCoverName")
    private String subCoverName;
    

    @JsonProperty("SubCoverDesc")
    private String subCoverDesc;
    
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

	@JsonProperty("CoreAppCode")
	private String coreAppCode;

	@JsonProperty("AmendId")
	private Integer amendId;

	@JsonProperty("Remarks")
	private String remarks;

	
}
