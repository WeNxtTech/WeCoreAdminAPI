package com.maan.eway.master.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class RiskListSaveReq {
	
	@JsonProperty("RiskId")
    private String     riskId     ;
    
	@JsonProperty("OwnHouseYn")
    private String     ownHouseYn     ;
	
	@JsonProperty("Remarks")
    private String     remarks;
    
	
}
