package com.maan.eway.master.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class DuplicationCheck {
	@JsonProperty("CoverDesc" )
    private String coverDesc ;
    

    
    @JsonProperty("SubCoverDesc" )
    private String subCoverDesc ;
}
