package com.maan.eway.factorrating.batch.configuration;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.maan.eway.springbatch.FactorRateRawInsert;
import com.maan.eway.springbatch.FactorRateRawMasterRepository;
import com.maan.eway.springbatch.SpringBatchMapperResponse;
import com.maan.eway.springbatch.UtilityServiceImpl;

public class RawDataItemWritter implements ItemWriter<FactorRateRawInsert>{

	Gson json = new Gson();

	@Autowired
	private FactorRateRawMasterRepository repository ;
	@Autowired
	private UtilityServiceImpl service;
	
	//@Value("#{jobParameters[ewayBatchData]}")
	private String data;
	
	static int totalrecordCount =0;
	
		
	public RawDataItemWritter(String data) {
		this.data = data;
	}


	@Override
	public void write(Chunk<? extends FactorRateRawInsert> items) throws Exception {
		try {
			repository.saveAll(items.getItems());
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
