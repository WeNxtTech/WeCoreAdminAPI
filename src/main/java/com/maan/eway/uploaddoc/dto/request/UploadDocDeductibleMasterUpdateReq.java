package com.maan.eway.uploaddoc.dto.request;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class UploadDocDeductibleMasterUpdateReq {

    // business key
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

    // amendable fields
    @JsonProperty("DeductStart")
    private Integer deductStart;

    @JsonProperty("DeductEnd")
    private Integer deductEnd;

    @JsonProperty("Rate")
    private Double rate;

    @JsonProperty("CalcType")
    private String calcType;

    @JsonProperty("Status")
    private String status;

    @JsonProperty("EffectiveDateStart")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date effectiveDateStart;

    @JsonProperty("EffectiveDateEnd")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date effectiveDateEnd;

    @JsonProperty("Remarks")
    private String remarks;
}