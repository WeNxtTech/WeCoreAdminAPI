package com.maan.eway.common.controller;

import java.util.Collections;
//import java.util.List;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maan.eway.common.req.DeleteTiraSearchedVehicleReq;
import com.maan.eway.common.req.GetAllTirraErrorHistory;
import com.maan.eway.common.req.GetTirraEorrorHistoryReq;
import com.maan.eway.common.req.TiraGetReq;
import com.maan.eway.common.req.TiraPushedDetailsReq;
import com.maan.eway.common.res.TiraErrorHistoryTotalRes;
import com.maan.eway.common.res.TiraPushedDetailsRes;
import com.maan.eway.common.res.TiraPushedListDetailsRes;
import com.maan.eway.common.service.ReportsService;
import com.maan.eway.res.CommonRes;
import com.maan.eway.res.SuccessRes2;
import com.maan.eway.service.PrintReqService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/reports")
@Api(tags="Reports Controller", description="API's")
public class ReportsController {
	
	@Autowired
	private ReportsService service;

	@Autowired
	private PrintReqService reqPrinter;

	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_APPROVER')")
	@PostMapping("/errorhistory")
	@ApiOperation(value="This method is to Get Tirra Error History")
	public ResponseEntity<CommonRes> getTirraErrorHistory(@RequestBody GetTirraEorrorHistoryReq req){
		reqPrinter.reqPrint(req);
		CommonRes data = new CommonRes();
		TiraErrorHistoryTotalRes res = service.getTirraEorrorHistory(req);
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
	@PostMapping("/alltirrahistory")
	@ApiOperation(value="This method is to Get All Tirra Error History")
	public ResponseEntity<CommonRes> getAllTirraErrorHistory(@RequestBody GetAllTirraErrorHistory req){
		reqPrinter.reqPrint(req);
		CommonRes data = new CommonRes();
		TiraErrorHistoryTotalRes res = service.getAllTirraErrorHistory(req);
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
	@PostMapping("/getalltiraintegpusheddetails")
	public ResponseEntity<CommonRes> getallTiraIntegrationPushedDetails(@RequestBody TiraPushedDetailsReq req) {
		reqPrinter.reqPrint(req);
		CommonRes data = new CommonRes();
		TiraPushedListDetailsRes res = service.getallTiraIntegrationPushedDetails(req);
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
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_APPROVER')")
	@PostMapping("/gettiraintegpusheddetails")
	public ResponseEntity<CommonRes> getTiraIntegrationPushedDetails(@RequestBody TiraGetReq req) {
		reqPrinter.reqPrint(req);
		CommonRes data = new CommonRes();
		List<TiraPushedDetailsRes> res = service.getTiraIntegrationPushedDetails(req);
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
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_APPROVER')")
	@PostMapping("/deletetirasearchedvehicle")
	public ResponseEntity<CommonRes> getTiraIntegrationPushedDetails(@RequestBody DeleteTiraSearchedVehicleReq req) {
		reqPrinter.reqPrint(req);
		CommonRes data = new CommonRes();
		SuccessRes2 res = service.getTiraIntegrationPushedDetails(req);
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
