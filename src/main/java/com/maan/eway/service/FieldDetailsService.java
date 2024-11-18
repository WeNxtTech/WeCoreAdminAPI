package com.maan.eway.service;

import com.maan.eway.master.req.GetFieldDetailsReq;
import com.maan.eway.master.req.SaveFieldDetailsReq;
import com.maan.eway.res.CommonRes;

public interface FieldDetailsService {

	CommonRes saveFieldDetails(SaveFieldDetailsReq req);

	CommonRes getFieldDetails(String fieldId,GetFieldDetailsReq req);

}