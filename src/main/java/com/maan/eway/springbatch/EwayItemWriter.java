package com.maan.eway.springbatch;

import java.util.List;

import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

import lombok.extern.slf4j.Slf4j;


@Component
@Configuration
@JobScope
@Slf4j
public class EwayItemWriter implements ItemWriter<FactorRateRawInsert>{

	Gson json = new Gson();

	@Autowired
	private FactorRateRawMasterRepository repository ;
		
	@Override
	public void write(List<? extends FactorRateRawInsert> items) throws Exception {
		try {
			repository.saveAll(items);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	

}
