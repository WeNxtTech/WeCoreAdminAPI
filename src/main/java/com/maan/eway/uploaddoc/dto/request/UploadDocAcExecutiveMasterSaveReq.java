package com.maan.eway.uploaddoc.dto.request;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class UploadDocAcExecutiveMasterSaveReq {

    @JsonProperty("AcExecutiveId")
    private Integer acExecutiveId;

    @JsonProperty("AcExecutiveName")
    private String acExecutiveName;

    @JsonProperty("OaCode")
    private String oaCode;

    @JsonProperty("BranchCode")
    private String branchCode;

    @JsonProperty("CompanyId")
    private String companyId;

    @JsonProperty("CommissionPercent")
    private Double commissionPercent;

    @JsonProperty("Status")
    private String status;

    @JsonProperty("EffectiveDateStart")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date effectiveDateStart;

    @JsonProperty("EffectiveDateEnd")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date effectiveDateEnd;

    @JsonProperty("BankCode")
    private String bankCode;

    @JsonProperty("CoreAppCode")
    private String coreAppCode;
}