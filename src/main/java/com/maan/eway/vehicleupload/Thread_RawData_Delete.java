package com.maan.eway.vehicleupload;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.maan.eway.springbatch.FactorRateRawMasterRepository;

public class Thread_RawData_Delete extends Thread{
	Logger log = LogManager.getLogger(Thread_RawData_Delete.class);
	
	 private String referenceNo;
	 
	 private FactorRateRawMasterRepository repository;
	
	 Thread_RawData_Delete(String referenceNo,FactorRateRawMasterRepository repository){
		 this.referenceNo=referenceNo;
		 this.repository=repository;
		 
	}
	
	@Override
	public void run() {
		try {
			repository.deleteFactorData(referenceNo);
			repository.deleteMotorRawData(referenceNo);
		}catch (Exception e) {
			log.error(e);
			e.printStackTrace();
		}
		
	}

}
