package com.maan.eway.master.service;

import java.util.List;

import com.maan.eway.error.Error;
import com.maan.eway.master.req.CompaniesTaxGetAllReq;
import com.maan.eway.master.req.CompaniesTaxGetRes;
import com.maan.eway.master.req.CompaniesTaxSaveReq;
import com.maan.eway.master.req.CountryChangeStatusReq;
import com.maan.eway.master.req.CountryTaxDropDownReq;
import com.maan.eway.master.req.CountryTaxGetAllReq;
import com.maan.eway.master.req.CountryTaxGetReq;
import com.maan.eway.master.req.CountryTaxSaveReq;
import com.maan.eway.master.req.GetAllProductTaxReq;
import com.maan.eway.master.req.GetAllTaxExcemtedReq;
import com.maan.eway.master.req.ProductTaxGetRes;
import com.maan.eway.master.req.ProductTaxSaveReq;
import com.maan.eway.master.req.TaxExemptionSaveReq;
import com.maan.eway.master.req.TaxExemtedGetRes;
import com.maan.eway.master.res.CountryTaxGetRes;
import com.maan.eway.master.res.DocumentMasterGetRes;
import com.maan.eway.res.DropDownRes;
import com.maan.eway.res.SuccessRes;
import com.maan.eway.res.SuccessRes2;

public interface TaxExcemptionSetupService {


	List<Error> validateTaxesExemption(TaxExemptionSaveReq req);

	SuccessRes2 saveTaxesExemption(TaxExemptionSaveReq req);

	TaxExemtedGetRes getallTaxesExempted(GetAllTaxExcemtedReq req);

}
