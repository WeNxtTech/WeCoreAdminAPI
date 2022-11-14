package com.maan.eway.master.req;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class UwQuestionChangeStatusReq {

	 @JsonProperty("UwQuestionId")
	 private String uwQuestionId;
	 
	 @JsonProperty("ProductId")
	 private String productId;
	 
	 @JsonProperty("SectionId")
	 private String sectionId;
	 
	 @JsonProperty("CoverId")
	 private String coverId;
	 
	 @JsonProperty("Status")
	 private String status;
	 
	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("EffectiveDateStart")
	private Date effectiveDateStart;
}
