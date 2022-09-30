package com.maan.eway.master.req;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class CompanyCityMasterGetReq implements Serializable {

    private static final long serialVersionUID = 1L;

	@JsonProperty("CityId")
    private String     cityId     ;
    
	@JsonProperty("StateId")
    private String     stateId     ;
    
	@JsonProperty("CountryId")
    private String     countryId     ;
    
	@JsonProperty("RegionId")
    private String    regionId     ;
    
	@JsonProperty("InsuranceId")
    private String    companyId     ;
    
}
