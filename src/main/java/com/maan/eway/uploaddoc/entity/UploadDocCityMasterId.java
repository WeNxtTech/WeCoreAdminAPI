package com.maan.eway.uploaddoc.entity;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class UploadDocCityMasterId implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer cityId;
	private String countryId;
	private String stateId;
	private Integer amendId;
}
