/**
 * @author : Ashok Kumar S 
 * @since  : 19-02-2025
 */
package com.maan.eway.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ClausesMasterV1GetAllReq {
	
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

}
