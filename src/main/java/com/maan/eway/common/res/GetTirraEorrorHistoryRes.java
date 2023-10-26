package com.maan.eway.common.res;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GetTirraEorrorHistoryRes {

	
	@JsonProperty("Chassisnumber")
    private String     reqChassisNumber ;
	@JsonProperty("ApiDescription")
    private String     apiDescription  ;
	@JsonProperty("ReqMethod")
    private String     requestmethod  ;
	@JsonProperty("ReqUrl")
    private String     requestUrl ;
	@JsonProperty("ReqHeaders")
    private String     requestHeaders ;
	@JsonProperty("ReqString")
    private String     requestString  ;
	@JsonProperty("ResponseStatus")
    private String     responseStatus  ;
	@JsonProperty("ResponseStatusDesc")
    private String     responseStatusDesc  ; //Successfull/Failed
	@JsonProperty("ResponseString")
    private String     responseString  ;
	
	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("EntryDate")
    private Date     entryDate  ;
}
