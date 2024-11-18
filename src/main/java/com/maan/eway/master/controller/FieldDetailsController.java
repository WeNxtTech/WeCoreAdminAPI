package com.maan.eway.master.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.maan.eway.master.req.SaveFieldDetailsReq;
import com.maan.eway.res.CommonRes;
import com.maan.eway.service.FieldDetailsService;

@RestController
@RequestMapping("/master")
public class FieldDetailsController {
	
	@Autowired
	private FieldDetailsService service;
	
	@PostMapping("/saveFieldDetails")
	public ResponseEntity<CommonRes> saveFieldDetails(@RequestBody SaveFieldDetailsReq req){
		CommonRes res = service.saveFieldDetails(req);
		return new ResponseEntity<CommonRes>(res,HttpStatus.ACCEPTED);
	}
	
	@GetMapping("getFieldDetails")
	public ResponseEntity<CommonRes> getFieldDetails(@RequestParam(name = "fieldId",required = false) String fieldId){
		CommonRes res = service.getFieldDetails(fieldId);
		return new ResponseEntity<CommonRes>(res,HttpStatus.ACCEPTED);
	}

}
