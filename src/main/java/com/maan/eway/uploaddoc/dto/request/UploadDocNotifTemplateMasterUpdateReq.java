package com.maan.eway.uploaddoc.dto.request;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class UploadDocNotifTemplateMasterUpdateReq {

    // business key
    @JsonProperty("NotifTemplateCode")
    private String notifTemplateCode;

    @JsonProperty("CompanyId")
    private String companyId;

    @JsonProperty("ProductId")
    private Long productId;

    // amendable fields
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

    @JsonProperty("Remarks")
    private String remarks;

    @JsonProperty("Status")
    private String status;

    @JsonProperty("CoreAppCode")
    private String coreAppCode;

    @JsonProperty("RegulatoryCode")
    private String regulatoryCode;

    @JsonProperty("UpdatedBy")
    private String updatedBy;
}