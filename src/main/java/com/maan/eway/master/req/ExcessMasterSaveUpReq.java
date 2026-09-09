/**
 * @author : Ashok Kumar S 
 * @since  : 25-02-2025
 */
package com.maan.eway.master.req;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ExcessMasterSaveUpReq {
	
	@JsonProperty("InsuranceId")
	@NotBlank(message = "Insurance Id is required.")
	private String companyId;
	
	@JsonProperty("ProductId")
	@NotBlank(message = "Product Id is required.")
	private String productId;
	
	@JsonProperty("SectionId")
	@NotBlank(message = "Section Id is required.")
	private String sectionId;
	
	@JsonProperty("CoverId")
	private String coverId;
	
	@JsonProperty("coverName")
	private String coverName;
	
	@JsonProperty("ExcessId")
	private Integer ExcessId;
	
	@JsonProperty("ExcessPercentage")
	@NotNull(message = "Section Id is required.")
	private Double  excessPercentage;
	
	@JsonProperty("ExcessAmount")
	@NotNull(message = "Excess Amount is required.")
	private Double excessAmount;	
	
	@JsonProperty("ExcessDescription")
	@NotBlank(message = "Excess Description is required.")
	private String excessDescription;
	
	@JsonProperty("Currency")
	@NotBlank(message = "Currency is required.")
	private String currency;
	
	@JsonProperty("CreatedBy")
	@NotBlank(message = "Created By is required.")
	private String createdBy;
	
	@JsonProperty("RegulatoryCode")
	private String regulatoryCode;

	@JsonProperty("CoreAppCode")
	private String coreAppCode;
	
	@JsonProperty("BranchCode")
	private String branchCode;
		
	@JsonProperty("Status")
	@NotBlank(message = "Status is required.")
	@Pattern(regexp = "^(Y|N)$", message = "provide valid status.")
	private String status;
	
	@JsonProperty("EffectiveDateStart")
	@NotNull(message = "Effective Date Start is required.")
	@FutureOrPresent(message = "Effective Date Start accepts present or future date.")
	@JsonFormat(pattern = "dd/MM/yyyy", shape = Shape.STRING)
	private LocalDate  effectiveDateStart;
	
}
