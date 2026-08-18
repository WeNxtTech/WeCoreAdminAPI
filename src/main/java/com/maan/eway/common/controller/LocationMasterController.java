package com.maan.eway.common.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maan.eway.batch.req.LocationMasterReq;
import com.maan.eway.bean.LocationMaster;
import com.maan.eway.common.res.LocationMasterRes;
import com.maan.eway.service.impl.LocationMasterService;

@RestController
@RequestMapping("/api/v1/locationmaster")
@CrossOrigin(origins = "*")
public class LocationMasterController {

	@Autowired
	private LocationMasterService service;

	// 1. Create Initial Location (amendId = 0)
	@PostMapping("/save")
	public ResponseEntity<LocationMasterRes> saveOrUpdate(@RequestBody LocationMasterReq req) {
		LocationMasterRes response = service.saveOrUpdateLocation(req);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	// 3. Fetch by Company ID
	@GetMapping("/company/{companyId}")
	public ResponseEntity<List<LocationMaster>> getByCompanyId(@PathVariable String companyId) {
		List<LocationMaster> list = service.getByCompanyId(companyId);
		return ResponseEntity.ok(list);
	}

	// 4. Fetch by Company ID and Branch ID (Works for '99999' or specific Branch
	// ID)
	@GetMapping("/company/{companyId}/branch/{branchId}")
	public ResponseEntity<List<LocationMaster>> getByCompanyAndBranchId(@PathVariable String companyId,
			@PathVariable String branchId) {
		List<LocationMaster> list = service.getByCompanyAndBranchId(companyId, branchId);
		return ResponseEntity.ok(list);
	}
}