package com.maan.eway.uploaddoc.entity;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Composite primary key for entity "StateMaster" (table "eway_state_master").
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class UploadDocStateMasterId implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer stateId;
	private String stateShortCode;
	private String countryId;
	private String regionCode;
	private Integer cityId;
	private Integer suburbId;
	private Integer amendId;
}
