package com.maan.eway.factorrating.batch;

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

import com.maan.eway.common.res.CommonRes;
import com.maan.eway.fileupload.FileUploadInputRequest;

@RestController
@RequestMapping("/eway/rating")
public class FactorRatingBatchController {
	
	@Autowired
	private FactorRatingBatchService service;
	
	
	@PostMapping("/convertExcelToCSV/{product_id}/{company_id}")
	public CommonRes convertExcelToCSV(@PathVariable("product_id") Integer product_id,
			@PathVariable("company_id") Integer company_id,@RequestParam("file") MultipartFile file) {
		return service.convertExcelToCSV(file,product_id,company_id,"");
	}
	
	@PostMapping("/rawdata/insert")
	public CommonRes rawdataInsert(@RequestBody FileUploadInputRequest req) {
		return service.rawdataInsert(req);
	}
	
	@GetMapping("/maindata/insert/{tran_id}")
	public CommonRes maindataInsert(@PathVariable("tran_id") String tran_id) {
		return service.maindataInsert(tran_id);
	}
	
	@GetMapping("/validate/records/{tran_id}")
	public CommonRes validateReocrds(@PathVariable("tran_id") String tran_id,@RequestHeader("Authorization") String token) {
		return service.validateReocrds(tran_id,token);
	}
	
	@GetMapping("/get/errorRecords/{tran_id}")
	public CommonRes getErrorRecords(@PathVariable("tran_id") String tranId) {
		return service.getErrorRecords(tranId);
	}
	
}
