package com.maan.eway.search;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maan.eway.common.res.CommonRes;
import com.maan.eway.search.dto.SearchTiraReq;
import com.maan.eway.search.service.SearchTiraService;

@RestController
@RequestMapping("/search")
public class PolicySearchController {
	@Autowired 
	private SearchTiraService service;
	
	@PostMapping("/api")
	public ResponseEntity<CommonRes> searchMethod(@RequestBody SearchTiraReq req) {
		CommonRes data = new CommonRes();
		data=service.searchTira(req);
		return new ResponseEntity<CommonRes>(data, HttpStatus.CREATED);
	}

}
