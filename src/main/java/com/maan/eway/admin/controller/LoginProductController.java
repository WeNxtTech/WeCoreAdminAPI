package com.maan.eway.admin.controller;

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

import com.maan.eway.admin.req.AttachCompnayProductRequest;
import com.maan.eway.admin.req.BrokerCompanyProductGetReq;
import com.maan.eway.admin.req.BrokerCompanyProductsGetRes;
import com.maan.eway.admin.req.BrokerProductCompaniesRes;
import com.maan.eway.admin.req.BrokerProductGetReq;
import com.maan.eway.admin.res.LoginCreationRes;
import com.maan.eway.admin.service.LoginProductService;
import com.maan.eway.admin.service.LoginValidationService;
import com.maan.eway.error.Error;
import com.maan.eway.res.CommonRes;
import com.maan.eway.res.DropDownRes;
import com.maan.eway.service.DropDownService;
import com.maan.eway.service.PrintReqService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(tags = "ADMIN : Login Product ", description = "API's")
@RequestMapping("/admin")
public class LoginProductController {


	@Autowired
	private  LoginProductService entityService;
	
	@Autowired
	private LoginValidationService validationService ;

	@Autowired
	private PrintReqService reqPrinter;
	
	
//*************************************** Add Products Apis **********************************************************//
		
	@PostMapping("/attachbrokerproducts")
	@ApiOperation(value="This method is to Attach Broker Products")
	public ResponseEntity<CommonRes> attachBrokerProducts(@RequestBody  AttachCompnayProductRequest req) {
		reqPrinter.reqPrint(req);
		CommonRes data = new CommonRes();
		List<Error> validation = validationService.validateBrokerProductReq(req);
		//// validation
		if (validation != null && validation.size() != 0) 	{
			data.setCommonResponse(null);
			data.setIsError(true);
			data.setErrorMessage(validation);
			data.setMessage("Failed");
			return new ResponseEntity<CommonRes>(data, HttpStatus.OK);

		} else {
			/////// save
			LoginCreationRes res = entityService.saveBrokerProductDetails(req);
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

//*************************************** Get Products Apis **********************************************************//
	
	@PostMapping("/getbrokerproducts")
	@ApiOperation(value="This method is to Get Broker Products")
	public ResponseEntity<CommonRes> getBrokerProducts(@RequestBody  BrokerProductGetReq req) {
		reqPrinter.reqPrint(req);
		CommonRes data = new CommonRes();
		
		/////// get
		List<BrokerProductCompaniesRes> res = entityService.getBrokerProducts(req);
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
	
	
//*************************************** Get One CompanyProducts Apis **********************************************************//	
	

	@PostMapping("/getbrokeronecompanyproducts")
	@ApiOperation(value="This method is to Get Broker Company Products")
	public ResponseEntity<CommonRes> getBrokerCompanyProducts(@RequestBody  BrokerCompanyProductGetReq req) {
		reqPrinter.reqPrint(req);
		CommonRes data = new CommonRes();
		
		/////// get
		List<BrokerCompanyProductsGetRes> res = entityService.getBrokerCompanyProducts(req);
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
