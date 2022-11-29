package com.maan.eway.master.req;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class DropdownTableDetailsSaveReq {

	@JsonProperty("RequestId")
	private String requestId;

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

	@JsonProperty("RegulatoryCode")
	private String regulatoryCode;

	@JsonProperty("CreatedBy")
	private String createdBy;


}
