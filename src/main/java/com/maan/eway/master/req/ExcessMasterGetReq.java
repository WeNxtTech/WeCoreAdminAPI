/**
 * @author : Ashok Kumar S 
 * @since  : 25-02-2025
 */
package com.maan.eway.master.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ExcessMasterGetReq {

	@JsonProperty("InsuranceId")
	@NotBlank(message = "Insurance Id is required.")
	private String companyId;
	
	@JsonProperty("ProductId")
	@NotBlank(message = "Product Id is required.")
	private String productId;
	
	@JsonProperty("SectionId")
	@NotBlank(message = "Section Id is required.")
	private String sectionId;
	
	@JsonProperty("ExcessId")
	@JsonFormat(shape = Shape.STRING)
	@NotNull(message = "Excess Id is required.")
	private Integer excessId;
	
}
