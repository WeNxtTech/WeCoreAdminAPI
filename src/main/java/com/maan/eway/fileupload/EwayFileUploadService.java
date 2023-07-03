package com.maan.eway.fileupload;

public interface EwayFileUploadService {

	com.maan.eway.res.CommonRes download(FileDownloadRequest req);

	com.maan.eway.res.CommonRes upload(String filePath, FileUploadInputRequest request,String string);

	

}
