package com.maan.eway.master.service;

import java.util.List;

import com.maan.eway.master.req.CompaniesTaxGetAllReq;
import com.maan.eway.master.req.CompaniesTaxGetRes;
import com.maan.eway.master.req.CompaniesTaxSaveReq;
import com.maan.eway.res.SuccessRes2;

public interface CompaniesTaxSetupService {

	List<String> validateCompanyTaxes(CompaniesTaxSaveReq req);

	SuccessRes2 saveComapnyTaxes(CompaniesTaxSaveReq req);

	CompaniesTaxGetRes getallCompanyTaxes(CompaniesTaxGetAllReq req);

}
