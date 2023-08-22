package com.maan.eway.master.res;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GetAllOneTimeTableDetailsRes {

	@JsonProperty("ParentId")
	private String parentId;

	@JsonProperty("ItemId")
	private String itemId;

	@JsonProperty("ItemType")
	private String itemType;

	@JsonProperty("ItemCode")
	private String itemCode;

	@JsonProperty("ItemValue")
	private String itemValue;

	@JsonProperty("Status")
	private String status;

	@JsonProperty("DisplayName")
	private String displayName;
	
	@JsonProperty("CreatedBy")
	private String createdBy;
	
	@JsonProperty("UpdatedBy")
	private String updatedBy;

	@JsonProperty("CompanyId")
	private String companyId;
	
	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("UpdatedDate")
	private Date updatedDate;
	
	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("EffectiveDateStart")
	private Date effectiveDateStart;

	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("EffectiveDateEnd")
	private Date effectiveDateEnd;

	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("EntryDate")
	private Date entryDate;
}
