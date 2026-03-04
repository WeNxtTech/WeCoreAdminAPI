/**
 * @author : Ashok Kumar S 
 * @since  : 12-02-2025
 */
package com.maan.eway.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.maan.eway.bean.FieldQueryTablequery;
import com.maan.eway.common.res.CommonRes;
import com.maan.eway.error.Error;
import com.maan.eway.req.FieldQueryTableQuerySaveUpReq;
import com.maan.eway.res.FieldQueryTableQueryRes;
import com.maan.eway.service.FieldQueryTableQueryService;

@RestController
@RequestMapping("/admin")
public class FieldQueryTableQueryController {
	
	private FieldQueryTableQueryService fieldQueryService;
	
	@Autowired
	public FieldQueryTableQueryController(FieldQueryTableQueryService fieldQueryService) {
		this.fieldQueryService = fieldQueryService;
	}
	
	
	/**
	 * Retrieves all field query table query details.
	 * <p>
	 * This endpoint fetches a list of all field query table queries from the service layer.
	 * If no data is found, it returns a {@code BAD_REQUEST} response.
	 * Otherwise, it returns a success response with the retrieved data.
	 * </p>
	 *
	 * @return A {@link ResponseEntity} containing a {@link CommonRes} object with the retrieved data
	 *         or a {@code BAD_REQUEST} response if no data is found.
	 */
	@GetMapping("/getall-fieldquery-tablequery")
	public ResponseEntity<CommonRes> getAllFieldQueryTablequeryDetails(){		
		List<FieldQueryTableQueryRes> allFieldQueryTableQueries = fieldQueryService.getAllFieldQueryTablequeryDetails();
		
		if(allFieldQueryTableQueries == null) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
		
		CommonRes response = new CommonRes();
		response.setMessage("Data retrieved successfully.");
		response.setIsError(false);
		response.setCommonResponse(allFieldQueryTableQueries);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	
	/**
	 * Retrieves the details of a specific field query table query based on the provided query ID.
	 * <p>
	 * This endpoint fetches a single field query table query by its query ID.
	 * If no matching record is found, it returns a {@code BAD_REQUEST} response.
	 * Otherwise, it returns a success response with the retrieved data.
	 * </p>
	 *
	 * @param queryId The unique identifier of the field query table query to retrieve.
	 * @return A {@link ResponseEntity} containing a {@link CommonRes} object with the retrieved data
	 *         or a {@code BAD_REQUEST} response if no data is found.
	 */
	@GetMapping("/get-fieldquery-tablequery")
	public ResponseEntity<CommonRes> getFieldQueryTablequeryDetails(@RequestParam("queryId") BigDecimal queryId){
		FieldQueryTableQueryRes fieldQueryTableQuery = fieldQueryService.getFieldQueryTablequeryDetails(queryId);
		
		if(fieldQueryTableQuery == null) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
		
		CommonRes response = new CommonRes();
		response.setMessage("Data retrieved successfully.");
		response.setIsError(false);
		response.setCommonResponse(fieldQueryTableQuery);
		return new ResponseEntity<>(response, HttpStatus.OK);	
	}

	
	/**
	 * Saves or updates the details of a field query table query.
	 * <p>
	 * This endpoint validates the request payload and performs either an insert or an update operation
	 * based on the presence of the {@code queryId} in the request.
	 * If validation fails, it returns an {@code UNPROCESSABLE_ENTITY} response with error details.
	 * If the operation succeeds, it returns a success response.
	 * </p>
	 *
	 * @param req The request payload containing the details of the field query table query to save or update.
	 * @return A {@link ResponseEntity} containing a {@link CommonRes} object with the operation status.
	 *         Returns {@code UNPROCESSABLE_ENTITY} if validation fails,
	 *         {@code BAD_REQUEST} if the save/update operation fails,
	 *         {@code CREATED} if a new entry is saved, or
	 *         {@code OK} if an existing entry is updated.
	 */
	@PostMapping("/save-fieldquery-tablequery")
	public ResponseEntity<CommonRes> saveOrUpdateFieldQueryTablequeryDetails(
			@RequestBody FieldQueryTableQuerySaveUpReq req){
		
		CommonRes response = new CommonRes();		
		List<Error> errors = fieldQueryService.validateParametersOfFieldQueryTablequerySaveRequest(req);
		
		if(!errors.isEmpty()) {
			response.setMessage("Validation failed");
			response.setIsError(true);
			response.setErrorMessage(errors);
			return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
		}
		
		FieldQueryTablequery fieldQueryTableQuery = fieldQueryService.saveUpdateFieldQueryTablequeryDetails(req);
		if(fieldQueryTableQuery == null) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
		
		response.setMessage("Data retrieved successfully");
		response.setIsError(false);
		if(req.getQueryId() == null) {
			response.setCommonResponse(Map.of("Status", "FieldQueryTableQuery details saved successfully"));
			return new ResponseEntity<>(response, HttpStatus.CREATED);
		}
		else {
			response.setCommonResponse(Map.of("Status", "FieldQueryTableQuery details updated successfully"));
			return new ResponseEntity<>(response, HttpStatus.OK);
		}	
			
	}
	
	
	
}
