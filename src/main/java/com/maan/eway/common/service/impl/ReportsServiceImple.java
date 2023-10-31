package com.maan.eway.common.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.maan.eway.batch.repository.TirraErrorHistoryRepository;
import com.maan.eway.bean.TirraErrorHistory;
import com.maan.eway.common.req.GetAllTirraErrorHistory;
import com.maan.eway.common.req.GetTirraEorrorHistoryReq;
import com.maan.eway.common.res.GetTirraEorrorHistoryRes;
import com.maan.eway.common.service.ReportsService;


@Service
@Transactional
public class ReportsServiceImple implements ReportsService {

	@Autowired
	private TirraErrorHistoryRepository repo;
	
	DozerBeanMapper mapper = new DozerBeanMapper();
	
	@Override
	public List<GetTirraEorrorHistoryRes> getTirraEorrorHistory(GetTirraEorrorHistoryReq req) {
		List<GetTirraEorrorHistoryRes> response = new ArrayList<GetTirraEorrorHistoryRes>();
		try {
			
			List<TirraErrorHistory> list = repo.findByReqRegNumberAndEntryDateBetween(req.getRegistrationNumber(), req.getEffectiveStartDate(), req.getEffectiveEndDate());
			
			if(list.size()>0) {
				for(TirraErrorHistory data : list) {
					GetTirraEorrorHistoryRes res = new GetTirraEorrorHistoryRes();
					mapper.map(data, res);
					
					if(data.getResponseStatus().equalsIgnoreCase("TIRA001"))
						res.setResponseStatusDesc("Successfull");
					else
						res.setResponseStatusDesc("Failed");
					
					response.add(res);
				}
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	@Override
	public List<GetTirraEorrorHistoryRes> getAllTirraErrorHistory(GetAllTirraErrorHistory req) {

		List<GetTirraEorrorHistoryRes> response = new ArrayList<GetTirraEorrorHistoryRes>();
		try {
			
			List<TirraErrorHistory> list = repo.findByEntryDateBetween( req.getEffectiveStartDate(), req.getEffectiveEndDate());
			
			if(list.size()>0) {
				for(TirraErrorHistory data : list) {
					GetTirraEorrorHistoryRes res = new GetTirraEorrorHistoryRes();
					mapper.map(data, res);
					
					if(data!=null && data.getResponseStatus().equalsIgnoreCase("TIRA001"))
						res.setResponseStatusDesc("Successfull");
					else
						res.setResponseStatusDesc("Failed");
					
					response.add(res);
				}
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		return response;
	
	}

}
