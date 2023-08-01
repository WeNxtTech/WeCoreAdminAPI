package com.maan.eway.vehicleupload;

import org.springframework.web.multipart.MultipartFile;

import com.maan.eway.batch.req.DeleteRecordReq;
import com.maan.eway.batch.req.EditRecordReq;
import com.maan.eway.batch.req.EwayUploadReq;
import com.maan.eway.batch.req.GetRecordsReq;
import com.maan.eway.batch.req.GetUploadTransactionReq;
import com.maan.eway.batch.req.SamplFileDownloadReq;
import com.maan.eway.batch.req.UpdateRecordReq;
import com.maan.eway.batch.res.EwayUploadRes;
import com.maan.eway.res.CommonRes;

public interface VehicleBatchService {

	EwayUploadRes batchUpload(MultipartFile file, EwayUploadReq req, String token);

	CommonRes getUploadTransaction(GetUploadTransactionReq req);

	CommonRes getTransactionStatus(GetUploadTransactionReq req);

	CommonRes getRecordByStatus(GetRecordsReq req);

	CommonRes editRecord(EditRecordReq req);

	CommonRes insertRecords(GetRecordsReq req, String auth);

	CommonRes updateRecords(UpdateRecordReq req,String token);

	CommonRes deleteRecords(DeleteRecordReq req);

	CommonRes sampleDownload(SamplFileDownloadReq req);

}
