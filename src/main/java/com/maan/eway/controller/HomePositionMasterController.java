package com.maan.eway.controller;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maan.eway.master.req.GetPolicyDetailsReq;
import com.maan.eway.master.req.GetQuoteCountReq;
import com.maan.eway.master.req.GetQuoteDetailsReq;
import com.maan.eway.res.CommonRes;
import com.maan.eway.res.GetPolicyDetailsRes;
import com.maan.eway.res.GetQuoteCountRes;
import com.maan.eway.res.GetQuoteDetailsRes;
import com.maan.eway.service.HomePositionMasterService;
import com.maan.eway.service.PrintReqService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api")
@Api(tags = "Home Position Master ", description = "API's")
public class HomePositionMasterController {

	@Autowired
	private  HomePositionMasterService	 entityService;
	
	@Autowired
	private  PrintReqService reqPrinter;
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_APPROVER')")
	@PostMapping("/getquotecounts")
	@ApiOperation(value = "This method is Get Quote Count")
	public ResponseEntity<CommonRes> getCustomerQuoteCount(@RequestBody GetQuoteCountReq req) {

		reqPrinter.reqPrint(req);
		CommonRes data = new CommonRes();
		// Save
		List<GetQuoteCountRes> res = entityService.getCustomerQuoteCount(req);
		data.setCommonResponse(res);
		data.setIsError(false);
		data.setErrorMessage(Collections.emptyList());
		data.setMessage("Success");

		if (res != null) {
			return new ResponseEntity<CommonRes>(data, HttpStatus.CREATED);
		} else {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}

	}
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_APPROVER')")
	@PostMapping("/getquotedetails")
	@ApiOperation(value = "This method is Get Quote Details")
	public ResponseEntity<CommonRes> getCustomerQuoteDetails(@RequestBody GetQuoteDetailsReq req) {

		reqPrinter.reqPrint(req);
		CommonRes data = new CommonRes();
		// Save
		List<GetQuoteDetailsRes> res = entityService.getCustomerQuoteDetails(req);
		data.setCommonResponse(res);
		data.setIsError(false);
		data.setErrorMessage(Collections.emptyList());
		data.setMessage("Success");

		if (res != null) {
			return new ResponseEntity<CommonRes>(data, HttpStatus.CREATED);
		} else {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}

	} 
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_APPROVER')")
	@PostMapping("/getpolicydetails")
	@ApiOperation(value = "This method is Get Policy Details")
	public ResponseEntity<CommonRes> getCustomerPolicyDetails(@RequestBody GetPolicyDetailsReq req) {

		reqPrinter.reqPrint(req);
		CommonRes data = new CommonRes();
		// Save
		List<GetPolicyDetailsRes> res = entityService.getCustomerPolicyDetails(req);
		data.setCommonResponse(res);
		data.setIsError(false);
		data.setErrorMessage(Collections.emptyList());
		data.setMessage("Success");

		if (res != null) {
			return new ResponseEntity<CommonRes>(data, HttpStatus.CREATED);
		} else {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}

	} 
	
	
}
