package com.maan.eway.uploaddoc.dto.request;

import lombok.Data;

/**
 * Identifies a record family by its business key (AMEND_ID excluded — the
 * latest amendment is always resolved server-side).
 */
@Data
public class UploadDocStateMasterGetReq {

	private Integer stateId;
	private String stateShortCode;
	private String countryId;
	private String regionCode;
	private Integer cityId;
	private Integer suburbId;
}
