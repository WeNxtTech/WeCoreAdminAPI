/**
 * @author : Ashok Kumar S 
 * @since  : 25-02-2025
 */
package com.maan.eway.master.controller;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maan.eway.bean.ExcessMaster;
import com.maan.eway.master.req.ExcessMasterGetAllReq;
import com.maan.eway.master.req.ExcessMasterGetReq;
import com.maan.eway.master.req.ExcessMasterSaveUpReq;
import com.maan.eway.master.res.ExcessMasterRes;
import com.maan.eway.master.service.ExcessMasterService;
import com.maan.eway.res.CommonRes;
import com.maan.eway.service.PrintReqService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

@RestController
@RequestMapping("/master")
@Validated
public class ExcessMasterController {

	@Autowired
	private ExcessMasterService excessService;
	
	@Autowired
	private  PrintReqService reqPrinter;
	
	
	
@PostMapping("/insertExcessMaster")
@PreAuthorize("hasAnyRole('ROLE_APPROVER','ROLE_USER','ROLE_ADMIN')")
public ResponseEntity<CommonRes> insertExcess(@RequestBody 
			@NotEmpty(message = "Excess master save or update list should not be empty.")
			@Valid ExcessMasterSaveUpReq req) {
	
	CommonRes data = new CommonRes();	
	reqPrinter.reqPrint(req);
	ExcessMaster savedOrUpdatedExcessMasters = excessService.saveAndUpdateExcessMaster(req);
	
	if(savedOrUpdatedExcessMasters!=null)
	{
		data.setIsError(false);
		data.setMessage("Success");
		data.setCommonResponse(Map.of("Status", "Excess master details saved sucessfully."));
	    return new ResponseEntity<CommonRes>(data,HttpStatus.OK);
	}
	else {
		data.setIsError(true);
		data.setMessage("Failed To Save");
		data.setCommonResponse(null);
		return new ResponseEntity<CommonRes>(data,HttpStatus.BAD_REQUEST);
	}

}


@PreAuthorize("hasAnyRole('ROLE_APPROVER','ROLE_USER','ROLE_ADMIN')")
@PostMapping("/getallExcessMaster")
public ResponseEntity<CommonRes> getallWarranty(@Valid @RequestBody ExcessMasterGetAllReq req)
{
	CommonRes data = new CommonRes();
	reqPrinter.reqPrint(req);
	
	List<ExcessMasterRes> res =excessService.getallExcessMaster(req);
	data.setCommonResponse(res);
	data.setErrorMessage(Collections.emptyList());
	data.setIsError(false);
	data.setMessage("Success");
	
	if(res!= null) {
		return new ResponseEntity<CommonRes> (data, HttpStatus.OK);
	}
	else {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
	}
}



@PreAuthorize("hasAnyRole('ROLE_APPROVER','ROLE_USER','ROLE_ADMIN')")
@PostMapping(value="/getallActiveExcessMaster",produces = "application/json")
public ResponseEntity<CommonRes> getAllActiveExcessMaster (@Valid @RequestBody ExcessMasterGetAllReq req) {

	CommonRes data = new CommonRes();

	// Save
	List<ExcessMasterRes> res = excessService.getAllActiveExcessMaster(req);
	data.setCommonResponse(res);
	data.setIsError(false);
	data.setErrorMessage(Collections.emptyList());
	data.setMessage("Success");

	if (res != null) {
		return new ResponseEntity<CommonRes>(data, HttpStatus.OK);
	} else {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
	}
}



@PreAuthorize("hasAnyRole('ROLE_APPROVER','ROLE_USER','ROLE_ADMIN')")
@PostMapping(value="/getExcessMaster",produces = "application/json")
public ResponseEntity<CommonRes> getExcessMaster(@Valid @RequestBody ExcessMasterGetReq req) {

	CommonRes data = new CommonRes();

	// Save
	ExcessMasterRes res = excessService.getExcessMaster(req);
	data.setCommonResponse(res);
	data.setIsError(false);
	data.setErrorMessage(Collections.emptyList());
	data.setMessage("Success");

	if (res != null) {
		return new ResponseEntity<CommonRes>(data, HttpStatus.OK);
	} else {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
	}
}

}
