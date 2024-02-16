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
import com.maan.eway.master.req.TravelPolicyTypeGetReq;
import com.maan.eway.master.req.TravelPolicyTypeSaveReq;
import com.maan.eway.master.res.TravelPolicyTypeGetRes1;
import com.maan.eway.master.service.TravelPolicyTypeService;
import com.maan.eway.req.CommonErrorModuleReq;
import com.maan.eway.res.CommonRes;
import com.maan.eway.res.SuccessRes2;
import com.maan.eway.service.PrintReqService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/TravelPolicyType")
public class TravelPolicyTypeController {
	
	@Autowired
	private TravelPolicyTypeService entityService;

	@Autowired
	private PrintReqService reqPrinter;
	
	
	
	@Autowired
	private FetchErrorDescServiceImpl errorDescService ;
	
	//Covers
		@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_APPROVER')")
		@PostMapping("/inserttravelpolicytype")
		@ApiOperation(value = "This method is Insert Travel Policy Type")
		public ResponseEntity<CommonRes> insertTravelPolicyType(@RequestBody TravelPolicyTypeSaveReq req) {

			reqPrinter.reqPrint(req);
			CommonRes data = new CommonRes();
			List<String> validationCodes =  entityService.validateTravelPolicyType(req);
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
			

			// validation
			if (validation != null && validation.size() != 0) {
				data.setCommonResponse(null);
				data.setIsError(true);
				data.setErrorMessage(validation);
				data.setMessage("Failed");
				return new ResponseEntity<CommonRes>(data, HttpStatus.OK);

			} else {

				// Insert
				SuccessRes2 res = entityService.insertTravelPolicyType(req);
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
		
		@PreAuthorize("hasAnyRole('ROLE_APPROVER','ROLE_USER','ROLE_ADMIN')")
		@PostMapping(value = "/getalltravelpolicytype")
		public ResponseEntity<CommonRes> getalltravelpolicytype(@RequestBody TravelPolicyTypeGetReq req)
		{
			CommonRes data = new CommonRes();
			TravelPolicyTypeGetRes1 res = entityService.getalltravelpolicytype(req);
			data.setCommonResponse(res);
			data.setErrorMessage(null);
			data.setIsError(false);
			data.setMessage("Success");
			
			if(res != null)
			{
				return new ResponseEntity<CommonRes>(data,HttpStatus.CREATED);
			}
			else
			{
		    	return new ResponseEntity<CommonRes>(data, HttpStatus.BAD_REQUEST); 
			}
		}

}
