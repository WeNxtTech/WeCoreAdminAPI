package com.maan.eway.admin.req;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ProductMasterGetAllReq implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("Limit")
    private String limit;
    
    @JsonProperty("Offset")
    private String offset;
    
	/*
	 * @JsonProperty("BankCode") private String bankCode;
	 */
    
	/*
	 * @JsonFormat(pattern ="dd/MM/yyyy")
	 * 
	 * @JsonProperty("EffectiveDate") private Date effectiveDate;
	 */


}
