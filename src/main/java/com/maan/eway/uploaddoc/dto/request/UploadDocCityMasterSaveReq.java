package com.maan.eway.uploaddoc.dto.request;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class UploadDocCityMasterSaveReq {

    @JsonProperty("CityId")
    private Integer cityId;

    @JsonProperty("CountryId")
    private String countryId;

    @JsonProperty("StateId")
    private String stateId;

    @JsonProperty("EffectiveDateStart")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date effectiveDateStart;

    @JsonProperty("EffectiveDateEnd")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date effectiveDateEnd;

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

    @JsonProperty("CreatedBy")
    private String createdBy;

    @JsonProperty("RegulatoryCode")
    private String regulatoryCode;

    @JsonProperty("CityNameLocal")
    private String cityNameLocal;
}