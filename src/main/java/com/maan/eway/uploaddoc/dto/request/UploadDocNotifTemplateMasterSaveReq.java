package com.maan.eway.uploaddoc.dto.request;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class UploadDocNotifTemplateMasterSaveReq {

	private String notifTemplateCode;
	private String notifTemplateName;
	private String toMessengerNo;
	private String toSmsNo;
	private String toEmail;
	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date effectiveDateStart;
	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date effectiveDateEnd;
	private String mailRequired;
	private String mailSubject;
	private String mailBody;
	private String mailRegards;
	private String smsRequired;
	private String smsSubject;
	private String smsBodyEn;
	private String smsRegards;
	private String whatsappRequired;
	private String whatsappSubject;
	private String whatsappBodyEn;
	private String whatsappRegards;
	private String remarks;
	private String status;
	private String coreAppCode;
	private String regulatoryCode;
	private String createdBy;
	private String companyId;
	private Long productId;
}
