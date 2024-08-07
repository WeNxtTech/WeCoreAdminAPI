package com.maan.eway.factorrating.batch.configuration;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.maan.eway.springbatch.FactorRateRawInsert;
import com.maan.eway.springbatch.FactorRateRawMasterRepository;

@Component
public class GroppingItemWriter  implements ItemWriter<List<FactorRateRawInsert>>{
	
	@Autowired
	private FactorRateRawMasterRepository factorRateRawMasterRepository;

	@Override
	public void write(Chunk<? extends List<FactorRateRawInsert>> items) throws Exception {
		//Predicate<FactorRateRawInsert> error_check = e -> "E".equals(e.getErrorStatus());
		
		//List<FactorRateRawInsert> facRate =items.getItems().stream().flatMap(p -> p.stream()).filter(error_check)
				//.collect(Collectors.toList());
		
		//factorRateRawMasterRepository.saveAll(facRate);	
			
		
	}

}
