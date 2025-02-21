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
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ClausesMasterV1StatusChangeReq {
	
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
	@NotNull(message = "Clauses ID is required.")
	private Integer clausesId;
	
	@NotBlank(message = "Status is required.")
	private String status;
	
	@NotBlank(message = "Updated By is required.")
	private String updatedBy;

	@NotNull(message = "Effectve On is reuired.")
	@FutureOrPresent(message = "Effective on date must be a present or future date.")
	@JsonFormat(pattern = "dd/MM/yyyy", shape = Shape.STRING)
	private LocalDate effectiveOn;
	
}
