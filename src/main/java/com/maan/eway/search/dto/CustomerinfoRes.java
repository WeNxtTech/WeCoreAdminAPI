package com.maan.eway.search.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerinfoRes {

	private String customerId;
	private String customerReferenceNo;
	private String clientName;
	private String idNumber;
	private String address1;
	private String cityName;
	private String mobileNo1;
	private String mobileCode1;
	private String email1;

}
