package com.maan.eway.springbatch;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maan.eway.fileupload.FileUploadInputRequest;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("/batch")
@Api(tags ="FACTOR : Excel Upload Api's")
public class EwaySpringBatchController {
	
	@Autowired
	private EwaySpringBatchService service;
	
	@PostMapping("/upload")
	public FileUploadInputRequest batchUpload (@RequestParam("file") MultipartFile file,@RequestParam("uploadReq") String uploadReq,@RequestHeader("Authorization") String authorization  ) {
		FileUploadInputRequest request =new FileUploadInputRequest();
		try {
			if(StringUtils.isNoneBlank(uploadReq)) {
				ObjectMapper mapper =new ObjectMapper();
				request =mapper.readValue(uploadReq, FileUploadInputRequest.class);
			}	
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		request.setAuthorization(authorization);
		return service.batchUpload(request,file);
	}
	
	@GetMapping("/getTranactionByTranId")
	public com.maan.eway.res.CommonRes getTranactionByTranId(@RequestParam("tranId") String tranId) {
		return service.getTranactionByTranId(tranId);
	}
	
	@GetMapping("/doMainJob")
	public com.maan.eway.res.CommonRes doMainJob(@RequestParam("tranId") String tranId) {
		return service.doMainJob(tranId);
	}
	
}
