package com.maan.eway.uploaddoc.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class UploadDocDeductibleMasterGetReq {

    @JsonProperty("DeductId")
    private Integer deductId;

    @JsonProperty("CompanyId")
    private String companyId;

    @JsonProperty("ProductId")
    private Integer productId;

    @JsonProperty("SectionId")
    private Integer sectionId;

    @JsonProperty("BranchCode")
    private String branchCode;
}