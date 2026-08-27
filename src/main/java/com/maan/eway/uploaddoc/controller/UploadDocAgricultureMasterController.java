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
import com.maan.eway.uploaddoc.dto.request.UploadDocAgricultureMasterGetReq;
import com.maan.eway.uploaddoc.dto.request.UploadDocAgricultureMasterSaveReq;
import com.maan.eway.uploaddoc.dto.request.UploadDocAgricultureMasterUpdateReq;
import com.maan.eway.uploaddoc.dto.response.UploadDocAgricultureMasterRes;
import com.maan.eway.uploaddoc.service.UploadDocAgricultureMasterService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/uploaddoc/agriculture-master")
@Tag(name = "Upload Document : Agriculture Master")
public class UploadDocAgricultureMasterController {

	@Autowired
	private UploadDocAgricultureMasterService service;

	@PostMapping
	@Operation(summary = "Create a new Agriculture Master record")
	public ResponseEntity<CommonRes> save(@RequestBody UploadDocAgricultureMasterSaveReq req) {
		CommonRes data = new CommonRes();
		List<Error> errors = service.validateSave(req);
		if (!errors.isEmpty()) {
			data.setIsError(true);
			data.setMessage("Validation Failed");
			data.setErrorMessage(errors);
			return new ResponseEntity<>(data, HttpStatus.UNPROCESSABLE_ENTITY);
		}
		UploadDocAgricultureMasterRes res = service.saveOrUpdate(req);
		data.setIsError(false);
		data.setMessage("Agriculture Master Created Successfully");
		data.setErrorMessage(Collections.emptyList());
		data.setCommonResponse(res);
		return new ResponseEntity<>(data, HttpStatus.CREATED);
	}

	@PutMapping
	@Operation(summary = "Amend an existing Agriculture Master record (creates the next AMEND_ID)")
	public ResponseEntity<CommonRes> update(@RequestBody UploadDocAgricultureMasterUpdateReq req) {
		CommonRes data = new CommonRes();
		List<Error> errors = service.validateUpdate(req);
		if (!errors.isEmpty()) {
			data.setIsError(true);
			data.setMessage("Validation Failed");
			data.setErrorMessage(errors);
			return new ResponseEntity<>(data, HttpStatus.UNPROCESSABLE_ENTITY);
		}
		UploadDocAgricultureMasterRes res = service.update(req);
		data.setIsError(false);
		data.setMessage("Agriculture Master Updated Successfully");
		data.setErrorMessage(Collections.emptyList());
		data.setCommonResponse(res);
		return new ResponseEntity<>(data, HttpStatus.OK);
	}

	@PostMapping("/search")
	@Operation(summary = "Get the latest amendment of an Agriculture Master record by its business key")
	public ResponseEntity<CommonRes> getById(@RequestBody UploadDocAgricultureMasterGetReq req) {
		CommonRes data = new CommonRes();
		UploadDocAgricultureMasterRes res = service.getLatest(req);
		if (res == null) {
			data.setIsError(true);
			data.setMessage("Agriculture Master Record Not Found");
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
	@Operation(summary = "List the latest amendment of every Agriculture Master record")
	public ResponseEntity<CommonRes> getAll(@RequestBody UploadDocAgricultureMasterGetReq req) {
		CommonRes data = new CommonRes();
		List<UploadDocAgricultureMasterRes> res = service.getAll(req);
		data.setIsError(false);
		data.setMessage("Success");
		data.setErrorMessage(Collections.emptyList());
		data.setCommonResponse(res);
		return new ResponseEntity<>(data, HttpStatus.OK);
	}

	@DeleteMapping
	@Operation(summary = "Delete the latest amendment of an Agriculture Master record by its business key")
	public ResponseEntity<CommonRes> delete(@RequestBody UploadDocAgricultureMasterGetReq req) {
		CommonRes data = new CommonRes();
		boolean deleted = service.delete(req);
		if (!deleted) {
			data.setIsError(true);
			data.setMessage("Agriculture Master Record Not Found");
			data.setErrorMessage(Collections.emptyList());
			return new ResponseEntity<>(data, HttpStatus.NOT_FOUND);
		}
		data.setIsError(false);
		data.setMessage("Agriculture Master Deleted Successfully");
		data.setErrorMessage(Collections.emptyList());
		return new ResponseEntity<>(data, HttpStatus.OK);
	}
}
