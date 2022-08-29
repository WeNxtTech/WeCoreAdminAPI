package com.maan.eway.admin.req;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class AttachedPreductReq {

	@JsonProperty("ProductId")
	private String productId ;
	
	@JsonProperty("ProductName")
	private String productName ;
	
	@JsonProperty("StartLimit")
	private String startLimit ;
	
	@JsonProperty("EndLimit")
	private String endLimit ;
	
	@JsonProperty("Status")
	private String status ;
	
	@JsonFormat(pattern="dd/MM/yyyy")
	@JsonProperty("EffectiveDate")
	private Date effectiveDate ;
	
	
	
}
