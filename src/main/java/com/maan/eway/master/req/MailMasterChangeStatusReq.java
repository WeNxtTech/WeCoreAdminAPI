package com.maan.eway.master.req;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;

import lombok.Data;

@Data
public class MailMasterChangeStatusReq implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("SNo")
    private String sNo;
    
    @JsonProperty("InsuranceId")
    private String companyId;
   
    @JsonProperty("Status")
    private String status;
    
}
