package com.maan.eway.springbatch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.maan.eway.bean.ListItemValue;
import com.maan.eway.master.req.FactorParamsInsert;
import com.maan.eway.master.req.FactorRateSaveReq;
import com.maan.eway.master.service.impl.FactorRateMasterServiceImpl;
import com.maan.eway.res.SuccessRes;

@Component
@JobScope
public class ItemWritter  implements ItemWriter<FactorBatchRecordRes> {
	
	Logger log = LogManager.getLogger(ItemWritter.class);
	
	
	
	@Autowired
	private FactorRateMasterServiceImpl service; 
	
	Map<String,Object> map=new HashMap<String,Object>();
	
	static Gson json = new Gson();
	
	boolean isExeute =true;

	private static DozerBeanMapper mapper =new DozerBeanMapper();
	
	@Override
	public void write(Chunk<? extends FactorBatchRecordRes> itemChunks) throws Exception {
		Integer amendId =0;
		List<ListItemValue> itemValues =new ArrayList<ListItemValue>();
		FactorRateSaveReq req = new FactorRateSaveReq();
		try {	
		    List<? extends FactorBatchRecordRes> items = itemChunks.getItems();
		    
			if(isExeute) {
				req=mapper.map(items.get(0), FactorRateSaveReq.class);
				amendId=service.upadateOldFactor(req);
				itemValues=service.getListItem("99999",req.getBranchCode(),"CALCULATION_TYPE");
				Map<String,Object> coverDetails=service.coverMasterDetails(req);
				map.put("amend_id", amendId);
				map.put("ListItemValue", itemValues);
				map.put("CoverDetails", coverDetails);
				isExeute =false;
			}
			
			FactorRateSaveReq factorRateSaveReq =mapper.map(items.get(0), FactorRateSaveReq.class);
			
			List<FactorParamsInsert> factorParms =items.stream()
													.map(p ->mapper.map(p, FactorParamsInsert.class))
													.collect(Collectors.toList());			
			factorRateSaveReq.setFactorParams(factorParms);
			service.insertNewFactorRate(
					factorRateSaveReq, Integer.valueOf(map.get("amend_id").toString()), factorParms.size(),(List<ListItemValue>) map.get("ListItemValue"), (Map)map.get("CoverDetails"));
			

		}catch (Exception e) {
			e.printStackTrace();
		}
		
	}


}
