package com.maan.eway.master.req;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class SectionMasterGetAllReq implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("Limit")
    private String limit;
    
    @JsonProperty("InsuranceId")
    private String insuranceId;
    
    @JsonProperty("ProductId")
    private String productId;
    
    @JsonProperty("Offset")
    private String offset;
  
}
