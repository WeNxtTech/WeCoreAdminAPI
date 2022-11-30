package com.maan.eway.fileupload;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FileDownloadRes {
	
	@JsonProperty("File")
	private String file;

}
