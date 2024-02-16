package com.maan.eway.notif.controller;

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

import com.maan.eway.common.service.impl.FetchErrorDescServiceImpl;
import com.maan.eway.error.Error;
import com.maan.eway.notif.req.SmsConfigInsertReq;
import com.maan.eway.notif.req.SmsGetReq;
import com.maan.eway.notif.res.SmsMasterGetRes;
import com.maan.eway.notif.service.SmsConfigMasterService;
import com.maan.eway.req.CommonErrorModuleReq;
import com.maan.eway.res.CommonRes;
import com.maan.eway.res.SuccessRes;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api")
@Api(tags = "MASTER : SMS Master", description = "API's")
public class SmsConfigMasterController {

	@Autowired
	private SmsConfigMasterService smsConfigService;
	@Autowired
	private FetchErrorDescServiceImpl errorDescService ;
	

	// Save
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_APPROVER','ROLE_USER')")
	@PostMapping("/insertsmsmaster")
	@ApiOperation(value = "This method is to Insert Sms Master")
	public ResponseEntity<CommonRes> insertsmsmaster(@RequestBody SmsConfigInsertReq req) {
	
		CommonRes data = new CommonRes();
		List<String> validationCodes = smsConfigService.validatesmsmaster(req);
		List<Error> validation = null;
		if(validationCodes!=null && validationCodes.size() > 0 ) {
			CommonErrorModuleReq comErrDescReq = new CommonErrorModuleReq();
			comErrDescReq.setBranchCode(req.getBranchCode());
			comErrDescReq.setInsuranceId(req.getCompanyId());
			comErrDescReq.setProductId("99999");
			comErrDescReq.setModuleId("31");
			comErrDescReq.setModuleName("MASTERS");
			
			validation = errorDescService.getErrorDesc(validationCodes ,comErrDescReq);
		}

	
		// Validation
		if (validation != null && validation.size() != 0) {
			data.setCommonResponse(null);
			data.setIsError(true);
			data.setErrorMessage(validation);
			data.setMessage("Failed");
			return new ResponseEntity<CommonRes>(data, HttpStatus.OK);

		} else {
			// Get all
			SuccessRes res = smsConfigService.insertsmsmaster(req);
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

// Get By Id
	@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	@PostMapping("/getbysmsid")
	@ApiOperation("This Method is to get by id")
	public ResponseEntity<CommonRes> getbysmsid(@RequestBody SmsGetReq req) {
		CommonRes data = new CommonRes();
		SmsMasterGetRes res = smsConfigService.getbysmsid(req);
		data.setCommonResponse(res);
		data.setErrorMessage(Collections.emptyList());
		data.setIsError(false);
		data.setMessage("Success");
		if (res != null) {
			return new ResponseEntity<CommonRes>(data, HttpStatus.CREATED);
		} else {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
	}

}
