package com.maan.eway.uploaddoc.entity;

import java.io.Serializable;
import java.util.Date;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Entity mapped to table "eway_notif_template_master".
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@DynamicInsert
@DynamicUpdate
@Builder
@IdClass(UploadDocNotifTemplateMasterId.class)
@Table(name = "eway_notif_template_master")
public class UploadDocNotifTemplateMaster implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "NOTIF_TEMPLATE_CODE", nullable = false, length = 20)
	private String notifTemplateCode;

	@Id
	@Column(name = "COMPANY_ID", nullable = false, length = 10)
	private String companyId;

	@Id
	@Column(name = "PRODUCT_ID", nullable = false)
	private Long productId;

	@Id
	@Column(name = "AMEND_ID", nullable = false)
	private Integer amendId;

	@Column(name = "NOTIF_TEMPLATENAME", length = 60)
	private String notifTemplateName;

	@Column(name = "TO_MESSENGERNO", length = 100)
	private String toMessengerNo;

	@Column(name = "TO_SMSNO", length = 100)
	private String toSmsNo;

	@Column(name = "TO_EMAIL", length = 100)
	private String toEmail;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "EFFECTIVE_DATE_START", nullable = false)
	private Date effectiveDateStart;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "EFFECTIVE_DATE_END", nullable = false)
	private Date effectiveDateEnd;

	@Column(name = "MAIL_REQUIRED", length = 10)
	private String mailRequired;

	@Column(name = "MAIL_SUBJECT", length = 500)
	private String mailSubject;

	@Column(name = "MAIL_BODY", length = 2000)
	private String mailBody;

	@Column(name = "MAIL_REGARDS", length = 500)
	private String mailRegards;

	@Column(name = "SMS_REQUIRED", nullable = false, length = 10)
	private String smsRequired;

	@Column(name = "SMS_SUBJECT", length = 500)
	private String smsSubject;

	@Column(name = "SMS_BODY_EN", length = 2000)
	private String smsBodyEn;

	@Column(name = "SMS_REGARDS", length = 500)
	private String smsRegards;

	@Column(name = "WHATSAPP_REQUIRED", length = 10)
	private String whatsappRequired;

	@Column(name = "WHATSAPP_SUBJECT", length = 500)
	private String whatsappSubject;

	@Column(name = "WHATSAPP_BODY_EN", length = 2000)
	private String whatsappBodyEn;

	@Column(name = "WHATSAPP_REGARDS", length = 500)
	private String whatsappRegards;

	@Temporal(TemporalType.DATE)
	@Column(name = "ENTRY_DATE")
	private Date entryDate;

	@Column(name = "REMARKS", length = 100)
	private String remarks;

	@Column(name = "STATUS", length = 10)
	private String status;

	@Column(name = "CORE_APP_CODE", nullable = false, length = 20)
	private String coreAppCode;

	@Column(name = "REGULATORY_CODE", nullable = false, length = 20)
	private String regulatoryCode;

	@Column(name = "CREATED_BY", nullable = false, length = 100)
	private String createdBy;

	@Column(name = "UPDATED_BY", length = 20)
	private String updatedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATED_DATE")
	private Date updatedDate;
}
