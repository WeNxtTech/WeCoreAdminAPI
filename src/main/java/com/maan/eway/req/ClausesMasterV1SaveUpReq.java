/**
 * @author : Ashok Kumar S 
 * @since  : 19-02-2025
 */
package com.maan.eway.req;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ClausesMasterV1SaveUpReq {
	
	@JsonFormat(shape = Shape.STRING)
	@NotNull(message = "Company ID is required.")
	private Integer companyId;
	
	@JsonFormat(shape = Shape.STRING)
	@NotNull(message = "Product ID is required.")
	private Integer productId;
	
	@JsonFormat(shape = Shape.STRING)
	@NotNull(message = "Section ID is required.")
	private Integer sectionId;
	
	@JsonFormat(shape = Shape.STRING)
	@NotNull(message = "Cover ID is required.")
	private Integer coverId;

	@JsonFormat(shape = Shape.STRING)
	private Integer clausesId;
				
	@NotBlank(message = "Clauses short description is required and cannot be blank.")
	private String clausesShortDesc;
	
	@NotBlank(message = "Clauses description is required and cannot be blank.")
	private String clausesDescription;
	
	@NotBlank(message = "Created by is required and cannot be blank.")
	private String createdBy;

	@JsonFormat(pattern = "dd/MM/yyyy", shape = Shape.STRING)	
	@NotNull(message = "Effective Date Start is required and cannot be null.")
	@FutureOrPresent(message = "Effective start date must be a present or future date.")
	private LocalDate effectiveDateStart;
	
}
