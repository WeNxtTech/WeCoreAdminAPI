package com.maan.eway.master.req;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class FactorTypeMasterSaveReq implements Serializable {

    private static final long serialVersionUID = 1L;

	@JsonProperty("FactorId")
	private String factorId;

	@JsonProperty("FactorName")
	private String factorName;

	@JsonProperty("FactorDesc")
	private String factorDesc;

	@JsonProperty("InputTable")
	private String inputTable;

	@JsonProperty("InputColumn")
	private String inputColumn;
	
	@JsonProperty("Status")
	private String status;
	
	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("EffectiveDateStart")
	private Date effectiveDateStart;

	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("EffectiveDateEnd")
	private Date effectiveDateEnd;
	
	@JsonProperty("Remarks")
	private String remarks;
	
	@JsonProperty("ProductId")
	private String productId;
	
	@JsonProperty("CreatedBy")
	private String createdBy;

}
