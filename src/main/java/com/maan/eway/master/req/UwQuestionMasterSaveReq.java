package com.maan.eway.master.req;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class UwQuestionMasterSaveReq implements Serializable {

    private static final long serialVersionUID = 1L;

	@JsonProperty("InsuranceId")
    private String    companyId ;
    
	@JsonProperty("ProductId")
    private String    productId ;
    
	@JsonProperty("UwQuestionId")
    private String   uwQuestionId ;
    
	@JsonProperty("UwQuestionDesc")
    private String   uwQuestionDesc ;
    
	@JsonProperty("QuestionType")
    private String   questionType ;
    
	@JsonFormat(pattern ="dd/MM/yyyy")
	@JsonProperty("EffectiveDateStart")
	private Date effectiveDateStart;

	@JsonFormat(pattern ="dd/MM/yyyy")
	@JsonProperty("EffectiveDateEnd")
	private Date effectiveDateEnd;

	@JsonProperty("Remarks")
	private String remarks;
	
	@JsonProperty("Status")
	private String status;

}
