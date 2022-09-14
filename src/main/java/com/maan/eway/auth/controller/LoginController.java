package com.maan.eway.auth.controller;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.eway.admin.req.MenuListReq;
import com.maan.eway.admin.res.LoginBrokerDetailsGetRes;
import com.maan.eway.auth.dto.CommonLoginRes;
import com.maan.eway.auth.dto.LoginRequest;
import com.maan.eway.auth.dto.LogoutRequest;
import com.maan.eway.auth.dto.Menu;
import com.maan.eway.auth.service.AuthendicationService;
import com.maan.eway.auth.service.LoginValidatedService;
import com.maan.eway.res.CommonRes;
import com.maan.eway.service.PrintReqService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(  tags="LOGIN : Login Token Creation", description = "API.")
@RequestMapping("/authentication")
public class LoginController {
	
	@Autowired
	private AuthendicationService authservice;
	@Autowired
	private LoginValidatedService loginValidationComponent;
	@Autowired
	private PrintReqService reqPrinter;

	@PostMapping("/login")
	@ApiOperation(value="This method is to Create Token For Access Other Apis")
	public ResponseEntity<CommonLoginRes> getloginToken(@RequestBody LoginRequest mslogin, HttpServletRequest http)  {
		CommonLoginRes res = new CommonLoginRes();
		reqPrinter.reqPrint(mslogin);
		res =loginValidationComponent.loginInputValidation(mslogin); 
		if(res.getErrorMessage()!=null &&  res.getErrorMessage().size()>0 ) {
			return new ResponseEntity<CommonLoginRes>(res, HttpStatus.OK);
		} 
		
		res = authservice.checkUserLogin(mslogin,http);
		if(res.getCommonResponse() !=null) {
			return new ResponseEntity<CommonLoginRes>(res, HttpStatus.CREATED);
		}
		else {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
		
	}
	

	
}
