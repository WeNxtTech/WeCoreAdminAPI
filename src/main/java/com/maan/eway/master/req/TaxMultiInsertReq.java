package com.maan.eway.master.req;

import jakarta.persistence.Column;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class TaxMultiInsertReq {

		@JsonProperty("TaxId")
		private String     taxId ;

	   @JsonProperty("TaxName")
	    private String     taxName ;
	    
	    @JsonProperty("TaxDesc")
	    private String     taxDesc ;
	    
	    

	    @JsonProperty("CalcType")
	    private String     calcType ;

	    
	    @JsonProperty("Value")
	    private String    value ;

	    @JsonProperty("Status")
	    private String     status ;
	    
	    @JsonProperty("TaxCode")    
	    private String   taxCode;

	    @JsonProperty("TaxFor")    
	    private String   taxFor;
	    
	    @JsonProperty("ChargeOrRefund")    
	    private String   chargeOrRefund;
}
