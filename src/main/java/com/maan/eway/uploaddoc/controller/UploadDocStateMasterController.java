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
import com.maan.eway.req.StateMasterRequest;
import com.maan.eway.uploaddoc.dto.request.UploadDocStateMasterGetReq;
import com.maan.eway.uploaddoc.dto.request.UploadDocStateMasterSaveReq;
import com.maan.eway.uploaddoc.dto.request.UploadDocStateMasterUpdateReq;
import com.maan.eway.uploaddoc.dto.response.UploadDocStateMasterRes;
import com.maan.eway.uploaddoc.service.UploadDocStateMasterService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * <h2>UploadDocStateMasterController</h2>
 * CRUD APIs for eway_state_master.
 */
@RestController
@RequestMapping("/api/v1/uploaddoc/state-master")
@Tag(name = "Upload Document : State Master")
public class UploadDocStateMasterController {

	@Autowired
	private UploadDocStateMasterService service;

	@PostMapping
	@Operation(summary = "Create a new State Master record")
	public ResponseEntity<CommonRes> save(@RequestBody UploadDocStateMasterSaveReq req) {
		CommonRes data = new CommonRes();
		List<Error> errors = service.validateSave(req);
		if (!errors.isEmpty()) {
			data.setIsError(true);
			data.setMessage("Validation Failed");
			data.setErrorMessage(errors);
			return new ResponseEntity<>(data, HttpStatus.UNPROCESSABLE_ENTITY);
		}
		UploadDocStateMasterRes res = service.save(req);
		data.setIsError(false);
		data.setMessage("State Master Created Successfully");
		data.setErrorMessage(Collections.emptyList());
		data.setCommonResponse(res);
		return new ResponseEntity<>(data, HttpStatus.CREATED);
	}

	@PutMapping
	@Operation(summary = "Amend an existing State Master record (creates the next AMEND_ID)")
	public ResponseEntity<CommonRes> update(@RequestBody UploadDocStateMasterUpdateReq req) {
		CommonRes data = new CommonRes();
		List<Error> errors = service.validateUpdate(req);
		if (!errors.isEmpty()) {
			data.setIsError(true);
			data.setMessage("Validation Failed");
			data.setErrorMessage(errors);
			return new ResponseEntity<>(data, HttpStatus.UNPROCESSABLE_ENTITY);
		}
		UploadDocStateMasterRes res = service.update(req);
		data.setIsError(false);
		data.setMessage("State Master Updated Successfully");
		data.setErrorMessage(Collections.emptyList());
		data.setCommonResponse(res);
		return new ResponseEntity<>(data, HttpStatus.OK);
	}

	@PostMapping("/search")
	@Operation(summary = "Get the latest amendment of a State Master record by its business key")
	public ResponseEntity<CommonRes> getById(@RequestBody UploadDocStateMasterGetReq req) {
		CommonRes data = new CommonRes();
		UploadDocStateMasterRes res = service.getLatest(req);
		if (res == null) {
			data.setIsError(true);
			data.setMessage("State Master Record Not Found");
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
	@Operation(summary = "List the latest amendment of every State Master record")
	public ResponseEntity<CommonRes> getAll(@RequestBody StateMasterRequest request) {
		CommonRes data = new CommonRes();
		List<UploadDocStateMasterRes> res = service.getAll(request);
		data.setIsError(false);
		data.setMessage("Success");
		data.setErrorMessage(Collections.emptyList());
		data.setCommonResponse(res);
		return new ResponseEntity<>(data, HttpStatus.OK);
	}

	@DeleteMapping
	@Operation(summary = "Delete the latest amendment of a State Master record by its business key")
	public ResponseEntity<CommonRes> delete(@RequestBody UploadDocStateMasterGetReq req) {
		CommonRes data = new CommonRes();
		boolean deleted = service.delete(req);
		if (!deleted) {
			data.setIsError(true);
			data.setMessage("State Master Record Not Found");
			data.setErrorMessage(Collections.emptyList());
			return new ResponseEntity<>(data, HttpStatus.NOT_FOUND);
		}
		data.setIsError(false);
		data.setMessage("State Master Deleted Successfully");
		data.setErrorMessage(Collections.emptyList());
		return new ResponseEntity<>(data, HttpStatus.OK);
	}
}
