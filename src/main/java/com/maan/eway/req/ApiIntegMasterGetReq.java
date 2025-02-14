/**
 * @author : Ashok Kumar S 
 * @since  : 10-02-2025
 */
package com.maan.eway.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ApiIntegMasterGetReq {
	
	@JsonProperty("companyId")
	@NotBlank(message = "Company ID is required, It should not be blank")
	private String companyId;
	
	@JsonProperty("productId")
	@JsonFormat(shape = Shape.STRING)
	@NotNull(message = "Product ID is required, It should not be null")
	private Integer productId;

	@JsonProperty("apiType")
	@NotBlank(message = "Api Type is required, It should not be blank")
	private String apiType;
	
}
