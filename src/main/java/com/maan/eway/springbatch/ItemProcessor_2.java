package com.maan.eway.springbatch;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class ItemProcessor_2 implements ItemProcessor<FactorBatchRecordRes, FactorBatchRecordRes>  {

	@Override
	public FactorBatchRecordRes process(FactorBatchRecordRes item) throws Exception {
	
		return item;
	}

}
