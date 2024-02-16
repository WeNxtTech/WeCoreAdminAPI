package com.maan.eway.master.controller;

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
import com.maan.eway.master.req.PolicyTypeMasterChangeStatusReq;
import com.maan.eway.master.req.PolicyTypeMasterGetAllReq;
import com.maan.eway.master.req.PolicyTypeMasterGetReq;
import com.maan.eway.master.req.PolicyTypeMasterSaveReq;
import com.maan.eway.master.res.PolicyTypeMasterGetRes;
import com.maan.eway.master.service.PolicyTypeMasterService;
import com.maan.eway.req.CommonErrorModuleReq;
import com.maan.eway.res.CommonRes;
import com.maan.eway.res.DropDownRes;
import com.maan.eway.res.DropdownCommonRes;
import com.maan.eway.res.SuccessRes;
import com.maan.eway.service.PrintReqService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(tags="MASTER : Policy Type Master", description = "API's")
@RequestMapping("/master")
public class PolicyTypeMasterController {

	@Autowired
	private PolicyTypeMasterService service;
	
	
	@Autowired
	private FetchErrorDescServiceImpl errorDescService ;
	
	@Autowired
	private PrintReqService reqPrinter;
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_APPROVER')")
	@PostMapping("/insertpolicytype")
	@ApiOperation("This method is to save Policy Type Master")
	public ResponseEntity<CommonRes> insertPolicyType(@RequestBody PolicyTypeMasterSaveReq req){
		
		reqPrinter.reqPrint(req);
		CommonRes data = new CommonRes();
		List<String> validationCodes =  service.validatePolicyType(req);
		List<Error> validation = null;
		if(validationCodes!=null && validationCodes.size() > 0 ) {
			CommonErrorModuleReq comErrDescReq = new CommonErrorModuleReq();
			comErrDescReq.setBranchCode("99999");
			comErrDescReq.setInsuranceId(req.getInsuranceId());
			comErrDescReq.setProductId("99999");
			comErrDescReq.setModuleId("31");
			comErrDescReq.setModuleName("MASTERS");
			
			validation = errorDescService.getErrorDesc(validationCodes ,comErrDescReq);
		}
		
		
		//Validation
		if(validation!=null && validation.size()!=0) {
		data.setCommonResponse(null);
		data.setErrorMessage(validation);
		data.setMessage("Failed");
		return new ResponseEntity<CommonRes>(data, HttpStatus.OK);

		}
		else {
		SuccessRes res = service.insertPolicyType(req);
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
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_APPROVER')")
	@PostMapping("/getpolicytype")
	@ApiOperation("This method is to get policy type")
	public ResponseEntity<CommonRes> getPolicyType(@RequestBody PolicyTypeMasterGetReq req){
		CommonRes data = new CommonRes();
		reqPrinter.reqPrint(req);
		PolicyTypeMasterGetRes res = service.getPolicyType(req);
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
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_APPROVER')")
	@PostMapping("/getallpolicytype")
	@ApiOperation("This method is to get all Policy Type Master")
	public ResponseEntity<CommonRes>getallPolicyType(@RequestBody PolicyTypeMasterGetAllReq req){
		CommonRes data = new CommonRes();
		reqPrinter.reqPrint(req);
		List<PolicyTypeMasterGetRes> res = service.getallPolicyType(req);
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
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_APPROVER')")
	@PostMapping("/getactivepolicytype")
	@ApiOperation("This method is to get all Active Policy Type Master")
	public ResponseEntity<CommonRes>getallactivePolicyType(@RequestBody PolicyTypeMasterGetAllReq req){
		CommonRes data = new CommonRes();
		reqPrinter.reqPrint(req);
		List<PolicyTypeMasterGetRes> res = service.getallactivePolicyType(req);
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
	
	
	// Policy Type Master Drop Down Type
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_APPROVER','ROLE_USER')")
	@PostMapping(value="/dropdown/policytype",produces = "application/json")
	@ApiOperation(value = "This method is get Policy Type Master Drop Down")

	public ResponseEntity<DropdownCommonRes> getPolicyTypeMasterDropdown(@RequestBody PolicyTypeMasterGetAllReq req ) {

		DropdownCommonRes data = new DropdownCommonRes();

		// Save
		List<DropDownRes> res = service.getPolicyTypeMasterDropdown(req);
		data.setCommonResponse(res);
		data.setIsError(false);
		data.setErrorMessage(Collections.emptyList());
		data.setMessage("Success");

		if (res != null) {
			return new ResponseEntity<DropdownCommonRes>(data, HttpStatus.CREATED);
		} else {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}

	}
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_APPROVER')")
	@PostMapping("/policytype/changestatus")
	@ApiOperation(value = "This method is get Referal Master Status Change")

	public ResponseEntity<CommonRes> changeStatusOfPolicyType(@RequestBody PolicyTypeMasterChangeStatusReq req) {

		CommonRes data = new CommonRes();
		// Change Status
		SuccessRes res = service.changeStatusOfPolicyType(req);
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
