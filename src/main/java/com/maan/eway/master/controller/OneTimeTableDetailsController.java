package com.maan.eway.master.controller;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maan.eway.error.Error;
import com.maan.eway.master.req.ColumnNameDropDownlReq;
import com.maan.eway.master.req.GetAllOneTimeTableDetailsReq;
import com.maan.eway.master.req.GetOneTimeTableDetailsReq;
import com.maan.eway.master.req.InsertOneTimeTableReq;
import com.maan.eway.master.res.GetAllOneTimeTableDetailsRes;
import com.maan.eway.master.service.OneTimeTableDetailsService;
import com.maan.eway.res.CommonRes;
import com.maan.eway.res.DropDownRes;
import com.maan.eway.res.SuccessRes2;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/dropdown")
@Api(tags = "MASTER : One Time Table Details Controller", description = "API's")

public class OneTimeTableDetailsController {

	@Autowired
	private OneTimeTableDetailsService service;
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_APPROVER')")
	@GetMapping("/tablename")
	@ApiOperation(value = "This method is to Table Name Drop Down")
	public ResponseEntity<CommonRes> tableName() {
		CommonRes data = new CommonRes();

		List<DropDownRes> res = service.tableName();
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
	@PostMapping("/columnname")
	@ApiOperation(value = "This method is to Column Name Drop Down")
	public ResponseEntity<CommonRes> columnName(@RequestBody ColumnNameDropDownlReq req) {
		CommonRes data = new CommonRes();

		List<DropDownRes> res = service.columnName(req);
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
	
	@PreAuthorize("hasAnyRole('ROLE_APPROVER','ROLE_USER','ROLE_ADMIN')")
	@PostMapping("/getallonetimetabledetails")
	@ApiOperation(value = "This method is to get all OneTime Table Details")
	public ResponseEntity<CommonRes> getAllOneTimeTableDetails(@RequestBody GetAllOneTimeTableDetailsReq req) {
		CommonRes data = new CommonRes();

		List<GetAllOneTimeTableDetailsRes> res = service.getAllOneTimeTableDetails(req);
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
	
	@PreAuthorize("hasAnyRole('ROLE_APPROVER','ROLE_USER','ROLE_ADMIN')")
	@PostMapping("/getactiveonetimetabledetails")
	@ApiOperation(value = "This method is to get all OneTime Table Details")
	public ResponseEntity<CommonRes> getActiveOneTimeTableDetails(@RequestBody GetAllOneTimeTableDetailsReq req) {
		CommonRes data = new CommonRes();

		List<GetAllOneTimeTableDetailsRes> res = service.getActiveOneTimeTableDetails(req);
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
	
	
	@PreAuthorize("hasAnyRole('ROLE_APPROVER','ROLE_USER','ROLE_ADMIN')")
	@PostMapping("/getonetimetabledetails")
	@ApiOperation(value = "This method is to get OneTime Table Details")
	public ResponseEntity<CommonRes> getOneTimeTableDetails(@RequestBody GetOneTimeTableDetailsReq req) {
		CommonRes data = new CommonRes();

		GetAllOneTimeTableDetailsRes res = service.getOneTimeTableDetails(req);
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
	

	@PreAuthorize("hasAnyRole('ROLE_APPROVER','ROLE_USER','ROLE_ADMIN')")
	@PostMapping("/insertonetimetable")
	@ApiOperation(value = "This method is Insert One Time Tables")
	public ResponseEntity<CommonRes> insertOneTimeTable(@RequestBody InsertOneTimeTableReq req) {

		CommonRes data = new CommonRes();

		List<Error> validation = service.insertOneTimeTableVali(req);
		// validation
		if (validation != null && validation.size() != 0) {
			data.setCommonResponse(null);
			data.setIsError(true);
			data.setErrorMessage(validation);
			data.setMessage("Failed");
			return new ResponseEntity<CommonRes>(data, HttpStatus.OK);

		} else {

			SuccessRes2 res = service.insertOneTimeTable(req);
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
