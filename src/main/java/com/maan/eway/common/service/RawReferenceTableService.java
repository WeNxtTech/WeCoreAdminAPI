package com.maan.eway.common.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.maan.eway.common.req.RawReferenceTableReq;
import com.maan.eway.common.res.RawReferenceTableRes;

@Service
public interface RawReferenceTableService {


	List<RawReferenceTableRes> tabledetails();

	List<RawReferenceTableRes> columndetails(RawReferenceTableReq req);

}
