package com.maan.eway.common.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.maan.eway.common.req.EserviceCustomerSaveReq;
import com.maan.eway.common.service.CustomerSaveValidationService;
import com.maan.eway.error.Error;

@Service
public class CustomerSaveValidationServiceImpl implements CustomerSaveValidationService {

	@Override
	public List<Error> validateCustomerDetails(EserviceCustomerSaveReq req) {
		// TODO Auto-generated method stub
		return null;
	}

}
