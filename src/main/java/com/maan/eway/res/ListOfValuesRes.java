/**
 * @author : Ashok Kumar S 
 * @since  : 13-02-2025
 */
package com.maan.eway.res;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ListOfValuesRes {
	
    @JsonProperty("code")
    private String code;

    @JsonProperty("codeDesc")
    private String codeDesc;

}
