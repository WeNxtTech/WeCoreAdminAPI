package com.maan.eway.common.service.impl;

import static org.junit.Assume.assumeFalse;

import java.text.DecimalFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dozer.DozerBeanMapper;

import com.google.gson.Gson;
import com.maan.eway.bean.CoverDetails;
import com.maan.eway.bean.EserviceCustomerDetails;
import com.maan.eway.bean.EserviceMotorDetails;
import com.maan.eway.bean.FactorRateRequestDetails;
import com.maan.eway.bean.HomePositionMaster;
import com.maan.eway.bean.MotorDataDetails;
import com.maan.eway.bean.MsCommonDetails;
import com.maan.eway.bean.MsCustomerDetails;
import com.maan.eway.bean.MsVehicleDetails;
import com.maan.eway.bean.PersonalInfo;
import com.maan.eway.common.req.CoverIdsReq;
import com.maan.eway.common.req.QuoteThreadReq;
import com.maan.eway.common.req.VehicleIdsReq;
import com.maan.eway.repository.CoverDetailsRepository;
import com.maan.eway.repository.EServiceMotorDetailsRepository;
import com.maan.eway.repository.EserviceCustomerDetailsRepository;
import com.maan.eway.repository.FactorRateRequestDetailsRepository;
import com.maan.eway.repository.HomePositionMasterRepository;
import com.maan.eway.repository.MotorDataDetailsRepository;
import com.maan.eway.repository.PersonalInfoRepository;

public class QuoteThreadCall implements Callable<Object>  {
	
	private Logger log = LogManager.getLogger(getClass());
	
	Gson json = new Gson();
	
	private String type;
	private QuoteThreadReq request ;
	private EntityManager em;
	
	private EserviceCustomerDetailsRepository eserCustRepo ;
	private EServiceMotorDetailsRepository eserMotRepo ;
	private FactorRateRequestDetailsRepository facRateRepo ;
	
	private PersonalInfoRepository perInfoRepo ;
	private MotorDataDetailsRepository motorRepo ;
	private CoverDetailsRepository coverRepo ;
	private HomePositionMasterRepository homeRepo ;
	
	
	
	public QuoteThreadCall(String type , QuoteThreadReq request , EntityManager em ,EserviceCustomerDetailsRepository eserCustRepo ,
			EServiceMotorDetailsRepository eserMotRepo  ,FactorRateRequestDetailsRepository facRateRepo  ,PersonalInfoRepository perInfoRepo  , MotorDataDetailsRepository motorRepo , 
			 CoverDetailsRepository coverRepo  , HomePositionMasterRepository homeRepo) {
		this.type = type;
		this.request = request;
		this.em=em;
		this.eserCustRepo = eserCustRepo ;
		this.eserMotRepo = eserMotRepo ;
		this.facRateRepo = facRateRepo ;
		this.perInfoRepo = perInfoRepo ;
		this.motorRepo = motorRepo ;
		this.coverRepo = coverRepo ;
		this.homeRepo = homeRepo ;
		
		
	}
	
	@Override
	public  Map<String, Object>  call() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		try {

			type = StringUtils.isBlank(type) ? "" : type;

			log.info("Thread_OneTime--> type: " + type);

			if (type.equalsIgnoreCase("CustomerSave")) {

				map.put("CustomerSave", call_CustomerSave(request));

			} else if (type.equalsIgnoreCase("MotorSave")) {

				map.put("MotorSave", call_MotorSave(request));

			} else if (type.equalsIgnoreCase("CoverSave")) {

				map.put("CoverSave", call_CoverSave(request));

			} else if (type.equalsIgnoreCase("QuoteSave")) {

				map.put("QuoteSave", call_QuoteSave(request));

			}

		} catch (Exception e) {
			log.error(e);
		}
		return map;
	}
	
	private String call_CustomerSave(QuoteThreadReq request) {
		String res= "" ;
		DozerBeanMapper dozerMapper = new DozerBeanMapper();
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<String> query = cb.createQuery(String.class);
			
			// Find All
			Root<EserviceMotorDetails> m = query.from(EserviceMotorDetails.class);

			// Select
			query.select(m.get("customerReferenceNo")).distinct(true);
			Predicate n1 = cb.equal (m.get("requestReferenceNo"), request.getRequestReferenceNo());		
			Predicate n2 = cb.equal (m.get("vehicleId"), request.getVehicleIdsList().get(0).getVehicleId());
			
			query.where(n1,n2) ;
			// Get Result
			TypedQuery<String> result = em.createQuery(query);
			List<String> list = result.getResultList();
			
			String customerRefNo = list.get(0);
			
			// Find Customer
			EserviceCustomerDetails custData = eserCustRepo.findByCustomerReferenceNo(customerRefNo);
			
			// Save Personal INfo
			PersonalInfo personalInfo = new PersonalInfo();
			dozerMapper.map(custData, personalInfo);
			personalInfo.setCustomerId(request.getCustomerId());
			personalInfo.setEntryDate(new Date());		
			personalInfo.setCreatedBy(request.getCreatedBy());
			;
			perInfoRepo.save(personalInfo);
			
			log.error("Save Personal Info is ---> " + json.toJson(personalInfo));
			
			res= "Success";
		}catch (Exception e) {
			e.printStackTrace();
			log.error("Exception is ---> " + e.getMessage());
			return null ;
		}
	
		return res;
	}
	
	
	private String call_MotorSave(QuoteThreadReq request) {
		String res= "" ;
		DozerBeanMapper dozerMapper = new DozerBeanMapper();
		try {
			List<Integer> vehicleIds = request.getVehicleIdsList().stream().map(VehicleIdsReq :: getVehicleId ).collect(Collectors.toList());
			 
			
			// Find Motor
			List<EserviceMotorDetails> eserMotors = eserMotRepo.findByRequestReferenceNoAndVehicleIdInOrderByVehicleIdAsc(request.getRequestReferenceNo() ,vehicleIds );
			List<FactorRateRequestDetails>  covers = facRateRepo.findByRequestReferenceNoAndDiscLoadIdAndVehicleIdInOrderByVehicleIdAsc(request.getRequestReferenceNo() , 0,vehicleIds );
			
			
			// Save Motro Details
			for ( EserviceMotorDetails mot : eserMotors) {
				MotorDataDetails motorData  = new MotorDataDetails();
				dozerMapper.map(mot, motorData);
				motorData.setEntryDate(new Date());	
				motorData.setCreatedBy(request.getCreatedBy());
				motorData.setQuoteNo(request.getQuoteNo());
				motorData.setCustomerId(request.getCustomerId());
				
				List<FactorRateRequestDetails>  filterCover = covers.stream().filter( o -> o.getVehicleId().equals(mot.getVehicleId() )).collect(Collectors.toList());
				motorData.setVdRefno(filterCover.get(0).getVdRefno());	
				motorData.setMsRefno(filterCover.get(0).getMsRefno());		
				motorData.setCdRefno(filterCover.get(0).getCdRefno());				
				
				motorRepo.save(motorData);
				log.error("Save Motor Info is ---> " + json.toJson(motorData));
				
			}
	
			res= "Success";
			
		}catch (Exception e) {
			e.printStackTrace();
			log.error("Exception is ---> " + e.getMessage());
			return null ;
		}
	
		return res;
	}
	
	
	private String call_CoverSave(QuoteThreadReq request) {
		String res= "" ;
		try {
			// Find Covers
			List<Integer> vehicleIds = request.getVehicleIdsList().stream().map(VehicleIdsReq :: getVehicleId ).collect(Collectors.toList());
			 
			
			// Find Motor
			List<FactorRateRequestDetails>  covers = facRateRepo.findByRequestReferenceNoAndVehicleIdInOrderByVehicleIdAsc(request.getRequestReferenceNo() , vehicleIds );

			for ( VehicleIdsReq vehReq :  request.getVehicleIdsList()) {
				
				if ( vehReq.getCoverIdList() != null && vehReq.getCoverIdList().size() > 0 ) {
					
					for ( CoverIdsReq covReq :  vehReq.getCoverIdList()) {
						
						if (covReq.getSubCoverYn().equalsIgnoreCase("N") ) {
							List<FactorRateRequestDetails> filerCover = covers.stream().filter(  o -> o.getVehicleId().equals(vehReq.getVehicleId()) &&
									o.getCoverId().equals(covReq.getCoverId()) && o.getSubCoverId().equals(covReq.getCoverId()) ).collect(Collectors.toList());
						
							res = InsertCoverDetails(filerCover);
							
						}else {
							
								List<FactorRateRequestDetails> filerSubCover = covers.stream().filter(  o -> o.getCoverId().equals(covReq.getCoverId()) && o.getSubCoverId().equals(Integer.valueOf(covReq.getSubCoverId()))).collect(Collectors.toList());
								
								res = InsertCoverDetails(filerSubCover);
							
						}
					}
				}
				
			}
			res= "Success";
		}catch (Exception e) {
			e.printStackTrace();
			log.error("Exception is ---> " + e.getMessage());
			return null ;
		}
	
		return res;
	}
		

		private String InsertCoverDetails(List<FactorRateRequestDetails> covers) {
			String res= "" ;
			DozerBeanMapper dozerMapper = new DozerBeanMapper();
			try {
				// Save Cover Details
				for ( FactorRateRequestDetails cov : covers) {
					CoverDetails coverData  = new CoverDetails();
					dozerMapper.map(cov, coverData);
					coverData.setEntryDate(new Date());	
					coverData.setCreatedBy(request.getCreatedBy());
					coverData.setQuoteNo(request.getQuoteNo());
					coverRepo.save(coverData);	
					log.error("Save Motor Info is ---> " + json.toJson(coverData));
					
				}
		
				res= "Success";
			}catch (Exception e) {
				e.printStackTrace();
				log.error("Exception is ---> " + e.getMessage());
				return null ;
			}
		
			return res;
		}
		
	private QuoteThreadRes call_QuoteSave(QuoteThreadReq request) {
		QuoteThreadRes res= new QuoteThreadRes() ;
		String pattern = "#####0.00";
		DecimalFormat df = new DecimalFormat(pattern);
		try {
			List<Integer> vehicleIds = request.getVehicleIdsList().stream().map(VehicleIdsReq :: getVehicleId ).collect(Collectors.toList());
			// Find Motor Data
			List<EserviceMotorDetails> eserMotors = eserMotRepo.findByRequestReferenceNoAndVehicleIdInOrderByVehicleIdAsc(request.getRequestReferenceNo() ,vehicleIds );
			
			EserviceMotorDetails motorData = eserMotors.get(0);
			
			EserviceCustomerDetails customerData = eserCustRepo.findByCustomerReferenceNo(motorData.getCustomerReferenceNo());			
			// Find Covers
			List<FactorRateRequestDetails>  covers = facRateRepo.findByRequestReferenceNoAndDiscLoadIdOrderByVehicleIdAsc(request.getRequestReferenceNo() , 0);
			
			
			// Save Home Position Master
			HomePositionMaster home = new HomePositionMaster();
			home.setQuoteNo(request.getQuoteNo());
			home.setRequestReferenceNo(request.getRequestReferenceNo());
			home.setCustomerId(request.getCustomerId());
			home.setCompanyId(motorData.getCompanyId());
			home.setBranchCode(motorData.getBranchCode());
			home.setProductId(Integer.valueOf(motorData.getProductId()));
			home.setSectionId(Integer.valueOf(motorData.getSectionId()));
			home.setProposalNo("");
			home.setAmendId(0);
			home.setLoginId(request.getCreatedBy());
			home.setApplicationId("");
			home.setApplicationNo(0L);
			home.setAgencyCode(Integer.valueOf(request.getAgencyCode()));
			home.setAcExecutiveId(null);
			home.setStatus("Y");
			home.setQuoteCreatedDate(new Date());
			home.setEntryDate(new Date());
			home.setInceptionDate(motorData.getPolicyStartDate());
			home.setExpiryDate(motorData.getPolicyEndDate());
			home.setLapsedDate(null);
			home.setLapsedRemarks(null);
			home.setLapsedUpdatedBy(null);
			home.setCurrency(motorData.getCurrency());
			home.setRemarks("");
			home.setVehicleNo(vehicleIds.size());
			
			//Double sum_ExcessCost=getList.stream().filter(o -> Double.valueOf(o.getExcessAmount()).doubleValue()>0D).mapToDouble(o->Double.valueOf(o.getExcessAmount()).doubleValue()).sum();
			Double premiumFc = covers.stream().filter( o -> o.getDiscLoadId().equals(0) && o.getPremiumExcludedTaxFc()!=null && o.getPremiumExcludedTaxFc().doubleValue() > 0D ).mapToDouble( o ->   o.getPremiumExcludedTaxFc()  ).sum();					
			Double overAllPremiumFc = covers.stream().filter( o -> o.getDiscLoadId().equals(0) && o.getPremiumIncludedTaxFc()!=null && o.getPremiumIncludedTaxFc().doubleValue() > 0D ).mapToDouble( o ->   o.getPremiumIncludedTaxFc()  ).sum();
			Double vatPremiumFc = overAllPremiumFc - premiumFc ;  
			Double vatPercent =  (vatPremiumFc*100) / premiumFc ;
			
			Double premiumLc = covers.stream().filter( o -> o.getDiscLoadId().equals(0) && o.getPremiumExcludedTaxLc()!=null && o.getPremiumExcludedTaxLc().doubleValue() > 0D ).mapToDouble( o ->   o.getPremiumExcludedTaxLc()  ).sum();					
			Double overAllPremiumLc = covers.stream().filter( o -> o.getDiscLoadId().equals(0) && o.getPremiumIncludedTaxLc()!=null && o.getPremiumIncludedTaxLc().doubleValue() > 0D ).mapToDouble( o ->   o.getPremiumIncludedTaxLc()  ).sum();
			Double vatPremiumLc = overAllPremiumLc - premiumLc ;  
			
			// No OF Vehicles
			eserMotors.sort(Comparator.comparing(EserviceMotorDetails :: getVehicleId).reversed()) ; ;
			home.setNoOfVehicles(eserMotors.get(0).getVehicleId());
			
			home.setPremiumFc(Double.valueOf(df.format(premiumFc)) );
			home.setOverallPremiumFc(Double.valueOf(df.format(overAllPremiumFc)));
			home.setVatPremiumFc(Double.valueOf(df.format(vatPremiumFc)));
			home.setVatPercent(Double.valueOf(df.format(vatPercent)));
			home.setPremiumLc(Double.valueOf(df.format(premiumLc)) );
			home.setOverallPremiumLc(Double.valueOf(df.format(overAllPremiumLc)));
			home.setVatPremiumLc(Double.valueOf(df.format(vatPremiumLc)));
			home.setFinalizeYn("N");
			home.setMobile(customerData.getMobileNo1());
			home.setEmail(customerData.getEmail1());
			
			homeRepo.save(home);
			
	/*		home.setExcessSign(null);
			home.setExcessPremium(null);
			home.setDiscountPremium(null);
			home.setPolicyFee(null);
			home.setOtherFee(null);
			home.setCommission(null);
			home.setCommissionPercentage(null);
			home.setVatCommission(null);
			home.setCalcPremium(null);
			home.setAdminReferralStatus(null);
			home.setAdminReferralStatus(null);
			home.setReferralDescription(null);
			home.setApprovedBy(null);
			home.setApprCanBy(null); */
			
			
			log.error("Save Motor Info is ---> " + json.toJson(home));
			
			// Response 
			res.setCustomerId(request.getCustomerId());
			res.setQuoteNo(request.getQuoteNo());
			res.setRequestReferenceNo(request.getRequestReferenceNo());
			
		}catch (Exception e) {
			e.printStackTrace();
			log.error("Exception is ---> " + e.getMessage());
			return null ;
		}
	
		return res;
	}

	

	

	

	
}
