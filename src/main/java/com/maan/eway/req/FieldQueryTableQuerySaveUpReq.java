/**
 * @author : Ashok Kumar S 
 * @since  : 12-02-2025
 */
package com.maan.eway.req;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@Getter
@Setter
public class FieldQueryTableQuerySaveUpReq {
	
	@JsonProperty("queryId")
	@JsonFormat(shape = Shape.STRING, pattern = "0")
    private BigDecimal queryId;

    @JsonProperty("queryName")
    private String queryName;

    @JsonProperty("sqlQuery")
    private String sqlQuery;

}
