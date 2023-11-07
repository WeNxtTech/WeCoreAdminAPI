package com.maan.eway.common.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.maan.eway.batch.repository.TirraErrorHistoryRepository;
import com.maan.eway.bean.MotorVehicleInfo;
import com.maan.eway.bean.TiraTrackingDetails;
import com.maan.eway.bean.TirraErrorHistory;
import com.maan.eway.common.req.DeleteTiraSearchedVehicleReq;
import com.maan.eway.common.req.GetAllTirraErrorHistory;
import com.maan.eway.common.req.GetTirraEorrorHistoryReq;
import com.maan.eway.common.req.TiraGetReq;
import com.maan.eway.common.req.TiraPushedDetailsReq;
import com.maan.eway.common.res.GetTirraEorrorHistoryRes;
import com.maan.eway.common.res.TiraErrorHistoryTotalRes;
import com.maan.eway.common.res.TiraPusehDetailsREs2;
import com.maan.eway.common.res.TiraPushedDetailsRes;
import com.maan.eway.common.res.TiraPushedListDetailsRes;
import com.maan.eway.common.service.ReportsService;
import com.maan.eway.repository.MotorVehicleInfoRepository;
import com.maan.eway.repository.TiraTrackingDetailsRepository;
import com.maan.eway.res.SuccessRes2;

@Service
@Transactional
public class ReportsServiceImple implements ReportsService {

	@Autowired
	private TirraErrorHistoryRepository repo;
	
	@Autowired
	private TiraTrackingDetailsRepository tiraRepo;

	@Autowired
	private MotorVehicleInfoRepository motVehRepo;

	
	@Override
	public TiraErrorHistoryTotalRes getTirraEorrorHistory(GetTirraEorrorHistoryReq req) {
		TiraErrorHistoryTotalRes res2 = new TiraErrorHistoryTotalRes();
		List<GetTirraEorrorHistoryRes> response = new ArrayList<GetTirraEorrorHistoryRes>();
		DozerBeanMapper mapper = new DozerBeanMapper();
		
		try {
			// Limit , Offset
			int limit = StringUtils.isBlank(req.getLimit()) ? 0 : Integer.valueOf(req.getLimit());
			int offset = StringUtils.isBlank(req.getOffset()) ? 100 : Integer.valueOf(req.getOffset());
			
			Pageable paging = PageRequest.of(limit, offset, Sort.by("entryDate").descending());
			
			Page<TirraErrorHistory> list = repo.findByReqRegNumberAndEntryDateBetween(paging, req.getRegistrationNumber(), req.getStartDate(), req.getEndDate());
						
			if(list.getContent().size()>0) {
				for(TirraErrorHistory data : list.getContent()) {
					GetTirraEorrorHistoryRes res = new GetTirraEorrorHistoryRes();
					mapper.map(data, res);
					res.setRegistrationNumber(data.getReqRegNumber());
					
					if(data.getResponseStatus().equalsIgnoreCase("TIRA001"))
						res.setResponseStatusDesc("Successfull");
					else
						res.setResponseStatusDesc("Failed");
					
					response.add(res);
				}
			}
			
			res2.setTiraHistory(response);
			res2.setTotalCount(response==null ?0L :Long.valueOf(response.size()) );
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		return res2;
	}

	@Override
	public TiraErrorHistoryTotalRes getAllTirraErrorHistory(GetAllTirraErrorHistory req) {
		TiraErrorHistoryTotalRes res2 = new TiraErrorHistoryTotalRes();
		List<GetTirraEorrorHistoryRes> response = new ArrayList<GetTirraEorrorHistoryRes>();
		DozerBeanMapper mapper = new DozerBeanMapper();
		
		try {
			// Limit , Offset
			int limit = StringUtils.isBlank(req.getLimit()) ? 0 : Integer.valueOf(req.getLimit());
			int offset = StringUtils.isBlank(req.getOffset()) ? 100 : Integer.valueOf(req.getOffset());
			
			Pageable paging = PageRequest.of(limit, offset, Sort.by("entryDate").descending());
			
			Page<TirraErrorHistory> list = repo.findByEntryDateBetween(paging, req.getStartDate(), req.getEndDate());
			if(list.getContent().size()>0) {
				for(TirraErrorHistory data : list.getContent()) {
					GetTirraEorrorHistoryRes res = new GetTirraEorrorHistoryRes();
					mapper.map(data, res);
					res.setRegistrationNumber(data.getReqRegNumber());
					
					if(data!=null && data.getResponseStatus().equalsIgnoreCase("TIRA001"))
						res.setResponseStatusDesc("Successfull");
					else
						res.setResponseStatusDesc("Failed");
					
					response.add(res);
				}
			}

			res2.setTiraHistory(response);
			res2.setTotalCount(response==null ?0L :Long.valueOf(response.size()) );
			
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		return res2;
	
	}

	@Override
	public TiraPushedListDetailsRes getallTiraIntegrationPushedDetails(TiraPushedDetailsReq req) {
		TiraPushedListDetailsRes res2 = new TiraPushedListDetailsRes();
		List<TiraPusehDetailsREs2> reslist = new ArrayList<TiraPusehDetailsREs2>();
		DozerBeanMapper mapper = new DozerBeanMapper();
		
		try {
			// Limit , Offset
			int limit = StringUtils.isBlank(req.getLimit()) ? 0 : Integer.valueOf(req.getLimit());
			int offset = StringUtils.isBlank(req.getOffset()) ? 100 : Integer.valueOf(req.getOffset());
			
			Pageable paging = PageRequest.of(limit, offset, Sort.by("entryDate").descending());
			
			Page<TiraTrackingDetails> list = tiraRepo.findByEntryDateBetween(paging, req.getStartDate(), req.getEndDate());
			if(list.getContent().size()>0) {
				for(TiraTrackingDetails data : list.getContent()) {
					TiraPusehDetailsREs2 res = new TiraPusehDetailsREs2();
					mapper.map(data, res);
					res.setChassisNumber(data.getChassisNo());
					res.setQuoteNo(data.getPolicyNo());
					reslist.add(res);
				}
			}

			res2.setTiraHistory(reslist);
			res2.setTotalCount(reslist==null ?0L :Long.valueOf(reslist.size()) );
			
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		return res2;
	
	}

	@Override
	public List<TiraPushedDetailsRes> getTiraIntegrationPushedDetails(TiraGetReq req) {
		List<TiraPushedDetailsRes> resList = new ArrayList<TiraPushedDetailsRes>();
		DozerBeanMapper mapper = new DozerBeanMapper();
		
		try {
			
			List<TiraTrackingDetails> list = tiraRepo.findByPolicyNo(req.getQuoteNo());
			if(list.size()>0) {
				for(TiraTrackingDetails data : list) {
					TiraPushedDetailsRes res = new TiraPushedDetailsRes();
					mapper.map(data, res);
					res.setChassisNumber(data.getChassisNo());
					
					// Request File 
					if (StringUtils.isNotBlank(res.getRequestFileimgurl()) && new File(res.getRequestFileimgurl()).exists()) {
						res.setRequestFileimgurl(new GetFileFromPath(res.getRequestFileimgurl()).call().getImgUrl());
					} else
						System.out.println("Request File is Not found!!" + res.getRequestFileimgurl());
					
					// Response File 
					if (StringUtils.isNotBlank(res.getResponseFileimgurl()) && new File(res.getResponseFileimgurl()).exists()) {
						res.setRequestFileimgurl(new GetFileFromPath(res.getResponseFileimgurl()).call().getImgUrl());
					} else
						System.out.println("Response File is Not found!!" + res.getResponseFileimgurl());
					
					resList.add(res);
				}
			}
			
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		return resList;
	
	}

	@Override
	public SuccessRes2 getTiraIntegrationPushedDetails(DeleteTiraSearchedVehicleReq req) {
		SuccessRes2 res = new SuccessRes2();
		
		try {
			
			MotorVehicleInfo vehInfo = motVehRepo.findByResRegNumberAndSavedFrom(req.getRegisterNumber(),"API");
			if(vehInfo!=null) {
				motVehRepo.delete(vehInfo);
				res.setSuccessId(req.getRegisterNumber());;
				res.setResponse("Deleted Successfully");
			} else {
				res.setSuccessId(req.getRegisterNumber());
				res.setResponse("No Data Found");
			}
			
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		return res;
	
	}

}
