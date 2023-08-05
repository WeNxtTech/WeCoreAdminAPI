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

import com.maan.eway.error.Error;
import com.maan.eway.master.req.InsertPlanBenefitsReq;
import com.maan.eway.master.req.SuccessResponse;
import com.maan.eway.master.service.PlanBenefitsService;
import com.maan.eway.res.CommonRes;
import com.maan.eway.service.PrintReqService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/planbenefits")
public class PlanBenefitsController {
	
	@Autowired
	private PlanBenefitsService entityService;

	@Autowired
	private PrintReqService reqPrinter;

	// save
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_APPROVER')")
	@PostMapping("/insertplanbenefits")
	@ApiOperation(value = "This method is Insert Plan Benefits Details")
	public ResponseEntity<CommonRes> insertPlanBenefits(@RequestBody InsertPlanBenefitsReq req) {

		reqPrinter.reqPrint(req);
		CommonRes data = new CommonRes();

		List<Error> validation = entityService.validatePlanBenefits(req);
		// validation
		if (validation != null && validation.size() != 0) {
			data.setCommonResponse(null);
			data.setIsError(true);
			data.setErrorMessage(validation);
			data.setMessage("Failed");
			return new ResponseEntity<CommonRes>(data, HttpStatus.OK);

		} else {

			// Insert
			SuccessResponse res = entityService.insertPlanBenefits(req);
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
	

}
