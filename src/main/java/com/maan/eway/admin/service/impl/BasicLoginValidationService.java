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

import com.maan.eway.admin.req.AdditionalInfoReq;
import com.maan.eway.admin.req.BrokerPersonalInfoReq;
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
			} else if (loginReq.getSubUserType().equalsIgnoreCase("bank") && StringUtils.isBlank(loginReq.getBankCode()) ) {
				errors.add(new Error("05", "Bank Code", "Please Select Bank Code"));
			}
		/*	if( loginReq.getAttachedBranches()==null || loginReq.getAttachedBranches().size() == 0 ) {
				errors.add(new Error("06", "Attached Branch", "Please Choose Atleast One Branch"));
			} */
			
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
				errors.add(new Error("07", "User Mobile", "Please Enter User Mobile"));
			} else if (personalReq.getUserMobile().length() > 20 ) {
				errors.add(new Error("07", "User Mobile", "User Mobile Must Be Under 20 Number Only Allowed"));
			} else if (! personalReq.getUserMobile().matches("[0-9]+") ) {
				errors.add(new Error("07", "User Mobile", "User Mobile Must Be Under 20 Number Only Allowed"));
			}
			
			if( StringUtils.isBlank(personalReq.getUserName()) ) {
				errors.add(new Error("08", "User Name", "Please Enter User Name"));
			} else if (personalReq.getUserName().length() > 100 ) {
				errors.add(new Error("08", "User Name ", "User Name Must Be Under 100 Characters Only Allowed"));
			} else if (isNotValidName(personalReq.getUserName()) ) {
				errors.add(new Error("08", "User Name ", "Please Enter Valid User Name"));
			} 
			 
			
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Exception is --->" + e.getMessage());
			errors.add(new Error("09", "Common Error", e.getMessage() ));
		}
		return errors;
	}
	
	
	public List<Error>  commonBrokerPersonalValidation(AdditionalInfoReq brokerReq ) {
		List<Error> errors = new ArrayList<Error>();
		try {
			// Login Validation
			if(StringUtils.isBlank(brokerReq.getAcExecutiveId())  ) {
				errors.add(new Error("09", "Ac Excutive Id", "Plese Enter AcExcutiveId" ));
			} else if(! brokerReq.getAcExecutiveId().matches("[0-9]+")  ) {
				errors.add(new Error("09", "Ac Excutive Id", "Plese Enter Valid Number AcExcutiveId" ));
			}
			
			if(StringUtils.isBlank(brokerReq.getAddress1())  ) {
				errors.add(new Error("10", "Address1", "Plese Enter Address1" ));
			} else if(brokerReq.getAddress1().length()>100 ) {
				errors.add(new Error("10", "Address1", "Address1 Must Be Under 100 Character Only Allowed" ));
			}
			
			if(StringUtils.isBlank(brokerReq.getAddress2())  ) {
				errors.add(new Error("11", "Address2", "Plese Enter Address2" ));
			} else if(brokerReq.getAddress2().length()>100 ) {
				errors.add(new Error("11", "Address2", "Address2 Must Be Under 100 Character Only Allowed" ));
			}
			
			if(StringUtils.isBlank(brokerReq.getAddress3())  ) {
				errors.add(new Error("12", "Address3", "Plese Enter Address3" ));
			} else if(brokerReq.getAddress3().length()>100 ) {
				errors.add(new Error("12", "Address3", "Address3 Must Be Under 100 Character Only Allowed" ));
			}
			
			if(StringUtils.isBlank(brokerReq.getApprovedPreparedBy())  ) {
				errors.add(new Error("13", "ApprovedPreparedBy", "Plese Enter Approved Prepared By" ));
			} else if(brokerReq.getApprovedPreparedBy().length()>30 ) {
				errors.add(new Error("13", "ApprovedPreparedBy", "ApprovedPreparedBy Must Be Under 30 Character Only Allowed" ));
			}
			
			if(StringUtils.isBlank(brokerReq.getCheckerYn())  ) {
				errors.add(new Error("14", "CheckerYn", "Plese Select Checker Yes or No" ));
			}
			
			if(StringUtils.isBlank(brokerReq.getCity())  ) {
				errors.add(new Error("15", "City", "Plese Select City" ));
			} else if(! brokerReq.getCity().matches("[0-9]+")  ) {
				errors.add(new Error("15", "City", "Plese Enter Valid Number In City" ));
			}
			
			if(StringUtils.isBlank(brokerReq.getCommissionVatYn())  ) {
				errors.add(new Error("16", "Commission Vat Yn", "Plese Enter Commission Vat Yn" ));
			}
			
			if(StringUtils.isBlank(brokerReq.getCompanyName())  ) {
				errors.add(new Error("17", "CompanyName", "Plese Enter CompanyName" ));
			}else if(brokerReq.getCompanyName().length()>100 ) {
				errors.add(new Error("17", "CompanyName", "CompanyName Must Be Under 100 Character Only Allowed" ));
			}
			
			if(StringUtils.isBlank(brokerReq.getCountry())  ) {
				errors.add(new Error("18", "Country", "Plese Select Country" ));
			} else if(! brokerReq.getCountry().matches("[0-9]+")  ) {
				errors.add(new Error("18", "Country", "Plese Enter Valid Number In Country" ));
			}
			
			if(StringUtils.isBlank(brokerReq.getCustConfirmYn())  ) {
				errors.add(new Error("19", "Customer Confirm Yn", "Plese Select Customer Confirm Yn" ));
			}
			
			if(StringUtils.isBlank(brokerReq.getCustomerId())  ) {
				errors.add(new Error("20", "CustomerId", "Plese Select CustomerId" ));
			} else if(! brokerReq.getCountry().matches("[0-9]+")  ) {
				errors.add(new Error("21", "CustomerId", "Plese Enter Valid Number In CustomerId" ));
			}
			
			if(StringUtils.isBlank(brokerReq.getEmirate())  ) {
				errors.add(new Error("21", "Emirate", "Plese Select Country" ));
			} else if(brokerReq.getEmirate().length() > 50  ) {
				errors.add(new Error("21", "Emirate", "Emirate Must Be Under 50 Character Only Allowed" ));
			}
			
			if(StringUtils.isBlank(brokerReq.getFax())  ) {
				errors.add(new Error("22", "Fax", "Plese Select Fax" ));
			} else if(brokerReq.getEmirate().length() > 50  ) {
				errors.add(new Error("22", "Fax", "Fax Must Be Under 50 Character Only Allowed" ));
			}
			
			if(StringUtils.isBlank(brokerReq.getMakerYn())  ) {
				errors.add(new Error("23", "MakerYn", "Plese Select MakerYn" ));
			}
			
			if(StringUtils.isBlank(brokerReq.getMissippiId())  ) {
				errors.add(new Error("24", "MissippiId", "Plese Select MissippiId" ));
			} else if(! brokerReq.getMissippiId().matches("[0-9]+")  ) {
				errors.add(new Error("24", "MissippiId", "Plese Enter Valid Number In MissippiId" ));
			}
			
			if(StringUtils.isBlank(brokerReq.getPobox())  ) {
				errors.add(new Error("25", "Post Box No", "Plese Enter Post Box No" ));
			} else if(! brokerReq.getPobox().matches("[0-9]+")  ) {
				errors.add(new Error("25", "Post Box No", "Plese Enter Valid Number In Post Box No" ));
			}
			
			if(StringUtils.isBlank(brokerReq.getRemarks())  ) {
				errors.add(new Error("26", "Remarks", "Plese Enter Remarks" ));
			} else if(brokerReq.getRemarks().length()>100  ) {
				errors.add(new Error("26", "Remarks", "Remarks Must Be Under 100 Characters Only Allowed" ));
			}
			
			if(StringUtils.isBlank(brokerReq.getRsaBrokerCode())  ) {
				errors.add(new Error("27", "RsaBrokerCode", "Plese Enter RsaBrokeCode" ));
			} else if(brokerReq.getRsaBrokerCode().length()>100  ) {
				errors.add(new Error("26", "RsaBrokerCode", "RsaBrokerCode Must Be Under 25 Characters Only Allowed" ));
			}
			
			if(StringUtils.isBlank(brokerReq.getState())  ) {
				errors.add(new Error("28", "State", "Plese Select State" ));
			}
			
			if(StringUtils.isBlank(brokerReq.getVatRegNo())  ) {
				errors.add(new Error("29", "VatRegNo", "Plese Select Country" ));
			} else if(brokerReq.getVatRegNo().length()>100  ) {
				errors.add(new Error("29", "VatRegNo", "VatRegNo Must Be Under 100 Characters Only Allowed" ));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Exception is --->" + e.getMessage());
			errors.add(new Error("09", "Common Error", e.getMessage() ));
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
