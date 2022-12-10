package com.maan.eway.admin.controller;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maan.eway.admin.service.AdminDropDownService;
import com.maan.eway.common.req.GetTableDropDownReq;
import com.maan.eway.common.req.LovDropDownReq;
import com.maan.eway.req.SubUserTypeReq;
import com.maan.eway.res.ColummnDropRes;
import com.maan.eway.res.CommonRes;
import com.maan.eway.res.DropDownRes;
import com.maan.eway.res.SubUserTypeDropDownRes;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/dropdown")
@Api(tags = "MASTER : Drop Down Controller", description = "API's")

public class AdminDropDownController {

	@Autowired
	private  AdminDropDownService dropDownService;
	
	@PostMapping("/gettabledetails")
	@ApiOperation(value = "This method is to Table Details Drop Down")
	public ResponseEntity<CommonRes> getTableDetails(@RequestBody GetTableDropDownReq req) {
		CommonRes data = new CommonRes();

		List<ColummnDropRes> res = dropDownService.getTableDetails(req);
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
	
	
	@PostMapping("/usertype")
	@ApiOperation(value = "This method is to UserType  Drop Down")
	public ResponseEntity<CommonRes> getUserType(@RequestBody LovDropDownReq req) {
		CommonRes data = new CommonRes();

		// Save
		List<DropDownRes> res = dropDownService.getUserType(req);
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
	@ApiOperation(value = "This method is to SubUserType  Drop Down")
	public ResponseEntity<CommonRes> getUserType(@RequestBody SubUserTypeReq req) {
		CommonRes data = new CommonRes();

		// Save
		List<SubUserTypeDropDownRes> res = dropDownService.getSubUserType(req);
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
	
	
	@PostMapping("/producticons")
	@ApiOperation(value = "This method is to UserType  Drop Down")
	public ResponseEntity<CommonRes> getProductIcons(@RequestBody LovDropDownReq req) {
		CommonRes data = new CommonRes();

		// Save
		List<DropDownRes> res = dropDownService.getProductIcons(req);
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
	
	@PostMapping("/calctypes")
	@ApiOperation(value = "This method is to CalcType  Drop Down")
	public ResponseEntity<CommonRes> getCalcTypes(@RequestBody LovDropDownReq req) {
		CommonRes data = new CommonRes();

		// Save
		List<DropDownRes> res = dropDownService.getCalcTypes(req);
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
	
	@PostMapping("/coveragetypes")
	@ApiOperation(value = "This method is to CoverageType  Drop Down")
	public ResponseEntity<CommonRes> getCoverageTypes(@RequestBody LovDropDownReq req) {
		CommonRes data = new CommonRes();

		// Save
		List<DropDownRes> res = dropDownService.getCoverageTypes(req);
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
	
	@PostMapping("/rangeparams")
	@ApiOperation(value = "This method is to CoverageType  Drop Down")
	public ResponseEntity<CommonRes> getRangeParams(@RequestBody LovDropDownReq req) {
		CommonRes data = new CommonRes();

		// Save
		List<DropDownRes> res = dropDownService.getRangeParams(req);
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
	
	
	@PostMapping("/discreteparams")
	@ApiOperation(value = "This method is to CoverageType  Drop Down")
	public ResponseEntity<CommonRes> getDiscreteParams(@RequestBody LovDropDownReq req) {
		CommonRes data = new CommonRes();

		// Save
		List<DropDownRes> res = dropDownService.getDiscreteParams(req);
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
	

	@PostMapping("/productcategory")
	@ApiOperation(value = "This method is to Product Category  Drop Down")
	public ResponseEntity<CommonRes> getProductCategory(@RequestBody LovDropDownReq req) {
		CommonRes data = new CommonRes();

		// Save
		List<DropDownRes> res = dropDownService.getProductCategory(req);
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
