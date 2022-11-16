package com.maan.eway.document.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.maan.eway.document.req.DocTypeDropDownReq;
import com.maan.eway.document.req.DocumentDeleteReq;
import com.maan.eway.document.req.DocumentUploadReq;
import com.maan.eway.document.req.FilePathReq;
import com.maan.eway.document.req.GetDocListReq;
import com.maan.eway.document.res.ClientDocListRes;
import com.maan.eway.document.res.DocCategoryRes;
import com.maan.eway.document.res.DocTypeRes;
import com.maan.eway.document.res.FilePathRes;
import com.maan.eway.document.service.DocumentService;
import com.maan.eway.res.CommonRes;
import com.maan.eway.service.PrintReqService;
import com.maan.eway.error.CommonValidationException;
import com.maan.eway.error.Error;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RequestMapping("/document")
@Api(tags = "DOCUMENT : Document ", description = "API's")
@RestController
public class DocumentController {

	@Autowired
	private DocumentService documentservice;
	
	@Autowired
	private PrintReqService reqPrinter;

	
	private Logger log = LogManager.getLogger(DocumentController.class);
	
	@PostMapping("/upload")
	public ResponseEntity<CommonRes> uploadFile(@RequestParam("File") MultipartFile file, @RequestParam("Req") String jsonString) throws CommonValidationException, JsonMappingException, JsonProcessingException{
		
		log.info(jsonString);
		
		DocumentUploadReq req =  new ObjectMapper().readValue(jsonString, DocumentUploadReq.class); 
		
    	List<Error> error = new ArrayList<Error>();
		error = documentservice.docvalidation(req,file);
		if (error != null && error.size() > 0) {
			
			CommonRes res = new CommonRes();
			res.setCommonResponse(null);
			res.setIsError(true);
			res.setErrorMessage(error);
			res.setMessage("File Upload Faild");
			
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(res);
			
		}else {
			CommonRes res = documentservice.fileupload(req,file);
			return ResponseEntity.status(HttpStatus.OK).body(res);
		}
		
	}
	
	
	@PostMapping("/uploadwithoutfile")
	public ResponseEntity<CommonRes> uploadWithoutFile( @RequestParam("Req") String jsonString) throws CommonValidationException, JsonMappingException, JsonProcessingException{
		
		log.info(jsonString);
		 MultipartFile file = null ;
		DocumentUploadReq req =  new ObjectMapper().readValue(jsonString, DocumentUploadReq.class); 
		
    	List<Error> error = new ArrayList<Error>();
		error = documentservice.docvalidation(req,file);
		if (error != null && error.size() > 0) {
			
			CommonRes res = new CommonRes();
			res.setCommonResponse(null);
			res.setIsError(true);
			res.setErrorMessage(error);
			res.setMessage("File Upload Faild");
			
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(res);
			
		}else {
			CommonRes res = documentservice.fileupload(req,file);
			return ResponseEntity.status(HttpStatus.OK).body(res);
		}
		
	}
	
	@PostMapping("/delete")
	public ResponseEntity<CommonRes> deleteFile(@RequestBody DocumentDeleteReq req)  {

		CommonRes res = documentservice.deleteFile(req);
		return ResponseEntity.status(HttpStatus.OK).body(res);
		
	}
	
	//DropDown
	@PostMapping("/dropdown/doctypes")
	@ApiOperation(value = "This method is to Get Insurance Companies")
	public ResponseEntity<CommonRes> getDocTypeDropDowns(@RequestBody DocTypeDropDownReq req) {
		CommonRes data = new CommonRes();
		List<DocTypeRes> res = documentservice.getDocTypeDropDowns(req);
		data.setCommonResponse(res);
		data.setIsError(false);
		data.setErrorMessage(Collections.emptyList());
		data.setMessage("Success");

		return new ResponseEntity<CommonRes>(data, HttpStatus.CREATED);

	}
	
	
	//Get Doc List
	@PostMapping("/getdoclist")
	@ApiOperation(value = "This method is to Get Document List")
	public ResponseEntity<CommonRes> getdoclist(@RequestBody GetDocListReq req) {

		reqPrinter.reqPrint(req);
		CommonRes data = new CommonRes();

		// Total Doc List
		List<ClientDocListRes> res = documentservice.getTotalDocList(req);

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

	//Get Original Image
	@RequestMapping(path = "/download", method = RequestMethod.POST)
	public ResponseEntity<Resource> download(@RequestParam("FilePath") String param) throws IOException {

		File file = new File(param);
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getName());

	    InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

	    return ResponseEntity.ok()
	            .headers(headers)
	            .contentLength(file.length())
	            .contentType(MediaType.APPLICATION_OCTET_STREAM)
	            .body(resource);
	}

	
	@PostMapping("/getoriginalimage" )	 
	@ApiOperation(value="This method is to Get Image File ")
	private ResponseEntity<CommonRes>  getFilePath(@RequestBody FilePathReq req) {
		reqPrinter.reqPrint(req);
		CommonRes data = new CommonRes()  ; 
		
		FilePathRes res = documentservice.getFilePath(req);
		data .setCommonResponse(res);
		data.setIsError(false);
		data.setErrorMessage(Collections.emptyList());
		data.setMessage("Success");

		if (res != null) {
		return new ResponseEntity<CommonRes>(data, HttpStatus.CREATED);
		} else {
		return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
		
	}
	
	@PostMapping("/getcompressedimage" )	 
	@ApiOperation(value="This method is to Get Compressed Image File ")
	private ResponseEntity<CommonRes>  getCompressedImages(@RequestBody FilePathReq req) {
		reqPrinter.reqPrint(req);
		CommonRes data = new CommonRes()  ; 
		
		FilePathRes res = documentservice.getCompressedImages(req);
		data .setCommonResponse(res);
		data.setIsError(false);
		data.setErrorMessage(Collections.emptyList());
		data.setMessage("Success");

		if (res != null) {
		return new ResponseEntity<CommonRes>(data, HttpStatus.CREATED);
		} else {
		return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
		
	}
	

}
	
	
	

