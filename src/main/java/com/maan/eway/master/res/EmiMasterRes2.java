package com.maan.eway.master.res;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class EmiMasterRes2 {
	private static final long serialVersionUID = 1L;
	@JsonProperty("ProductId")
	private String productId;

	@JsonProperty("InsuranceId")
	private String companyId;

	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("EffectiveDateStart")
	private Date effectiveDateStart;

	@JsonProperty("Status")
	private String status;

	@JsonProperty("Remarks")
	private String remarks;

	@JsonProperty("CreatedBy")
	private String createdBy;

	@JsonProperty("EmiDetails")
	private List<EmiDetailsRes> emiDetails;

	@JsonProperty("UpdatedBy")
	private String updatedBy;

	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("UpdatedDate")
	private Date updatedDate;

	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("EffectiveDateEnd")
	private Date effectiveDateEnd;

	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("EntryDate")
	private Date entryDate;

	@JsonProperty("AmendId")
	private String amendId;

	@JsonProperty("PolicyType")
	private String policyType;

	@JsonProperty("PolicyTypeDesc")
	private String policyDesc;

}
