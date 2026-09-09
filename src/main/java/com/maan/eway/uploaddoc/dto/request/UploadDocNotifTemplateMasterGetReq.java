package com.maan.eway.uploaddoc.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class UploadDocNotifTemplateMasterGetReq {

    @JsonProperty("NotifTemplateCode")
    private String notifTemplateCode;

    @JsonProperty("CompanyId")
    private String companyId;

    @JsonProperty("ProductId")
    private Long productId;
}