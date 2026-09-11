package com.maan.eway.master.res;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class EmiDetailsRes {
	@JsonProperty("EmiId")
	private String emiId;

	@JsonProperty("PremiumStart")
	private String premiumStart;

	@JsonProperty("PremiumEnd")
	private String premiumEnd;

	@JsonProperty("InterestPercent")
	private String interestPercent;

	@JsonProperty("AdvancePercent")
	private String advancePercent;

	@JsonProperty("InstallmentTypeId")
	private String installmentTypeId;

	@JsonProperty("InstallmentPeriod")
	private String installmentPeriod;

	@JsonProperty("InstallmentTypeDesc")
	private String installmentTypeDesc;
	
	@JsonProperty("AdvanceYn")
	private String advanceYn;
	
	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("EntryDate")
	private Date entryDate;
	
	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("EffectiveDateStart")
	private Date effectiveDateStart;
	
	@JsonProperty("Remarks")
	private String remarks;
	
	@JsonProperty("Status")
	private String status;
	
	@JsonProperty("ProductId")
	private String productId;

	@JsonProperty("InsuranceId")
	private String companyId;
	
	@JsonProperty("AmendId")
	private String amendId;

	@JsonProperty("PolicyType")
	private String policyType;

	@JsonProperty("PolicyTypeDesc")
	private String policyDesc;
	
	@JsonProperty("TaxIds")
	private List<String> taxIds;
}
