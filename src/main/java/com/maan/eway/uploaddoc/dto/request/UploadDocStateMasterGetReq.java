package com.maan.eway.uploaddoc.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * Identifies a record family by its business key (AMEND_ID excluded —
 * the latest amendment is always resolved server-side).
 */
@Data
public class UploadDocStateMasterGetReq {

    @JsonProperty("StateId")
    private Integer stateId;

    @JsonProperty("StateShortCode")
    private String stateShortCode;

    @JsonProperty("CountryId")
    private String countryId;

    @JsonProperty("RegionCode")
    private String regionCode;

    @JsonProperty("CityId")
    private Integer cityId;

    @JsonProperty("SuburbId")
    private Integer suburbId;
}