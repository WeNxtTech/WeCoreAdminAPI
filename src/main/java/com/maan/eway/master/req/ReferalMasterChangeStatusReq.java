package com.maan.eway.master.req;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ReferalMasterChangeStatusReq {

	@JsonProperty("ReferalId")
	private String referalId;

	@JsonProperty("CreatedBy")
	private String createdBy;

	@JsonProperty("InsuranceId")
	private String companyId;

	@JsonProperty("Status")
	private String status;

	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("EffectiveDateStart")
	private Date effectiveDateStart;
}
