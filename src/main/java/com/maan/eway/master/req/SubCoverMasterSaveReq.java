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
    private Integer subCoverId;
    
    @JsonProperty("InsuranceId")
    private String companyId;
    
    @JsonProperty("SubCoverName")
    private String subCoverName;
    

    @JsonProperty("SubCoverDesc")
    private String subCoverDesc;
    
    @JsonFormat(pattern="dd/MM/yyyy")
    @JsonProperty("EffectiveDateStart")
    private Date effectiveDateStart;
    
    @JsonFormat(pattern="dd/MM/yyyy")
    @JsonProperty("EffectiveDateEnd")
    private Date effectiveDateEnd;
    
    @JsonProperty("CoreAppCode")
    private String coreAppCode;
    

    @JsonProperty("Remarks")
    private String remarks;
}
