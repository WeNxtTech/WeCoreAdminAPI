/**
 * @author : Ashok Kumar S 
 * @since  : 13-02-2025
 */
package com.maan.eway.req;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class FlowFieldLOVGetReq {
	
	@JsonProperty("companyId")
	@JsonFormat(shape = Shape.STRING, pattern = "0")
	private BigDecimal companyId;

    @JsonProperty("productId")
    @JsonFormat(shape = Shape.STRING, pattern = "0")
    private BigDecimal productId;
    
    @JsonProperty("integType")
    private String integType;    

}
