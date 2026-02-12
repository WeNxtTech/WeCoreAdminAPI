package com.maan.eway.ttrncloseing.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.eway.ttrncloseing.bean.TTrnClosing;

import lombok.Data;
@Data
public class TtrnGetResForMonth {

	@JsonProperty("List")
	private List<TtrnRes> resList;
	
	@JsonIgnore
	private List<TTrnClosing> ttrnlist;
	
	@JsonProperty("Status")
	private String status ;
	
	@JsonProperty("EffDate")
	private String effDate ;
	
	
}
