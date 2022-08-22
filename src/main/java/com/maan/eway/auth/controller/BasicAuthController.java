package com.maan.eway.auth.controller;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maan.eway.auth.dto.ChangePasswordReq;
import com.maan.eway.auth.service.AuthendicationService;
import com.maan.eway.auth.service.LoginValidatedService;
import com.maan.eway.error.Error;
import com.maan.eway.res.CommonRes;
import com.maan.eway.res.DropDownRes;
import com.maan.eway.service.DropDownService;
import com.maan.eway.service.PrintReqService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(  tags="Basic Authentication", description = "API's.")
@RequestMapping("/basicauth")
public class BasicAuthController {

	@Autowired
	private DropDownService dropDownService;
	
	@Autowired
	private PrintReqService reqPrinter;
	
	private Logger log = LogManager.getLogger(LoginController.class);
	
	@Autowired
	private AuthendicationService authservice;
	
	@Autowired
	private LoginValidatedService loginValidationComponent;
	
	/*
	@PostMapping("/getLoginEncryptResponse")   
	private LoginEncryptResponse getLoginEncryptResponse(@RequestBody PaymentResUrlReq request , HttpServletRequest http) {
		return authservice.getLoginEncryptResponse(request , http);
	} */
	
	@GetMapping("/inscompanies")
	@ApiOperation(value = "This method is to Get Insurance Companies Drop Down")
	public ResponseEntity<CommonRes> getInsuranceCompanies() {

		CommonRes data = new CommonRes();

		// Save
		List<DropDownRes> res = dropDownService.getInsuranceCompanies();
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
	
	@GetMapping("/usertypes")
	@ApiOperation(value = "This method is to User Types Drop Down")
	public ResponseEntity<CommonRes> getUserTypes() {

		CommonRes data = new CommonRes();

		// Save
		List<DropDownRes> res = dropDownService.getUserTypes();
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

	
	
	@PostMapping("/changepassword")
	@ApiOperation(value="This method is to change Login Password")
	public ResponseEntity<CommonRes> getChangePassword(@RequestBody ChangePasswordReq req) throws Exception {
		reqPrinter.reqPrint(req);
		CommonRes data = new CommonRes();
		
		// Validation
		
		List<Error> validation = loginValidationComponent.LoginChangePasswordValidation(req);
		if(validation!= null && validation.size()!=0) {
			data.setCommonResponse(null);
			data.setIsError(true);
			data.setErrorMessage(validation);
			data.setMessage("Failed");
			
			return new ResponseEntity<CommonRes>(data, HttpStatus.OK);
		}
		else {
			// Save 
			String res = authservice.LoginChangePassword(req);
			data.setCommonResponse(res);
			data.setIsError(false);
			data.setErrorMessage(Collections.emptyList());
			data.setMessage("Success");
			
			if( res !=null && StringUtils.isNotBlank(res)  ) {
				return new ResponseEntity<CommonRes>(data, HttpStatus.CREATED);
			}
			else {
				return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
			}
		}
	
	} 
}
