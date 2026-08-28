package com.maan.eway.uploaddoc.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class UploadDocAcExecutiveMasterGetReq {

    @JsonProperty("AcExecutiveId")
    private Integer acExecutiveId;

    @JsonProperty("BranchCode")
    private String branchCode;

    @JsonProperty("CompanyId")
    private String companyId;

    @JsonProperty("BankCode")
    private String bankCode;
}