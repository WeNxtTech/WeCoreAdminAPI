package com.maan.eway.common.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
//import java.util.List;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maan.eway.common.req.DataManipulationReq;
import com.maan.eway.common.req.DeleteTiraSearchedVehicleReq;
import com.maan.eway.common.req.GetAllTirraErrorHistory;
import com.maan.eway.common.req.GetTirraEorrorHistoryReq;
import com.maan.eway.common.req.PolicyRevertReq;
import com.maan.eway.common.req.TiraGetReq;
import com.maan.eway.common.req.TiraPushedDetailsReq;
import com.maan.eway.common.req.TransactionCheckStatusReq;
import com.maan.eway.common.res.TiraErrorHistoryTotalRes;
import com.maan.eway.common.res.TiraPushedDetailsRes;
import com.maan.eway.common.res.TiraPushedListDetailsRes;
import com.maan.eway.common.res.TransactionCheckStatusRes;
import com.maan.eway.common.service.ReportsService;
import com.maan.eway.error.Error;
import com.maan.eway.res.CommonRes;
import com.maan.eway.res.LogDetailsRes;
import com.maan.eway.res.SuccessRes2;
import com.maan.eway.service.PrintReqService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/reports")
@Api(tags="Reports Controller", description="API's")
public class ReportsController {
	
	@Autowired
	private ReportsService service;

	@Autowired
	private PrintReqService reqPrinter;

	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_APPROVER')")
	@PostMapping("/errorhistory")
	@ApiOperation(value="This method is to Get Tirra Error History")
	public ResponseEntity<CommonRes> getTirraErrorHistory(@RequestBody GetTirraEorrorHistoryReq req){
		reqPrinter.reqPrint(req);
		CommonRes data = new CommonRes();
		TiraErrorHistoryTotalRes res = service.getTirraEorrorHistory(req);
		data.setCommonResponse(res);
		data.setErrorMessage(Collections.emptyList());
		data.setIsError(false);
		data.setMessage("Success");
		if(res!=null) {
			return new ResponseEntity<CommonRes>(data, HttpStatus.CREATED);
		}
		else {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
	}
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_APPROVER')")
	@PostMapping("/alltirrahistory")
	@ApiOperation(value="This method is to Get All Tirra Error History")
	public ResponseEntity<CommonRes> getAllTirraErrorHistory(@RequestBody GetAllTirraErrorHistory req){
		reqPrinter.reqPrint(req);
		CommonRes data = new CommonRes();
		TiraErrorHistoryTotalRes res = service.getAllTirraErrorHistory(req);
		data.setCommonResponse(res);
		data.setErrorMessage(Collections.emptyList());
		data.setIsError(false);
		data.setMessage("Success");
		if(res!=null) {
			return new ResponseEntity<CommonRes>(data, HttpStatus.CREATED);
		}
		else {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
	}
	
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_APPROVER')")
	@PostMapping("/getalltiraintegpusheddetails")
	public ResponseEntity<CommonRes> getallTiraIntegrationPushedDetails(@RequestBody TiraPushedDetailsReq req) {
		reqPrinter.reqPrint(req);
		CommonRes data = new CommonRes();
		TiraPushedListDetailsRes res = service.getallTiraIntegrationPushedDetails(req);
		data.setCommonResponse(res);
		data.setIsError(false);
		data.setErrorMessage(Collections.emptyList());
		data.setMessage("Success");
		if (res != null) {
			return new ResponseEntity<CommonRes>(data, HttpStatus.CREATED);
		} else {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
	}
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_APPROVER')")
	@PostMapping("/gettiraintegpusheddetails")
	public ResponseEntity<CommonRes> getTiraIntegrationPushedDetails(@RequestBody TiraGetReq req) {
		reqPrinter.reqPrint(req);
		CommonRes data = new CommonRes();
		List<TiraPushedDetailsRes> res = service.getTiraIntegrationPushedDetails(req);
		data.setCommonResponse(res);
		data.setIsError(false);
		data.setErrorMessage(Collections.emptyList());
		data.setMessage("Success");
		if (res != null) {
			return new ResponseEntity<CommonRes>(data, HttpStatus.CREATED);
		} else {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
	}
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_APPROVER')")
	@PostMapping("/deletetirasearchedvehicle")
	public ResponseEntity<CommonRes> deleteTiraSearchVehicle(@RequestBody DeleteTiraSearchedVehicleReq req) {
		reqPrinter.reqPrint(req);
		CommonRes data = new CommonRes();
		SuccessRes2 res = service.deleteTiraSearchVehicle(req);
		data.setCommonResponse(res);
		data.setIsError(false);
		data.setErrorMessage(Collections.emptyList());
		data.setMessage("Success");
		if (res != null) {
			return new ResponseEntity<CommonRes>(data, HttpStatus.CREATED);
		} else {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
	}
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_APPROVER')")
	@PostMapping("/transactioncheckstatus")
	public ResponseEntity<CommonRes> getTransactionCheckStatusDetails(@RequestBody TransactionCheckStatusReq req) {
		reqPrinter.reqPrint(req);
		CommonRes data = new CommonRes();
		TransactionCheckStatusRes res = service.getTransactionCheckStatusDetails(req);
		data.setCommonResponse(res);
		data.setIsError(false);
		data.setErrorMessage(Collections.emptyList());
		data.setMessage("Success");
		if (res != null) {
			return new ResponseEntity<CommonRes>(data, HttpStatus.CREATED);
		} else {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
	}
	
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_APPROVER')")
	@PostMapping("/policyreverttoquote")
	public ResponseEntity<CommonRes> policyRevertToQuote(@RequestBody PolicyRevertReq req) {
		reqPrinter.reqPrint(req);
		CommonRes data = new CommonRes();
		SuccessRes2 res = service.policyRevertToQuote(req);
		data.setCommonResponse(res);
		data.setIsError(false);
		data.setErrorMessage(Collections.emptyList());
		data.setMessage("Success");
		if (res != null) {
			return new ResponseEntity<CommonRes>(data, HttpStatus.CREATED);
		} else {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
	}
	
//	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_APPROVER')")
	@PostMapping("/datamanipulation")
	public ResponseEntity<CommonRes> dataManipulation(@RequestBody DataManipulationReq req) {
		reqPrinter.reqPrint(req);
		CommonRes data = new CommonRes();
		List<Error> validation = service.validatedataManipulation(req);
		// validation
		if (validation != null && validation.size() != 0) {
			data.setCommonResponse(null);
			data.setIsError(true);
			data.setErrorMessage(validation);
			data.setMessage("Failed");
			return new ResponseEntity<CommonRes>(data, HttpStatus.OK);

		} else {
		List<Map<String, Object>> res = service.dataManipulation(req);
		if (res == null && req.getQuery().toLowerCase().contains("select")) {
			List<Error> error = new ArrayList<Error>();
			error.add(new Error("01","Not Found", "No Data Found"));
			data.setCommonResponse(res);
			data.setErrorMessage(error);
			data.setIsError(true);
			data.setMessage("Failed");
			return new ResponseEntity<>(data, HttpStatus.CREATED);
		}else {
		data.setCommonResponse(res);
		data.setIsError(false);
		data.setErrorMessage(Collections.emptyList());
		data.setMessage("Success");
		}
		if (res != null) {
			return new ResponseEntity<CommonRes>(data, HttpStatus.CREATED);
		} else {
			
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
		}
	}
	@GetMapping("/LogDetails")
	public ResponseEntity<CommonRes> LogDetails() {
		CommonRes data = new CommonRes();
		String filePath=System.getProperty("user.dir").replace("bin", "logs");
		System.out.println("filePath==>"+filePath);
		List<LogDetailsRes> res = service.LogDetails(filePath);
		
		data.setCommonResponse(res);
		data.setIsError(false);
		data.setErrorMessage(Collections.emptyList());
		data.setMessage("Success");
		
		if (res != null) {
			return new ResponseEntity<CommonRes>(data, HttpStatus.CREATED);
		} else {
			
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
		
	}
	@GetMapping("/downloadLogfile/{fileName}")
	public ResponseEntity<Resource> downloadLogfile(@PathVariable ("fileName") String fileName) {
	    final HttpHeaders httpHeaders = new HttpHeaders();
	    String filePath=System.getProperty("user.dir").replace("bin", "logs");
	    File file = new File(filePath+"/"+fileName);
	    final FileSystemResource resource = new FileSystemResource(file);
	    httpHeaders.set(HttpHeaders.LAST_MODIFIED, String.valueOf(file.lastModified()));
	    httpHeaders.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"");
	    httpHeaders.set(HttpHeaders.CONTENT_LENGTH, String.valueOf(file.length()));
	    return ResponseEntity.ok()
	        .headers(httpHeaders)
	        .contentLength(file.length())
	        .contentType(MediaType.APPLICATION_OCTET_STREAM)
	        .body(resource);
	}
}
