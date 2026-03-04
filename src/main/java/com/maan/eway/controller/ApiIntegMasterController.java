/**
 * @author : Ashok Kumar S 
 * @since  : 10-02-2025
 */
package com.maan.eway.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maan.eway.bean.ApiIntegMaster;
import com.maan.eway.common.res.CommonRes;
import com.maan.eway.req.ApiIntegMasterGetAllReq;
import com.maan.eway.req.ApiIntegMasterGetReq;
import com.maan.eway.req.ApiIntegMasterSaveUpReq;
import com.maan.eway.res.ApiIntegMasterRes;
import com.maan.eway.service.ApiIntegMasterService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/admin")
@Validated
public class ApiIntegMasterController {
	private ApiIntegMasterService apiIntegService;
	
	@Autowired	
	public ApiIntegMasterController(ApiIntegMasterService apiIntegService) {
		this.apiIntegService = apiIntegService;
	}


	/**
	 * Retrieves all API integration master details based on the provided request parameters.
	 * <p>
	 * This method validates the request before processing. If validation fails,
	 * an error response with {@code HttpStatus.UNPROCESSABLE_ENTITY} is returned. (@valid)
	 * If no data is found, a {@code HttpStatus.BAD_REQUEST} response is returned.
	 * Otherwise, it returns the retrieved data with {@code HttpStatus.OK}.
	 * </p>
	 *
	 * @param req The request body containing parameters for retrieving API integration master details.
	 * @return {@link ResponseEntity} containing {@link CommonRes} with the retrieved data or error details.
	 */
	@PostMapping("/getall-api-integ-master")
	public ResponseEntity<CommonRes> getAllApiIntegMasterDetails(@Valid @RequestBody ApiIntegMasterGetAllReq req){		
		
		List<ApiIntegMasterRes> allApiIntegMaster = apiIntegService.getAllApiIntegMasterDetails(req);
		if(allApiIntegMaster == null) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
		
		CommonRes response = new CommonRes();
		response.setMessage("Data retrieved successfully.");
		response.setIsError(false);
		response.setCommonResponse(allApiIntegMaster);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	
	/**
	 * Retrieves API integration master details based on the provided request parameters.
	 * <p>
	 * This method first validates the input request. If validation fails, an error response (@valid)
	 * with {@code HttpStatus.UNPROCESSABLE_ENTITY} is returned. If no data is found,
	 * a {@code HttpStatus.BAD_REQUEST} response is returned. Otherwise, the requested
	 * API integration master details are returned with {@code HttpStatus.OK}.
	 * </p>
	 *
	 * @param req The request body containing parameters for retrieving API integration master details.
	 * @return {@link ResponseEntity} containing {@link CommonRes} with the retrieved data or error details.
	 */
	@PostMapping("/get-api-integ-master")
	public ResponseEntity<CommonRes> getApiIntegMasterDetails(@Valid @RequestBody ApiIntegMasterGetReq req){
		
		ApiIntegMasterRes apiIntegMaster = apiIntegService.getApiIntegMasterDetails(req);
		if(apiIntegMaster == null) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
		
		CommonRes response = new CommonRes();		
		response.setMessage("Data retrieved successfully.");
		response.setIsError(false);
		response.setCommonResponse(apiIntegMaster);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	
	/**
	 * Saves or updates API integration master details based on the provided request parameters.
	 * <p>
	 * This method first validates the input request. If validation fails, an error response(@valid)
	 * with {@code HttpStatus.UNPROCESSABLE_ENTITY} is returned. If saving/updating fails,
	 * a {@code HttpStatus.BAD_REQUEST} response is returned. Otherwise, the API integration
	 * master details are successfully saved or updated, and a {@code HttpStatus.OK} response is returned.
	 * </p>
	 *
	 * @param req The request body containing parameters for saving or updating API integration master details.
	 * @return {@link ResponseEntity} containing {@link CommonRes} with the operation result or error details.
	 */
	@PostMapping("/save-api-integ-master")
	public ResponseEntity<CommonRes> saveApiIntegMasterDetails(@Valid @RequestBody ApiIntegMasterSaveUpReq req){
		
		ApiIntegMaster apiIntegMaster = apiIntegService.saveUpdateApiIntegMasterDetails(req);
		if(apiIntegMaster == null) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
		
		CommonRes response = new CommonRes();
		response.setMessage("Data retrieved successfully.");
		response.setIsError(false);
		response.setCommonResponse(Map.of("Status","ApiIntegMaster details saved successfully"));
		return new ResponseEntity<>(response, HttpStatus.OK);	
	}

}
