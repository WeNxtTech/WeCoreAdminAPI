package com.maan.eway.nonmotor.upload;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import com.maan.eway.batch.entity.EwayEmplyeeDetailRaw;
import com.maan.eway.batch.repository.EwayEmplyeeDetailRawRepository;

public class NonMotorRawDataValWritter implements ItemWriter<EwayEmplyeeDetailRaw>{
	
	@Autowired
	private EwayEmplyeeDetailRawRepository emplyeeDetailRawRepo;;

	@Override
	public void write(Chunk<? extends EwayEmplyeeDetailRaw> chunk) throws Exception {
		emplyeeDetailRawRepo.saveAll(chunk.getItems());
	}

}
