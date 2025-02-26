/**
 * @author : Ashok Kumar S 
 * @since  : 25-02-2025
 */
package com.maan.eway.master.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ExcessMasterGetAllReq {

	@JsonProperty("InsuranceId")
	@NotBlank(message = "Insurance Id is required.")
	private String companyId;
	
	@JsonProperty("ProductId")
	@NotBlank(message = "Product Id is required.")
	private String productId;
	
	@JsonProperty("SectionId")
	@NotBlank(message = "Section Id is required.")
	private String sectionId;
		
}
