package com.maan.eway.master.req;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class SectionCoverMasterGetAllReq implements Serializable {

    private static final long serialVersionUID = 1L;
    @JsonProperty("InsuranceId")
    private String insuranceId;
    
    @JsonProperty("ProductId")
    private String productId;
    
    @JsonProperty("SectionId")
    private String sectionId;

    @JsonProperty("Limit")
    private String limit;
    
    @JsonProperty("Offset")
    private String offset;
  

}
