package com.maan.eway.notif.service;

import java.util.List;

import com.maan.eway.notif.req.SmsConfigInsertReq;
import com.maan.eway.notif.req.SmsGetReq;
import com.maan.eway.notif.res.SmsMasterGetRes;
import com.maan.eway.res.SuccessRes;

public interface SmsConfigMasterService {


	SuccessRes insertsmsmaster(SmsConfigInsertReq req);

	SmsMasterGetRes getbysmsid(SmsGetReq req);

	List<String> validatesmsmaster(SmsConfigInsertReq req);

}
