/**
 * @author : Ashok Kumar S 
 * @since  : 11-02-2025
 */
package com.maan.eway.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class ApiIntegMasterGetAllReq {
	
	@JsonProperty("companyId")
	@NotBlank(message = "Company ID is required, It should not be blank")
	private String companyId;
	
	@JsonProperty("productId")
	@JsonFormat(shape = Shape.STRING)
	@NotNull(message = "Product ID is required, It should not be null")
	private Integer productId;
	
}
