package com.maan.eway.common.controller;

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

import com.maan.eway.common.req.RawReferenceTableReq;
import com.maan.eway.common.res.RawReferenceTableRes;
import com.maan.eway.common.service.RawReferenceTableService;
import com.maan.eway.res.CommonRes;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/dropdown")
@Api(tags="MASTER : Raw Reference Table Controller", description="API's")
public class RawReferenceTableController {

	@Autowired
	private RawReferenceTableService rawservice;
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_APPROVER')")
	@GetMapping("/tabledropdown")
	@ApiOperation(value="This method is to Table Drop down")
	public ResponseEntity<CommonRes> tabledetails(){
		CommonRes data = new CommonRes();
		List<RawReferenceTableRes> res = rawservice.tabledetails();
		data.setCommonResponse(res);
		data.setIsError(false);
		data.setErrorMessage(Collections.emptyList());
		data.setMessage("Success");
		if(res!=null) {
			return new ResponseEntity<CommonRes>(data, HttpStatus.CREATED); 
		}
		else {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
		
	}
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_APPROVER')")
	@PostMapping("/columndropdown")
	@ApiOperation(value="This method is to Column Drop down")
	public ResponseEntity<CommonRes> columndetails(@RequestBody RawReferenceTableReq req){
		CommonRes data = new CommonRes();
		List<RawReferenceTableRes> res = rawservice.columndetails(req);
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
