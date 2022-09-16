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
    
    @JsonProperty("InsuranceId")
    private String companyId;
    
    @JsonProperty("SubCoverName")
    private String subCoverName;
    

    @JsonProperty("SubCoverDesc")
    private String subCoverDesc;
    
    @JsonFormat(pattern="dd/MM/yyyy")
    @JsonProperty("EffectiveDate")
    private Date effectiveDate;
    
    @JsonProperty("CoreAppCode")
    private String coreAppCode;
    
    @JsonProperty("Status")
    private String status;

    @JsonProperty("Remarks")
    private String remarks;
}
