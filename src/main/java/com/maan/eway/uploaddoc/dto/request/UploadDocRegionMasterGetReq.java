package com.maan.eway.uploaddoc.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class UploadDocRegionMasterGetReq {

    @JsonProperty("RegionCode")
    private String regionCode;

    @JsonProperty("CountryId")
    private String countryId;
}