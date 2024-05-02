package com.maan.eway.bean;

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

public class MenuMasterId {

	
	 private static final long serialVersionUID = 1L;
	 //--- ENTITY KEY ATTRIBUTES 
		
	  private Integer  menuId ;
		
	  private String   usertype ;
	  
	  private String   companyId ;
		
}
