/**
 * @author : Ashok Kumar S 
 * @since  : 12-02-2025
 */
package com.maan.eway.service;

import java.math.BigDecimal;
import java.util.List;

import com.maan.eway.bean.FieldQueryTablequery;
import com.maan.eway.error.Error;
import com.maan.eway.req.FieldQueryTableQuerySaveUpReq;
import com.maan.eway.res.FieldQueryTableQueryRes;

public interface FieldQueryTableQueryService {

	public List<Error> validateParametersOfFieldQueryTablequerySaveRequest(FieldQueryTableQuerySaveUpReq req);
	
	public FieldQueryTablequery saveUpdateFieldQueryTablequeryDetails(FieldQueryTableQuerySaveUpReq req);
	
	public List<FieldQueryTableQueryRes> getAllFieldQueryTablequeryDetails();
	
	public FieldQueryTableQueryRes getFieldQueryTablequeryDetails (BigDecimal queryId);
	
}
