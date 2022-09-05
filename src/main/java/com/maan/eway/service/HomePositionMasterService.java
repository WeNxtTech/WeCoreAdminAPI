package com.maan.eway.service;

import java.util.List;

import com.maan.eway.master.req.GetQuoteCountReq;
import com.maan.eway.res.GetQuoteCountRes;

public interface HomePositionMasterService {

	List<GetQuoteCountRes> getCustomerQuoteCount(GetQuoteCountReq req);

}
