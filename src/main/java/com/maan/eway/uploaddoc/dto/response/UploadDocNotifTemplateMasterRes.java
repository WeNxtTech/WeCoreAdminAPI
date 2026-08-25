package com.maan.eway.uploaddoc.dto.response;

import java.util.Date;

import lombok.Data;

@Data
public class UploadDocNotifTemplateMasterRes {

	private String notifTemplateCode;
	private String notifTemplateName;
	private String toMessengerNo;
	private String toSmsNo;
	private String toEmail;
	private Date effectiveDateStart;
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
	private Date entryDate;
	private String remarks;
	private String status;
	private String coreAppCode;
	private String regulatoryCode;
	private String createdBy;
	private String updatedBy;
	private Date updatedDate;
	private String companyId;
	private Long productId;
	private Integer amendId;
}
