package com.maan.eway.uploaddoc.dto.response;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class UploadDocDeductibleMasterRes {

    @JsonProperty("DeductMasterId")
    private Long deductMasterId;

    @JsonProperty("DeductId")
    private Integer deductId;

    @JsonProperty("DeductStart")
    private Integer deductStart;

    @JsonProperty("DeductEnd")
    private Integer deductEnd;

    @JsonProperty("Rate")
    private Double rate;

    @JsonProperty("CalcType")
    private String calcType;

    @JsonProperty("AmendId")
    private Integer amendId;

    @JsonProperty("Status")
    private String status;

    @JsonProperty("EntryDate")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date entryDate;

    @JsonProperty("EffectiveDateStart")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date effectiveDateStart;

    @JsonProperty("EffectiveDateEnd")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date effectiveDateEnd;

    @JsonProperty("Remarks")
    private String remarks;

    @JsonProperty("BranchCode")
    private String branchCode;

    @JsonProperty("CompanyId")
    private String companyId;

    @JsonProperty("ProductId")
    private Integer productId;

    @JsonProperty("SectionId")
    private Integer sectionId;
}