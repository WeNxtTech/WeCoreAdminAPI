package com.maan.eway.uploaddoc.dto.response;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class UploadDocNotifTemplateMasterRes {

    @JsonProperty("NotifTemplateCode")
    private String notifTemplateCode;

    @JsonProperty("NotifTemplateName")
    private String notifTemplateName;

    @JsonProperty("ToMessengerNo")
    private String toMessengerNo;

    @JsonProperty("ToSmsNo")
    private String toSmsNo;

    @JsonProperty("ToEmail")
    private String toEmail;

    @JsonProperty("EffectiveDateStart")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date effectiveDateStart;

    @JsonProperty("EffectiveDateEnd")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date effectiveDateEnd;

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

    @JsonProperty("SmsRegards")
    private String smsRegards;

    @JsonProperty("WhatsappRequired")
    private String whatsappRequired;

    @JsonProperty("WhatsappSubject")
    private String whatsappSubject;

    @JsonProperty("WhatsappBodyEn")
    private String whatsappBodyEn;

    @JsonProperty("WhatsappRegards")
    private String whatsappRegards;

    @JsonProperty("EntryDate")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date entryDate;

    @JsonProperty("Remarks")
    private String remarks;

    @JsonProperty("Status")
    private String status;

    @JsonProperty("CoreAppCode")
    private String coreAppCode;

    @JsonProperty("RegulatoryCode")
    private String regulatoryCode;

    @JsonProperty("CreatedBy")
    private String createdBy;

    @JsonProperty("UpdatedBy")
    private String updatedBy;

    @JsonProperty("UpdatedDate")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date updatedDate;

    @JsonProperty("CompanyId")
    private String companyId;

    @JsonProperty("ProductId")
    private Long productId;

    @JsonProperty("AmendId")
    private Integer amendId;
}