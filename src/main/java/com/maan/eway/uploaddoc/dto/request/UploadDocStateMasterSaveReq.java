package com.maan.eway.uploaddoc.dto.request;

import java.util.Date;

import lombok.Data;

@Data
public class UploadDocStateMasterSaveReq {

	private Integer stateId;
	private String stateName;
	private String stateShortCode;
	private String countryId;
	private String regionCode;
	private String status;
	private Date effectiveDateStart;
	private Date effectiveDateEnd;
	private String coreAppCode;
	private String tiraCode;
	private String createdBy;
	private String remarks;
	private String regulatoryCode;
	private String city;
	private String suburb;
	private Integer areaGroup;
	private Integer cityId;
	private Integer suburbId;
	private String suburbLocal;
	private String stateNameLocal;
	private String cityLocal;
}
