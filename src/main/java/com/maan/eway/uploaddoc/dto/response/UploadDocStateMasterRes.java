package com.maan.eway.uploaddoc.dto.response;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class UploadDocStateMasterRes {
	
	@JsonProperty("StateId")
	private Integer stateId;
	
	@JsonProperty("StateName")
	private String stateName;
	
	@JsonProperty("StateShortCode")
	private String stateShortCode;
	
	@JsonProperty("CountryId")
	private String countryId;
	
	@JsonProperty("RegionCode")
	private String regionCode;
	
	@JsonProperty("AmendId")
	private Integer amendId;
	
	@JsonProperty("Status")
	private String status;
	
	@JsonProperty("EntryDate")
	private Date entryDate;
	
	@JsonProperty("EffectiveDateStart")
	private Date effectiveDateStart;
	
	@JsonProperty("EffectiveDateEnd")
	private Date effectiveDateEnd;
	
	@JsonProperty("CoreAppCode")
	private String coreAppCode;
	
	@JsonProperty("TiraCode")
	private String tiraCode;
	
	@JsonProperty("CreatedBy")
	private String createdBy;
	
	@JsonProperty("Remarks")
	private String remarks;
	
	@JsonProperty("RegulatoryCode")
	private String regulatoryCode;
	
	@JsonProperty("UpdatedDate")
	private Date updatedDate;
	
	@JsonProperty("UpdatedBy")
	private String updatedBy;
	
	@JsonProperty("City")
	private String city;
	
	@JsonProperty("Suburb")
	private String suburb;
	
	@JsonProperty("AreaGroup")
	private Integer areaGroup;
	
	@JsonProperty("CityId")
	private Integer cityId;
	
	@JsonProperty("SuburbId")
	private Integer suburbId;
	
	@JsonProperty("SuburbLocal")
	private String suburbLocal;
	
	@JsonProperty("StateNameLocal")
	private String stateNameLocal;
	
	@JsonProperty("CityLocal")
	private String cityLocal;
}
