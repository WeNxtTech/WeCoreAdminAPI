package com.maan.eway.batch.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.eway.batch.res.EwayUploadRes;

import lombok.Data;

@Data
public class EwayBatchReq {
	
	@JsonProperty("EwayUploadRes")
	private EwayUploadRes ewayUploadRes;
}
