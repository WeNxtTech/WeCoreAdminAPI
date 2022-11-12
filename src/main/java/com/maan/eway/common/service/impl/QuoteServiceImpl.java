package com.maan.eway.common.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.maan.eway.common.req.NewQuoteReq;
import com.maan.eway.res.CommonRes;
import com.maan.eway.common.service.QuoteService;
import com.maan.eway.common.service.QuoteThreadService;


@Service
public class QuoteServiceImpl implements QuoteService {


	@Autowired
	private QuoteThreadService otSer ;
	
	private Logger log = LogManager.getLogger(QuoteServiceImpl.class);
	
	@Override
	public CommonRes generateNewQuote(NewQuoteReq req) {
			CommonRes	res = otSer.call_OT_Insert(req);
			return res ;
			
	}


}
