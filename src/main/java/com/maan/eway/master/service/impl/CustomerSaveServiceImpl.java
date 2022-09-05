package com.maan.eway.master.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.maan.eway.bean.CoverMaster;
import com.maan.eway.bean.CustomerDetails;
import com.maan.eway.bean.SectionMaster;
import com.maan.eway.master.req.CoverGetReq;
import com.maan.eway.master.req.CustomerSaveReq;
import com.maan.eway.master.req.SectionGetReq;
import com.maan.eway.master.res.CoverGetRes;
import com.maan.eway.master.res.SectionGetRes;
import com.maan.eway.master.service.CustomerSaveService;
import com.maan.eway.repository.CoverMasterRepository;
import com.maan.eway.repository.CustomerDetailsRepository;
import com.maan.eway.repository.SectionMasterRepository;
import com.maan.eway.res.SuccessRes;

@Service
@Transactional
public class CustomerSaveServiceImpl  implements CustomerSaveService{

	private Logger log=LogManager.getLogger(CustomerSaveServiceImpl.class);

	@Autowired
	private CoverMasterRepository coverrepo;

	@Autowired
	private SectionMasterRepository sectionrepo;
	
	@Autowired
	private CustomerDetailsRepository customerrepo;
	
	@Override
	public List<SectionGetRes> getsection(SectionGetReq req) {
		List<SectionGetRes> resList = new ArrayList<SectionGetRes>();
		ModelMapper mapper = new ModelMapper();
		try {
			List<SectionMaster> sectionmaster = sectionrepo.findByProductIdAndCompanyId(req.getProductId(),req.getCompanyId());
			for(SectionMaster data : sectionmaster){
				SectionGetRes res = new SectionGetRes();
				mapper.map(data, SectionGetRes.class);
				resList.add(res);
			}
		}
		catch(Exception e) {
			e.printStackTrace();
			log.info("Log Details"+e.getMessage());
			return null;
		}
		return resList;
	}
	@Override
	public List<CoverGetRes> getcover(CoverGetReq req) {
		List<CoverGetRes> resList = new ArrayList<CoverGetRes>();
		ModelMapper mapper = new ModelMapper();
		try {
			List<CoverMaster> covermaster = coverrepo.findByProductIdAndCompanyId(req.getProductId(),req.getCompanyId());
			for(CoverMaster data : covermaster){
				CoverGetRes res = new CoverGetRes();
				mapper.map(data, CoverGetRes.class);
				resList.add(res);
			}
		}
		catch(Exception e) {
			e.printStackTrace();
			log.info("Log Details"+e.getMessage());
			return null;
		}
		return resList;
	}
	
	@Override
	public SuccessRes savecustomer(CustomerSaveReq req) {
		SuccessRes res = new SuccessRes();
		DozerBeanMapper mapper = new DozerBeanMapper();
		try {
			Long newId =0L;
			Date entryDate = null;
			CustomerDetails data = new CustomerDetails();
			CustomerDetails id = customerrepo.findByCustomerId(req.getCustomerId());
			if(id.getCustomerId().equalsIgnoreCase(req.getCustomerId())){
				data = mapper.map(req, CustomerDetails.class);
				data.setClientName(id.getClientName());
				data.setGenderId(id.getGenderId());
				data.setGenderDesc(id.getGenderDesc());
				data.setBranchCode(id.getBranch());
				res.setResponse("Updated Successful");	
				res.setSuccessId(id.getCustomerId());
				}
			else {
				data = mapper.map(req, CustomerDetails.class);
				newId = customerrepo.count()+1;
				data.setEntryDate(new Date());
				data.setCustomerId(newId.toString());
				}
			customerrepo.save(data);
			res.setResponse("Inserted Successful");	
			res.setSuccessId(newId.toString());
		}
		catch(Exception e) {
			e.printStackTrace();
			log.info("Log Details"+e.getMessage());
			return null;
		}
		return res;
	}

}
