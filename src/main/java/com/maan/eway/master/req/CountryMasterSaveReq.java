package com.maan.eway.master.req;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class CountryMasterSaveReq implements Serializable {

    private static final long serialVersionUID = 1L;

	@JsonProperty("CountryId")
	private String countryId;

	@JsonProperty("CountryName")
	private String countryName;
	
	@JsonProperty("CountryShortCode")
	private String countryShortCode;

	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("EffectiveDateStart")
	private Date effectiveDateStart;
	
	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("EffectiveDateEnd")
	private Date effectiveDateEnd;

	@JsonProperty("MobileCode")
	private String mobileCode;

	@JsonProperty("Status")
	private String status;

	@JsonProperty("CoreAppCode")
	private String coreAppCode;

	@JsonProperty("Remarks")
	private String remarks;
	
	@JsonProperty("TiraCode")
	private String tiraCode;
	
	@JsonProperty("CreatedBy")
	private String createdBy;

}
