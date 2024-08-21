package com.maan.eway.vehicleupload;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.maan.eway.batch.entity.EserviceMotorDetailsRaw;
import com.maan.eway.batch.repository.EserviceMotorDetailsRawRepository;

@Component("veh_apicall_itemWriter")
public class APIItemWriter implements ItemWriter<EserviceMotorDetailsRaw>{

	@Autowired
	private EserviceMotorDetailsRawRepository eserviceMotorRawRepo;;

	@Override
	public void write(Chunk<? extends EserviceMotorDetailsRaw> chunk) throws Exception {
		
		eserviceMotorRawRepo.saveAll(chunk.getItems());
	}
	
}