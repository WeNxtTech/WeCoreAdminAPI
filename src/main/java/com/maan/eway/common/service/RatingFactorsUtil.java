package com.maan.eway.common.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.persistence.Tuple;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import com.maan.eway.bean.ConstantTableDetails;
import com.maan.eway.bean.DropdownTableDetails;
import com.maan.eway.master.req.MasterApiCallReq;
import com.maan.eway.req.calcengine.CalcEngine;
import com.maan.eway.upgrade.criteria.CriteriaService;
import com.maan.eway.upgrade.criteria.SpecCriteria;

@Component

public class RatingFactorsUtil {
	@Autowired
	protected CriteriaService crservice;
	
	protected SimpleDateFormat DD_MM_YYYY = new SimpleDateFormat("dd/MM/yyyy")  ;
	
	
	@Cacheable(cacheNames = {"LoadConstant"},keyGenerator  = "loadConstantKeyGen",value = "LoadConstant" )
	public List<MasterApiCallReq> LoadConstant(CalcEngine engine) {
		try {
			String todayInString = DD_MM_YYYY.format(new Date());
			String search="companyId:"+ engine.getInsuranceId() +";productId:"+engine.getProductId()+";status:{Y,R};"+todayInString+"~effectiveDateStart&effectiveDateEnd;branchCode:{99999,"+engine.getBranchCode()+"};";
			List<Tuple> result=null;
			SpecCriteria criteria = crservice.createCriteria(ConstantTableDetails.class, search, "itemId"); 
			result=crservice.getResult(criteria, 0, 50);
			List<MasterApiCallReq> refreqs=null;
			if(result!=null && result.size()>0) {
				
				refreqs=new ArrayList<MasterApiCallReq>();
				
				
				
				for(Tuple r :result) {
					String itemId=r.get("itemId")==null?"":r.get("itemId").toString();
					
					MasterApiCallReq req=MasterApiCallReq.builder().apiLink(r.get("apiUrl")==null?"":r.get("apiUrl").toString())
							.primaryTable(r.get("keyTable")==null?"":r.get("keyTable").toString())
							.primaryKey(r.get("keyName")==null?"":r.get("keyName").toString())
							.build();
					
					
					List<Tuple> loadDropdown = loadDropdown(engine, itemId);
					List<Map<String,String>> mps=null;
					if(loadDropdown!=null && loadDropdown.size()>0) {
						mps=new ArrayList<Map<String,String>>();	
						for(Tuple l :loadDropdown) {
							Map<String,String> mp=new HashMap<String,String>();
							mp.put("JsonKey", l.get("requestJsonKey")==null?"":l.get("requestJsonKey").toString());
							mp.put("JsonColum", l.get("requestColumn")==null?"":l.get("requestColumn").toString());
							mp.put("JsonTable", l.get("requestTable")==null?"":l.get("requestTable").toString());
							mps.add(mp);
						}
						req.setMp(mps);
					}
					refreqs.add(req);
				}
				
			}
			
			return refreqs;
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}	
	
	public List<Tuple> loadDropdown(CalcEngine engine,String itemId){
		try {
			String todayInString = DD_MM_YYYY.format(new Date());
			String search="companyId:"+ engine.getInsuranceId() +";productId:"+engine.getProductId()+";status:{Y,R};"+todayInString+"~effectiveDateStart&effectiveDateEnd;branchCode:{99999,"+engine.getBranchCode()+"};itemId:"+itemId+";";
			List<Tuple> result=null;
			SpecCriteria criteria = crservice.createCriteria(DropdownTableDetails.class, search, "requestId"); 
			result=crservice.getResult(criteria, 0, 50);

			return result;
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
