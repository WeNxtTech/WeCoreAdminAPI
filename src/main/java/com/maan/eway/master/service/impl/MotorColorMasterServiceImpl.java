package com.maan.eway.master.service.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.maan.eway.master.service.MotorColorMasterService;
import com.maan.eway.repository.MotorColorMasterRepository;

@Service
@Transactional
public class MotorColorMasterServiceImpl implements  MotorColorMasterService {

	@Autowired
	private  MotorColorMasterRepository repo;

	@PersistenceContext
	private EntityManager em;

	Gson json = new Gson();

	private Logger log = LogManager.getLogger(MotorColorMasterServiceImpl.class);


}

	


