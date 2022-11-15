package com.maan.eway.common.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.maan.eway.bean.RawReferenceTableDetails;
import com.maan.eway.common.req.RawReferenceTableReq;
import com.maan.eway.common.res.RawReferenceTableRes;
import com.maan.eway.common.service.RawReferenceTableService;
import com.maan.eway.repository.RawReferenceTableDetailsRepository;

@Service
@Transactional
public class RawReferenceTableServiceImpl implements RawReferenceTableService {

	@Autowired
	private RawReferenceTableDetailsRepository rawRepo;
	Gson json = new Gson();

	private Logger log = LogManager.getLogger(RawReferenceTableServiceImpl.class);

	@Override
	public List<RawReferenceTableRes> tabledetails() {
		List<RawReferenceTableRes> resList = new ArrayList<RawReferenceTableRes>();
		try {
			List<RawReferenceTableDetails> getList = rawRepo
					.findByTableNameAndStatusOrderByTableIdAsc("RAW_REFERENCE_TABLE", "Y");

			for (RawReferenceTableDetails data : getList) {
				RawReferenceTableRes res = new RawReferenceTableRes();
				res.setCode(data.getTableId().toString());
				res.setCodeDesc(data.getColumnName());
				res.setStatus(data.getStatus());
				resList.add(res);
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Exception is ---> " + e.getMessage());
			return null;
		}

		return resList;
	}

	@Override
	public List<RawReferenceTableRes> columndetails(RawReferenceTableReq req) {
		List<RawReferenceTableRes> resList = new ArrayList<RawReferenceTableRes>();
		try {
			List<RawReferenceTableDetails> getList = rawRepo
					.findByTableIdAndStatusOrderByColumnIdAsc(Integer.valueOf(req.getTableId()), "Y");
			for (RawReferenceTableDetails data : getList) {
				if (!data.getTableName().equalsIgnoreCase("RAW_REFERENCE_TABLE")) {
					RawReferenceTableRes res = new RawReferenceTableRes();
					res.setCode(data.getColumnId().toString());
					res.setCodeDesc(data.getColumnName());
					res.setStatus(data.getStatus());
					resList.add(res);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Exception is ---> " + e.getMessage());
			return null;
		}
		return resList;
	}

}
