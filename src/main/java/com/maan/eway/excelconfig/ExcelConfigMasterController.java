package com.maan.eway.excelconfig;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/excel/config")
public class ExcelConfigMasterController {

	@Autowired
	ExcelConfigMasterServiceImpl service;

	@PostMapping("/saveupload")
	public ResponseEntity<CommonResponse> saveUploadType(@RequestBody UploadTypeSaveReq req) {

		List<Errors> errorList = service.validate(req);

		CommonResponse response = new CommonResponse();

		if (errorList.size() > 0) {

			response.setError(true);
			response.setErrorMessage(errorList);
			response.setMessage("Insertion Failed");
			response.setResult(null);

			return new ResponseEntity<CommonResponse>(response, HttpStatus.OK);

		} else {

			SuccessResponse sRes = service.saveUploadType(req);

			if (sRes != null) {

				response.setError(false);
				response.setErrorMessage(Collections.emptyList());
				response.setMessage("Insertion Successful");
				response.setResult(sRes);

				return new ResponseEntity<CommonResponse>(response, HttpStatus.OK);

			} else {
				return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);

			}
		}
	}

	@PostMapping("/getall")
	public ResponseEntity<CommonResponse> getall(@RequestBody UploadTypeGetAllReq req) {

		CommonResponse comRes = new CommonResponse();

		List<UploadTypeResponse> list = service.getAll(req);

		comRes.setError(false);
		comRes.setErrorMessage(Collections.emptyList());
		comRes.setMessage("Found");
		comRes.setResult(list);

		if (list != null) {
			return new ResponseEntity<CommonResponse>(comRes, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("/get")
	public ResponseEntity<CommonResponse> getUploadType(@RequestBody UploadTypeGetReq req) {

		UploadTypeResponse resp = service.get(req);

		CommonResponse comRes = new CommonResponse();

		comRes.setError(false);
		comRes.setErrorMessage(Collections.emptyList());
		comRes.setMessage("Found");
		comRes.setResult(resp);

		if (resp != null) {

			return new ResponseEntity<CommonResponse>(comRes, HttpStatus.OK);
		} else {

			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("/delete")
	public ResponseEntity<CommonResponse> deleteUploadType(@RequestBody UploadTypeDeleteReq req) {

		SuccessResponse sRes = service.deleteUploadType(req);

		CommonResponse comRes = new CommonResponse();

		comRes.setError(false);
		comRes.setErrorMessage(Collections.emptyList());
		comRes.setMessage("Deleted Successfully");
		comRes.setResult(sRes);

		if (sRes != null) {
			return new ResponseEntity<CommonResponse>(comRes, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
	}

//	@GetMapping("/")
//	public List<EwayUploadTypeMaster> get(){
//		return service.getCriteria();
//	}
//	
//	@GetMapping("/criteria")
//	public List<EwayUploadTypeMaster> getAll(){
//		return service.get2Criteria();
//	}
}
