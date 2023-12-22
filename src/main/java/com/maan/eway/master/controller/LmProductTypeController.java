package com.maan.eway.master.controller;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maan.eway.common.res.CommonRes;
import com.maan.eway.master.req.LmProductTypeChangeStatus;
import com.maan.eway.master.req.LmProductTypeDropDownReq;
import com.maan.eway.master.req.LmProductTypeGetDetailsReq;
import com.maan.eway.master.req.LmProductTypeSaveReq;
import com.maan.eway.master.res.LmProductTypeResponse;
import com.maan.eway.master.service.LmProductTypeService;
import com.maan.eway.repository.LmProductTypeRepository;
import com.maan.eway.res.DropDownRes;
import com.maan.eway.res.DropdownCommonRes;
import com.maan.eway.res.SuccessRes;

@RestController
@RequestMapping(value="/lmproducttypeapi")
public class LmProductTypeController {
	
	@Autowired
	private LmProductTypeService service;
	
	@Autowired
	private LmProductTypeRepository repo;
	
	@PreAuthorize("hasAnyRole('ROLE_APPROVER','ROLE_USER','ROLE_ADMIN')")
	@PostMapping(value= "/insertproducttype")
	public ResponseEntity<CommonRes> insertproducttype(@RequestBody LmProductTypeSaveReq req)
	{
		CommonRes valids = new CommonRes();
		
		List<com.maan.eway.error.Error> validation = service.validation(req);
		
		if(validation != null && validation.size() != 0)
		{
			valids.setIsError(true);
			valids.setErrorMessage(validation);
			valids.setCommonResponse(null);
			valids.setMessage("failed");
			return new ResponseEntity<CommonRes>(valids, HttpStatus.OK);
		}
		else
		{
			SuccessRes res = service.saveProductTypes(req);
			valids.setCommonResponse(res);
			valids.setIsError(false);
			valids.setErrorMessage(Collections.emptyList());
			valids.setMessage("Success");

			if(res != null)
			{	
			   return new ResponseEntity<CommonRes>(valids, HttpStatus.CREATED);
			}
			else
			{
				return new ResponseEntity<>(null, HttpStatus.CREATED);
			}
		}
	}
	
	@PreAuthorize("hasAnyRole('ROLE_APPROVER','ROLE_USER','ROLE_ADMIN')")
	@PostMapping(value = "/getallproducttypedetails")
	public ResponseEntity<CommonRes> getallproducttypedetails(@RequestBody LmProductTypeGetDetailsReq req)
	{
		CommonRes data = new CommonRes();
		List<LmProductTypeResponse> res = service.getallproducttypedetails(req);
		data.setCommonResponse(res);
		data.setErrorMessage(null);
		data.setIsError(false);
		data.setMessage("Success");
		
		if(res != null)
		{
			return new ResponseEntity<CommonRes>(data,HttpStatus.CREATED);
		}
		else
		{
	    	return new ResponseEntity<CommonRes>(data, HttpStatus.BAD_REQUEST); 
		}
	}
	
	@PreAuthorize("hasAnyRole('ROLE_APPROVER','ROLE_USER','ROLE_ADMIN')")
	@PostMapping("/getactiveproducttype")
	public ResponseEntity<CommonRes> getactiveproducttype(@RequestBody LmProductTypeGetDetailsReq req)
	{
		CommonRes data = new CommonRes();
		List<LmProductTypeResponse> res = service.getproductTypeDetails(req);
		
		data.setCommonResponse(res);
		data.setErrorMessage(null);
		data.setIsError(false);
		data.setMessage("Success");
		
		if(res != null)
		{
			return new ResponseEntity<CommonRes>(data,HttpStatus.CREATED);
		}
		else
		{
			return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
		}
	}
	
	@PreAuthorize("hasAnyRole('ROLE_APPROVER','ROLE_USER','ROLE_ADMIN')")
	@PostMapping("/getproducttypebyid")
	public ResponseEntity<CommonRes> getproducttypebyid(@RequestBody LmProductTypeGetDetailsReq req)
	{
		CommonRes data = new CommonRes();
		List<LmProductTypeResponse> res = service.getProductTypeById(req);
		
		data.setCommonResponse(res);
		data.setErrorMessage(null);
		data.setIsError(false);
		data.setMessage("Success");
		
		if(res != null)
		{
			return new ResponseEntity<CommonRes>(data,HttpStatus.CREATED);
		}
		else
		{
			return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
		}
	}
	
	@PreAuthorize("hasAnyRole('ROLE_APPROVER','ROLE_USER','ROLE_ADMIN')")
	@PostMapping("/changestatusofpttype")
	public ResponseEntity<CommonRes> changeStatusOfProductType(@RequestBody LmProductTypeChangeStatus req)
	{
		CommonRes res = new CommonRes();
		
		SuccessRes success = service.changeStatusOfProductType(req);
		res.setCommonResponse(success);
		res.setIsError(false);
		res.setErrorMessage(null);
		res.setMessage("Success");
		
		if(success != null)
		{
			return new ResponseEntity<CommonRes>(res,HttpStatus.CREATED);
		}
		else
		{
			return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
		}
		
	}
	
	@PreAuthorize("hasAnyRole('ROLE_APPROVER','ROLE_USER','ROLE_ADMIN')")
	@PostMapping("/dropdownproducttype")
	public ResponseEntity<DropdownCommonRes> getLmProductTypeDropDown(@RequestBody LmProductTypeDropDownReq req)
	{
		DropdownCommonRes data = new DropdownCommonRes();
		
		List<DropDownRes> res = service.getProductTypeDropDown(req);
		
		data.setCommonResponse(res);
		data.setIsError(false);
		data.setErrorMessage(null);
		data.setMessage("Success");
		
		if(res != null)
		{
			return new ResponseEntity<DropdownCommonRes>(data,HttpStatus.CREATED);
		}
		else
		{
			return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
		}
		
	}
}
