package com.maan.eway.master.req;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ConstantTableDetailsSaveReq implements Serializable {

    private static final long serialVersionUID = 1L;

	@JsonProperty("ItemId")
	private String itemId;

	@JsonProperty("InsuranceId")
	private String insuranceId;
	
//	@JsonProperty("BranchCode")
//	private String branchCode;
	
	@JsonProperty("ProductId")
	private String productId;

	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("EffectiveDateStart")
	private Date effectiveDateStart;
	
	@JsonProperty("TableType")
	private String tableType;
	
	@JsonProperty("ApiUrl")
	private String apiUrl;

	@JsonProperty("ApiName")
	private String apiName;
	
	@JsonProperty("KeyName")
	private String keyName;
	
	@JsonProperty("KeyTable")
	private String keyTable;

	@JsonProperty("RequestYn")
	private String requestYn;

	@JsonProperty("CreatedBy")
	private String createdBy;
	
	@JsonProperty("Status")
	private String status;
	
	@JsonProperty("DropdownTableDetails")
	private List<DropdownTableDetailsSaveReq> dropdownTableDetailsSaveReq;
	

}
