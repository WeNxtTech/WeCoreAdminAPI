package com.maan.eway.bean;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginMasterId implements Serializable {

	private static final long serialVersionUID = 1L;

	private String loginId;

	private String companyId;
	
	private String     agencyCode ;
	
	private String     branchCode ;
	
	private String     userType ;
}
