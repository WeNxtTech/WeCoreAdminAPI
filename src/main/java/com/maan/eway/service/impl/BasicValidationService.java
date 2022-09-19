package com.maan.eway.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.maan.eway.error.Error;
import com.maan.eway.master.req.DocumentMasterSaveReq;
import com.maan.eway.master.req.ProductDocumentMasterSaveReq;

@Service
public class BasicValidationService {

	// Date null check
	public boolean dateNullValidation (Date date  ) {
		if(date == null  ) {
			return true ;
		} else {
			return false ;
		}
	}
	
	//Effective Date
	public boolean effectiveDateValidation (Date date  ) {
		Date today = new Date();
		if (date.compareTo(today) > 0) {
			return true;
		} else {
			return false;
		}
	}

	public List<Error> validateDocument(DocumentMasterSaveReq req) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Error> validateDocument(List<ProductDocumentMasterSaveReq> reqList) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
