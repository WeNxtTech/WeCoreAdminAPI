package com.maan.eway.master.res;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class SectionDocumentConfigRes {

	@JsonProperty("Id")
	private String id;
	
	@JsonProperty("Type")
	private String type;
	
	@JsonProperty("Name")
	private String Name;
	
	@JsonProperty("Status")
	private String status;
	
	@JsonProperty("StatusDesc")
	private String statusDesc;

	
	@JsonProperty("DocumentDetails")
	private List<DocumentConfigRes> documentList ;

}
