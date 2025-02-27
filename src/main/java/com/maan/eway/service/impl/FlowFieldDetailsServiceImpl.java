/**
 * @author : Ashok Kumar S 
 * @since  : 11-02-2025
 */
package com.maan.eway.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.maan.eway.bean.FlowFieldDetails;
import com.maan.eway.bean.FlowFieldDetailsId;
import com.maan.eway.error.Error;
import com.maan.eway.repository.FlowFieldDetailsRepository;
import com.maan.eway.req.FlowFieldDetailsGetAllReq;
import com.maan.eway.req.FlowFieldDetailsGetReq;
import com.maan.eway.req.FlowFieldDetailsSaveUpReq;
import com.maan.eway.res.FlowFieldDetailsRes;
import com.maan.eway.service.FlowFieldDetailsService;

@Service
public class FlowFieldDetailsServiceImpl implements FlowFieldDetailsService {
	private static final Logger log = LogManager.getLogger(FlowFieldDetailsServiceImpl.class);

	private final BigDecimal KEY_ID_FOR_ROOT = new BigDecimal(701);
	private final BigDecimal KEY_ID_NONROOT_START = new BigDecimal(702);
	
	private final String ROOT_JSON_KEY = "Root";	
	private final String HEADER_KEYID_ROOT = "99999";
	
	private FlowFieldDetailsRepository flowFieldRepo;	
	private ModelMapper mapper;
	
	@Autowired
	public FlowFieldDetailsServiceImpl(FlowFieldDetailsRepository flowFieldRepo, ModelMapper mapper) {
		this.flowFieldRepo = flowFieldRepo;
		this.mapper = mapper;
	}

	
	@Override
	public List<Error> validateFlowFieldDetailsGetAllRequest(FlowFieldDetailsGetAllReq req) {
		List<Error> errors = new ArrayList<>();
		
		if(req.getCompanyId() == null) {
			errors.add(new Error("1", "companyId", "Company ID is required, It should not be null"));
		}
		if(req.getProductId() == null) {
			errors.add(new Error("2", "productId", "Product ID is required, It should not be null"));
		}
		if(StringUtils.isBlank(req.getIntegType())) {
			errors.add(new Error("3", "integType", "Integ Type is required, It should not be blank"));
		}
		
		return errors;
	}
	
		
	@Override
	public List<Error> validateFlowFieldDetailsGetRequest(FlowFieldDetailsGetReq req) {
		List<Error> errors = new ArrayList<>();
		
		if(req.getCompanyId() == null) {
			errors.add(new Error("1", "companyId", "Company ID is required, It should not be null"));
		}
		if(req.getProductId() == null) {
			errors.add(new Error("2", "productId", "Product ID is required, It should not be null"));
		}
		if(StringUtils.isBlank(req.getIntegType())) {
			errors.add(new Error("3", "integType", "Integ Type is required, It should not be blank"));
		}
		if(req.getKeyId() == null) {
			errors.add(new Error("4", "keyId", "Key ID is required, It should not be null"));
		}
		
		return errors;
	}
	
	
	/**
	 * Validates the request object for saving or updating FlowFieldDetails.
	 * <p>
	 * This method checks for missing or invalid fields and returns a list of errors.
	 * - Ensures required fields are not null or blank.
	 * - Validates conditional dependencies (e.g., defaultValue when defaultYN is 'Y').
	 * - Ensures query-related fields are provided when defaultYN is 'N'.
	 * </p>
	 *
	 * @param req the request object containing FlowFieldDetails data
	 * @return a list of validation errors; returns an empty list if no validation issues are found
	 */
	@Override
	public List<Error> validateFlowFieldDetailsSaveAndUpdateRequest(FlowFieldDetailsSaveUpReq req) {
		List<Error> errors = new ArrayList<>();
		
		if(req.getCompanyId() == null) {
			errors.add(new Error("1", "companyId", "Company ID is required, It should not be null"));
		}
		if(req.getProductId() == null) {
			errors.add(new Error("2", "productId", "Product ID is required, It should not be null"));
		}
		if(StringUtils.isBlank(req.getIntegType())) {
			errors.add(new Error("3", "integType", "Integ Type is required, It should not be blank"));
		}
		if(StringUtils.isBlank(req.getJsonKey())) {
			errors.add(new Error("4", "jsonKey", "Json Key is required, It should not be blank"));
		}
		
		if(StringUtils.isBlank(req.getIsHeader())) {
			errors.add(new Error("5", "isHeader", "IsHeader is required, It should not be blank"));
		}		
		
		if(StringUtils.isBlank(req.getHeaderKeyid())) {
			errors.add(new Error("6", "headerKeyId", "Header KeyId is required, It should not be blank"));
		}
		
		if(StringUtils.isBlank(req.getIsarray())) {
			errors.add(new Error("7", "isArray", "IsArray is required, It should not be blank"));
		}
		if(StringUtils.isBlank(req.getStatus())) {
			errors.add(new Error("8", "status", "Status is required, It should not be blank"));
		}
		
		if(StringUtils.isBlank(req.getDefaultYn())) {
			errors.add(new Error("9", "defaultYn", "Default YN is required, It should not be blank"));
		}
		
		if("Yes".equals(req.getIsarray()) && "No".equals(req.getIsHeader())) {
			errors.add(new Error("10", "headerKeyId", "This combination is not allowed. If Array is selected Yes and Header must be Yes."));
		}
		return errors;
	}
	
	
	
	/**
	 * Retrieves all Flow Field Details based on the provided request parameters.
	 *
	 * @param req the request object containing company ID, product ID, and integration type
	 * @return a list of FlowFieldDetailsRes objects if found, otherwise returns null
	 */
	@Override
	public List<FlowFieldDetailsRes> getAllFlowFieldDetails(FlowFieldDetailsGetAllReq req) {
		try {			
			List<FlowFieldDetails> allFlowFieldDetails = flowFieldRepo.findAllByCompanyIdAndProductIdAndIntegTypeOrderByKeyId(
					req.getCompanyId(), req.getProductId(), req.getIntegType());
				
			return allFlowFieldDetails.stream()
					.map(flowField -> mapper.map(flowField, FlowFieldDetailsRes.class))
					.toList();
		} catch (Exception e) {
			log.error("Exception : {}", e.getMessage(), e);
			return null;
		}
	}
	
	
	/**
	 * Retrieves Flow Field Details based on the provided request parameters.
	 *
	 * @param req the request object containing company ID, product ID, integration type, and key ID
	 * @return a FlowFieldDetailsRes object if found, otherwise returns null
	 * @throws NoSuchElementException if the requested flow field details are not found
	 */
	@Override
	public FlowFieldDetailsRes getFlowFieldDetails(FlowFieldDetailsGetReq req) {
		try {
			FlowFieldDetailsId flowFieldDetailsId = new FlowFieldDetailsId(
					req.getCompanyId(), req.getProductId(), req.getIntegType(), req.getKeyId());
			Optional<FlowFieldDetails> optFlowFieldDetails = flowFieldRepo.findById(flowFieldDetailsId);
			
			if(optFlowFieldDetails.isEmpty()) {
				throw new NoSuchElementException("Flow Field Details looking for is not found");
			}
			
			return mapper.map(optFlowFieldDetails.get(), FlowFieldDetailsRes.class);
		} catch (Exception e) {
			log.error("Exception : {}", e.getMessage(), e);
			return null;
		}
	}
	
	
	/**
	 * Saves or updates Flow Field Details based on the provided request parameters.
	 * 
	 * @param req the request object containing details for saving or updating Flow Field Details
	 * @return the saved or updated FlowFieldDetails object
	 * @throws IllegalArgumentException if setting properties for Flow Field Details fails
	 */
	@Override
	public FlowFieldDetails saveAndUpdateFlowFieldDetails(FlowFieldDetailsSaveUpReq req) {
		try {

			FlowFieldDetails flowFieldDetails = settingPropertiesForFlowFieldDetails(req);
			if(flowFieldDetails == null) {
				throw new IllegalArgumentException("Failed to set properties for Flow Field Details.");
			}
			
			return flowFieldRepo.saveAndFlush(flowFieldDetails);		
		} catch (Exception e) {
			log.error("Exception : {}", e.getMessage(), e);
			return null;
		}
	}
	
		
	/**
	 * Sets properties for a FlowFieldDetails entity based on the provided request parameters.
	 * <p>
	 * - If the JSON key is the root key, it sets specific header-related values.<br>
	 * - If the JSON key is non-root, it determines whether to generate a new key ID or use an existing one.<br>
	 * - It also sets various attributes such as data type, pattern, status, and query-related fields.<br>
	 * </p>
	 *
	 * @param req the request object containing the details for creating or updating a FlowFieldDetails entity
	 * @return a FlowFieldDetails object with the mapped properties, or null if an exception occurs
	 */
	private FlowFieldDetails settingPropertiesForFlowFieldDetails(FlowFieldDetailsSaveUpReq req) {
	    try {

	        FlowFieldDetails flowField = new FlowFieldDetails();

	        // Set mandatory fields
	        flowField.setCompanyId(req.getCompanyId());
	        flowField.setProductId(req.getProductId());
	        
	        flowField.setIntegType(req.getIntegType()); 
	        flowField.setJsonKey(req.getJsonKey());
	        flowField.setIsHeader(req.getIsHeader());
	        
	        if (ROOT_JSON_KEY.equalsIgnoreCase(req.getJsonKey())) {
	            flowField.setKeyId(KEY_ID_FOR_ROOT);
	            flowField.setHeaderKeyid(HEADER_KEYID_ROOT); 
	        }else {
	        	if (req.getKeyId() == null) {

	        		FlowFieldDetails topFlowFieldDetails = flowFieldRepo.findTopByCompanyIdAndProductIdAndIntegTypeOrderByKeyIdDesc(
	        				req.getCompanyId(), req.getProductId(), req.getIntegType());

	        		if (topFlowFieldDetails == null) {  flowField.setKeyId(KEY_ID_NONROOT_START); } 
	        		else { flowField.setKeyId(topFlowFieldDetails.getKeyId().add(BigDecimal.ONE)); }	                
	        	} 
	        	else {
	        		flowField.setKeyId(req.getKeyId());  
	        	}

	        	flowField.setHeaderKeyid(req.getHeaderKeyid()); 
	        }
      
	        flowField.setIsarray(req.getIsarray());
	        
	        String dataType = StringUtils.isBlank(req.getDatatype()) ? null :req.getDatatype();	        	        
	        flowField.setDatatype(dataType);
	        
	        String pattern = StringUtils.isBlank(req.getPattern())? null : req.getPattern();
	        flowField.setPattern(pattern); 
	        flowField.setStatus(req.getStatus()); 
	        
	        flowField.setDefaultYn(req.getDefaultYn());

	        String defaultValue = StringUtils.isBlank(req.getDefaultValue())? null : req.getDefaultValue();        	
            flowField.setDefaultValue(defaultValue); 
            
        	flowField.setQueryId(req.getQueryId());
        	
            String queryCol = StringUtils.isBlank(req.getQueryCol()) ? null : req.getQueryCol();
            flowField.setQueryCol(queryCol);
            
            String queryAlias = StringUtils.isBlank(req.getQueryAlias()) ? null : req.getQueryAlias();            
            flowField.setQueryAlias(queryAlias);
	        
	        return flowField; 

	    } catch (Exception e) {
	        log.error("Exception: {}", e.getMessage(), e);
	        return null;
	    }
	}
	
	
	

}
