package com.maan.eway.fileupload;

import java.io.File;
import java.util.Date;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maan.eway.master.req.FactorRateSaveReq;

@RestController
@RequestMapping("/file")
public class EwayFileUploadController {
	
	@Value("${rating.upload.path}")
	private String ratingFilePath;
	
	@Autowired
	private EwayFileUploadService service;
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_APPROVER','ROLE_USER')")
	@PostMapping("/download")
	public com.maan.eway.res.CommonRes download(@RequestBody FileDownloadRequest req) {
		return service.download(req);
	}
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_APPROVER','ROLE_USER')")
	@PostMapping("/upload")
	public com.maan.eway.res.CommonRes upload(@RequestParam("file") MultipartFile file,@RequestParam("uploadReq") String uploadReq ,@RequestHeader("Authorization") String tokens ){
		com.maan.eway.res.CommonRes response = new com.maan.eway.res.CommonRes();
		try {
			String fileName =FilenameUtils.getBaseName(file.getOriginalFilename());
			String extension=FilenameUtils.getExtension(file.getOriginalFilename());
			String filePath =ratingFilePath +fileName+"_"+new Date().getSeconds()+"."+extension;		
			FileUploadInputRequest request =new FileUploadInputRequest();
			if(StringUtils.isNotBlank(uploadReq)) {
				ObjectMapper mapper =new ObjectMapper();
				request =mapper.readValue(uploadReq, FileUploadInputRequest.class);
			}
			 file.transferTo( new File(filePath));
			 response =service.upload(filePath,request,tokens.replaceAll("Bearer ", "").split(",")[0]) ;
		}catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}
	
	//Premia table Download
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_APPROVER','ROLE_USER')")
	@PostMapping("/premiadownload")
	public com.maan.eway.res.CommonRes premiaDownload(@RequestBody PremiaFileDownloadRequest req) {
		return service.premiaDownload(req);
	}
}
