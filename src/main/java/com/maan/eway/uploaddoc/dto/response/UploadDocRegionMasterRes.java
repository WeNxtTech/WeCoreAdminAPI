package com.maan.eway.uploaddoc.dto.response;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class UploadDocRegionMasterRes {

    @JsonProperty("RegionCode")
    private String regionCode;

    @JsonProperty("CountryId")
    private String countryId;

    @JsonProperty("AmendId")
    private Integer amendId;

    @JsonProperty("RegionShortCode")
    private String regionShortCode;

    @JsonProperty("RegionName")
    private String regionName;

    @JsonProperty("EntryDate")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date entryDate;

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

    @JsonProperty("CreatedBy")
    private String createdBy;

    @JsonProperty("TiraCode")
    private String tiraCode;

    @JsonProperty("RegulatoryCode")
    private String regulatoryCode;

    @JsonProperty("UpdatedBy")
    private String updatedBy;

    @JsonProperty("UpdatedDate")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date updatedDate;

    @JsonProperty("RegionNameLocal")
    private String regionNameLocal;
}