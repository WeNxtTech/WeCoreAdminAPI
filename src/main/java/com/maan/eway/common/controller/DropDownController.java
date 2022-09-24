package com.maan.eway.common.controller;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maan.eway.common.service.DropDownService;
import com.maan.eway.req.SubUserTypeReq;
import com.maan.eway.res.CommonRes;
import com.maan.eway.res.DropDownRes;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/dropdown")
@Api(tags = "MASTER : Drop Down Controller", description = "API's")

public class DropDownController {

	@Autowired
	private  DropDownService dropDownService;
	

	
	@GetMapping("/covernotetype")
	@ApiOperation(value = "This method is to Cover Note Drop Down")
	public ResponseEntity<CommonRes> coverNoteType() {
		CommonRes data = new CommonRes();

		List<DropDownRes> res = dropDownService.coverNoteType();
		data.setCommonResponse(res);
		data.setIsError(false);
		data.setErrorMessage(Collections.emptyList());
		data.setMessage("Success");

		if (res != null) {
			return new ResponseEntity<CommonRes>(data, HttpStatus.CREATED);
		} else {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}

}

	
	@GetMapping("/paymentmode")
	@ApiOperation(value = "This method is to Payment Mode Drop Down")
	public ResponseEntity<CommonRes> paymentmode() {
		CommonRes data = new CommonRes();

		List<DropDownRes> res = dropDownService.paymentmode();
		data.setCommonResponse(res);
		data.setIsError(false);
		data.setErrorMessage(Collections.emptyList());
		data.setMessage("Success");

		if (res != null) {
			return new ResponseEntity<CommonRes>(data, HttpStatus.CREATED);
		} else {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
	}

		@GetMapping("/endorsementtype")
		@ApiOperation(value = "This method is to Endorsement Type Drop Down")
		public ResponseEntity<CommonRes> endorsementtype() {
			CommonRes data = new CommonRes();

			List<DropDownRes> res = dropDownService.endorsementtype();
			data.setCommonResponse(res);
			data.setIsError(false);
			data.setErrorMessage(Collections.emptyList());
			data.setMessage("Success");

			if (res != null) {
				return new ResponseEntity<CommonRes>(data, HttpStatus.CREATED);
			} else {
				return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
			}
}

		
		@GetMapping("/discounttypeoffered")
		@ApiOperation(value = "This method is to Discount Type Offered Drop Down")
		public ResponseEntity<CommonRes> discounttypeoffered() {
			CommonRes data = new CommonRes();

			List<DropDownRes> res = dropDownService.discounttypeoffered();
			data.setCommonResponse(res);
			data.setIsError(false);
			data.setErrorMessage(Collections.emptyList());
			data.setMessage("Success");

			if (res != null) {
				return new ResponseEntity<CommonRes>(data, HttpStatus.CREATED);
			} else {
				return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
			}
}

		@GetMapping("/istaxexcempted")
		@ApiOperation(value = "This method is to Tax Excepmted Drop Down")
		public ResponseEntity<CommonRes> taxexcempted() {
			CommonRes data = new CommonRes();

			List<DropDownRes> res = dropDownService.taxexcempted();
			data.setCommonResponse(res);
			data.setIsError(false);
			data.setErrorMessage(Collections.emptyList());
			data.setMessage("Success");

			if (res != null) {
				return new ResponseEntity<CommonRes>(data, HttpStatus.CREATED);
			} else {
				return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
			}
}
		
		@GetMapping("/taxexcemptiontype")
		@ApiOperation(value = "This method is to Tax Excepmtion Type Drop Down")
		public ResponseEntity<CommonRes> taxexcemptiontype() {
			CommonRes data = new CommonRes();

			List<DropDownRes> res = dropDownService.taxexcemptiontype();
			data.setCommonResponse(res);
			data.setIsError(false);
			data.setErrorMessage(Collections.emptyList());
			data.setMessage("Success");

			if (res != null) {
				return new ResponseEntity<CommonRes>(data, HttpStatus.CREATED);
			} else {
				return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
			}
}
		
		
		@GetMapping("/policyholdertype")
		@ApiOperation(value = "This method is to Policy Holder Type Drop Down")
		public ResponseEntity<CommonRes> policyholdertype() {
			CommonRes data = new CommonRes();

			List<DropDownRes> res = dropDownService.policyholdertype();
			data.setCommonResponse(res);
			data.setIsError(false);
			data.setErrorMessage(Collections.emptyList());
			data.setMessage("Success");

			if (res != null) {
				return new ResponseEntity<CommonRes>(data, HttpStatus.CREATED);
			} else {
				return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
			}
}
		
		@GetMapping("/policyholderidtype")
		@ApiOperation(value = "This method is to Policy Holder ID Type Drop Down")
		public ResponseEntity<CommonRes> policyholderidtype() {
			CommonRes data = new CommonRes();

			List<DropDownRes> res = dropDownService.policyholderidtype();
			data.setCommonResponse(res);
			data.setIsError(false);
			data.setErrorMessage(Collections.emptyList());
			data.setMessage("Success");

			if (res != null) {
				return new ResponseEntity<CommonRes>(data, HttpStatus.CREATED);
			} else {
				return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
			}
}
		
		@GetMapping("/policyholdergender")
		@ApiOperation(value = "This method is to Policy Holder Gender Drop Down")
		public ResponseEntity<CommonRes> policyholdergender() {
			CommonRes data = new CommonRes();

			List<DropDownRes> res = dropDownService.policyholdergender();
			data.setCommonResponse(res);
			data.setIsError(false);
			data.setErrorMessage(Collections.emptyList());
			data.setMessage("Success");

			if (res != null) {
				return new ResponseEntity<CommonRes>(data, HttpStatus.CREATED);
			} else {
				return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
			}
}
		
	
}
