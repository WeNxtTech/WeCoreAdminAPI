/**
 * @author : Ashok Kumar S 
 * @since  : 11-02-2025
 */
package com.maan.eway.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maan.eway.bean.FlowFieldDetails;
import com.maan.eway.common.res.CommonRes;
import com.maan.eway.error.Error;
import com.maan.eway.req.FlowFieldDetailsGetAllReq;
import com.maan.eway.req.FlowFieldDetailsGetReq;
import com.maan.eway.req.FlowFieldDetailsSaveUpReq;
import com.maan.eway.res.FlowFieldDetailsRes;
import com.maan.eway.service.FlowFieldDetailsService;
import com.maan.eway.service.impl.FlowFieldDetailsServiceImpl;

@RestController
@RequestMapping("/admin")
public class FlowFieldDetailsController {
	
	private FlowFieldDetailsService flowFieldService;

	@Autowired
	public FlowFieldDetailsController(FlowFieldDetailsServiceImpl flowFieldService) {
		this.flowFieldService = flowFieldService;
	}
	
	/**
	 * Retrieves all flow field details based on the provided request parameters.
	 * <p>
	 * This method validates the request before processing. If validation fails,
	 * an error response with {@code HttpStatus.UNPROCESSABLE_ENTITY} is returned.
	 * If no data is found, a {@code HttpStatus.BAD_REQUEST} response is returned.
	 * If the retrieval is successful, the response contains the list of flow field details.
	 * </p>
	 *
	 * @param req The request body containing parameters for retrieving flow field details.
	 * @return {@link ResponseEntity} containing {@link CommonRes} with the retrieved data or an error message.
	 */
	@PostMapping("/getall-flowfield-details")
	public ResponseEntity<CommonRes> getAllFlowFieldDetails (@RequestBody FlowFieldDetailsGetAllReq req){
		CommonRes response = new CommonRes();
		
		List<Error> errors = flowFieldService.validateFlowFieldDetailsGetAllRequest(req);
		if(!errors.isEmpty()) {
			response.setMessage("Validation failed.");
			response.setIsError(true);
			response.setErrorMessage(errors);			
			return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
		}
		
		
		List<FlowFieldDetailsRes> allFlowFieldDetails = flowFieldService.getAllFlowFieldDetails(req);
		if(allFlowFieldDetails == null) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
		
		response.setMessage("Data retrieved successfully.");
		response.setIsError(false);
		response.setCommonResponse(allFlowFieldDetails);
		return new ResponseEntity<>(response, HttpStatus.OK);		
	}
	
	
	/**
	 * Retrieves specific flow field details based on the provided request parameters.
	 * <p>
	 * This method validates the request before processing. If validation fails,
	 * an error response with {@code HttpStatus.UNPROCESSABLE_ENTITY} is returned.
	 * If no data is found, a {@code HttpStatus.BAD_REQUEST} response is returned.
	 * Otherwise, it returns the flow field details with {@code HttpStatus.OK}.
	 * </p>
	 *
	 * @param req The request body containing parameters to fetch the flow field details.
	 * @return {@link ResponseEntity} containing {@link CommonRes} with the retrieved data or an error message.
	 */
	@PostMapping("get-flowfield-details")
	public ResponseEntity<CommonRes> getFlowFieldDetails(@RequestBody FlowFieldDetailsGetReq req){
		CommonRes response =  new CommonRes();
		
		List<Error> errors = flowFieldService.validateFlowFieldDetailsGetRequest(req);
		if(!errors.isEmpty()) {
			response.setMessage("Validation failed.");
			response.setIsError(true);
			response.setErrorMessage(errors);			
			return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
		}
		
		FlowFieldDetailsRes flowFieldDetails = flowFieldService.getFlowFieldDetails(req);
		if(flowFieldDetails == null) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
		
		response.setMessage("Data retrieved successfully.");
		response.setIsError(false);
		response.setCommonResponse(flowFieldDetails);
		return new ResponseEntity<>(response, HttpStatus.OK);		
	}
	
	
	/**
	 * Saves or updates flow field details based on the provided request parameters.
	 * <p>
	 * This method performs validation before processing. If validation fails,
	 * an error response with {@code HttpStatus.UNPROCESSABLE_ENTITY} is returned.
	 * If the save/update operation fails, a {@code HttpStatus.BAD_REQUEST} response is returned.
	 * Otherwise, it returns a success message with {@code HttpStatus.CREATED} for new entries
	 * or {@code HttpStatus.OK} for updates.
	 * </p>
	 *
	 * @param req The request body containing flow field details to be saved or updated.
	 * @return {@link ResponseEntity} containing {@link CommonRes} with the operation status.
	 */
	@PostMapping("save-flowfield-details")
	public ResponseEntity<CommonRes> saveAndUpdateFlowFieldDetails(@RequestBody FlowFieldDetailsSaveUpReq req){
		CommonRes response = new CommonRes();
		
		List<Error> errors = flowFieldService.validateFlowFieldDetailsSaveAndUpdateRequest(req);
		if(!errors.isEmpty()) {
			response.setMessage("Validation failed.");
			response.setIsError(true);
			response.setErrorMessage(errors);			
			return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
		}
		
		FlowFieldDetails flowFieldDetails = flowFieldService.saveAndUpdateFlowFieldDetails(req);
		if(flowFieldDetails == null) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
		
		response.setMessage("Data saved successfully.");
		response.setIsError(false);		
		if(req.getKeyId() == null) {
			response.setCommonResponse(Map.of("Status", "FlowFieldDetails saved successfully."));
			return new ResponseEntity<>(response, HttpStatus.CREATED);	
		}
		else {
			response.setCommonResponse(Map.of("Status", "FlowFieldDetails updated successfully."));
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
	}
	
	
}
