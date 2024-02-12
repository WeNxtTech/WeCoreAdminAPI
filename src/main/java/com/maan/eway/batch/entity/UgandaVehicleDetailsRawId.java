package com.maan.eway.batch.entity;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UgandaVehicleDetailsRawId implements Serializable{/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long rowNum;
	
	private String requestReferenceNo;

}
