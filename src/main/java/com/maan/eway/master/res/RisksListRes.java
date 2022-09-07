package com.maan.eway.master.res;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class RisksListRes {

	@JsonProperty("RiskId")
    private String     riskId     ;
    
	@JsonProperty("OwnHouseYn")
    private String     ownHouseYn     ;
	
	@JsonProperty("Remarks")
    private String     remarks;
}
