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

import com.maan.eway.master.req.ExcessMasterReq;
import com.maan.eway.master.res.ExcessMasterRes;
import com.maan.eway.master.service.ExcessMasterService;
import com.maan.eway.master.service.impl.ExcessMasterDropdownReq;
import com.maan.eway.res.CommonRes;
import com.maan.eway.res.DropDownRes;
import com.maan.eway.res.DropdownCommonRes;
import com.maan.eway.res.SuccessRes;
import com.maan.eway.service.PrintReqService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(tags="MASTER : Excess MASTER",description="API's")
@RequestMapping("/master")
public class ExcessMasterController {

	@Autowired
	private ExcessMasterService excessService;
	
	@Autowired
	private  PrintReqService reqPrinter;
	
	
	
@PostMapping("/insertExcessMaster")
@PreAuthorize("hasAnyRole('ROLE_APPROVER','ROLE_USER','ROLE_ADMIN')")
public ResponseEntity<CommonRes> insertExcess(@RequestBody List<ExcessMasterReq> req) 
{
	CommonRes data = new CommonRes();	
	reqPrinter.reqPrint(req);
	List<SuccessRes> reqlist= excessService.saveExcess(req);
	if(reqlist!=null)
	{
		data.setCommonResponse(reqlist);
		data.setIsError(false);
		data.setMessage("Success");
	    return new ResponseEntity<CommonRes>(data,HttpStatus.CREATED);
	}
	else {
		data.setIsError(true);
		data.setMessage("Failed To Save");
		data.setCommonResponse(null);
		return new ResponseEntity<CommonRes>(data,HttpStatus.OK);
	}

}

//Get All Warranty Master
@PreAuthorize("hasAnyRole('ROLE_APPROVER','ROLE_USER','ROLE_ADMIN')")
@PostMapping("/getallExcessMaster")
@ApiOperation("This method is getall ExcessMaster")
public ResponseEntity<CommonRes> getallWarranty(@RequestBody ExcessMasterReq req)
{
CommonRes data = new CommonRes();
reqPrinter.reqPrint(req);

List<ExcessMasterRes> res =excessService.getallExcessMaster(req);
data.setCommonResponse(res);
data.setErrorMessage(Collections.emptyList());
data.setIsError(false);
data.setMessage("Success");

if(res!= null) {
	return new ResponseEntity<CommonRes> (data, HttpStatus.CREATED);
}
else {
	return new ResponseEntity<> (null, HttpStatus.BAD_REQUEST);
}
}



@PreAuthorize("hasAnyRole('ROLE_APPROVER','ROLE_USER','ROLE_ADMIN')")
@PostMapping(value="/dropdown/Excess",produces = "application/json")
@ApiOperation(value = "This method is get Warranty Master Drop Down")

public ResponseEntity<CommonRes> getExcessMasterDropdown(@RequestBody ExcessMasterDropdownReq req) {

	CommonRes data = new CommonRes();

	// Save
	List<ExcessMasterRes> res = excessService.getExcessMasterDropdown(req);
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
@PostMapping(value="/getExcessMaster",produces = "application/json")
@ApiOperation(value = "This method is get Warranty Master Drop Down")

public ResponseEntity<CommonRes> getExcessMasterId(@RequestBody ExcessMasterDropdownReq req) {

	CommonRes data = new CommonRes();

	// Save
	ExcessMasterRes res = excessService.getExcessMasterById(req);
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
