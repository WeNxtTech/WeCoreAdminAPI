package com.maan.eway.master.req;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SaveFieldDetailsReq {
	
	@JsonProperty("ProductId")
	private String productId;
	
	@JsonProperty("SectionId")
	private String sectionId;
	
	@JsonProperty("InsuranceId")
	private String insuranceId;
	
	@JsonProperty("FieldId")
	private String fieldId;
	
	@JsonProperty("EffectiveDate")
	private String effectiveDate;
	
	@JsonProperty("LoginId")
	private String loginId;
	
	@JsonProperty("Status")
	private String status;
	
	@JsonProperty("Fields")
	private List<Fieldvalues> fields;
	
	@JsonProperty("FieldName")
	private String fieldName;
	
}
