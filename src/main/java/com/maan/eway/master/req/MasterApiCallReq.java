package com.maan.eway.master.req;

import java.util.List;
import java.util.Map;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MasterApiCallReq {

	private String apiLink;
	
	private String primaryId;
	
	private String primaryKey;
	
	private String primaryTable;

	private String apiRequest;
	
	private List<Map<String,String>> mp;
	
	private String tokenl;
	
	private String param;
}
