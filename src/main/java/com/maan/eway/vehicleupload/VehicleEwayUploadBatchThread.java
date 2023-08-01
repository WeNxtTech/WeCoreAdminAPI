package com.maan.eway.vehicleupload;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.maan.eway.batch.entity.EwayUploadTypeMaster;
import com.maan.eway.batch.entity.EwayXlconfigMaster;
import com.maan.eway.batch.res.EwayUploadRes;

public class VehicleEwayUploadBatchThread implements Runnable{
	
	Logger log =LogManager.getLogger(VehicleEwayUploadBatchThread.class);
	
	private VehicleBatchServiceImpl batchServiceImpl;
	
	private EwayUploadRes uploadRes;
	
	private List<EwayXlconfigMaster> xlConfigData;
	
	private EwayUploadTypeMaster uploadTypeMaster;

	public VehicleEwayUploadBatchThread(VehicleBatchServiceImpl batchServiceImpl, EwayUploadRes uploadRes,
			List<EwayXlconfigMaster> xlConfigData, EwayUploadTypeMaster uploadTypeMaster) {
		
		this.batchServiceImpl=batchServiceImpl;
		
		this.uploadRes=uploadRes;
		
		this.uploadTypeMaster=uploadTypeMaster;
		
		this.xlConfigData =xlConfigData;
		
	}

	@Override
	public void run() {
		try {
			log.info("EwayUploadBatchThread || doRawdataInsert|| Start doing raw table insertion...");
			batchServiceImpl.doRawdataInsert(uploadRes,uploadTypeMaster,xlConfigData);
			log.info("EwayUploadBatchThread || doRawdataInsert|| raw table insertion completed");
		}catch (Exception e) {
			log.error(e);
			e.printStackTrace();
		}
		
	}

}
