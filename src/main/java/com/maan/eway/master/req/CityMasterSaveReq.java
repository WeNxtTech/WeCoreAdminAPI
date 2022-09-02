package com.maan.eway.master.req;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class CityMasterSaveReq implements Serializable {

    private static final long serialVersionUID = 1L;

	@JsonProperty("CityId")
	private String cityId;

	@JsonProperty("CityName")
	private String cityName;

	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("EffectiveDate")
	private Date effectiveDate;

	@JsonProperty("StateId")
	private String stateId;

	@JsonProperty("CountryId")
	private String countryId;
	
	@JsonProperty("Status")
	private String status;

}
