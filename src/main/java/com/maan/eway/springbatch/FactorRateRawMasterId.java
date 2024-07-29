package com.maan.eway.springbatch;

import java.io.Serializable;

import com.maan.eway.bean.FactorRateMasterId;

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
public class FactorRateRawMasterId implements Serializable {
	
	 private static final long serialVersionUID = 1L;

	    //--- ENTITY KEY ATTRIBUTES 
	    private Integer    factorTypeId ;
	    
	    private Integer    sno ;
	    
	    private String     companyId ;
	    
	    private Integer    productId ;
	    
	    private String     branchCode ;
	    
	    private String     agencyCode ;
	    
	    private Integer    sectionId ;
	    
	    private Integer    coverId ;
	    
	    private Integer    subCoverId ;
	    
	    private String tranId;
	    
	    private Integer   amendId;

}
