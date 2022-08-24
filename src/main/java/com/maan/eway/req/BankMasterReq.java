package com.maan.eway.req;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class BankMasterReq {

    private static final long serialVersionUID = 1L;

    //----------------------------------------------------------------------
    // ENTITY PRIMARY KEY 
    //----------------------------------------------------------------------
	@JsonProperty("BankCode")
    private String     bankCode     ;
	@JsonProperty("Effectivedatestart")
    private Date       effectiveDateStart ;
	@JsonProperty("Effectivedateend")
    private Date       effectiveDateEnd ;

    //----------------------------------------------------------------------
    // ENTITY DATA FIELDS 
    //----------------------------------------------------------------------    
	@JsonProperty("Bankshortname")
    private String     bankShortName ;
	@JsonProperty("Bankfullname")
    private String     bankFullName ;
	@JsonProperty("Entrydate")
    private Date       entryDate    ;
	@JsonProperty("Status")
    private String     status       ;

	
}
