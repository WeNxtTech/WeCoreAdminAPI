/**
 * @author : Ashok Kumar S 
 * @since  : 11-02-2025
 */
package com.maan.eway.req;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.fasterxml.jackson.annotation.JsonFormat.Shape;

@NoArgsConstructor
@Getter
@Setter
public class FlowFieldDetailsGetReq {
	
	@JsonProperty("companyId")
	@JsonFormat(shape = Shape.STRING, pattern = "0")
	private BigDecimal companyId;

    @JsonProperty("productId")
    @JsonFormat(shape = Shape.STRING, pattern = "0")
    private BigDecimal productId;

    @JsonProperty("integType")
    private String integType;

    @JsonProperty("keyId")
    @JsonFormat(shape = Shape.STRING, pattern = "0")
    private BigDecimal keyId;
}
