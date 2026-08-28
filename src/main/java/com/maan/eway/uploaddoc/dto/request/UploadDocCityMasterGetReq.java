package com.maan.eway.uploaddoc.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class UploadDocCityMasterGetReq {

    @JsonProperty("CityId")
    private Integer cityId;

    @JsonProperty("CountryId")
    private String countryId;

    @JsonProperty("StateId")
    private String stateId;
}