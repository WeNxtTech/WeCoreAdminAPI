package com.maan.eway.master.req;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class InsertOneTimeTableReq {


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
	
	@JsonProperty("DisplayName")
	private String displayName;
	

	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("EffectiveDateStart")
	private Date effectiveDateStart;

	@JsonProperty("Status")
	private String status;

	@JsonProperty("CreatedBy")
	private String createdBy;
	
	@JsonProperty("UpdatedBy")
	private String updatedBy;

	@JsonProperty("InsuranceId")
	private String companyId;
	

}
