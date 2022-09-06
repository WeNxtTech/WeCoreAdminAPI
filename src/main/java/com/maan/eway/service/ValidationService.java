package com.maan.eway.service;

import java.util.List;

import com.maan.eway.error.Error;
import com.maan.eway.master.req.CustomerSaveReq;

public interface ValidationService {

	List<Error> validateCustomerSave(CustomerSaveReq req) ;

	List<Error> validateCustomerUpdate(CustomerSaveReq req);
}
