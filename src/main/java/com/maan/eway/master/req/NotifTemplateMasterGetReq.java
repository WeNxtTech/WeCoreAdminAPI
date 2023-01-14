package com.maan.eway.master.req;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;

import lombok.Data;

@Data
public class NotifTemplateMasterGetReq implements Serializable {

    private static final long serialVersionUID = 1L;

	@JsonProperty("NotifTemplateCode")
	private String notifTemplateCode;
	
	@JsonProperty("InsuranceId")
	private String insuranceId;
	
	@JsonProperty("ProductId")
	private String productId;
    
}
