package com.maan.eway.admin.service;

import com.maan.eway.admin.req.AttachIssuerReferalReq;
import com.maan.eway.admin.res.LoginCreationRes;

public interface LoginReferalService {

	LoginCreationRes attachIssuerReferal(AttachIssuerReferalReq req);
}
