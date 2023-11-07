package com.maan.eway.common.res;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class LastSmSSentRes {


	@JsonProperty("SNo")
    private String     sNo ;
	@JsonProperty("SmsFrom")
    private String     smsFrom  ;
	@JsonProperty("MobileNo")
    private String     mobileNo  ;
	@JsonProperty("SmsType")
    private String     smsType ;
	@JsonProperty("SmsContent")
    private String     smsContent ;
	
	@JsonFormat(pattern = "dd/MM/yyyy hh:mm:ss")
	@JsonProperty("reqTime")
    private Date     reqTime  ;
//	
//	@JsonProperty("ResTime")
//    private String     resTime  ;
	
	@JsonProperty("ResStatus")
    private String     resStatus  ; //Successfull/Failed
	
	@JsonProperty("ResMessage")
    private String     resMessage  ;
	
	@JsonProperty("NotifNo")
	private String notifNo;
	
	@JsonFormat(pattern = "dd/MM/yyyy hh:mm:ss")
	@JsonProperty("EntryDate")
    private Date     entryDate  ;
	
	@JsonProperty("PushedBy")
	private String pushedBy;
	
	@JsonProperty("SmsRegards")
	private String smsRegards;
	
}
