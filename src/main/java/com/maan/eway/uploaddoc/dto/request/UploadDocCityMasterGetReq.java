package com.maan.eway.uploaddoc.dto.request;

import lombok.Data;

@Data
public class UploadDocCityMasterGetReq {

	private Integer cityId;
	private String countryId;
	private String stateId;
}
