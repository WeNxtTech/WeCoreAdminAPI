package com.maan.eway.master.res;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class RegionMasterRes implements Serializable {

    private static final long serialVersionUID = 1L;

	@JsonProperty("RegionCode")
	private String regionCode;

	@JsonFormat(pattern = "dd/MM/YYYY")
	@JsonProperty("EffectiveDateStart")
	private Date effectiveDateStart;

	@JsonFormat(pattern = "dd/MM/YYYY")
	@JsonProperty("EffectiveDateEnd")
	private Date effectiveDateEnd;

	@JsonProperty("ShortCode")
	private String shortCode;

	@JsonProperty("RegionName")
	private String regionName;

	@JsonProperty("Status")
	private String status;

	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("EntryDate")
	private Date entryDate;

	@JsonProperty("InsuranceId")
	private String companyId;

	@JsonProperty("CoreAppCode")
	private String coreAppCode;

	@JsonProperty("AmendId")
	private Integer amendId;

	@JsonProperty("Remarks")
	private String remarks;
}
