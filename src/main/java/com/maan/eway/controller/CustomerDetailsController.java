package com.maan.eway.controller;

import java.util.ArrayList;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maan.eway.master.req.CustomerDetailsGetAllReq;
import com.maan.eway.master.req.CustomerGetReq;
import com.maan.eway.master.req.CustomerSaveReq;
import com.maan.eway.master.res.CustomerGetRes;
import com.maan.eway.master.res.CustomerGetallRes;
import com.maan.eway.master.service.CustomerDetailsService;
import com.maan.eway.req.SubUserTypeReq;
import com.maan.eway.res.CommonRes;
import com.maan.eway.res.DropDownRes;
import com.maan.eway.res.SuccessRes;
import com.maan.eway.service.DropDownService;
import com.maan.eway.service.PrintReqService;
import com.maan.eway.service.ValidationService;
import com.maan.eway.error.Error;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(tags = "CUSTOMER : Details ", description = "API's")
@RequestMapping("/api")
public class CustomerDetailsController {

	@Autowired
	private PrintReqService reqPrinter;

	@Autowired
	private CustomerDetailsService entityService;

	@Autowired
	private ValidationService validationService;
	
	@PostMapping("/insertcustomer")
	@ApiOperation(value = "This method is to insert Customer")
	public ResponseEntity<CommonRes> savecustomer(@RequestBody CustomerSaveReq req){
		CommonRes data = new CommonRes();
		reqPrinter.reqPrint(req);
		
		List<Error> error = validationService.validateCustomerSave(req)  ;
		if(error !=null && error.size()>0 ) {	
			data.setCommonResponse(error);
			data.setErrorMessage(Collections.emptyList());
			data.setIsError(false);
			data.setMessage("Success");
			return new ResponseEntity<CommonRes>(data, HttpStatus.CREATED);
		} else {
			SuccessRes res = entityService.savecustomer(req);
			data.setCommonResponse(res);
			data.setErrorMessage(Collections.emptyList());
			data.setIsError(false);
			data.setMessage("Success");
			
			if(res!=null) {
				return new ResponseEntity<CommonRes>(data, HttpStatus.CREATED);
			}
			else {
				return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
			}
		}
	}
	
	@PostMapping("/updatecustomer")
	@ApiOperation(value = "This method is to update Customer")
	public ResponseEntity<CommonRes> updateCustomer(@RequestBody CustomerSaveReq req){
		CommonRes data = new CommonRes();
		reqPrinter.reqPrint(req);
		
		List<Error> error = validationService.validateCustomerUpdate(req)  ;
		if(error !=null && error.size()>0 ) {	
			data.setCommonResponse(error);
			data.setErrorMessage(Collections.emptyList());
			data.setIsError(false);
			data.setMessage("Success");
			return new ResponseEntity<CommonRes>(data, HttpStatus.CREATED);
		} else {
			SuccessRes res = entityService.updateCustomer(req);
			data.setCommonResponse(res);
			data.setErrorMessage(Collections.emptyList());
			data.setIsError(false);
			data.setMessage("Success");
			if(res!=null) {
				return new ResponseEntity<CommonRes>(data, HttpStatus.CREATED);
			}
			else {
				return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
			}
		}
		
	}
	


	
	@PostMapping("/getcustomer")
	@ApiOperation(value = "This method is to Get Customer")
	public ResponseEntity<CommonRes> getcustomer(@RequestBody CustomerGetReq req){
		CommonRes data = new CommonRes();
		reqPrinter.reqPrint(req);
		CustomerGetRes res = entityService.getcustomer(req);
		data.setCommonResponse(res);
		data.setErrorMessage(Collections.emptyList());
		data.setIsError(false);
		data.setMessage("Success");
		if(res!=null) {
			return new ResponseEntity<CommonRes>(data, HttpStatus.CREATED);
		}
		else {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
		
	}

	@PostMapping("/getallcustomer")
	@ApiOperation(value = "This method is to Getall Customer")
	public ResponseEntity<CommonRes> getallcustomer(@RequestBody CustomerDetailsGetAllReq req){
		CommonRes data = new CommonRes();
		List<CustomerGetallRes> res = entityService.getallcustomer(req);
		data.setCommonResponse(res);
		data.setErrorMessage(Collections.emptyList());
		data.setIsError(false);
		data.setMessage("Success");
		if(res!=null) {
			return new ResponseEntity<CommonRes>(data, HttpStatus.CREATED);
		}
		else {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
		
	}


	
	
	}

