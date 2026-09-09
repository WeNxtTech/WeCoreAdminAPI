package com.maan.eway.uploaddoc.dto.request;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class UploadDocCityMasterUpdateReq {

    // business key
    @JsonProperty("CityId")
    private Integer cityId;

    @JsonProperty("CountryId")
    private String countryId;

    @JsonProperty("StateId")
    private String stateId;

    // amendable fields
    @JsonProperty("EffectiveDateEnd")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date effectiveDateEnd;

    @JsonProperty("EffectiveDateStart")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date effectiveDateStart;

    @JsonProperty("CityName")
    private String cityName;

    @JsonProperty("Status")
    private String status;

    @JsonProperty("Remarks")
    private String remarks;

    @JsonProperty("CoreAppCode")
    private String coreAppCode;

    @JsonProperty("TiraCode")
    private String tiraCode;

    @JsonProperty("UpdatedBy")
    private String updatedBy;

    @JsonProperty("RegulatoryCode")
    private String regulatoryCode;

    @JsonProperty("CityNameLocal")
    private String cityNameLocal;
}