package com.maan.eway.admin.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.maan.eway.admin.req.AdditionalInfoReq;
import com.maan.eway.admin.req.AttachBrokerBranchReq;
import com.maan.eway.admin.req.AttachCompaniesReq;
import com.maan.eway.admin.req.AttachCompnayProductRequest;
import com.maan.eway.admin.req.AttachIssuerBrannchReq;
import com.maan.eway.admin.req.AttachIssuerReferalReq;
import com.maan.eway.admin.req.AttachReferalReq;
import com.maan.eway.admin.req.AttacheIssuerBranchReq;
import com.maan.eway.admin.req.AttachedBranchesReq;
import com.maan.eway.admin.req.AttachedProductReq;
import com.maan.eway.admin.req.BrokerBranchesReq;
import com.maan.eway.admin.req.BrokerCreationReq;
import com.maan.eway.admin.req.CommonLoginCreationReq;
import com.maan.eway.admin.req.IssuerCraeationReq;
import com.maan.eway.admin.req.UserCreationReq;
import com.maan.eway.admin.service.LoginValidationService;
import com.maan.eway.bean.LoginMaster;
import com.maan.eway.error.Error;
import com.maan.eway.repository.LoginMasterRepository;

@Service
public class LoginValidationServiceImpl implements LoginValidationService  {

	@Autowired
	private BasicLoginValidationService basicValidation;
	
	private Logger log=LogManager.getLogger(LoginValidationService.class);
	
	@Autowired
	private LoginMasterRepository loginRepo;
	
//*************************************** Login Creation Apis Validations**********************************************************//
	
	@Override
	public List<Error> validateBrokerCreation(BrokerCreationReq req) {
		List<Error> errors = new ArrayList<Error>();
		 DozerBeanMapper dozerMapper = new  DozerBeanMapper();
		try {
			
			// Common Errors
			CommonLoginCreationReq commonReq = new CommonLoginCreationReq();
			dozerMapper.map(req, commonReq);
			List<Error>  commonErrors = basicValidation.commonLoginCreationValidation(commonReq) ;
			if(commonErrors.size()>0  ) {
				errors.addAll(commonErrors);
			}
			
			// Additional Broker Validataion
			AdditionalInfoReq  brokerReq = new AdditionalInfoReq();
			dozerMapper.map(req.getPersonalInformation() , brokerReq); 
			List<Error>  brokerErrors = basicValidation.commonBrokerPersonalValidation(brokerReq) ;
			if(brokerErrors.size()>0  ) {
				errors.addAll(brokerErrors);
			}
			
			if(StringUtils.isBlank(commonReq.getLoginInformation().getBrokerCompanyYn()))  {
				errors.add(new Error("01","BrokerCompanyYn","Please Select BrokerCompany Y or N"));
			} else if( ! (commonReq.getLoginInformation().getBrokerCompanyYn().equalsIgnoreCase("Y") || commonReq.getLoginInformation().getBrokerCompanyYn().equalsIgnoreCase("N")) )  {
				errors.add(new Error("01","BrokerCompanyYn","Please Select BrokerCompany Y or N"));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Exception is --->" + e.getMessage());
			errors.add(new Error("07", "Common Error", e.getMessage() ));
			return errors;
		}
		return errors;
	}

	
	@Override
	public List<Error> validateIssuerCreation(IssuerCraeationReq req) {
		List<Error> errors = new ArrayList<Error>();
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setAmbiguityIgnored(true);	
		try {
			
			// Common Errors
			CommonLoginCreationReq commonReq = new CommonLoginCreationReq();
			mapper.map(req, commonReq);
			List<Error>  commonErrors = basicValidation.commonLoginCreationValidation(commonReq) ;
			if(commonErrors.size()>0  ) {
				errors.addAll(commonErrors);
			}
			
			// Additional Errors
			
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Exception is --->" + e.getMessage());
			errors.add(new Error("07", "Common Error", e.getMessage() ));
			return errors;
		}
		return errors;
	}
	
	
	@Override
	public List<Error> validateUserCreation(UserCreationReq req) {
		List<Error> errors = new ArrayList<Error>();
		DozerBeanMapper dozerMapper = new  DozerBeanMapper();
		try {
			
			// Common Errors
			CommonLoginCreationReq commonReq = new CommonLoginCreationReq();
			dozerMapper.map(req, commonReq);
			List<Error>  commonErrors = basicValidation.commonLoginCreationValidation(commonReq) ;
			if(commonErrors.size()>0  ) {
				errors.addAll(commonErrors);
			}
			
			// Additional Broker Validataion
			AdditionalInfoReq  brokerReq = new AdditionalInfoReq();
			dozerMapper.map(req.getPersonalInformation() , brokerReq); 
			List<Error>  brokerErrors = basicValidation.commonBrokerPersonalValidation(brokerReq) ;
			if(brokerErrors.size()>0  ) {
				errors.addAll(brokerErrors);
			}
			
			if(StringUtils.isBlank(req.getLoginInformation().getOaCode())  ) {
				errors.add(new Error("06","Broker","Please Select Broker OaCode"));
			}
			if(StringUtils.isBlank(commonReq.getLoginInformation().getBrokerCompanyYn()))  {
				errors.add(new Error("01","BrokerCompanyYn","Please Select BrokerCompany Y or N"));
			} else if( ! (commonReq.getLoginInformation().getBrokerCompanyYn().equalsIgnoreCase("Y") || commonReq.getLoginInformation().getBrokerCompanyYn().equalsIgnoreCase("N")) )  {
				errors.add(new Error("01","BrokerCompanyYn","Please Select BrokerCompany Y or N"));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Exception is --->" + e.getMessage());
			errors.add(new Error("07", "Common Error", e.getMessage() ));
			return errors;
		}
		return errors;
	}
	
//***************************************Add Branch Apis Validations **********************************************************//	
	
	@Override
	public List<Error> validateBrokerBranchReq(AttachCompaniesReq req) {
		List<Error> errors = new ArrayList<Error>();
		try {
			// Branch Validation
			if(StringUtils.isBlank(req.getLoginId()) ) {
				errors.add(new Error("01", "LoginId", "Plese Enter LoginId" ));
			}
			if (StringUtils.isBlank(req.getBrokerCompanyYn())) {
				errors.add(new Error("02", "BrokerCompanyYn", "Plese Enter BrokerCompanyYn"));
			}  
				
			if(req.getAttachedCompanies()==null || req.getAttachedCompanies().size()== 0 ) {
				errors.add(new Error("03", "Attached Companies", "Plese select Atleast One  Company" ));
			} else {
				Long rowNo = 0L ;
				for(AttachedBranchesReq  data : req.getAttachedCompanies() ) {
					rowNo = rowNo + 1L ;
					if(StringUtils.isBlank(data.getInsuranceId()) ) {
						errors.add(new Error("03", "Insurance Id", "Plese Select Atleast One Company" ));
					}
					
					if(data.getAttachedBranches()==null || data.getAttachedBranches().size()== 0 ) {
						errors.add(new Error("03", "Attached Companies", "Plese select Atleast One  Branch in Company Row No : " +  rowNo  ));
					}
					for (BrokerBranchesReq data2 : data.getAttachedBranches()) {
						if (req.getBrokerCompanyYn().equals("N")) {
							if (StringUtils.isBlank(data2.getBranchCode())) {
								errors.add(new Error("04", "Branch Code", "Plese Enter Branch Code"));
							} else if (req.getBrokerCompanyYn().equals("Y")) {
								if (data2.getBrokerBranchCode() == null || data2.getBrokerBranchCode().size() == 0) {
									errors.add(new Error("04", "Broker Branch Code", "Plese select Atleast One  Broker Branch Code"+rowNo));
								}
							}
						}
					}
				}	
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Exception is --->" + e.getMessage());
			errors.add(new Error("09", "Common Error", e.getMessage() ));
		}
		return errors;
	}


	@Override
	public List<Error> validateIssuerBranchReq(AttachIssuerBrannchReq req) {
		List<Error> errors = new ArrayList<Error>();
		try {
			// Branch Validation
			if(StringUtils.isBlank(req.getLoginId()) ) {
				errors.add(new Error("01", "LoginId", "Plese Enter LoginId" ));
			}
			
			if(req.getAttachedCompanies()==null || req.getAttachedCompanies().size()== 0 ) {
				errors.add(new Error("02", "Attached Companies", "Plese select Atleast One  Company" ));
			} else {
				Long rowNo = 0L ;
				for(AttacheIssuerBranchReq  data : req.getAttachedCompanies() ) {
					rowNo = rowNo + 1L ;
					if(StringUtils.isBlank(data.getInsuranceId()) ) {
						errors.add(new Error("01", "Insurance Id", "Plese Select Atleast One Company" ));
					}
					
					if(data.getAttachedBranches()==null || data.getAttachedBranches().size()== 0 ) {
						errors.add(new Error("02", "Attached Companies", "Plese select Atleast One  Branch in Company Row No : " +  rowNo  ));
					}
				}	
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Exception is --->" + e.getMessage());
			errors.add(new Error("09", "Common Error", e.getMessage() ));
		}
		return errors;
	}
	
//***************************************Add Broker Products Apis Validations **********************************************************//	

	@Override
	public List<Error> validateBrokerProductReq(AttachCompnayProductRequest req) {
		List<Error> errors = new ArrayList<Error>();
		try {
			//Product Validation
			if(StringUtils.isBlank(req.getLoginId()) ) {
				errors.add(new Error("01", "LoginId", "Plese Enter LoginId" ));
			}
			
			boolean status = false ;
			
			if(req.getAttachedProducts()==null || req.getAttachedProducts().size()== 0 ) {
				errors.add(new Error("02", "Attached Companies", "Plese select Atleast One  Product" ));
			} else {
				Long productRow = 0L;
				for (AttachedProductReq product :  req.getAttachedProducts() ) {
					productRow = productRow + 1L ;
					if(StringUtils.isBlank(product.getProductId())) {
						errors.add(new Error("02", "ProductId", "Plese Enter Product Id in  Product Row No : " +  productRow ));
					}
					
					if(StringUtils.isBlank(product.getProductName())) {
						errors.add(new Error("02", "Product Name", "Plese Enter Product Name in  Product Row No : " +  productRow ));
					}
					
					if(product.getEffectiveDate()==null ) {
						errors.add(new Error("02", "Effective Date ", "Please select Effective Date  in  Product Row No : " +  productRow ));
					} else {
						Calendar cal = new GregorianCalendar(); 
						long MILLIS_IN_A_DAY = (1000 * 60 * 60 * 24) * 1 ;
						Date today = new Date();
						Date yesterday  = new Date(today.getTime() - MILLIS_IN_A_DAY) ;	
						cal.setTime(yesterday);  cal.set(Calendar.HOUR_OF_DAY, 23); cal.set(Calendar.MINUTE, 59);
						if(product.getEffectiveDate().before(yesterday) ) {
							errors.add(new Error("02", "Effective Date ", "Plese Enter Future Date as Effective Date  in  Product Row No : " +  productRow ));
						}
					}
					if(StringUtils.isBlank(product.getStatus())) {
						errors.add(new Error("02", "Status", "Plese Select Status in  Product Row No : " +  productRow ));
					} else if(product.getStatus().equalsIgnoreCase("Y") ) {
						status = true ;
					}
					if(StringUtils.isBlank(product.getSumInsuredStart())) {
						errors.add(new Error("02", "Sum Insured Start", "Plese Enter Sum Insured Start in  Product Row No : " +  productRow ));
					} else if (! product.getSumInsuredStart().matches("[0-9.]+") ) {
						errors.add(new Error("02", "Sum Insured Start", "Plese Enter Valid Number Sum Insured Start in  Product Row No : " +  productRow ));
					}
					if(StringUtils.isBlank(product.getSumInsuredEnd())) {
						errors.add(new Error("02", "Sum Insured End", "Plese Enter Sum Insured End in  Product Row No : " +  productRow ));
					} else if (! product.getSumInsuredEnd().matches("[0-9.]+") ) {
						errors.add(new Error("02", "Sum Insured End", "Plese Enter Valid Number Sum Insured End in  Product Row No : " +  productRow ));
					} else if (StringUtils.isNotBlank(product.getSumInsuredStart()) && StringUtils.isBlank(product.getSumInsuredEnd())  ) {
						if (Long.valueOf(product.getSumInsuredStart()) > Long.valueOf(product.getSumInsuredEnd()) ) {
							errors.add(new Error("02", "Sum Insured End", "Sum Insured Start Greater Than Sum Insured End in  Product Row No : " +  productRow ));
						}
					}
					
					if (StringUtils.isBlank(product.getPaymentYn())) {
						errors.add(new Error("05", "Payment", "Please Select Payment"));
					} else if (product.getPaymentYn().length() > 1) {
						errors.add(new Error("05", "Payment", "Enter Payment 1 Character Only"));
					}else if(!("Y".equals(product.getPaymentYn())||"N".equals(product.getPaymentYn()))) {
						errors.add(new Error("05", "Payment", "Enter Payment Y or N Only"));
					}
					
					if (StringUtils.isBlank(product.getCommissionVatYn())) {
						errors.add(new Error("05", "CommissionVat", "Please Select CommissionVat"));
					} else if (product.getCommissionVatYn().length() > 1) {
						errors.add(new Error("05", "CommissionVat", "Enter CommissionVat 1 Character Only"));
					}else if(!("Y".equals(product.getCommissionVatYn())||"N".equals(product.getCommissionVatYn()))) {
						errors.add(new Error("05", "CommissionVat", "Enter CommissionVat Y or N Only"));
					}
					
					if (StringUtils.isBlank(product.getCheckerYn())) {
						errors.add(new Error("05", "Checker", "Please Select Checker"));
					} else if (product.getCheckerYn().length() > 1) {
						errors.add(new Error("05", "Checker", "Enter Checker 1 Character Only"));
					}else if(!("Y".equals(product.getCheckerYn())||"N".equals(product.getCheckerYn()))) {
						errors.add(new Error("05", "Checker", "Enter Checker Y or N Only"));
					}
					
					if (StringUtils.isBlank(product.getMakerYn())) {
						errors.add(new Error("05", "Maker", "Please Select Maker"));
					} else if (product.getMakerYn().length() > 1) {
						errors.add(new Error("05", "Maker", "Enter Maker 1 Character Only"));
					}else if(!("Y".equals(product.getMakerYn())||"N".equals(product.getMakerYn()))) {
						errors.add(new Error("05", "Maker", "Enter Maker Y or N Only"));
					}
					
					if (StringUtils.isBlank(product.getCustConfirmYn())) {
						errors.add(new Error("05", "CustomerConfirmation", "Please Select CustomerConfirmation"));
					} else if (product.getCustConfirmYn().length() > 1) {
						errors.add(new Error("05", "CustomerConfirmation", "Enter CustomerConfirmation 1 Character Only"));
					}else if(!("Y".equals(product.getCustConfirmYn())||"N".equals(product.getCustConfirmYn()))) {
						errors.add(new Error("05", "CustomerConfirmation", "Enter CustomerConfirmation Y or N Only"));
					}
					
					if(StringUtils.isBlank(product.getCommissionPercent())) {
						errors.add(new Error("02", "Commission Percent", "Plese Enter Commission Percent in  Product Row No : " +  productRow ));
					} else if (! product.getCommissionPercent().matches("[0-9]+") ) {
						errors.add(new Error("02", "Commission Percent", "Plese Enter Valid Number Commission Percent in  Product Row No : " +  productRow ));
					} else if (product.getCommissionPercent().length()>2 ) {
						errors.add(new Error("02", "Commission Percent", "Plese Enter Valid Number Commission Percent in  Product Row No : " +  productRow ));
					}
				}	
				
				if( status == false   ) {
					errors.add(new Error("02", "Status", "Plese Select Active Status for Alteast One Product " ));
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Exception is --->" + e.getMessage());
			errors.add(new Error("09", "Common Error", e.getMessage() ));
		}
		return errors;
	}
	
//***************************************Add Issuer Referal Apis Validations **********************************************************//	
	
	@Override
	public List<Error> validateIssuerReferalReq(AttachIssuerReferalReq req) {
		List<Error> errors = new ArrayList<Error>();
		try {
			//Referal Validation
			if(StringUtils.isBlank(req.getLoginId()) ) {
				errors.add(new Error("01", "LoginId", "Plese Enter LoginId" ));
			}
			
			if(StringUtils.isBlank(req.getInsuranceId()) ) {
				errors.add(new Error("02", "InsuranceId", "Plese Enter InsuranceId" ));
			}
			if(StringUtils.isBlank(req.getBranchCode()) ) {
				errors.add(new Error("03", "BrnchCode", "Plese Enter BranchCode" ));
			}
			
			if(req.getAttachedReferals()==null || req.getAttachedReferals().size()== 0 ) {
				errors.add(new Error("02", "Attached Referals", "Plese select Atleast One  Referal" ));
			} else {
				Long referalRow  = 0L;
				boolean status = false ;
				for (AttachReferalReq referal :  req.getAttachedReferals() ) {
					referalRow = referalRow + 1L ;
					if(StringUtils.isBlank(referal.getReferalId())) {
						errors.add(new Error("02", "ReferalId", "Plese Enter ReferalId in  Referal Row No : " +  referalRow  ));
					}
					
					if(StringUtils.isBlank(referal.getReferalName())) {
						errors.add(new Error("02", "Referal Name", "Plese Enter Referal Name in  Referal Row No : " +  referalRow  ));
					}
					
					if(referal.getEffectiveDate()==null ) {
						errors.add(new Error("02", "Effective Date ", "Please select Effective Date  in  Referal Row No : " +  referalRow  ));
					} else {
						Calendar cal = new GregorianCalendar();  
						Date today =  new Date(); 
						cal.setTime(today); cal.add(Calendar.DAY_OF_MONTH, -1); cal.set(Calendar.HOUR_OF_DAY, 23); cal.set(Calendar.MINUTE, 50);
						today = cal.getTime();
						if(referal.getEffectiveDate().before(today) ) {
							errors.add(new Error("02", "Effective Date ", "Plese Enter Future Date as Effective Date  in  Referal Row No : " +  referalRow ));
						}
					}
					if(StringUtils.isBlank(referal.getStatus())) {
						errors.add(new Error("02", "Status", "Plese Select Status in  Referal Row No : " +  referal ));
					} else if (referal.getStatus().equalsIgnoreCase("Y") ) {
						status = true ;
					}
					if(StringUtils.isBlank(referal.getSumInsuredStart())) {
						errors.add(new Error("02", "Start Limit", "Plese Enter Sum Insured Start in  Referal Row No : " +  referalRow ));
					} else if (! referal.getSumInsuredStart().matches("[0-9]+") ) {
						errors.add(new Error("02", "Start Limit", "Plese Enter Valid Number Sum Insured Start  in  Referal Row No : " +  referalRow ));
					}
					if(StringUtils.isBlank(referal.getSumInsuredEnd())) {
						errors.add(new Error("02", "End Limit", "Plese Enter Sum Insured End in  Referal Row No : " +  referalRow ));
					} else if (! referal.getSumInsuredEnd().matches("[0-9]+") ) {
						errors.add(new Error("02", "End Limit", "Plese Enter Valid Number Sum Insured End in  Referal Row No : " +  referalRow ));
					} else if (StringUtils.isNotBlank(referal.getSumInsuredEnd()) && StringUtils.isBlank(referal.getSumInsuredEnd())  ) {
						if (Long.valueOf(referal.getSumInsuredEnd()) > Long.valueOf(referal.getSumInsuredStart()) ) {
							errors.add(new Error("02", "End Limit", "Sum Insured Start Greater Than Sum Insured End in  Referal Row No : " +  referalRow ));
						}
					}
				}	
				
				if( status == false   ) {
					errors.add(new Error("02", "Status", "Plese Select Active Status for Alteast One Referal " ));
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Exception is --->" + e.getMessage());
			errors.add(new Error("09", "Common Error", e.getMessage() ));
		}
		return errors;
	}


@Override
public List<Error> validateBrokerCompanyBranchReq(AttachBrokerBranchReq req) {
	List<Error> errors = new ArrayList<Error>();
	try {
		
		if(StringUtils.isBlank(req.getLoginId()) ) {
			errors.add(new Error("01", "LoginId", "Plese Enter LoginId" ));
		}
		//Login  Data
		LoginMaster loginData = loginRepo.findByLoginId(req.getLoginId());
		if (loginData.getBrokerCompanyYn() != null && !loginData.getBrokerCompanyYn().equals("N")) {
			if (loginData.getBrokerCompanyYn().equals("Y") && loginData.getUserType().equalsIgnoreCase("BROKER")) {
				if (StringUtils.isBlank(req.getBranchCode())) {
					errors.add(new Error("03", "BranchCode", "Plese Enter BranchCode"));
				}
				if (StringUtils.isBlank(req.getAttachedBranch())) {
					errors.add(new Error("03", "AttachedBranchCode", "Plese Enter AttachedBranchCode"));
				}
			}
		}
		
		if (StringUtils.isBlank(req.getBranchCode())) {
			errors.add(new Error("03", "BranchCode", "Plese Enter BranchCode"));
		}
		if(StringUtils.isBlank(req.getCompanyId()) ) {
			errors.add(new Error("02", "InsuranceId", "Plese Enter InsuranceId" ));
		}
		
		if(StringUtils.isBlank(req.getBrokerAttachedCompany()) ) {
			errors.add(new Error("03", "AttachedComapany", "Plese Enter AttachedComapany" ));
		}
		
	
		
		
	} catch (Exception e) {
		e.printStackTrace();
		log.info("Exception is --->" + e.getMessage());
		errors.add(new Error("09", "Common Error", e.getMessage() ));
	}
	return errors;
}
	


}
