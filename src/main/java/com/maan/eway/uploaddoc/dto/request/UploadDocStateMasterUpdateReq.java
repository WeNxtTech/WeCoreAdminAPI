package com.maan.eway.uploaddoc.dto.request;

import java.util.Date;

import lombok.Data;

/**
 * Update/amend request. The business key fields identify the record family;
 * the service resolves the current highest AMEND_ID and creates the next one.
 */
@Data
public class UploadDocStateMasterUpdateReq {

	// business key (identifies the record family being amended)
	private Integer stateId;
	private String stateShortCode;
	private String countryId;
	private String regionCode;
	private Integer cityId;
	private Integer suburbId;

	// amendable fields
	private String stateName;
	private String status;
	private Date effectiveDateStart;
	private Date effectiveDateEnd;
	private String coreAppCode;
	private String tiraCode;
	private String updatedBy;
	private String remarks;
	private String regulatoryCode;
	private String city;
	private String suburb;
	private Integer areaGroup;
	private String suburbLocal;
	private String stateNameLocal;
	private String cityLocal;
}
