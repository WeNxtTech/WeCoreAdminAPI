package com.maan.eway.vehicleupload;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.maan.eway.batch.entity.EwayUploadTypeMaster;
import com.maan.eway.batch.res.EwayUploadRes;


public class VehicleThread_CSV_Convertion implements Runnable {
	
	Logger log =LogManager.getFormatterLogger(VehicleThread_CSV_Convertion.class);
	
	private EwayUploadRes uploadRes;
	
	private VehicleCSVFileConvertion csvFileConvertion;
	
	private EwayUploadTypeMaster uploadTypeMaster;

	public VehicleThread_CSV_Convertion(EwayUploadRes uploadRes, VehicleCSVFileConvertion csvFileConvertion, EwayUploadTypeMaster uploadTypeMaster) {
		
		this.uploadRes=uploadRes;
		
		this.csvFileConvertion =csvFileConvertion;
		
		this.uploadTypeMaster=uploadTypeMaster;
	}

	@Override
	public void run() {
		try {
			log.info("Thread_CSV_Convertion || Run || Start doing CSVCovertion ..");
			csvFileConvertion.doCSVCovertion(uploadRes,uploadTypeMaster);
			log.info("Thread_CSV_Convertion || Run || CSVConvertion completed");
		}catch (Exception e) {
			log.error(e);
			e.printStackTrace();
		}
		
	}
}
