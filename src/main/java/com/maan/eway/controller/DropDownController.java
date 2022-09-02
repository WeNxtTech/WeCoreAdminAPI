package com.maan.eway.controller;

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

import com.maan.eway.req.SubUserTypeReq;
import com.maan.eway.res.CommonRes;
import com.maan.eway.res.DropDownRes;
import com.maan.eway.service.DropDownService;
import com.maan.eway.service.PrintReqService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(tags = "DROP DOWN : Normal Drop Down ", description = "API's")
@RequestMapping("/dropdown")
public class DropDownController {

	@Autowired
	private DropDownService dropDownService;
	@Autowired
	private PrintReqService reqPrinter;

	

	// Gender

	@GetMapping("/gender")
	@ApiOperation(value = "This method is to Gender Types Drop Down")
	public ResponseEntity<CommonRes> getgender() {
		CommonRes data = new CommonRes();

		// Save
		List<DropDownRes> res = dropDownService.getgender();
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
	
	@GetMapping("/usertype")
	@ApiOperation(value = "This method is to Gender Types Drop Down")
	public ResponseEntity<CommonRes> getUserType() {
		CommonRes data = new CommonRes();

		// Save
		List<DropDownRes> res = dropDownService.getUserType();
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

	@PostMapping("/subusertype")
	@ApiOperation(value = "This method is to Gender Types Drop Down")
	public ResponseEntity<CommonRes> getUserType(@RequestBody SubUserTypeReq req) {
		CommonRes data = new CommonRes();

		// Save
		List<DropDownRes> res = dropDownService.getSubUserType(req);
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

