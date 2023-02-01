package com.maan.eway.springbatch;

import org.springframework.web.multipart.MultipartFile;

import com.maan.eway.fileupload.FileUploadInputRequest;

public interface EwaySpringBatchService {

	FileUploadInputRequest batchUpload(FileUploadInputRequest request, MultipartFile file);

	com.maan.eway.res.CommonRes  getTranactionByTranId(String tranId);

}
