package com.maan.eway.master.req;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ConstantTableChangeStatusReq {

	@JsonProperty("InsuranceId")
	private String companyId;

	@JsonProperty("BranchCode")
	private String branchCode;

	@JsonProperty("ItemId")
	private String itemId;

	@JsonProperty("Status")
	private String status;
	 

}
