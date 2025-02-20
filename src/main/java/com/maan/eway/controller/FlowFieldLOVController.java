/**
 * @author : Ashok Kumar S 
 * @since  : 13-02-2025
 */
package com.maan.eway.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maan.eway.common.res.CommonRes;
import com.maan.eway.error.Error;
import com.maan.eway.req.FlowFieldLOVGetReq;
import com.maan.eway.res.ListOfValuesRes;
import com.maan.eway.service.FlowFieldLOVService;

@RestController
@RequestMapping("/dropdown")
public class FlowFieldLOVController {
	
	FlowFieldLOVService flowLOVService;
	
	@Autowired
	public FlowFieldLOVController(FlowFieldLOVService flowLOVService) {
		this.flowLOVService = flowLOVService;
	}

	/**
	 * Retrieves a dropdown list of available parent JSON keys.
	 * <p>
	 * This endpoint validates the request parameters and fetches a list of parent JSON keys 
	 * based on the given {@link FlowFieldLOVGetReq}. If validation fails, it returns an 
	 * {@code UNPROCESSABLE_ENTITY} response with the validation errors. If no data is found, 
	 * a {@code BAD_REQUEST} response is returned. Otherwise, a success response with the 
	 * list of values is returned.
	 * </p>
	 *
	 * @param req The request object containing necessary parameters for fetching parent JSON keys.
	 * @return ResponseEntity containing {@link CommonRes} with the list of parent JSON keys or an error response.
	 */
	@PostMapping("/flowfield-header-keys")
	public ResponseEntity<CommonRes> dropdownToChooseParentJsonKey(@RequestBody FlowFieldLOVGetReq req){
		CommonRes response = new CommonRes();
	    
		// Validate request parameters
		List<Error> errors = flowLOVService.validateParametersOfFlowFieldLOVGetRequest(req);
		if(!errors.isEmpty()) {
			response.setMessage("Validation failed.");
			response.setIsError(true);
			response.setErrorMessage(errors);
			return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
		}
		
	    // Fetch parent JSON keys
		List<ListOfValuesRes> parentJsonKeys = flowLOVService.dropdownToChooseParentJsonKey(req);
		
		if(parentJsonKeys == null) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
		
		response.setMessage("Data retrieved successfully");
		response.setIsError(false);
		response.setCommonResponse(parentJsonKeys);
		return new ResponseEntity<>(response, HttpStatus.OK);		
	}
	
	/**
	 * Retrieves a list of available flow field data types for dropdown selection.
	 *
	 * @return a {@link ResponseEntity} containing a {@link CommonRes} object with the list 
	 *         of data types if successful, or {@code BAD_REQUEST} if exception occured.
	 */
	@GetMapping("/flowfield-datatypes")
	public ResponseEntity<CommonRes> dropdownToChooseDataTypes(){
		List<ListOfValuesRes> dataTypes = flowLOVService.dropdownToChooseDatatypes();
		
		if(dataTypes == null) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
		
		CommonRes response = new CommonRes();
		response.setMessage("Data retrieved successfully");
		response.setIsError(false);
		response.setCommonResponse(dataTypes);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	

}
