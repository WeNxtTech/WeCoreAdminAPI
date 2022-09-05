package com.maan.eway.master.controller;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maan.eway.master.req.CoverGetReq;
import com.maan.eway.master.req.CustomerSaveReq;
import com.maan.eway.master.req.SectionGetReq;
import com.maan.eway.master.res.CoverGetRes;
import com.maan.eway.master.res.SectionGetRes;
import com.maan.eway.master.service.CustomerSaveService;
import com.maan.eway.res.CommonRes;
import com.maan.eway.res.SuccessRes;
import com.maan.eway.service.PrintReqService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping("/api")
@Api(tags = "Customer :Master ", description = "API's")
public class CustomersaveController {

	@Autowired
	private CustomerSaveService service;
	@Autowired
	private  PrintReqService reqPrinter;
	
	
	@PostMapping("/getsection")
	public ResponseEntity<CommonRes> getsection(@RequestBody SectionGetReq req) {
		CommonRes data = new CommonRes();
		List<SectionGetRes> res = service.getsection(req);
		data.setCommonResponse(res);
		data.setIsError(false);
		data.setErrorMessage(Collections.emptyList());
		data.setMessage("Success");
		
		if(data!=null) {
			return new ResponseEntity<CommonRes>(data, HttpStatus.CREATED);
		}
		else {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping("/getcover")
	public ResponseEntity<CommonRes> getcover(@RequestBody CoverGetReq req){
		CommonRes data = new CommonRes();
		List<CoverGetRes> res = service.getcover(req);
		data.setCommonResponse(res);
		data.setErrorMessage(Collections.emptyList());
		data.setIsError(false);
		data.setMessage("Success");
		if(data!=null) {
			return new ResponseEntity<CommonRes>(data, HttpStatus.CREATED);
		}
		else {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
		
	}
	
	@PostMapping("/insertcustomer")
	@ApiOperation(value = "This method is to insert Customer")
	public ResponseEntity<CommonRes> savecustomer(@RequestBody CustomerSaveReq req){
		CommonRes data = new CommonRes();
		reqPrinter.reqPrint(req);
		SuccessRes res = service.savecustomer(req);
		data.setCommonResponse(res);
		data.setErrorMessage(Collections.emptyList());
		data.setIsError(false);
		data.setMessage("Success");
		if(data!=null) {
			return new ResponseEntity<CommonRes>(data, HttpStatus.CREATED);
		}
		else {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
		
	}
	
	
}
