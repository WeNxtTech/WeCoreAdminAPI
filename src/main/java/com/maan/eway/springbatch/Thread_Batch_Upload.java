package com.maan.eway.springbatch;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.maan.eway.fileupload.FileUploadInputRequest;

public class Thread_Batch_Upload implements Runnable {

	
	Logger log =LogManager.getLogger(getClass());
	
	private String type;
	private UtilityServiceImpl utilService;
	private  FileUploadInputRequest request;

	public Thread_Batch_Upload(UtilityServiceImpl utilService, FileUploadInputRequest request, String type) {
		this.type=type;
		this.request=request;
		this.utilService=utilService;
	}

	@Override
	public void run() {
		log.info("Thread_Batch_Upload thread calling || "+type);
		try {
			if("Batch_upload".equalsIgnoreCase(type)) {
				utilService.convertToCsvFile(utilService,request);
			}
		}catch (Exception e) {
			log.error(e);
		}
		
	}

}
