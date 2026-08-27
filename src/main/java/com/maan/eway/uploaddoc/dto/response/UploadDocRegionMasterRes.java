package com.maan.eway.uploaddoc.dto.response;

import java.util.Date;

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
	private Date entryDate;
	
	@JsonProperty("Status")
	private String status;
	
	@JsonProperty("EffectiveDateStart")
	private Date effectiveDateStart;
	
	@JsonProperty("EffectiveDateEnd")
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
	private Date updatedDate;
	
	@JsonProperty("RegionNameLocal")
	private String regionNameLocal;
}
