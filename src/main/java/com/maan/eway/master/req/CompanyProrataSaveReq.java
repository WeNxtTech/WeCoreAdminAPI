package com.maan.eway.master.req;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class CompanyProrataSaveReq implements Serializable {

    private static final long serialVersionUID = 1L;

	@JsonProperty("ProductId")
	private String productId;

    @JsonProperty("InsuranceId")
    private String     companyId;
        
    @JsonFormat(pattern = "dd/MM/yyyy")
    @JsonProperty("EffectiveDateStart")
    private Date       effectiveDateStart ;
 
    @JsonProperty("CreatedBy")
    private String   createdBy ;
    
    @JsonProperty("PolicyTypeId")
    private String   policyTypeId;
    
    @JsonProperty("ProrataDetails")
    private List<ProrataMultiInsertReq> ProrataMultiInsertReq ; 
    
}
