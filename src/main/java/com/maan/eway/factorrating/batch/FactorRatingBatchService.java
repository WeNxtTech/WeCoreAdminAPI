package com.maan.eway.factorrating.batch;

import org.springframework.web.multipart.MultipartFile;

import com.maan.eway.common.res.CommonRes;
import com.maan.eway.fileupload.FileUploadInputRequest;

public interface FactorRatingBatchService {

	CommonRes convertExcelToCSV(MultipartFile file,Integer product_id, Integer company_id,String request_ref_no);

	CommonRes rawdataInsert(FileUploadInputRequest req);

	CommonRes maindataInsert(String tran_id);

	CommonRes validateReocrds(String tran_id,String token);

	CommonRes getErrorRecords(String tranId);

}
