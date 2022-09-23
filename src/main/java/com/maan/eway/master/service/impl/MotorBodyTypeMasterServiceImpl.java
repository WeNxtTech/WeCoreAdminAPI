package com.maan.eway.master.service.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.maan.eway.master.service.MotorBodyTypeMasterService;
import com.maan.eway.repository.MotorBodyTypeMasterRepository;

@Service
@Transactional
public class MotorBodyTypeMasterServiceImpl implements MotorBodyTypeMasterService {

	@Autowired
	private MotorBodyTypeMasterRepository repo;

	@PersistenceContext
	private EntityManager em;

	Gson json = new Gson();

	private Logger log = LogManager.getLogger(MotorBodyTypeMasterServiceImpl.class);

	
	
}
