package com.maan.eway.uploaddoc.dto.request;

import java.util.Date;

import lombok.Data;

@Data
public class UploadDocCityMasterSaveReq {

	private Integer cityId;
	private String countryId;
	private String stateId;
	private Date effectiveDateEnd;
	private Date effectiveDateStart;
	private String cityName;
	private String status;
	private String remarks;
	private String coreAppCode;
	private String tiraCode;
	private String createdBy;
	private String regulatoryCode;
	private String cityNameLocal;
}
