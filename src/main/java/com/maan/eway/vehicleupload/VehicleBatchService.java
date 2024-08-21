package com.maan.eway.vehicleupload;

import java.util.List;

import com.maan.eway.batch.req.DeleteRecordReq;
import com.maan.eway.batch.req.EditRecordReq;
import com.maan.eway.batch.req.EwayUploadReq;
import com.maan.eway.batch.req.GetRecordsReq;
import com.maan.eway.batch.req.GetUploadTransactionReq;
import com.maan.eway.batch.req.GetUploadTypeReq;
import com.maan.eway.batch.req.MoveRecordsReq;
import com.maan.eway.batch.req.SamplFileDownloadReq;
import com.maan.eway.batch.req.SaveUploadTypeReq;
import com.maan.eway.batch.req.UpdateEmployeeRecordReq;
import com.maan.eway.batch.req.UpdateRecordReq;
import com.maan.eway.batch.res.SaveXlConfigReq;
import com.maan.eway.res.CommonRes;

public interface VehicleBatchService {

	CommonRes batchUpload(EwayUploadReq req, String token);

	CommonRes getUploadTransaction(GetUploadTransactionReq req);

	CommonRes getTransactionStatus(GetUploadTransactionReq req);

	CommonRes getRecordByStatus(GetRecordsReq req);

	CommonRes editRecord(EditRecordReq req);

	CommonRes insertRecords(GetRecordsReq req, String auth);

	CommonRes updateRecords(UpdateRecordReq req,String token);

	CommonRes deleteRecords(DeleteRecordReq req);

	CommonRes sampleDownload(SamplFileDownloadReq req);

	CommonRes saveUploadMaster(SaveUploadTypeReq req);

	CommonRes getUploadMaster(GetUploadTypeReq req);

	Object moveRecords(MoveRecordsReq req, String token);

	CommonRes saveExcelField(List<SaveXlConfigReq> req);

	CommonRes getUploadRecord(MoveRecordsReq req);

	CommonRes updateEmployeeRecord(UpdateEmployeeRecordReq req);

	Object batchCreateQuote(String request_ref_no, String authorization);

	CommonRes vehicleValidation(String request_ref_no);

	void deleteRawData(String request_ref_no);

}
