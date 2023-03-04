package com.maan.eway.master.req;

import java.math.BigDecimal;

import javax.persistence.Column;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ProrataMultiInsertReq {
	


	@JsonProperty("StartFrom")
	private String startfrom;

	@JsonProperty("EndTo")
	private String endto;

	@JsonProperty("Percent")
	private String percent;


	@JsonProperty("Status")
    private String     status ;
	


}
