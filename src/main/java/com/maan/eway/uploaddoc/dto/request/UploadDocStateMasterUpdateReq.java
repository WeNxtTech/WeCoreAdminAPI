package com.maan.eway.uploaddoc.dto.request;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * Update/amend request. The business key fields identify the record family;
 * the service resolves the current highest AMEND_ID and creates the next one.
 */
@Data
public class UploadDocStateMasterUpdateReq {

    // business key (identifies the record family being amended)
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

    // amendable fields
    @JsonProperty("StateName")
    private String stateName;

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

    @JsonProperty("TiraCode")
    private String tiraCode;

    @JsonProperty("UpdatedBy")
    private String updatedBy;

    @JsonProperty("Remarks")
    private String remarks;

    @JsonProperty("RegulatoryCode")
    private String regulatoryCode;

    @JsonProperty("City")
    private String city;

    @JsonProperty("Suburb")
    private String suburb;

    @JsonProperty("AreaGroup")
    private Integer areaGroup;

    @JsonProperty("SuburbLocal")
    private String suburbLocal;

    @JsonProperty("StateNameLocal")
    private String stateNameLocal;

    @JsonProperty("CityLocal")
    private String cityLocal;
}