package com.maan.eway.admin.service;

import com.maan.eway.admin.req.AttachCompnayProductRequest;
import com.maan.eway.admin.res.LoginCreationRes;

public interface LoginProductService {

	LoginCreationRes saveBrokerProductDetails(AttachCompnayProductRequest req);
}
