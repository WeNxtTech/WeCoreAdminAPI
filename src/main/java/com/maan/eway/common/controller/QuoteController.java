package com.maan.eway.common.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maan.eway.common.req.NewQuoteReq;
import com.maan.eway.common.service.QuoteService;
import com.maan.eway.res.CommonRes;
import com.maan.eway.service.PrintReqService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/quote")
public class QuoteController {

	@Autowired
	private  PrintReqService reqPrinter;
	
	@Autowired
	private  QuoteService entityService ;
	
	@PostMapping("/newquote")
	@ApiOperation(value = "This method is Insert Cover Details")
	public ResponseEntity<CommonRes> generateNewQuote(@RequestBody NewQuoteReq req) {

		reqPrinter.reqPrint(req);
		// Save
		CommonRes res = entityService.generateNewQuote(req);
		
		if (res != null) {
			return new ResponseEntity<CommonRes>(res, HttpStatus.CREATED);
		} else {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
		

	}
}
