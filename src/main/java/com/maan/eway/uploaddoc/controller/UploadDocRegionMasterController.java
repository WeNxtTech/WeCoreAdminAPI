package com.maan.eway.uploaddoc.controller;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maan.eway.common.res.CommonRes;
import com.maan.eway.error.Error;
import com.maan.eway.req.RegionMasterRequest;
import com.maan.eway.uploaddoc.dto.request.UploadDocRegionMasterGetReq;
import com.maan.eway.uploaddoc.dto.request.UploadDocRegionMasterSaveReq;
import com.maan.eway.uploaddoc.dto.request.UploadDocRegionMasterUpdateReq;
import com.maan.eway.uploaddoc.dto.response.UploadDocRegionMasterRes;
import com.maan.eway.uploaddoc.service.UploadDocRegionMasterService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * <h2>UploadDocRegionMasterController</h2>
 * CRUD APIs for eway_region_master.
 */
@RestController
@RequestMapping("/api/v1/uploaddoc/region-master")
@Tag(name = "Upload Document : Region Master")
public class UploadDocRegionMasterController {

	@Autowired
	private UploadDocRegionMasterService service;

	@PostMapping
	@Operation(summary = "Create a new Region Master record")
	public ResponseEntity<CommonRes> save(@RequestBody UploadDocRegionMasterSaveReq req) {
		CommonRes data = new CommonRes();
		List<Error> errors = service.validateSave(req);
		if (!errors.isEmpty()) {
			data.setIsError(true);
			data.setMessage("Validation Failed");
			data.setErrorMessage(errors);
			return new ResponseEntity<>(data, HttpStatus.UNPROCESSABLE_ENTITY);
		}
		UploadDocRegionMasterRes res = service.saveOrUpdate(req);
		data.setIsError(false);
		data.setMessage("Region Master Created Successfully");
		data.setErrorMessage(Collections.emptyList());
		data.setCommonResponse(res);
		return new ResponseEntity<>(data, HttpStatus.CREATED);
	}

	@PutMapping
	@Operation(summary = "Amend an existing Region Master record (creates the next AMEND_ID)")
	public ResponseEntity<CommonRes> update(@RequestBody UploadDocRegionMasterUpdateReq req) {
		CommonRes data = new CommonRes();
		List<Error> errors = service.validateUpdate(req);
		if (!errors.isEmpty()) {
			data.setIsError(true);
			data.setMessage("Validation Failed");
			data.setErrorMessage(errors);
			return new ResponseEntity<>(data, HttpStatus.UNPROCESSABLE_ENTITY);
		}
		UploadDocRegionMasterRes res = service.update(req);
		data.setIsError(false);
		data.setMessage("Region Master Updated Successfully");
		data.setErrorMessage(Collections.emptyList());
		data.setCommonResponse(res);
		return new ResponseEntity<>(data, HttpStatus.OK);
	}

	@PostMapping("/search")
	@Operation(summary = "Get the latest amendment of a Region Master record by its business key")
	public ResponseEntity<CommonRes> getById(@RequestBody UploadDocRegionMasterGetReq req) {
		CommonRes data = new CommonRes();
		UploadDocRegionMasterRes res = service.getLatest(req);
		if (res == null) {
			data.setIsError(true);
			data.setMessage("Region Master Record Not Found");
			data.setErrorMessage(Collections.emptyList());
			return new ResponseEntity<>(data, HttpStatus.NOT_FOUND);
		}
		data.setIsError(false);
		data.setMessage("Success");
		data.setErrorMessage(Collections.emptyList());
		data.setCommonResponse(res);
		return new ResponseEntity<>(data, HttpStatus.OK);
	}

	@PostMapping("/list")
	@Operation(summary = "List the latest amendment of every Region Master record")
	public ResponseEntity<CommonRes> getAll(@RequestBody RegionMasterRequest request) {
	    CommonRes data = new CommonRes();
	    List<UploadDocRegionMasterRes> res = service.getAll(request);
	    data.setIsError(false);
	    data.setMessage("Success");
	    data.setErrorMessage(Collections.emptyList());
	    data.setCommonResponse(res);
	    return new ResponseEntity<>(data, HttpStatus.OK);
	}

	@DeleteMapping
	@Operation(summary = "Delete the latest amendment of a Region Master record by its business key")
	public ResponseEntity<CommonRes> delete(@RequestBody UploadDocRegionMasterGetReq req) {
		CommonRes data = new CommonRes();
		boolean deleted = service.delete(req);
		if (!deleted) {
			data.setIsError(true);
			data.setMessage("Region Master Record Not Found");
			data.setErrorMessage(Collections.emptyList());
			return new ResponseEntity<>(data, HttpStatus.NOT_FOUND);
		}
		data.setIsError(false);
		data.setMessage("Region Master Deleted Successfully");
		data.setErrorMessage(Collections.emptyList());
		return new ResponseEntity<>(data, HttpStatus.OK);
	}
}
