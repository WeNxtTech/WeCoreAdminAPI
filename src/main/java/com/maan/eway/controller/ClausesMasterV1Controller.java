/**
 * @author : Ashok Kumar S 
 * @since  : 19-02-2025
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

import com.maan.eway.bean.ClausesMasterV1;
import com.maan.eway.common.res.CommonRes;
import com.maan.eway.req.ClausesMasterV1GetAllReq;
import com.maan.eway.req.ClausesMasterV1GetReq;
import com.maan.eway.req.ClausesMasterV1SaveUpReq;
import com.maan.eway.req.ClausesMasterV1StatusChangeReq;
import com.maan.eway.res.ClausesMasterV1Res;
import com.maan.eway.service.ClausesMasterV1Service;
import com.maan.eway.error.Error;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/admin")
@Validated
public class ClausesMasterV1Controller {
	
	private ClausesMasterV1Service clausesService;

	@Autowired
	public ClausesMasterV1Controller(ClausesMasterV1Service clausesService) {
		this.clausesService = clausesService;
	}
	
	
	/**
	 * Retrieves all clauses based on the provided request parameters.
	 * If validation fails, an error response with {@code HttpStatus.UNPROCESSABLE_ENTITY} is returned
	 *
	 * @param req the request object containing filter criteria; must be valid.
	 * @return a {@link ResponseEntity} containing a {@link CommonRes} object with clauses data 
	 *         if found, or an error response if no data is retrieved.
	 */
	@PostMapping("/getall-clauses")
	public ResponseEntity<CommonRes> getAllClausesMaster(@Valid @RequestBody ClausesMasterV1GetAllReq req){
		List<ClausesMasterV1Res> allClauses = clausesService.getAllClausesMaster(req);
		
		if(allClauses == null) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
		
		CommonRes response = new CommonRes();
		response.setMessage("Data retrieved successfully.");
		response.setIsError(false);
		response.setCommonResponse(allClauses);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	/**
	 * Retrieves all active clauses based on the provided request parameters.
	 * If validation fails, an error response with {@code HttpStatus.UNPROCESSABLE_ENTITY} is returned
	 *
	 * @param req the request object containing filter criteria; must be valid.
	 * @return a {@link ResponseEntity} containing a {@link CommonRes} object with active clauses data 
	 *         if found, or an error response if no data is retrieved.
	 */
	@PostMapping("/getall-active-clauses")
	public ResponseEntity<CommonRes> getAllActiveClausesMaster(@Valid @RequestBody ClausesMasterV1GetAllReq req){
		List<ClausesMasterV1Res> allClauses = clausesService.getAllActiveClausesMaster(req);
		
		if(allClauses == null) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
		
		CommonRes response = new CommonRes();
		response.setMessage("Data retrieved successfully.");
		response.setIsError(false);
		response.setCommonResponse(allClauses);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	/**
	 * Retrieves a single clause based on the provided request parameters.
	 * If validation fails, an error response with {@code HttpStatus.UNPROCESSABLE_ENTITY} is returned
	 *
	 * @param req the request object containing the filter criteria; must be valid.
	 * @return a {@link ResponseEntity} containing a {@link CommonRes} object with the clause data
	 *         if found, or a bad request response if no data is retrieved.
	 */
	@PostMapping("/get-clause")
	public ResponseEntity<CommonRes> getSingleClausesMaster(@Valid @RequestBody ClausesMasterV1GetReq req){
		ClausesMasterV1Res clausesMaster = clausesService.getSingleClausesMaster(req);
		
		if(clausesMaster == null) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
		
		CommonRes response = new CommonRes();
		response.setMessage("Data retrieved successfully.");
		response.setIsError(false);
		response.setCommonResponse(clausesMaster);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	
	/**
	 * Retrieves a single clause based on the provided request parameters.
	 * If validation fails, an error response with {@code HttpStatus.UNPROCESSABLE_ENTITY} is returned
	 *
	 * @param req the request object containing the filter criteria; must be valid.
	 * @return a {@link ResponseEntity} containing a {@link CommonRes} object with the clause data
	 *         if found, or a bad request response if no data is retrieved.
	 */
	@PostMapping("/get-active-clause")
	public ResponseEntity<CommonRes> getSingleActiveClausesMaster(@Valid @RequestBody ClausesMasterV1GetReq req){
		ClausesMasterV1Res clausesMaster = clausesService.getSingleActiveClausesMaster(req);
		
		if(clausesMaster == null) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
		
		CommonRes response = new CommonRes();
		response.setMessage("Data retrieved successfully.");
		response.setIsError(false);
		response.setCommonResponse(clausesMaster);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
		
	
	/**
	 * Saves or updates a Clauses Master record based on the provided request data.
	 * If validation fails, an error response with {@code HttpStatus.UNPROCESSABLE_ENTITY} is returned
	 *
	 * @param req the request object containing the clause details; must be valid.
	 * @return a {@link ResponseEntity} containing a {@link CommonRes} object with a success message
	 *         and status if the operation is successful, or a bad request response if the operation fails.
	 */
	@PostMapping("/save-clause")
	public ResponseEntity<CommonRes> saveAndUpdateClausesMaster(@Valid @RequestBody ClausesMasterV1SaveUpReq req){
		ClausesMasterV1 clausesMaster = clausesService.saveAndUpdateClausesMasterDetails(req);
		
		if(clausesMaster == null) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
		
		CommonRes response = new CommonRes();
		response.setMessage("Data saved successfully.");
		response.setIsError(false);
		
		if(req.getClausesId() == null) {
			response.setCommonResponse(Map.of("status", "Clauses Master was successfully saved."));
		}
		else {
			response.setCommonResponse(Map.of("status", "Clauses Master was successfully updated."));
		}
		
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	/**
	 * Activates or deactivates a Clauses Master record based on the provided request data.
	 *
	 * @param req the request object containing the clause ID and new status; must be valid.
	 * @return a {@link ResponseEntity} containing a {@link CommonRes} object with a success message
	 *         and status if the operation is successful, or an appropriate error response if the operation fails.
	 */
	@PostMapping("/enable-disable-clause")
	public ResponseEntity<CommonRes> activateOrInactivateClause(@Valid @RequestBody ClausesMasterV1StatusChangeReq req){
		Boolean result = clausesService.activateOrInactivateClausesMaster(req);
		
		if(result == null) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}

		CommonRes response = new CommonRes();
		if(result == Boolean.FALSE) {
			response.setMessage("Failed");
			response.setIsError(false);
			response.setErrorMessage(List.of(new Error ("1","status","Can not update the same status already present.")));
			return new ResponseEntity<>(response, HttpStatus.NOT_ACCEPTABLE);
		}
		
		response.setMessage("Data saved successfully.");
		response.setIsError(false);		
		response.setCommonResponse(Map.of("status", "Status updated successfully"));		
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

}
