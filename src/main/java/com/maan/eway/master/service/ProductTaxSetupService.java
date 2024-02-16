package com.maan.eway.master.service;

import java.util.List;

import com.maan.eway.master.req.GetAllProductTaxReq;
import com.maan.eway.master.req.ProductTaxGetRes;
import com.maan.eway.master.req.ProductTaxSaveReq;
import com.maan.eway.res.SuccessRes2;

public interface ProductTaxSetupService {


	List<String> validateProductTaxes(ProductTaxSaveReq req);

	SuccessRes2 saveProductTaxes(ProductTaxSaveReq req);

	ProductTaxGetRes getallProductTaxes(GetAllProductTaxReq req);

	ProductTaxGetRes getactiveProductTaxes(GetAllProductTaxReq req);

}
