package com.maan.eway.master.service;

import java.util.List;

import com.maan.eway.error.Error;
import com.maan.eway.master.req.CountryChangeStatusReq;
import com.maan.eway.master.req.CountryTaxDropDownReq;
import com.maan.eway.master.req.CountryTaxGetAllReq;
import com.maan.eway.master.req.CountryTaxGetReq;
import com.maan.eway.master.req.CountryTaxSaveReq;
import com.maan.eway.master.res.CountryTaxGetRes;
import com.maan.eway.master.res.DocumentMasterGetRes;
import com.maan.eway.res.DropDownRes;
import com.maan.eway.res.SuccessRes;
import com.maan.eway.res.SuccessRes2;

public interface CountryTaxSetupService {

	List<String> validateCountryTax(CountryTaxSaveReq req);

	SuccessRes2 saveCountryTax(CountryTaxSaveReq req);

	List<CountryTaxGetRes> getallCountryTax(CountryTaxGetAllReq req);

	CountryTaxGetRes getByCountryTaxId(CountryTaxGetReq req);

	List<CountryTaxGetRes> getActiveCountryTax(CountryTaxGetAllReq req);

	List<DropDownRes> getCountryTaxDropdown(CountryTaxDropDownReq req);

	SuccessRes2 changeStatusOfCountryTax(CountryChangeStatusReq req);

}
