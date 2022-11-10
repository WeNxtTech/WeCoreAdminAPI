package com.maan.eway.common.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.maan.eway.bean.LoginMaster;
import com.maan.eway.common.req.NewQuoteReq;
import com.maan.eway.common.req.QuoteThreadReq;
import com.maan.eway.common.service.QuoteThreadService;
import com.maan.eway.repository.CoverDetailsRepository;
import com.maan.eway.repository.EServiceMotorDetailsRepository;
import com.maan.eway.repository.EserviceCustomerDetailsRepository;
import com.maan.eway.repository.FactorRateRequestDetailsRepository;
import com.maan.eway.repository.HomePositionMasterRepository;
import com.maan.eway.repository.LoginMasterRepository;
import com.maan.eway.repository.MotorDataDetailsRepository;
import com.maan.eway.repository.PersonalInfoRepository;
import com.maan.eway.thread.MyTaskList;

@Service
public class QuoteThreadServiceImpl implements QuoteThreadService {

	private ForkJoinPool forkjoin = new ForkJoinPool(4);
	
	private Logger log = LogManager.getLogger(QuoteThreadServiceImpl.class);
	
	
	
	@PersistenceContext
	private EntityManager em;
	
	@Autowired
	private EserviceCustomerDetailsRepository eserCustRepo ;
	
	@Autowired
	private EServiceMotorDetailsRepository eserMotRepo ;
	
	@Autowired
	private FactorRateRequestDetailsRepository facRateRepo ;
	
	@Autowired
	private PersonalInfoRepository perInfoRepo ;
	
	@Autowired
	private MotorDataDetailsRepository motorRepo ;
	
	@Autowired
	private CoverDetailsRepository coverRepo ;
	
	@Autowired
	private HomePositionMasterRepository homeRepo ;
	
	@Autowired
	private LoginMasterRepository loginRepo ;
	
	@Override
	public NewQuoteRes call_OT_Insert(NewQuoteReq req) {
		NewQuoteRes response = new NewQuoteRes();
		SimpleDateFormat idf = new SimpleDateFormat("yyMMddhhmmssSS");
		try {
			List<Callable<Object>> queue = new ArrayList<Callable<Object>>();
			
			MyTaskList taskList = new MyTaskList(queue);
			
			// Id Generate
			Random rand = new Random();
            int random=rand.nextInt(90)+10; 
            
            String customerId = "C-" + idf.format(new Date()) + random ;
            String quoteNo  = "Q-"+ idf.format(new Date()) + random ;
            LoginMaster loginData  = loginRepo.findByLoginId(req.getCreatedBy());
            
            QuoteThreadReq request = new QuoteThreadReq();
            request.setAgencyCode(loginData.getAgencyCode());
            request.setCreatedBy(loginData.getLoginId());
            request.setCustomerId(customerId);
            request.setQuoteNo(quoteNo);
            request.setRequestReferenceNo(req.getRequestReferenceNo());
            request.setVehicleIdsList(req.getVehicleIdsList());
            
            QuoteThreadCall customerSave = new QuoteThreadCall("CustomerSave" , request , em , eserCustRepo ,eserMotRepo  ,facRateRepo  ,perInfoRepo  , motorRepo ,coverRepo  , homeRepo);
            QuoteThreadCall motorSave = new QuoteThreadCall("MotorSave" , request , em , eserCustRepo ,eserMotRepo  ,facRateRepo  ,perInfoRepo  , motorRepo ,coverRepo  , homeRepo);
            QuoteThreadCall coverSave = new QuoteThreadCall("CoverSave" , request , em , eserCustRepo ,eserMotRepo  ,facRateRepo  ,perInfoRepo  , motorRepo ,coverRepo  , homeRepo);
            QuoteThreadCall quoteSave = new QuoteThreadCall("QuoteSave" , request , em , eserCustRepo ,eserMotRepo  ,facRateRepo  ,perInfoRepo  , motorRepo ,coverRepo  , homeRepo);
            
            queue.add(customerSave);
			queue.add(motorSave);
			queue.add(coverSave);
			queue.add(quoteSave);
			
			ConcurrentLinkedQueue<Future<Object>> invoke = (ConcurrentLinkedQueue<Future<Object>>) forkjoin
					.invoke(taskList);
			
			int success = 0;
			String custRes = "" ;
			String motRes = "" ;
			String covRes = "" ;
			
			QuoteThreadRes quoteRes = new QuoteThreadRes(); 
			
			for (Future<Object> callable : invoke) {

				log.info(callable.getClass() + "," + callable.isDone());

				if (callable.isDone()) {
					Map<String, Object> map = (Map<String, Object>) callable.get();

					for (Entry<String, Object> future : map.entrySet()) {
						if ("CustomerSave".equalsIgnoreCase(future.getKey())) {

							custRes = (String) future.getValue();

						} else if ("MotorSave".equalsIgnoreCase(future.getKey())) {

							motRes = (String) future.getValue();
							
						} else if ("CoverSave".equalsIgnoreCase(future.getKey())) {

							covRes = (String) future.getValue();
							
						} else if ("QuoteSave".equalsIgnoreCase(future.getKey())) {

							quoteRes = (QuoteThreadRes) future.getValue();
						}
					}

					success++;
				}
			}
			
			if (custRes.equalsIgnoreCase("Success") && motRes.equalsIgnoreCase("Success") && covRes.equalsIgnoreCase("Success")  ) {
				response.setQuoteNo(quoteRes.getQuoteNo());
				response.setRequestReferenceNo(quoteRes.getRequestReferenceNo());
				response.setCustomerId(quoteRes.getCustomerId());
				response.setResponse("Saved SuccessFully");
			} else {
				return null;	
			}
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Exception is --> " +  e.getMessage());
			return null;	
		}
		return response ;
	}

	
}
