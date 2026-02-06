package com.maan.eway.ttrncloseing.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maan.eway.common.res.CommonRes;
import com.maan.eway.ttrncloseing.dto.HomePositionReq;
import com.maan.eway.ttrncloseing.dto.TtrnGetReq;
import com.maan.eway.ttrncloseing.dto.TtrnReq;
import com.maan.eway.ttrncloseing.service.TTrnCloseingService;


@RestController
@RequestMapping("/ttrn")
public class TTrnCloseingController {
	
	@Autowired
	 private TTrnCloseingService ttrnservice;
	
	@PostMapping("/insertTTrn")
	public ResponseEntity<CommonRes> postMethodName(@RequestBody TtrnReq req) {
		CommonRes data = new CommonRes();
		data=ttrnservice.insertTTRNDetails(req);
		return new ResponseEntity<CommonRes>(data, HttpStatus.CREATED);
	}
	
	@PostMapping("/updateDateHPM")
	public ResponseEntity<CommonRes> updateDate(@RequestBody HomePositionReq req) {
		CommonRes data = new CommonRes();
		data=ttrnservice.updateHPM(req);
		return new ResponseEntity<CommonRes>(data, HttpStatus.CREATED);
	}
	
	@PostMapping("/getTTrn")
	public ResponseEntity<CommonRes> ttrn(@RequestBody TtrnGetReq req) {
		CommonRes data = new CommonRes();
		data=ttrnservice.getTTrnList(req);
		return new ResponseEntity<CommonRes>(data, HttpStatus.CREATED);
	}
	
	@PostMapping("/getTTrnAllYear")
	public ResponseEntity<CommonRes> ttrn(@RequestBody HomePositionReq req) {
		CommonRes data = new CommonRes();
		data=ttrnservice.getTTrnAllYear(req);
		return new ResponseEntity<CommonRes>(data, HttpStatus.CREATED);
	}
	

}
