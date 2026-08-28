package com.maan.eway.uploaddoc.dto.request;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class UploadDocAgricultureMasterUpdateReq {

    // business key
    @JsonProperty("Sno")
    private Integer sno;

    @JsonProperty("CompanyId")
    private Integer companyId;

    @JsonProperty("ProductId")
    private Integer productId;

    // amendable fields
    @JsonProperty("ProvinceId")
    private Integer provinceId;

    @JsonProperty("ProvinceDesc")
    private String provinceDesc;

    @JsonProperty("DistrictId")
    private Integer districtId;

    @JsonProperty("DistrictDesc")
    private String districtDesc;

    @JsonProperty("Aez")
    private Integer aez;

    @JsonProperty("CropId")
    private Integer cropId;

    @JsonProperty("CropDesc")
    private String cropDesc;

    @JsonProperty("YieldPercentage")
    private Integer yieldPercentage;

    @JsonProperty("PerHaCost")
    private Double perHaCost;

    @JsonProperty("SectionId")
    private Integer sectionId;

    @JsonProperty("CoreAppCode")
    private String coreAppCode;

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