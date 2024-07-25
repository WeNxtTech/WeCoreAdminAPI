package com.maan.eway.master.req;

import java.math.BigDecimal;

import jakarta.persistence.Column;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ProrataMultiInsertReq {
	
	@JsonProperty("Sno")
    private String sno ;

	@JsonProperty("StartFrom")
	private String startfrom;

	@JsonProperty("EndTo")
	private String endto;

	@JsonProperty("Percent")
	private String percent;
	
	@JsonProperty("Remarks")
    private String     remarks ;

	@JsonProperty("Status")
    private String     status ;
	


}
