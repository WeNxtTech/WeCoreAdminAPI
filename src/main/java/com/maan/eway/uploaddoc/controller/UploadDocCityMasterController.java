package com.maan.eway.uploaddoc.controller;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maan.eway.common.res.CommonRes;
import com.maan.eway.error.Error;
import com.maan.eway.uploaddoc.dto.request.UploadDocCityMasterGetReq;
import com.maan.eway.uploaddoc.dto.request.UploadDocCityMasterSaveReq;
import com.maan.eway.uploaddoc.dto.request.UploadDocCityMasterUpdateReq;
import com.maan.eway.uploaddoc.dto.response.UploadDocCityMasterRes;
import com.maan.eway.uploaddoc.service.UploadDocCityMasterService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/uploaddoc/city-master")
@Tag(name = "Upload Document : City Master")
public class UploadDocCityMasterController {

	@Autowired
	private UploadDocCityMasterService service;

	@PostMapping
	@Operation(summary = "Create a new City Master record")
	public ResponseEntity<CommonRes> save(@RequestBody UploadDocCityMasterSaveReq req) {
		CommonRes data = new CommonRes();
		List<Error> errors = service.validateSave(req);
		if (!errors.isEmpty()) {
			data.setIsError(true);
			data.setMessage("Validation Failed");
			data.setErrorMessage(errors);
			return new ResponseEntity<>(data, HttpStatus.UNPROCESSABLE_ENTITY);
		}
		UploadDocCityMasterRes res = service.save(req);
		data.setIsError(false);
		data.setMessage("City Master Created Successfully");
		data.setErrorMessage(Collections.emptyList());
		data.setCommonResponse(res);
		return new ResponseEntity<>(data, HttpStatus.CREATED);
	}

	@PutMapping
	@Operation(summary = "Amend an existing City Master record (creates the next AMEND_ID)")
	public ResponseEntity<CommonRes> update(@RequestBody UploadDocCityMasterUpdateReq req) {
		CommonRes data = new CommonRes();
		List<Error> errors = service.validateUpdate(req);
		if (!errors.isEmpty()) {
			data.setIsError(true);
			data.setMessage("Validation Failed");
			data.setErrorMessage(errors);
			return new ResponseEntity<>(data, HttpStatus.UNPROCESSABLE_ENTITY);
		}
		UploadDocCityMasterRes res = service.update(req);
		data.setIsError(false);
		data.setMessage("City Master Updated Successfully");
		data.setErrorMessage(Collections.emptyList());
		data.setCommonResponse(res);
		return new ResponseEntity<>(data, HttpStatus.OK);
	}

	@PostMapping("/search")
	@Operation(summary = "Get the latest amendment of a City Master record by its business key")
	public ResponseEntity<CommonRes> getById(@RequestBody UploadDocCityMasterGetReq req) {
		CommonRes data = new CommonRes();
		UploadDocCityMasterRes res = service.getLatest(req);
		if (res == null) {
			data.setIsError(true);
			data.setMessage("City Master Record Not Found");
			data.setErrorMessage(Collections.emptyList());
			return new ResponseEntity<>(data, HttpStatus.NOT_FOUND);
		}
		data.setIsError(false);
		data.setMessage("Success");
		data.setErrorMessage(Collections.emptyList());
		data.setCommonResponse(res);
		return new ResponseEntity<>(data, HttpStatus.OK);
	}

	@GetMapping
	@Operation(summary = "List the latest amendment of every City Master record")
	public ResponseEntity<CommonRes> getAll() {
		CommonRes data = new CommonRes();
		List<UploadDocCityMasterRes> res = service.getAll();
		data.setIsError(false);
		data.setMessage("Success");
		data.setErrorMessage(Collections.emptyList());
		data.setCommonResponse(res);
		return new ResponseEntity<>(data, HttpStatus.OK);
	}

	@DeleteMapping
	@Operation(summary = "Delete the latest amendment of a City Master record by its business key")
	public ResponseEntity<CommonRes> delete(@RequestBody UploadDocCityMasterGetReq req) {
		CommonRes data = new CommonRes();
		boolean deleted = service.delete(req);
		if (!deleted) {
			data.setIsError(true);
			data.setMessage("City Master Record Not Found");
			data.setErrorMessage(Collections.emptyList());
			return new ResponseEntity<>(data, HttpStatus.NOT_FOUND);
		}
		data.setIsError(false);
		data.setMessage("City Master Deleted Successfully");
		data.setErrorMessage(Collections.emptyList());
		return new ResponseEntity<>(data, HttpStatus.OK);
	}
}
