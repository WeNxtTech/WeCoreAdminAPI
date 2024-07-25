package com.maan.eway.springbatch;

import java.util.List;

import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
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
	@Autowired
	private UtilityServiceImpl service;
	
	@Value("#{jobParameters[ewayBatchData]}")
	private String data;
	
	static int totalrecordCount =0;
	
		
	@Override
	public void write(Chunk<? extends FactorRateRawInsert> itemChunks) throws Exception {
		try {
			List<? extends FactorRateRawInsert> items = itemChunks.getItems();
			repository.saveAll(items);
		}catch (Exception e) {
			e.printStackTrace();
			SpringBatchMapperResponse factorData =new SpringBatchMapperResponse();
		    ObjectMapper mapper = new ObjectMapper();
		    try {
		    	factorData = mapper.readValue(data, SpringBatchMapperResponse.class);
			    totalrecordCount=Integer.valueOf(factorData.getTotalRecordsCount());
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			service.updateBatchTransaction (factorData.getTranId(), e.getMessage() ,"","BatchInsertion Failed...Contact Admin","E");

		}
	}
	

}
