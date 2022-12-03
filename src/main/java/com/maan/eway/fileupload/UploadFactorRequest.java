package com.maan.eway.fileupload;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UploadFactorRequest {
	
	@JsonProperty("SNo" )
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
    
    @JsonProperty("Rate" )
    private String Rate ;
    
    @JsonProperty("minimumPremium" )
    private String MinimumPremium ;
    
    @JsonProperty("CalcType")
    private String CalcType ;
    
    @JsonProperty("Status")
    private String status ;
  
    @JsonProperty("RegulatoryCode")
    private String RegulatoryCode;
    
    @JsonProperty("MasterYn" )
    private String masterYn ;
  
    @JsonProperty("ApiUrl" )
    private String apiUrl;

}
