/**
 * @author : Ashok Kumar S 
 * @since  : 11-02-2025
 */
package com.maan.eway.req;

import org.hibernate.validator.constraints.URL;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.fasterxml.jackson.annotation.JsonFormat.Shape;

@NoArgsConstructor
@Getter
@Setter
public class ApiIntegMasterSaveUpReq {
		
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
	
	@JsonProperty("status")
	@NotBlank(message = "Status is required, It should not be blank")
	private String status;
	
	@JsonProperty("apiDesc")
	@NotBlank(message = "Api Desc is required, It should not be blank")
	private String apiDesc;
	
	@JsonProperty("apiUrl")
	@NotBlank(message = "Api URL is required, It should not be blank")
	@URL(message = "Invalid URL format")
	private String apiUrl;

}
