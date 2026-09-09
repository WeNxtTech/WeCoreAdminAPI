package com.maan.eway.uploaddoc.dto.response;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class UploadDocCityMasterRes {
	@JsonProperty("CityId")
	private Integer cityId;
	@JsonProperty("CountryId")
	private String countryId;
	@JsonProperty("StateId")
	private String stateId;
	@JsonProperty("AmendId")
	private Integer amendId;
	@JsonProperty("EffectiveDateEnd")
	private Date effectiveDateEnd;
	@JsonProperty("EffectiveDateStart")
	private Date effectiveDateStart;
	@JsonProperty("CityName")
	private String cityName;
	@JsonProperty("Status")
	private String status;
	@JsonProperty("Remarks")
	private String remarks;
	@JsonProperty("EntryDate")
	private Date entryDate;
	@JsonProperty("CoreAppCode")
	private String coreAppCode;
	@JsonProperty("TiraCode")
	private String tiraCode;
	@JsonProperty("CreatedBy")
	private String createdBy;
	@JsonProperty("RegulatoryCode")
	private String regulatoryCode;
	@JsonProperty("UpdatedBy")
	private String updatedBy;
	@JsonProperty("UpdatedDate")
	private Date updatedDate;
	@JsonProperty("CityNameLocal")
	private String cityNameLocal;
}
