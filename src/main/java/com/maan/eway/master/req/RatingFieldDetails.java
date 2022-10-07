package com.maan.eway.master.req;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class RatingFieldDetails {

	@JsonProperty("RatingFiledId")
    private String ratingFieldId     ;
	@JsonProperty("RangeYn")
    private String     rangeYn      ;
	@JsonProperty("ColumnsId")
    private String     columnsId ;
	
	@JsonProperty("Status")
    private String     status ;
	
}
