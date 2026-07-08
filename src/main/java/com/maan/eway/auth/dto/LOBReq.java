package com.maan.eway.auth.dto;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class LOBReq implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonProperty("Id")
	private Long id;

	@JsonProperty("ClassName")
	private String className;

	@JsonProperty("ClassCode")
	private String classCode;
	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("EffectiveDateStart")
	private Date effectiveDateStart;

	@JsonProperty("ClassDescription")
	private String classDescription;

	@JsonProperty("RegulatoryCode")
	private String regulatoryCode;

	@JsonProperty("CoreAppCode")
	private String coreAppCode;

	@JsonProperty("ProductIconId")
	private String productIconId;

	@JsonProperty("Status")
	private String status;

	@JsonProperty("Remarks")
	private String remarks;

	@JsonProperty("CreatedBy")
	private String createdBy;

}
