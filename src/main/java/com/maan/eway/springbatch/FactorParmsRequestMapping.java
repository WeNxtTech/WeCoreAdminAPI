package com.maan.eway.springbatch;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FactorParmsRequestMapping {
	
	@JsonProperty("sNo" )
	private String sno;

	@JsonProperty("param1" )
    private String param1 ;
    
    @JsonProperty("param2" )
    private String param2 ;
    
    @JsonProperty("param3" )
    private String param3 ;
    
    @JsonProperty("param4" )
    private String param4 ;
    
    @JsonProperty("param5" )
    private String param5 ;
    
    @JsonProperty("param6" )
    private String param6 ;
    
    @JsonProperty("param7" )
    private String param7 ;
    
    @JsonProperty("param8" )
    private String param8 ;
    
    @JsonProperty("param9" )
    private String param9 ;
    
    @JsonProperty("param10" )
    private String param10 ;
    
    @JsonProperty("param11" )
    private String param11 ;
    
    @JsonProperty("param12" )
    private String param12 ;
    
    @JsonProperty("rate" )
    private String rate ;
    
    @JsonProperty("minPremium" )
    private String minimumPremium ;
    
    @JsonProperty("calcType" )
    private String calType ;
    
    @JsonProperty("status" )
    private String status ;
  
    @JsonProperty("regulatoryCode" )
    private String regulatoryCode;
    
    @JsonProperty("masterYn" )
    private String masterYn ;
  
    @JsonProperty("apiUrl" )
    private String apiUrl;
    

}
