package com.maan.eway.master.req;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class EmiMasterGetByPolicyReq implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("PolicyType")
    private String policyType;
    @JsonProperty("ProductId")
    private String productId;
    @JsonProperty("InsuranceId")
    private String companyId;
    
}