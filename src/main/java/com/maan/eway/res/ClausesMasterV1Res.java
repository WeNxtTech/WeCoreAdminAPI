/**
 * @author : Ashok Kumar S 
 * @since  : 19-02-2025
 */
package com.maan.eway.res;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ClausesMasterV1Res {
	
	@JsonFormat(shape = Shape.STRING)	
	private Integer companyId;
	
	@JsonFormat(shape = Shape.STRING)	
	private Integer productId;
	
	@JsonFormat(shape = Shape.STRING)	
	private Integer sectionId;
	
	@JsonFormat(shape = Shape.STRING)	
	private Integer coverId;
	
	@JsonFormat(shape = Shape.STRING)
	private Integer clausesId;					
	
	private String clausesShortDesc;
	
	private String clausesDescription;
	
	private String status;
	
	@JsonFormat(pattern = "dd/MM/yyyy", shape = Shape.STRING)
	private LocalDate effectiveDateStart;
	
	@JsonFormat(pattern = "dd/MM/yyyy", shape = Shape.STRING)
	private LocalDate effectiveDateEnd;

}
