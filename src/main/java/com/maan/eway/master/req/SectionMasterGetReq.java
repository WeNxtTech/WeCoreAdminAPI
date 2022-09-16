package com.maan.eway.master.req;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class SectionMasterGetReq {
	@JsonProperty("SectionId")
    private Integer     sectionId     ;
	
	@JsonProperty("InsuranceId")
    private String insuranceId;
    
}
