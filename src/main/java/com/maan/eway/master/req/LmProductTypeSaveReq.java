package com.maan.eway.master.req;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class LmProductTypeSaveReq implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@JsonProperty("PtType")
	private String     PT_TYPE ;

	@JsonProperty("PtTypeDesc")
    private String     PT_TYPE_DESC ;

	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("EffectiveDateStart")
    private Date       EFFECTIVE_DATE_START ;

	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("EffectiveDateEnd")
    private Date       EFFECTIVE_DATE_END ;
    
	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("EntryDate")
    private Date       ENTRY_DATE ;
    
	@JsonProperty("CreatedBy")
    private String CREATED_BY;
    
	@JsonProperty("UpdatedBy")
    private String UPDATED_BY;
    
	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("UpdatedDate")
    private Date UPDATED_DATE;
    
	@JsonProperty("AmendId")
    private Integer AMEND_ID;
    
	@JsonProperty("Status")
    private String STATUS;

}
