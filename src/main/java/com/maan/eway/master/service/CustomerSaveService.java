package com.maan.eway.master.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.maan.eway.master.req.CoverGetReq;
import com.maan.eway.master.req.CustomerSaveReq;
import com.maan.eway.master.req.SectionGetReq;
import com.maan.eway.master.res.CoverGetRes;
import com.maan.eway.master.res.SectionGetRes;
import com.maan.eway.res.SuccessRes;


public interface CustomerSaveService {

	List<SectionGetRes> getsection(SectionGetReq req);

	List<CoverGetRes> getcover(CoverGetReq req);

	SuccessRes savecustomer(CustomerSaveReq req);

}
