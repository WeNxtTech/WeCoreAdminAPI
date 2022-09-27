package com.maan.eway.master.req;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class SubCoverMasterSaveReq implements Serializable {

    private static final long serialVersionUID = 1L;

    
    @JsonProperty("SubCoverId")
    private String subCoverId;
    
    @JsonProperty("SubCoverName")
    private String subCoverName;
    

	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("EffectiveDateStart")
	private Date effectiveDateStart;
	
	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("EffectiveDateEnd")
	private Date effectiveDateEnd;
	
    @JsonProperty("SubCoverDesc")
    private String subCoverDesc;
    
    @JsonProperty("CoreAppCode")
    private String coreAppCode;
    
    @JsonProperty("TiraCode")
    private String tiraCode;
    
    @JsonProperty("CreatedBy")
    private String createdBy;
    
    @JsonProperty("Status")
    private String status;

    @JsonProperty("Remarks")
    private String remarks;
}
