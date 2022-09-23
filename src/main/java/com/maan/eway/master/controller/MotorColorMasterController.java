package com.maan.eway.master.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maan.eway.master.service.MotorColorMasterService;
import com.maan.eway.service.PrintReqService;

import io.swagger.annotations.Api;

@RestController
@Api(tags = "MASTER :Motor Color Master ", description = "API's")
@RequestMapping("/master")
public class MotorColorMasterController {

	@Autowired
	private MotorColorMasterService service;
	@Autowired
	private PrintReqService reqPrinter;

		  
	 }
