package com.maan.eway.common.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.maan.eway.common.req.NewQuoteReq;
import com.maan.eway.common.service.QuoteService;
import com.maan.eway.common.service.QuoteThreadService;
import com.maan.eway.error.Error;
import com.maan.eway.res.CommonRes;

@Service
public class QuoteServiceImpl implements QuoteService {


	@Autowired
	private QuoteThreadService otSer ;
	
	private Logger log = LogManager.getLogger(QuoteServiceImpl.class);
	
	@Override
	public CommonRes generateNewQuote(NewQuoteReq req) {
		CommonRes response = new CommonRes();
		List<Error> errors = new ArrayList<Error>();
		try {
			
			NewQuoteRes	res = call_OT_Prem(req);

			// Response 
			response.setCommonResponse(res);
			response.setIsError(false);
			response.setErrorMessage(Collections.emptyList());
			response.setMessage("Success");

		} catch (Exception e) {
			log.error(e);
			errors.add(new Error("01" ,"Common Error" , e.getMessage()));
			response.setCommonResponse(null);
			response.setIsError(true);
			response.setErrorMessage(errors);
			response.setMessage("Failed");
		}
		return response;
	}

	
	private NewQuoteRes call_OT_Prem(NewQuoteReq req) {
		NewQuoteRes res = new NewQuoteRes();
		try {

			res = otSer.call_OT_Insert(req);
			return res;
			
		} catch (Exception e) {
			log.error(e);
			return null ;
		}
	}
}
