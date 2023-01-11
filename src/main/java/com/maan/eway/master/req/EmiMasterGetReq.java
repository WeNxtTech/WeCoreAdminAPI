package com.maan.eway.master.req;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class EmiMasterGetReq implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("EmiId")
    private String emiId;
    @JsonProperty("ProductId")
    private String productId;
    @JsonProperty("InsuranceId")
    private String companyId;
    
}
