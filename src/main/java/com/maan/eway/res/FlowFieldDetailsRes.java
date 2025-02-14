/**
 * @author : Ashok Kumar S 
 * @since  : 10-02-2025
 */
package com.maan.eway.res;

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
public class FlowFieldDetailsRes {
	
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

    @JsonProperty("jsonKey")
    private String jsonKey;

    @JsonProperty("isHeader")
    private String isHeader;

    @JsonProperty("headerKeyId")  
    private String headerKeyid;

    @JsonProperty("isArray")
    private String isarray;

    @JsonProperty("dataType")
    private String datatype;

    @JsonProperty("pattern")
    private String pattern;

    @JsonProperty("defaultYn")
    private String defaultYn;

    @JsonProperty("defaultValue")
    private String defaultValue;

    @JsonProperty("status")
    private String status;

    @JsonProperty("queryId")
    @JsonFormat(shape = Shape.STRING, pattern = "0")
    private BigDecimal queryId;
    
    @JsonProperty("queryCol")
    private String queryCol;
    
    @JsonProperty("queryAlias")
    private String queryAlias;
    
}
