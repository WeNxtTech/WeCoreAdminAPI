package com.maan.eway.bean;

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
public class BuildingRiskDetailsId implements Serializable{


    private static final long serialVersionUID = 1L;

    //--- ENTITY KEY ATTRIBUTES 
    private String     requestReferenceNo ;
    
    private Integer    riskId ;
    
    private String     quoteNo ;
    
    private String  sectionId;
    
    private Integer    locationId ;

	private Integer coverId;


}
