package com.maan.eway.batch.entity;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EwayEmplyeeDetailRawId implements Serializable {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5962317177659569306L;


	private String requestReferenceNo;
	
	
	private Integer riskId;
	
	
	private String quoteNo;
	
	
	private Integer productId;
	
	
	private Integer companyId;
	
	private Integer nationalityId;

}
