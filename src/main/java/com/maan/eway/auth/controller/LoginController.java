package com.maan.eway.auth.controller;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maan.eway.auth.dto.CommonLoginResponse;
import com.maan.eway.auth.dto.LoginRequest;
import com.maan.eway.auth.service.AuthendicationService;
import com.maan.eway.auth.service.LoginValidatedService;
import com.maan.eway.error.Error;
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
	public ResponseEntity<CommonRes> getloginToken(@RequestBody LoginRequest mslogin, HttpServletRequest http)  {
		CommonRes res = new CommonRes();
		res =loginValidationComponent.loginInputValidation(mslogin); 
		if(res.getErrorMessage()!=null &&  res.getErrorMessage().size()>0 ) {
			return new ResponseEntity<CommonRes>(res, HttpStatus.OK);
		} 
		
		res = authservice.checkUserLogin(mslogin,http);
		if(res.getCommonResponse() !=null) {
			return new ResponseEntity<CommonRes>(res, HttpStatus.CREATED);
		}
		else {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
		
	}
	
/*	@PostMapping("/changepassword")
	@ApiOperation(value="This method is to Change Login Password")
	public CommonCrmRes getChangePwd(@RequestBody ChangePasswordReq req) throws Exception {
		
		CommonCrmRes res = new CommonCrmRes();
		
		List<Error> error = loginValidationComponent.LoginChangePwdValidation(req);
		if (error != null && error.size() > 0) {
			throw new CommonValidationException(error, null);
		}
		res = authservice.LoginChangePassword(req);
		return res;
	}
    
	@PostMapping("/logout")
	@ApiOperation(value="This method is used to Logout From Screen")
	public CommonCrmRes logout(@RequestBody LogoutRequest mslogin)  {		
		return authservice.logout(mslogin);
	}
	
 
	

@PostMapping("/getbyloginid")
@ApiOperation("This method is to get by loginid")
public ResponseEntity<CommonCrmRes> getloginid(@RequestBody LoginGetReq req){
	CommonCrmRes data = new CommonCrmRes();
	LoginGetRes res = authservice.getloginid(req);
	data.setCommonResponse(res);
	data.setErrorMessage(Collections.emptyList());
	data.setIsError(false);
	data.setMessage("Success");
	if(res!=null) {
		return new ResponseEntity<CommonCrmRes>(data, HttpStatus.CREATED);
	}
	else {
		return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
	}
}


	
	
	//Login Creation 
	@PostMapping("/insertlogin")
	@ApiOperation(value="This method is used to Create Login")
	public ResponseEntity<CommonCrmRes> Insertlogin(@RequestBody InsertLoginMasterReq req) throws CommonValidationException  {		
		reqPrinter.reqPrint(req);
		CommonCrmRes data = new CommonCrmRes();
		List<Error> validation = loginValidationComponent.InsertLoginValidation(req);
		//// validation
		if (validation != null && validation.size() != 0) {
			data.setCommonResponse(null);
			data.setIsError(true);
			data.setErrorMessage(validation);
			data.setMessage("Failed");
			return new ResponseEntity<CommonCrmRes>(data, HttpStatus.OK);

		} else {
			/////// save

			SuccessRes res =  authservice.InsertLogin(req);
			data.setCommonResponse(res);
			data.setIsError(false);
			data.setErrorMessage(Collections.emptyList());
			data.setMessage("Success");
			if (res != null) {
				return new ResponseEntity<CommonCrmRes>(data, HttpStatus.CREATED);
			} else {
				return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
			}
		}
	}

	
	




	// Get Login Details
		@PostMapping("/getlogindetails")
		@ApiOperation(value = "This method is to Get Login Details")
		public ResponseEntity<CommonCrmRes> getLoginDetails(@RequestBody LoginDetailsGetReq req) {
			reqPrinter.reqPrint(req);
			CommonCrmRes data = new CommonCrmRes();

			// Get All
			List<LoginDetailsGetRes> res = authservice.getLogintDetails(req);
			data.setCommonResponse(res);
			data.setIsError(false);
			data.setErrorMessage(Collections.emptyList());
			data.setMessage("Success");

			if (res != null) {
				return new ResponseEntity<CommonCrmRes>(data, HttpStatus.CREATED);
			} else {
				return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
			}
		}	
*/

}
