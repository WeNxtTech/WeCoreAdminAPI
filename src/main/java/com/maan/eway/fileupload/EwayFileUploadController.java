package com.maan.eway.fileupload;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maan.eway.common.res.CommonRes;
import com.maan.eway.error.Error;

@RestController
@RequestMapping("/file")
public class EwayFileUploadController {
	
	
	@Autowired
	private EwayFileUploadService service;
	
	
	@PostMapping("/download")
	public com.maan.eway.res.CommonRes download(@RequestBody FileDownloadRequest req) {
		return service.download(req);
	}
	

}
