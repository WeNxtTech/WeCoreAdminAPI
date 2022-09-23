package com.maan.eway.master.req;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MotorBodySaveReq {

	@JsonProperty("BODYID")
	private int bodyId;
	@JsonProperty("BODYNAMEEN")
	private String bodyNameEn;
	@JsonProperty("STATUS")
	private String status;
	
	@JsonFormat(pattern="dd/MM/yyyy")
	@JsonProperty("ENTRY_DATE")
	private Date entryDate;

	@JsonFormat(pattern="dd/MM/yyyy")
	@JsonProperty("EFFECTIVE_DATE")
	private Date effectiveDate;
	@JsonProperty("AMEND_ID")
	private int amendId	;
	@JsonProperty("REMARKS")
	private String remarks;
	@JsonProperty("INSCOMPANYID")
	private String insCompanyId;
	@JsonProperty("SEATING_CAPACITY")
	private int seatingCapacity	;
	@JsonProperty("TONNAGE")
	private int tonnage	;
	@JsonProperty("CYLINDERS")
	private int cylinders	;
	
}                                      
  
   