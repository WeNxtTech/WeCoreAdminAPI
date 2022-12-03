package com.maan.eway.fileupload;

import com.maan.eway.master.req.FactorRateSaveReq;

public interface EwayFileUploadService {

	com.maan.eway.res.CommonRes download(FileDownloadRequest req);

	com.maan.eway.res.CommonRes upload(String filePath, FileUploadInputRequest request);


}
