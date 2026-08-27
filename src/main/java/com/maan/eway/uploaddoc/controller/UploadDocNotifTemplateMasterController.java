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
import com.maan.eway.uploaddoc.dto.request.UploadDocNotifTemplateMasterGetReq;
import com.maan.eway.uploaddoc.dto.request.UploadDocNotifTemplateMasterSaveReq;
import com.maan.eway.uploaddoc.dto.request.UploadDocNotifTemplateMasterUpdateReq;
import com.maan.eway.uploaddoc.dto.response.UploadDocNotifTemplateMasterRes;
import com.maan.eway.uploaddoc.service.UploadDocNotifTemplateMasterService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Named with an "UploadDoc" prefix to avoid a Spring bean-name collision with
 * the pre-existing {@code com.maan.eway.master.controller.NotifTemplateMasterController}.
 */
@RestController
@RequestMapping("/api/v1/uploaddoc/notif-template-master")
@Tag(name = "Upload Document : Notif Template Master")
public class UploadDocNotifTemplateMasterController {

	@Autowired
	private UploadDocNotifTemplateMasterService service;

	@PostMapping
	@Operation(summary = "Create a new Notif Template Master record")
	public ResponseEntity<CommonRes> save(@RequestBody UploadDocNotifTemplateMasterSaveReq req) {
		CommonRes data = new CommonRes();
		List<Error> errors = service.validateSave(req);
		if (!errors.isEmpty()) {
			data.setIsError(true);
			data.setMessage("Validation Failed");
			data.setErrorMessage(errors);
			return new ResponseEntity<>(data, HttpStatus.UNPROCESSABLE_ENTITY);
		}
		UploadDocNotifTemplateMasterRes res = service.save(req);
		data.setIsError(false);
		data.setMessage("Notif Template Master Created Successfully");
		data.setErrorMessage(Collections.emptyList());
		data.setCommonResponse(res);
		return new ResponseEntity<>(data, HttpStatus.CREATED);
	}

	@PutMapping
	@Operation(summary = "Amend an existing Notif Template Master record (creates the next AMEND_ID)")
	public ResponseEntity<CommonRes> update(@RequestBody UploadDocNotifTemplateMasterUpdateReq req) {
		CommonRes data = new CommonRes();
		List<Error> errors = service.validateUpdate(req);
		if (!errors.isEmpty()) {
			data.setIsError(true);
			data.setMessage("Validation Failed");
			data.setErrorMessage(errors);
			return new ResponseEntity<>(data, HttpStatus.UNPROCESSABLE_ENTITY);
		}
		UploadDocNotifTemplateMasterRes res = service.update(req);
		data.setIsError(false);
		data.setMessage("Notif Template Master Updated Successfully");
		data.setErrorMessage(Collections.emptyList());
		data.setCommonResponse(res);
		return new ResponseEntity<>(data, HttpStatus.OK);
	}

	@PostMapping("/search")
	@Operation(summary = "Get the latest amendment of a Notif Template Master record by its business key")
	public ResponseEntity<CommonRes> getById(@RequestBody UploadDocNotifTemplateMasterGetReq req) {
		CommonRes data = new CommonRes();
		UploadDocNotifTemplateMasterRes res = service.getLatest(req);
		if (res == null) {
			data.setIsError(true);
			data.setMessage("Notif Template Master Record Not Found");
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
	@Operation(summary = "List the latest amendment of every Notif Template Master record")
	public ResponseEntity<CommonRes> getAll(@RequestBody UploadDocNotifTemplateMasterGetReq req) {
		CommonRes data = new CommonRes();
		List<UploadDocNotifTemplateMasterRes> res = service.getAll(req);
		data.setIsError(false);
		data.setMessage("Success");
		data.setErrorMessage(Collections.emptyList());
		data.setCommonResponse(res);
		return new ResponseEntity<>(data, HttpStatus.OK);
	}

	@DeleteMapping
	@Operation(summary = "Delete the latest amendment of a Notif Template Master record by its business key")
	public ResponseEntity<CommonRes> delete(@RequestBody UploadDocNotifTemplateMasterGetReq req) {
		CommonRes data = new CommonRes();
		boolean deleted = service.delete(req);
		if (!deleted) {
			data.setIsError(true);
			data.setMessage("Notif Template Master Record Not Found");
			data.setErrorMessage(Collections.emptyList());
			return new ResponseEntity<>(data, HttpStatus.NOT_FOUND);
		}
		data.setIsError(false);
		data.setMessage("Notif Template Master Deleted Successfully");
		data.setErrorMessage(Collections.emptyList());
		return new ResponseEntity<>(data, HttpStatus.OK);
	}
}
