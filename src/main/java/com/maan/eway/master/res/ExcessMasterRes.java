/**
 * @author : Ashok Kumar S 
 * @since  : 25-02-2025
 */
package com.maan.eway.master.res;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ExcessMasterRes {
	
	@JsonProperty("CompanyId")
	private String companyId;
	
	@JsonProperty("ProductId")
	private String productId;
	
	@JsonProperty("SectionId")
	private String sectionId;
	
	@JsonProperty("CoverId")
	private String coverId;
		
	@JsonProperty("CoverName")
	private String coverName;
		
	@JsonProperty("ExcessPercentage")
	@JsonFormat(shape = Shape.STRING)
	private Integer excessPercentage;
	
	@JsonProperty("ExcessAmount")
	@JsonFormat(shape = Shape.STRING)
	private Double excessAmount;	
	
	@JsonProperty("ExcessDescription")
	private String excessDescription;
	
	@JsonProperty("currency")
	private String Currency;
	
	@JsonProperty("CreatedBy")
	private String createdBy;
	
	@JsonProperty("RegulatoryCode")
	private String regulatoryCode;

	@JsonProperty("CoreAppCode")
	private String coreAppCode;
	
	@JsonProperty("ExcessId")
	private String excessId;
	
	@JsonProperty("Status")
	private String status;	
	
	@JsonProperty("EffectiveDateStart")
	private Date effectiveDateStart;
	
}
