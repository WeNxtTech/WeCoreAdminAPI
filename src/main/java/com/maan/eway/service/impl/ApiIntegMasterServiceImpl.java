/**
 * @author : Ashok Kumar S 
 * @since  : 10-02-2025
 */
package com.maan.eway.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.maan.eway.bean.ApiIntegMaster;
import com.maan.eway.bean.ApiIntegMasterId;
import com.maan.eway.bean.FlowFieldDetails;
import com.maan.eway.repository.ApiIntegMasterRepository;
import com.maan.eway.repository.FlowFieldDetailsRepository;
import com.maan.eway.req.ApiIntegMasterGetAllReq;
import com.maan.eway.req.ApiIntegMasterGetReq;
import com.maan.eway.req.ApiIntegMasterSaveUpReq;
import com.maan.eway.res.ApiIntegMasterRes;
import com.maan.eway.service.ApiIntegMasterService;

@Service
public class ApiIntegMasterServiceImpl implements ApiIntegMasterService{
	private static final Logger log = LogManager.getLogger(ApiIntegMasterServiceImpl.class);
		
	private ApiIntegMasterRepository apiIntegRepo;
	private FlowFieldDetailsRepository flowFieldRepo;
	private ModelMapper mapper;
	
	@Autowired
	public ApiIntegMasterServiceImpl(ApiIntegMasterRepository apiIntegRepo, FlowFieldDetailsRepository flowFieldRepo,
			ModelMapper mapper) {
		this.apiIntegRepo = apiIntegRepo;
		this.flowFieldRepo = flowFieldRepo;
		this.mapper = mapper;
	}
		
	
	/**
	 * Retrieves all API integration master details based on the provided request parameters.
	 * <p>
	 * This method fetches API integration master records using {@code companyId} and {@code productId}.
	 * If no records are found, a {@link NoSuchElementException} is thrown.
	 * Additionally, it calculates the total number of flow fields associated with each API type.
	 * </p>
	 *
	 * @param req The request object containing company and product identifiers.
	 * @return A list of {@link ApiIntegMasterRes} containing API integration master details.
	 */
	@Override
	public List<ApiIntegMasterRes> getAllApiIntegMasterDetails (ApiIntegMasterGetAllReq req) {
		try {
			List<ApiIntegMaster> allApiIntegMasters = apiIntegRepo.findAllByCompanyIdAndProductId(
					req.getCompanyId(), req.getProductId());
						
			List<ApiIntegMasterRes> apiIntegMasterList = allApiIntegMasters.stream()
					.map(apiInteg -> mapper.map(apiInteg, ApiIntegMasterRes.class))
					.collect(Collectors.toList());
			
			apiIntegMasterList.forEach(
					apiInteg -> apiInteg.setFlowCount(
							findingTotalFlowFieldDetailsCountAganistApiType(apiInteg)));
			
			return apiIntegMasterList;
		} catch (Exception e) {
			log.error("Exception: {}", e.getMessage(), e);
			return null;
		}		
	}
		
	
	/**
	 * Retrieves the details of a specific API integration master based on the provided request parameters.
	 * <p>
	 * This method fetches an API integration master record using {@code companyId}, {@code productId}, 
	 * and {@code apiType}. If no record is found, a {@link NoSuchElementException} is thrown.
	 * Additionally, it calculates the total number of flow fields associated with the API type.
	 * </p>
	 *
	 * @param req The request object containing company ID, product ID, and API type.
	 * @return An {@link ApiIntegMasterRes} containing the API integration master details.
	 *         Returns {@code null} if an exception occurs.
	 */
	@Override
	public ApiIntegMasterRes getApiIntegMasterDetails(ApiIntegMasterGetReq req) {
		try {
			ApiIntegMasterId apiIntegMasterId = new ApiIntegMasterId(
					req.getCompanyId(), req.getProductId(), req.getApiType());
			
			Optional<ApiIntegMaster> optApiIntegMaster = apiIntegRepo.findById(apiIntegMasterId);
			
			if(optApiIntegMaster.isEmpty()) {
				throw new NoSuchElementException("ApiIntegMaster is looking for is not found");
			}
			
			ApiIntegMasterRes apiIntegMaster = mapper.map(optApiIntegMaster.get(), ApiIntegMasterRes.class);
			apiIntegMaster.setFlowCount(findingTotalFlowFieldDetailsCountAganistApiType(apiIntegMaster));
			
			return apiIntegMaster;
		} catch (Exception e) {
			log.error("Exception: {}", e.getMessage(), e);
			return null;
		}				
	}
	
	
	/**
	 * Saves or updates an API Integration Master record.
	 * <p>
	 * This method maps the request object to an entity and persists it in the database.
	 * If an exception occurs, an error is logged, and {@code null} is returned.
	 * </p>
	 *
	 * @param req The request object containing API integration master details.
	 * @return The saved or updated {@link ApiIntegMaster} entity.
	 *         Returns {@code null} if an exception occurs.
	 */
	@Override
	public ApiIntegMaster saveUpdateApiIntegMasterDetails (ApiIntegMasterSaveUpReq req) {
		try {
			ApiIntegMaster apiIntegMaster = mapper.map(req, ApiIntegMaster.class);
			apiIntegMaster.setApiType(req.getApiType().trim().toUpperCase());
			
			return apiIntegRepo.saveAndFlush(apiIntegMaster);			
		} catch (Exception e) {
			log.error("Exception: {}", e.getMessage(), e);
			return null;
		}	
	}
	

	/**
	 * Retrieves the total count of FlowFieldDetails associated with a given API type.
	 * <p>
	 * This method fetches all FlowFieldDetails records that match the company ID, 
	 * product ID, integration type (API type), and status, then returns the count.
	 * If an exception occurs, an error is logged, and {@code -1} is returned.
	 * </p>
	 *
	 * @param res The {@link ApiIntegMasterRes} object containing company ID, 
	 *            product ID, API type, and status.
	 * @return The count of matching FlowFieldDetails records.
	 *         Returns {@code -1} if an exception occurs.
	 */
	private int findingTotalFlowFieldDetailsCountAganistApiType(ApiIntegMasterRes res) {
		try {
			List<FlowFieldDetails> allFlowFieldsAganistApiType = flowFieldRepo.
					findAllByCompanyIdAndProductIdAndIntegTypeAndStatus(
						new BigDecimal(res.getCompanyId()), new BigDecimal(res.getProductId()), 
						res.getApiType(), res.getStatus());
			
			return allFlowFieldsAganistApiType.size();
		} catch (Exception e) {
			log.error("Exception: {}", e.getMessage(), e);
			return -1;
		}	
	}
	
}
