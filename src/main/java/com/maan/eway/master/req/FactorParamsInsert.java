package com.maan.eway.master.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class FactorParamsInsert {
	
	@JsonProperty("SNo" )
	private String sno;

	@JsonProperty("Param1" )
    private String param1 ;
    
    @JsonProperty("Param2" )
    private String param2 ;
    
    @JsonProperty("Param3" )
    private String param3 ;
    
    @JsonProperty("Param4" )
    private String param4 ;
    
    @JsonProperty("Param5" )
    private String param5 ;
    
    @JsonProperty("Param6" )
    private String param6 ;
    
    @JsonProperty("Param7" )
    private String param7 ;
    
    @JsonProperty("Param8" )
    private String param8 ;
    
    @JsonProperty("Param9" )
    private String param9 ;
    
    @JsonProperty("Param10" )
    private String param10 ;
    
    @JsonProperty("Param11" )
    private String param11 ;
    
    @JsonProperty("Param12" )
    private String param12 ;
    
    @JsonProperty("Rate" )
    private String rate ;
    
    @JsonProperty("MinimumRate" )
    private String minimumRate ;
    
    @JsonProperty("MinimumRateYN")
    private String minimumRateYN;
    
    @JsonProperty("MinimumPremium" )
    private String minimumPremium ;
    
    @JsonProperty("CalcType" )
    private String calType ;
    
    @JsonProperty("Status" )
    private String status ;
  
    @JsonProperty("RegulatoryCode" )
    private String regulatoryCode;
    
    @JsonProperty("MasterYn" )
    private String masterYn ;
  
    @JsonProperty("ApiUrl" )
    private String apiUrl;
    
    @JsonProperty("xlAgencyCode" )
    private String xlAgencyCode;
    
    // New Params
    @JsonProperty("Param13" )
    private String param13 ;
    
    @JsonProperty("Param14" )
    private String param14 ;
    
    @JsonProperty("Param15" )
    private String param15 ;
    
    @JsonProperty("Param16" )
    private String param16 ;
    
    @JsonProperty("Param17" )
    private String param17 ;
    
    @JsonProperty("Param18" )
    private String param18 ;
    
    @JsonProperty("Param19" )
    private String param19 ;
    
    @JsonProperty("Param20" )
    private String param20 ;
    
    @JsonProperty("Param21" )
    private String param21 ;
    
    @JsonProperty("Param22" )
    private String param22 ;
    
    @JsonProperty("Param23" )
    private String param23 ;
    
    @JsonProperty("Param24" )
    private String param24 ;
    
    @JsonProperty("Param25" )
    private String param25 ;
    
    @JsonProperty("Param26" )
    private String param26 ;
    
    @JsonProperty("Param27" )
    private String param27 ;
    
    @JsonProperty("Param28" )
    private String param28 ;
    
    @JsonProperty("ExcessPercent")
	private String excessPercent;
	
	@JsonProperty("ExcessAmount")
	private String excessAmount;
	
	@JsonProperty("ExcessDesc")
	private String excessDesc;
    
}
