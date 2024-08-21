package com.maan.eway.vehicleupload;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maan.eway.batch.repository.UgandaVehicleDetailsRepository;
import com.maan.eway.batch.req.DeleteRecordReq;
import com.maan.eway.batch.req.EditRecordReq;
import com.maan.eway.batch.req.EwayUploadReq;
import com.maan.eway.batch.req.GetRecordsReq;
import com.maan.eway.batch.req.GetUploadTransactionReq;
import com.maan.eway.batch.req.GetUploadTypeReq;
import com.maan.eway.batch.req.MoveRecordsReq;
import com.maan.eway.batch.req.SamplFileDownloadReq;
import com.maan.eway.batch.req.SaveUploadTypeReq;
import com.maan.eway.batch.req.UpdateEmployeeRecordReq;
import com.maan.eway.batch.req.UpdateRecordReq;
import com.maan.eway.batch.res.EwayUploadRes;
import com.maan.eway.batch.res.SaveXlConfigReq;
import com.maan.eway.res.CommonRes;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("/eway/vehicle")
@Api(tags ="PRODUCTS : Excel Upload Api's")
public class VehicleBatchController {
	
	Logger log =LogManager.getLogger(VehicleBatchServiceImpl.class);
	
	@Autowired
	private VehicleBatchService service;
	
	@Autowired
	private UgandaVehicleDetailsRepository repository ;
	
	
	@PostMapping(value="/batch/upload")
	public CommonRes batchUpload(@RequestBody EwayUploadReq req,@RequestHeader("Authorization") String token) {				
		return service.batchUpload(req,token.replaceAll("Bearer ", "").split(",")[0]);		
	}
	
	@GetMapping("/vehicle/validation/{request_ref_no}")
	public CommonRes vehicleValidation(@PathVariable("request_ref_no") String request_ref_no) {
		return service.vehicleValidation(request_ref_no);
	}
	
	@PostMapping("/get/transaction/status")
	public CommonRes getTransactionStatus(@RequestBody GetUploadTransactionReq req) {
		return service.getTransactionStatus(req);
	}
	
	@PostMapping("/getUploadTransaction")
	public CommonRes getUploadTransaction(@RequestBody GetUploadTransactionReq req) {
		return service.getUploadTransaction(req);
	}
	
	@PostMapping("/get/recordsByStatus")
	public CommonRes getRecordByStatus(@RequestBody GetRecordsReq req) {
		return service.getRecordByStatus(req);
	}
	
	@PostMapping("/edit/record")
	public CommonRes editRecord(@RequestBody EditRecordReq req) {
		return service.editRecord(req);
	}
	
	//@PostMapping("/insert/records")
	public CommonRes insertRecords(@RequestBody GetRecordsReq req,@RequestHeader("Authorization") String Authorization) {
		return service.insertRecords(req,Authorization.replaceAll("Bearer ", "").split(",")[0]);
	}
	
	@PostMapping("/update/records")
	public CommonRes updateRecords(@RequestBody UpdateRecordReq req,@RequestHeader("Authorization") String Authorization) {
		return service.updateRecords(req,Authorization.replaceAll("Bearer ", "").split(",")[0]);
	}

	@PostMapping("/delete/records")
	public CommonRes deleteRecords(@RequestBody DeleteRecordReq req) {
		return service.deleteRecords(req);
	}
	
	@PostMapping("/sample/download")
	public CommonRes sampleDownload(@RequestBody SamplFileDownloadReq req) {
		return service.sampleDownload(req);
	}
	
	@PostMapping("/saveUploadType")
	public CommonRes saveUploadMaster(@RequestBody SaveUploadTypeReq req) {
		return service.saveUploadMaster(req);
	}
	
	@PostMapping("/getUploadType")
	public CommonRes getUploadMaster(@RequestBody GetUploadTypeReq req) {
		return service.getUploadMaster(req);
	}
	
   
	@PostMapping("/saveExcelField")
	public CommonRes saveExcelField(@RequestBody List<SaveXlConfigReq> req) {
		return service.saveExcelField(req);
	}
	
    @PostMapping("/insert/records")
    public Object moveRecords(@RequestBody MoveRecordsReq req,@RequestHeader("Authorization") String token) {
    	return service.moveRecords(req,token.replaceAll("Bearer ", "").split(",")[0]);
    }
    
    
    @PostMapping("/get/upload/record")
    public CommonRes getUploadRecord(@RequestBody MoveRecordsReq req) {
    	return service.getUploadRecord(req);
    }

    @PostMapping("/update/employee/record")
    public CommonRes updateEmployeeRecord(@RequestBody UpdateEmployeeRecordReq req) {
    	return service.updateEmployeeRecord(req);
    }
    
    @PostMapping("/renewalQuote")
    public Map<String,Object> getRenewalQuote(@RequestBody RenewalQuoteReq req){
    	 Map<String,Object> response = new  HashMap<String,Object>();
    	try {
    		List<Map<String,Object>> list =repository.getrenewalData(req.getRegistrationNo(), req.getCompanyId());
    		response =list.get(0);
    	}catch (Exception e) {
			e.printStackTrace();
		}
		return response;
    }
    
    @GetMapping("/batch/createquote/{request_ref_no}")
    public Object batchCreateQuote(@PathVariable("request_ref_no") String request_ref_no,@RequestHeader("Authorization") String authorization) {
    	return service.batchCreateQuote(request_ref_no,authorization.replaceAll("Bearer ", "").split(",")[0]);
    }
    
    @GetMapping("/delete/rawdata/{request_ref_no}")
    public void deleteRawData(@PathVariable("request_ref_no") String request_ref_no) {
    	service.deleteRawData(request_ref_no);
    }

}
