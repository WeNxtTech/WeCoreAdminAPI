package com.maan.eway.master.res;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationTempleteMasterGetRes {

	@JsonProperty("NotifTemplateCode")
	private String notifTemplateCode;

	@JsonProperty("NotifTemplatename")
	private String notifTemplatename;

	@JsonProperty("ToMessengerno")
	private String toMessengerno;

	@JsonProperty("ToSmsno")
	private String toSmsno;

	@JsonProperty("ToEmail")
	private String toEmail;

	@JsonProperty("InsuranceId")
	private String companyId;

	@JsonProperty("ProductId")
	private String productId;

	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("EffectiveDateStart")
	private Date effectiveDateStart;

	@JsonFormat(pattern = "dd/MM/YYYY")
	@JsonProperty("EffectiveDateEnd")
	private Date EffectiveDateEnd;

	@JsonProperty("MailRequired")
	private String mailRequired;

	@JsonProperty("MailSubject")
	private String mailSubject;

	@JsonProperty("MailBody")
	private String mailBody;

	@JsonProperty("MailRegards")
	private String mailRegards;

	@JsonProperty("SmsRequired")
	private String smsRequired;

	@JsonProperty("SmsSubject")
	private String smsSubject;

	@JsonProperty("SmsBodyEn")
	private String smsBodyEn;

	@JsonProperty("WhatsappRequired")
	private String whatsappRequired;

	@JsonProperty("WhatsappSubject")
	private String whatsappSubject;

	@JsonProperty("WhatsappBodyEn")
	private String whatsappBodyEn;

	@JsonProperty("WhatsappRegards")
	private String whatsappRegards;

	@JsonProperty("Remarks")
	private String remarks;

	@JsonProperty("Status")
	private String status;

	@JsonProperty("CreatedBy")
	private String createdBy;

	@JsonProperty("UpdatedBy")
	private String updatedBy;

	@JsonFormat(pattern = "dd/MM/YYYY")
	@JsonProperty("UpdateDate")
	private Date updateDate;

	@JsonProperty("CoreAppCode")
	private String coreAppCode;

	@JsonProperty("RegulatoryCode")
	private String regulatoryCode;

	@JsonFormat(pattern = "dd/MM/YYYY")
	@JsonProperty("EntryDate")
	private Date entryDate;
	
	@JsonProperty("SmsRegards")
	private String smsRegards;

}
