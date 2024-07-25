package com.maan.eway.master.res;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class CountryTaxGetRes {

	  @JsonProperty("TaxId")
		private String taxId;

		@JsonProperty("TaxName")
		private String taxName;

		@JsonProperty("TaxDesc")
		private String taxDesc;
		
		@JsonFormat(pattern = "dd/MM/yyyy")
		@JsonProperty("EffectiveDateStart")
		private Date effectiveDateStart;

		@JsonFormat(pattern = "dd/MM/yyyy")
		@JsonProperty("EntryDate")
		private Date entryDate;

		@JsonFormat(pattern = "dd/MM/yyyy")
		@JsonProperty("EffectiveDateEnd")
		private Date effectiveDateEnd;
		
		@JsonProperty("Remarks")
		private String remarks;
		
		@JsonProperty("CountryId")
		private String countryId;
		
		@JsonProperty("Status")
		private String status;

		@JsonProperty("CreatedBy")
		private String createdBy;
		
		@JsonProperty("CodeDescLocal")
		private String codeDescLocal;
}
