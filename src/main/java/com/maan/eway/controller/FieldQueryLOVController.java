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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maan.eway.common.res.CommonRes;
import com.maan.eway.res.ListOfValuesRes;
import com.maan.eway.service.impl.FieldQueryLOVService;

@RestController
@RequestMapping("/dropdown")
public class FieldQueryLOVController {
	
	private FieldQueryLOVService fieldQueryLOVService;
	
	@Autowired	
	public FieldQueryLOVController(FieldQueryLOVService fieldQueryLOVService) {
		this.fieldQueryLOVService = fieldQueryLOVService;
	}


	/**
	 * Retrieves a dropdown list of available Query IDs.
	 * <p>
	 * This endpoint fetches a list of Query IDs that can be selected from a dropdown.
	 * If the service returns null, a {@code BAD_REQUEST} response is returned.
	 * Otherwise, a success response with the list of values is returned.
	 * </p>
	 */
	@GetMapping("/fieldquery-queryid")
	public ResponseEntity<CommonRes> dropdownToChooseQueryId(){
		List<ListOfValuesRes> lovList = fieldQueryLOVService.dropdownToChooseQueryId();
		
		if(lovList == null) {
			return new ResponseEntity<> (null, HttpStatus.BAD_REQUEST);
		}
		
		CommonRes response = new CommonRes();
		response.setMessage("Data retrieved successfully.");
		response.setIsError(false);
		response.setCommonResponse(lovList);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

}
