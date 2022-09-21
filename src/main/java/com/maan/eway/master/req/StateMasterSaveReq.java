package com.maan.eway.master.req;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class StateMasterSaveReq implements Serializable {

    private static final long serialVersionUID = 1L;

	@JsonProperty("StateId")
	private String stateId;

	@JsonProperty("StateName")
	private String stateName;

	@JsonProperty("StateShortCode")
	private String stateShortCode;

	@JsonProperty("CountryId")
	private Integer countryId;

	@JsonProperty("CountryName")
	private String countryName;
	
	@JsonProperty("RegionCode")
	private Integer regionCode;

	@JsonProperty("RegionName")
	private String regionName;
 
	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("EffectiveDateEnd")
	private Date effectiveDateEnd;

	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("EffectiveDateStart")
	private Date effectiveDateStart;


	@JsonProperty("Status")
	private String status;

	@JsonProperty("CoreAppCode")
	private String coreAppCode;

	@JsonProperty("AmendId")
	private Integer amendId;

	@JsonProperty("Remarks")
	private String remarks;

}
