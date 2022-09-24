package com.maan.eway.master.service.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.maan.eway.error.Error;
import com.maan.eway.master.req.MotorMakeModelGetAllReq;
import com.maan.eway.master.req.MotorMakeModelGetReq;
import com.maan.eway.master.req.MotorMakeModelSaveReq;
import com.maan.eway.master.res.MotorColorGetRes;
import com.maan.eway.master.res.MotorMakeModelGetRes;
import com.maan.eway.master.service.MotorMakeModelMasterService;
import com.maan.eway.repository.MotorMakeModelMasterRepository;
import com.maan.eway.res.SuccessRes;

@Service
@Transactional
public class MotorMakeModelMasterServiceImpl implements MotorMakeModelMasterService {

	@Autowired
	private MotorMakeModelMasterRepository repo;

	@PersistenceContext
	private EntityManager em;

	Gson json = new Gson();

	private Logger log = LogManager.getLogger(MotorMakeModelMasterServiceImpl.class);

	@Override
	public List<Error> validateMotorMakeModel(MotorMakeModelSaveReq req) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SuccessRes saveMotorMakeModel(MotorMakeModelSaveReq req) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MotorMakeModelGetRes getMotorMakeModel(MotorMakeModelGetReq req) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<MotorMakeModelGetRes> getallMotorMakeModel(MotorMakeModelGetAllReq req) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<MotorColorGetRes> getactiveMakeModel(MotorMakeModelGetAllReq req) {
		// TODO Auto-generated method stub
		return null;
	}


}
