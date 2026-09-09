package com.maan.eway.uploaddoc.dto.request;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class UploadDocRegionMasterUpdateReq {

    // business key
    @JsonProperty("RegionCode")
    private String regionCode;

    @JsonProperty("CountryId")
    private String countryId;

    // amendable fields
    @JsonProperty("RegionShortCode")
    private String regionShortCode;

    @JsonProperty("RegionName")
    private String regionName;

    @JsonProperty("Status")
    private String status;

    @JsonProperty("EffectiveDateStart")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date effectiveDateStart;

    @JsonProperty("EffectiveDateEnd")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date effectiveDateEnd;

    @JsonProperty("CoreAppCode")
    private String coreAppCode;

    @JsonProperty("Remarks")
    private String remarks;

    @JsonProperty("TiraCode")
    private String tiraCode;

    @JsonProperty("RegulatoryCode")
    private String regulatoryCode;

    @JsonProperty("UpdatedBy")
    private String updatedBy;

    @JsonProperty("RegionNameLocal")
    private String regionNameLocal;
}