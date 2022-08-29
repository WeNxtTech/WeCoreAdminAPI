package com.maan.eway.admin.req;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ReferalMasterSaveReq implements Serializable {

    private static final long serialVersionUID = 1L;

	@JsonProperty("ReferalId")
	private String referalId;

	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("EffectiveDate")
	private Date effectiveDate;

	@JsonProperty("CompanyId")
	private String companyId;

	@JsonProperty("ReferalName")
	private String referalName;

	@JsonProperty("ReferalDesc")
	private String referalDesc;

	@JsonProperty("Remarks")
	private String remarks;

	@JsonProperty("Status")
	private String status;

}
