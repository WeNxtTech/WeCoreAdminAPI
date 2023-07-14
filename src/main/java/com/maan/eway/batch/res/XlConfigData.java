package com.maan.eway.batch.res;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class XlConfigData {
	
	@JsonProperty("DataType")
	private String datatype;
	@JsonProperty("RawTableColumns")
	private String rawTableColumns;

}
