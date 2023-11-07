package com.maan.eway.common.res;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class LastMailSentRes {


	@JsonProperty("MailTranId")
    private String     mailTranId ;
	
	@JsonProperty("MailSubject")
    private String     mailSubject  ;
	
	@JsonProperty("MailBody")
    private String     mailBody  ;
	
	@JsonProperty("MailRegards")
    private String     mailRegards ;
	
	@JsonProperty("Sender")
    private String     sender ;
	
	@JsonFormat(pattern = "dd/MM/yyyy hh:mm:ss")
	@JsonProperty("PushedEntryDate")
    private Date     pushedEntryDate  ;

	@JsonProperty("ToEmail")
    private String     toEmail  ; //Successfull/Failed
	
	@JsonProperty("FromEmail")
    private String     fromEmail  ;
	
	@JsonProperty("Status")
	private String status;
	
	@JsonProperty("MailResponse")
	private String mailResponse;
	
	@JsonProperty("NotifNo")
	private String notifNo;
	
	@JsonProperty("PushedBy")
	private String pushedBy;
	

}
