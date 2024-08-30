package com.maan.eway.res;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogDetailsRes {

	@JsonProperty("FilePath")
	private String filePath;
	@JsonProperty("FileName")
	private String fileName;
	
}
