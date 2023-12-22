package com.maan.eway.master.service;

import java.util.List;

import com.maan.eway.error.Error;
import com.maan.eway.master.req.LmProductTypeChangeStatus;
import com.maan.eway.master.req.LmProductTypeDropDownReq;
import com.maan.eway.master.req.LmProductTypeGetDetailsReq;
import com.maan.eway.master.req.LmProductTypeSaveReq;
import com.maan.eway.master.res.LmProductTypeResponse;
import com.maan.eway.res.DropDownRes;
import com.maan.eway.res.SuccessRes;

public interface LmProductTypeService {

	List<Error> validation(LmProductTypeSaveReq req);

	SuccessRes saveProductTypes(LmProductTypeSaveReq req);

	List<LmProductTypeResponse> getallproducttypedetails(LmProductTypeGetDetailsReq req);

	List<LmProductTypeResponse> getproductTypeDetails(LmProductTypeGetDetailsReq req);

	List<LmProductTypeResponse> getProductTypeById(LmProductTypeGetDetailsReq req);

	SuccessRes changeStatusOfProductType(LmProductTypeChangeStatus req);

	List<DropDownRes> getProductTypeDropDown(LmProductTypeDropDownReq req);

}
