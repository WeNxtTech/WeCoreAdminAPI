package com.maan.eway.admin.service;

import java.util.Date;
import java.util.List;

import com.maan.eway.admin.req.AttachCompnayProductRequest;
import com.maan.eway.admin.req.BrokerCompanyProductGetReq;
import com.maan.eway.admin.req.BrokerCompanyProductsGetRes;
import com.maan.eway.admin.req.BrokerProductCompaniesRes;
import com.maan.eway.admin.req.BrokerProductGetReq;
import com.maan.eway.admin.res.LoginCreationRes;
import com.maan.eway.admin.res.LoginProductCriteriaRes;

public interface LoginProductService {

	LoginCreationRes saveBrokerProductDetails(AttachCompnayProductRequest req);

	List<BrokerProductCompaniesRes> getBrokerProducts(BrokerProductGetReq req);

	List<BrokerCompanyProductsGetRes> getBrokerCompanyProducts(BrokerCompanyProductGetReq req);

	List<LoginProductCriteriaRes> getBrokerProductDetails(String loginId, List<String> companyIds, Date today);
}
