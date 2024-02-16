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

import com.maan.eway.common.res.CommonRes;
import com.maan.eway.common.service.impl.FetchErrorDescServiceImpl;
import com.maan.eway.error.Error;
import com.maan.eway.req.CommonErrorModuleReq;
import com.maan.eway.res.SuccessRes;

@RestController
@RequestMapping("/excel/config")
public class ExcelConfigMasterController {

	@Autowired
	ExcelConfigMasterServiceImpl service;
	
	@Autowired
	private FetchErrorDescServiceImpl errorDescService ;

	@PostMapping("/saveupload")
	public ResponseEntity<CommonRes> saveUploadType(@RequestBody UploadTypeSaveReq req) {
		
	
		CommonRes data = new CommonRes();
		List<String> validationCodes =  service.validate(req);
		List<Error> validation = null;
		if(validationCodes!=null && validationCodes.size() > 0 ) {
			CommonErrorModuleReq comErrDescReq = new CommonErrorModuleReq();
			comErrDescReq.setBranchCode("9999");
			comErrDescReq.setInsuranceId(req.getCompanyId());
			comErrDescReq.setProductId("99999");
			comErrDescReq.setModuleId("31");
			comErrDescReq.setModuleName("MASTERS");
			
			validation = errorDescService.getErrorDesc(validationCodes ,comErrDescReq);
		}
		

		CommonResponse response = new CommonResponse();

		if(validation !=null && validation.size()!=0) {
			data.setCommonResponse(null);
			data.setIsError(true);
			data.setErrorMessage(validation);
			data.setMessage("Failed");
			return new ResponseEntity<CommonRes>(data,HttpStatus.OK);
		} else {
			//save
			SuccessRes res = service.saveUploadType(req);
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
			comRes.setError(true);
			comRes.setErrorMessage(Collections.emptyList());
			comRes.setMessage("No Data Found");
			comRes.setResult(Collections.emptyList());
			return new ResponseEntity<CommonResponse>(comRes, HttpStatus.OK);
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
