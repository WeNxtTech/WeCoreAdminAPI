package com.maan.eway.master.res;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class DropdownTableDetailsRes {

	@JsonProperty("RequestId")
	private String requestId;

	@JsonProperty("ItemId")
	private String itemId;
	
	@JsonProperty("CompanyId")
	private String companyId;

	@JsonProperty("BranchCode")
	private String branchCode;

	@JsonProperty("ProductId")
	private String productId;

	@JsonProperty("RequestJsonKey")
	private String requestJsonKey;

	@JsonProperty("RequestColumn")
	private String requestColumn;

	@JsonProperty("RequestTable")
	private String requestTable;

	@JsonProperty("Status")
	private String status;

	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("EffectiveDateStart")
	private Date effectiveDateStart;
	
	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("EffectiveDateEnd")
	private Date effectiveDateEnd;

	@JsonProperty("RegulatoryCode")
	private String regulatoryCode;

	@JsonProperty("CreatedBy")
	private String createdBy;


}
