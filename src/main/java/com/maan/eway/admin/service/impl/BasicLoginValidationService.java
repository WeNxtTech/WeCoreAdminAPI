package com.maan.eway.admin.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.maan.eway.admin.req.CommonLoginCreationReq;
import com.maan.eway.admin.req.CommonLoginInformationReq;
import com.maan.eway.admin.req.CommonPersonalInforReq;
import com.maan.eway.bean.LoginMaster;
import com.maan.eway.error.Error;
import com.maan.eway.repository.LoginMasterRepository;

@Service
public class BasicLoginValidationService {
	
	private Logger log=LogManager.getLogger(LoginCreationServiceImpl.class);

	@Autowired
	private LoginMasterRepository loginRepo ;
	
	public List<Error>  commonLoginCreationValidation(CommonLoginCreationReq req ) {
		List<Error> errors = new ArrayList<Error>();
		try {
			// Login Validation
			CommonLoginInformationReq loginReq = req.getLoginInformation() ;
			
			if (StringUtils.isBlank(loginReq .getCreatedBy())) {
				errors.add(new Error("01", "Created By", "Please Enter Created By"));
			}
			
			if (StringUtils.isBlank(loginReq.getLoginId())) {
				errors.add(new Error("02", "Login Id", "Please Enter Login Id"));
			} else if (loginReq.getLoginId().length() > 50 || loginReq.getLoginId().length() < 5  ) {
				errors.add(new Error("02", "Login Id", "Login Id Under 5 - 50 Characters Only Allowed"));
			} else {
				LoginMaster  loginData = loginRepo.findByLoginId(loginReq.getLoginId());
				if(loginData!=null ) {
					if (StringUtils.isBlank( loginReq.getOaCode()) ) {
						errors.add(new Error("02", "Login Id", "Login Id Already Exist"));
					} else if(! loginReq.getOaCode().equalsIgnoreCase(loginData.getOaCode() ) ) {
						errors.add(new Error("02", "Login Id", "Login Id Already Exist"));
					}
				} 
			}
			
			if (StringUtils.isBlank(loginReq.getPassword())) {
				errors.add(new Error("04", "Password", "Please Enter Password"));
			} else if (loginReq.getPassword().length() > 50) {
				errors.add(new Error("03", "Oa Code", "Password Must Be Under 50 Characters Only Allowed"));
			}
			
			if (StringUtils.isBlank(loginReq.getStatus())) {
				errors.add(new Error("05", "Status", "Please Select Status"));
			} 
			
			if( StringUtils.isBlank(loginReq.getUserType()) ) {
				errors.add(new Error("05", "UserType", "Please Select UserType"));
			} else if (loginReq.getUserType().length() > 20 ) {
				errors.add(new Error("05", "UserType", "UserType Under 20 Characters Only Allowed"));
				
			} else if (StringUtils.isBlank(loginReq.getSubUserType())) {
				errors.add(new Error("05", "Sub UserType", "Please Select Sub UserType"));
			} else if (loginReq.getSubUserType().length() > 20 ) {
				errors.add(new Error("05", "Sub UserType", "Sub UserType Under 20 Characters Only Allowed"));
			}
			if( loginReq.getAttachedBranches()==null || loginReq.getAttachedBranches().size() == 0 ) {
				errors.add(new Error("06", "User Mail", "Please Choose Atleast One Branch"));
			}
			
			// Personal Info Validation
			CommonPersonalInforReq personalReq = req.getPersonalInformation() ; 
			
			if( StringUtils.isBlank(personalReq.getUserMail()) ) {
				errors.add(new Error("06", "User Mail", "Please Select User Mail"));
			} else if (personalReq.getUserMail().length() > 50  ) {
				errors.add(new Error("06", "User Mail", "Mail Under Must Be 50 Characters Only Allowed"));
			}  else if( isNotValidMail(personalReq.getUserMail()) ){
				errors.add(new Error("08", "User Mail", "Please Enter Valid User Mail"));
			}
			
			if( StringUtils.isBlank(personalReq.getUserMobile()) ) {
				errors.add(new Error("07", "User Mobile", "Please Select User Mobile"));
			} else if (personalReq.getUserMobile().length() > 20 ) {
				errors.add(new Error("07", "User Mobile", "User Mobile Must Be Under 20 Characters Only Allowed"));
			}
			if( StringUtils.isBlank(personalReq.getUserName()) ) {
				errors.add(new Error("08", "User Name", "Please Select User Name"));
			} else if (personalReq.getUserName().length() > 100 ) {
				errors.add(new Error("08", "User Name ", "User Name Must Be Under 100 Characters Only Allowed"));
			} else if (isNotValidName(personalReq.getUserName()) ) {
				errors.add(new Error("08", "User Name ", "Please Enter Valid User Name"));
			} 
			 
			
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Exception is --->" + e.getMessage());
			errors.add(new Error("07", "Common Error", e.getMessage() ));
		}
		return errors;
	}
	
	public boolean isNotValidName(String name) {
		String s = name;
		String regx = "^[\\p{L} .'-]+$";
		Pattern p = Pattern.compile(regx);
		Matcher m = p.matcher(s);
		try {
			if (m.matches()) {
				return false;
			}

		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
			return true;
		}
		return true;
	}
	
	public boolean isNotValidMail(String mail) {
		String regex = "^[a-zA-Z0-9_+&*-]+(?:\\." + "[a-zA-Z0-9_+&*-]+)*@" + "(?:[a-zA-Z0-9-]+\\.)+[a-z"
				+ "A-Z]{2,7}$";
		Pattern pattern = Pattern.compile(regex);
		Matcher m = pattern.matcher(mail);
		try {
			if (m.matches()) {
				return false;
			}

		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
			return true;
		}
		return true;
	}
	
}
