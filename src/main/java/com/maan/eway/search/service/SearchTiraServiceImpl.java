package com.maan.eway.search.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.maan.eway.bean.BuildingRiskDetails;
import com.maan.eway.bean.CommonDataDetails;
import com.maan.eway.bean.HomePositionMaster;
import com.maan.eway.bean.MotorDataDetails;
import com.maan.eway.bean.PersonalInfo;
import com.maan.eway.bean.SectionDataDetails;
import com.maan.eway.common.res.CommonRes;
import com.maan.eway.error.Error;
import com.maan.eway.repository.BuildingRiskDetailsRepository;
import com.maan.eway.repository.CommonDataDetailsRepository;
import com.maan.eway.repository.HomePositionMasterRepository;
import com.maan.eway.repository.MotorDataDetailsRepository;
import com.maan.eway.repository.PersonalInfoRepository;
import com.maan.eway.repository.SectionDataDetailsRepo;
import com.maan.eway.search.dto.BuildingRiskRes;
import com.maan.eway.search.dto.CustomerinfoRes;
import com.maan.eway.search.dto.HumanDetailsRes;
import com.maan.eway.search.dto.MotorDetailsRes;
import com.maan.eway.search.dto.PolicyDetailsRes;
import com.maan.eway.search.dto.QuoteDetailsDto;
import com.maan.eway.search.dto.SearchTiraReq;
import com.maan.eway.search.dto.SectionDetailsRes;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service 
public class SearchTiraServiceImpl implements  SearchTiraService{

	@Autowired
	private SectionDataDetailsRepo sectionrepo;
	
	@Autowired
	private MotorDataDetailsRepository motorrepo;
	
	@Autowired
	private PersonalInfoRepository personalrepo;
	
	@Autowired
	private HomePositionMasterRepository homerepo;
	
	@Autowired
	private BuildingRiskDetailsRepository buildingrepo;
	
	@Autowired
	private CommonDataDetailsRepository commonrepo;
	
	@PersistenceContext
	private EntityManager em;
	
	@Override
	public CommonRes searchTira(SearchTiraReq req) {
		CommonRes res = new CommonRes();
		List<Error> errors = new ArrayList<Error>();
		List<QuoteDetailsDto> dtoList = new ArrayList<>();
		Set<String> quoteNo = new HashSet<>();
		List<SectionDataDetails> section = new ArrayList<>();
		List<MotorDataDetails> motor = new ArrayList<>();
		try
		{
			
			if (StringUtils.isNotBlank(req.getCoverNoteNo()) ) {
				section = sectionrepo.findByCoverNoteReferenceNoAndCompanyId(req.getCoverNoteNo(), req.getCompanyId());
				if (section != null && !section.isEmpty()) {
					quoteNo = section.stream().map(SectionDataDetails::getQuoteNo).filter(Objects::nonNull)
							.collect(Collectors.toSet());

				} else {
					errors=setErrorMethod("10001","CoverNoteNo" ,"No records found CoverNote NO : " + req.getCoverNoteNo());
					res.setCommonResponse(null);
					res.setErroCode(0);
					res.setIsError(true);
					res.setMessage("Failed");
					res.setErrorMessage(errors);
					return res;
				}

			} else if (StringUtils.isNotBlank(req.getStickerno())) {
				section = sectionrepo.findByStickerNumberAndCompanyId(req.getStickerno(), req.getCompanyId());
				if (section != null && !section.isEmpty()) {
					quoteNo = section.stream().map(SectionDataDetails::getQuoteNo).filter(Objects::nonNull)
							.collect(Collectors.toSet());
				} else {
					errors = setErrorMethod("10002", "Stickerno", "No records found Stickerno : " + req.getStickerno());
					res.setCommonResponse(null);
					res.setErroCode(0);
					res.setIsError(true);
					res.setMessage("Failed");
					res.setErrorMessage(errors);
					return res;
				}

			} else if (StringUtils.isNotBlank(req.getChassesNo())) {
				motor = motorrepo.findByChassisNumberAndCompanyId(req.getChassesNo(), req.getCompanyId());
				if (motor != null && !motor.isEmpty()) {
					quoteNo = motor.stream().map(MotorDataDetails::getQuoteNo).filter(Objects::nonNull)
							.collect(Collectors.toSet());
				} else {
					errors = setErrorMethod("10003", "ChassisNo", "No records found ChassisNo : " + req.getChassesNo());
					res.setCommonResponse(null);
					res.setErroCode(0);
					res.setIsError(true);
					res.setMessage("Failed");
					res.setErrorMessage(errors);
					return res;
				}

			} else if (StringUtils.isNotBlank(req.getRegNo())) {
				motor = motorrepo.findByRegistrationNumberAndCompanyId(req.getRegNo(), req.getCompanyId());
				if (motor != null && !motor.isEmpty()) {
					quoteNo = motor.stream().map(MotorDataDetails::getQuoteNo).filter(Objects::nonNull)
							.collect(Collectors.toSet());
				} else {
					errors = setErrorMethod("10003", "RegistrationNumber", "No records found RegistrationNumber :" + req.getRegNo());
					res.setCommonResponse(null);
					res.setErroCode(0);
					res.setIsError(true);
					res.setMessage("Failed");
					res.setErrorMessage(errors);
					return res;
				}

			} else if (StringUtils.isNotBlank(req.getMobileNo())) {
				List<PersonalInfo> pi = personalrepo.findByMobileNo1OrMobileNo2OrMobileNo3AndMobileCode1(req.getMobileNo(), req.getMobileNo(), req.getMobileNo(), req.getMobileCode());
				Set<String> collect = pi.stream().map(PersonalInfo::getCustomerId).filter(Objects::nonNull)
						.collect(Collectors.toSet());
				if (collect != null && !collect.isEmpty()) {
					List<HomePositionMaster> homeList = homerepo.findByCustomerIdInAndCompanyIdAndStatus(collect,req.getCompanyId(),req.getPolicyOrQuote());
					if (homeList != null && !homeList.isEmpty()) {
						quoteNo = homeList.stream().map(HomePositionMaster::getQuoteNo).filter(Objects::nonNull)
								.collect(Collectors.toSet());

						for (String qu1 : quoteNo) {
							moblieSeach(dtoList, homeList, qu1);
						}
						res.setCommonResponse(dtoList);
						res.setErroCode(0);
						res.setIsError(false);
						res.setMessage("Success");
						return res;

					}
					else
					{
						errors = setErrorMethod("10004", "Mobile No", "No records found Mobile No :" + req.getMobileNo());
						res.setCommonResponse(null);
						res.setErroCode(0);
						res.setIsError(true);
						res.setMessage("Failed");
						res.setErrorMessage(errors);
						return res;
						
					}
				} else {
					errors = setErrorMethod("10004", "Mobile No", "No records found Mobile No :" + req.getMobileNo());
					res.setCommonResponse(null);
					res.setErroCode(0);
					res.setIsError(true);
					res.setMessage("Failed");
					res.setErrorMessage(errors);
					return res;
				}

			} else if (StringUtils.isNotBlank(req.getQuoteNo()))
			{
				section = sectionrepo.findByQuoteNoAndCompanyId(req.getQuoteNo(), req.getCompanyId());
				motor=motorrepo.findByQuoteNoAndCompanyId(req.getQuoteNo(), req.getCompanyId());
				quoteNo.add(req.getQuoteNo());	
			}
			
			for (String qu : quoteNo) {
				QuoteDetailsDto dto = new QuoteDetailsDto();
				if (StringUtils.isNotBlank(req.getCoverNoteNo()) || StringUtils.isNotBlank(req.getStickerno()) || StringUtils.isNotBlank(req.getQuoteNo()) ) {
					sectionSearch(section, qu, dto);

				} else if (StringUtils.isNotBlank(req.getRegNo()) || StringUtils.isNotBlank(req.getChassesNo()) || StringUtils.isNotBlank(req.getQuoteNo())) {
					motorSearch(motor, qu, dto);
				}
				dtoList.add(dto);
			}

			res.setCommonResponse(dtoList);
			res.setErroCode(0);
			res.setIsError(false);
			res.setMessage("Success");
			res.setErrorMessage(null);
			return res;

		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	private List<Error> setErrorMethod(String code,String field, String mes) {
		List<Error> errors = new ArrayList<Error>();
		try {
			Error error1 = new Error(code,field,mes);
			errors.add(error1);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return errors;
	}

	private void sectionSearch(List<SectionDataDetails> section, String qu, QuoteDetailsDto dto) {
		try {
			HomePositionMaster home = homerepo.findByQuoteNo(qu);
			List<MotorDetailsRes> motorResList = new ArrayList<>();
			List<SectionDetailsRes> secresList = new ArrayList<>();
			List<BuildingRiskRes> bulidingList = new ArrayList<>();
			List<HumanDetailsRes> humanList = new ArrayList<>();
			if (home != null) {
				setHomePosition(home, dto);
				PersonalInfo pi = personalrepo.findByCustomerId(home.getCustomerId());
				setPi(dto, pi);
			}
			List<SectionDataDetails> sec1 = section.stream().filter(t -> t.getQuoteNo().equalsIgnoreCase(qu))
					.collect(Collectors.toList());
			for (SectionDataDetails sec : sec1) {
				SectionDetailsRes secres = new SectionDetailsRes();
				secres.setStickerNumber(sec.getStickerNumber());
				secres.setCoverNoteReferenceNo(sec.getCoverNoteReferenceNo());
				secres.setResponseStatusCode(sec.getResponseStatusCode());
				secres.setQuoteNo(sec.getQuoteNo());
				secres.setTiraResponseId(sec.getTiraResponseId());
				secres.setProductId(sec.getProductId());
				secres.setProductName(sec.getProductDesc());
				secres.setSectionId(sec.getSectionId());
				secres.setSectionName(sec.getSectionDesc());
				secresList.add(secres);
			}
			dto.setSectionList(secresList);
			if ("A".equalsIgnoreCase(sec1.get(0).getProductType())) {
				List<BuildingRiskDetails> buliding = buildingrepo.findByQuoteNo(qu);
				if (buliding != null && !buliding.isEmpty()) {
					for (BuildingRiskDetails building : buliding) {
						BuildingRiskRes buildRes = new BuildingRiskRes();
						buildRes.setRiskId(building.getRiskId());
						buildRes.setCoverId(building.getCoverId() + "");
						buildRes.setQuoteNo(building.getQuoteNo());
						buildRes.setSectionId(building.getSectionId());
						buildRes.setLocationId(building.getLocationId());
						buildRes.setLocationName(building.getLocationName());
						buildRes.setBranchCode(building.getBranchCode());
						buildRes.setIndustryId(building.getIndustryId());
						buildRes.setBrokerTiraCode(building.getBrokerTiraCode());
						buildRes.setSalePointCode(building.getSalePointCode());
						buildRes.setSectionname(building.getSectionDesc());
						buildRes.setProductid(building.getProductId());
						buildRes.setProductName(building.getProductDesc());
						bulidingList.add(buildRes);
					}
					dto.setBuildinglist(bulidingList);
				}
			} else if ("M".equalsIgnoreCase(sec1.get(0).getProductType())) {

				List<MotorDataDetails> motr = motorrepo.findByQuoteNo(qu);
				if (motr != null && !motr.isEmpty()) {
					for (MotorDataDetails mot : motr) {
						MotorDetailsRes motRes = new MotorDetailsRes();
						motRes.setQuoteNo(mot.getQuoteNo());
						motRes.setCoverId(mot.getCoverId() + "");
						motRes.setChassisNumber(mot.getChassisNumber());
						motRes.setRegistrationNumber(mot.getRegistrationNumber());
						motRes.setVehicleId(mot.getVehicleId());
						motRes.setProductId(mot.getProductId());
						motRes.setSectionId(mot.getSectionId());
						motRes.setSectionname(mot.getSectionName());
						motRes.setProductName(mot.getProductName());
						motRes.setBrokerTiraCode(mot.getBrokerTiraCode());
						motRes.setSalePointCode(mot.getSalePointCode());
						motorResList.add(motRes);
					}
					dto.setMotorResList(motorResList);
				}
			} else {
				List<CommonDataDetails> com = commonrepo.findByQuoteNo(qu);
				if (com != null && !com.isEmpty()) {
					for (CommonDataDetails human : com) {
						HumanDetailsRes hum = new HumanDetailsRes();
						hum.setRiskId(human.getRiskId());
						hum.setQuoteNo(human.getQuoteNo());
						hum.setCoverId(human.getCoverId() + "");
						hum.setSectionId(human.getSectionId());
						hum.setSectionName(human.getSectionDesc());
						hum.setLocationId(human.getLocationId() + "");
						hum.setLocationName(human.getLocationName());
						hum.setBranchCode(human.getBranchCode());
						hum.setBrokerTiraCode(human.getBrokerTiraCode());
						hum.setSalePointCode(human.getSalePointCode());
						humanList.add(hum);
					}
					dto.setMotorResList(motorResList);
				}

			}

		}catch (Exception e) {
			e.printStackTrace();
			System.out.println("Exception in sectionSearch Method ->" +e.getMessage());
		}
	}

	private void motorSearch(List<MotorDataDetails> motor, String qu, QuoteDetailsDto dto) {
		HomePositionMaster home = homerepo.findByQuoteNo(qu);
		List<SectionDetailsRes> secresList = new ArrayList<>();
		 List<MotorDetailsRes> motorResList =new ArrayList<>();
		if (home != null) {
			setHomePosition(home, dto);
			PersonalInfo pi = personalrepo.findByCustomerId(home.getCustomerId());
			setPi(dto, pi);
		}
		
			List<SectionDataDetails> sec1 =sectionrepo.findByQuoteNo(qu);
			if(sec1 != null && !sec1.isEmpty()) {
			for (SectionDataDetails sec : sec1) {
				SectionDetailsRes secres = new SectionDetailsRes();
				secres.setStickerNumber(sec.getStickerNumber());
				secres.setCoverNoteReferenceNo(sec.getCoverNoteReferenceNo());
				secres.setResponseStatusCode(sec.getResponseStatusCode());
				secres.setQuoteNo(sec.getQuoteNo());
				secres.setTiraResponseId(sec.getTiraResponseId());
				secres.setProductId(sec.getProductId());
				secres.setProductName(sec.getProductDesc());
				secres.setSectionId(sec.getSectionId());
				secres.setSectionName(sec.getSectionDesc());
				
				secresList.add(secres);
			}
			dto.setSectionList(secresList);
		}
			List<MotorDataDetails> collect = motor.stream().filter(t -> t.getQuoteNo().equalsIgnoreCase(qu))
			.collect(Collectors.toList());
			for (MotorDataDetails mot : collect) {
				MotorDetailsRes motRes = new MotorDetailsRes();
				motRes.setQuoteNo(mot.getQuoteNo());
				motRes.setChassisNumber(mot.getChassisNumber());
				motRes.setRegistrationNumber(mot.getRegistrationNumber());
				motRes.setVehicleId(mot.getVehicleId());
				motRes.setProductId(mot.getProductId());
				motRes.setSectionId(mot.getSectionId());
				motRes.setBrokerTiraCode(mot.getBrokerTiraCode());
				motRes.setSalePointCode(mot.getSalePointCode());
				motRes.setSectionname(mot.getSectionName());
				motRes.setProductName(mot.getProductName());
				motorResList.add(motRes);
			}
			dto.setMotorResList(motorResList);
	}

	private void moblieSeach(List<QuoteDetailsDto> dtoList, List<HomePositionMaster> homeList, String qu1) {
		try {
			Optional<HomePositionMaster> homeOpt = homeList.stream().filter(t -> t.getQuoteNo().equalsIgnoreCase(qu1))
					.findFirst();
			QuoteDetailsDto dto = new QuoteDetailsDto();
			List<SectionDetailsRes> secresList = new ArrayList<>();
			List<MotorDetailsRes> motorResList = new ArrayList<>();
			List<BuildingRiskRes> bulidingList = new ArrayList<>();
			List<HumanDetailsRes> humanList = new ArrayList<>();
			if (homeOpt.isPresent()) {
				HomePositionMaster home = homeOpt.get();
				setHomePosition(home, dto);
				PersonalInfo pi2 = personalrepo.findByCustomerId(home.getCustomerId());
				setPi(dto, pi2);
			}
			List<SectionDataDetails> sec1 = sectionrepo.findByQuoteNo(qu1);
			if (sec1 != null && !sec1.isEmpty()) {
				for (SectionDataDetails sec : sec1) {
					SectionDetailsRes secres = new SectionDetailsRes();
					secres.setStickerNumber(sec.getStickerNumber());
					secres.setCoverNoteReferenceNo(sec.getCoverNoteReferenceNo());
					secres.setResponseStatusCode(sec.getResponseStatusCode());
					secres.setQuoteNo(sec.getQuoteNo());
					secres.setTiraResponseId(sec.getTiraResponseId());
					secres.setProductId(sec.getProductId());
					secres.setProductName(sec.getProductDesc());
					secres.setSectionId(sec.getSectionId());
					secres.setSectionName(sec.getSectionDesc());
					secresList.add(secres);
				}
				dto.setSectionList(secresList);
			}
			if ("A".equalsIgnoreCase(sec1.get(0).getProductType())) {
				List<BuildingRiskDetails> buliding = buildingrepo.findByQuoteNo(qu1);
				if (buliding != null && !buliding.isEmpty()) {
					for (BuildingRiskDetails building : buliding) {
						BuildingRiskRes buildRes = new BuildingRiskRes();
						buildRes.setRiskId(building.getRiskId());
						buildRes.setCoverId(building.getCoverId() + "");
						buildRes.setQuoteNo(building.getQuoteNo());
						buildRes.setSectionId(building.getSectionId());
						buildRes.setSectionname(building.getSectionDesc());
						buildRes.setProductid(building.getProductId());
						buildRes.setProductName(building.getProductDesc());;
						buildRes.setLocationId(building.getLocationId());
						buildRes.setLocationName(building.getLocationName());
						buildRes.setBranchCode(building.getBranchCode());
						buildRes.setIndustryId(building.getIndustryId());
						buildRes.setBrokerTiraCode(building.getBrokerTiraCode());
						buildRes.setSalePointCode(building.getSalePointCode());
						bulidingList.add(buildRes);
					}
					dto.setBuildinglist(bulidingList);
				}
			} else if ("M".equalsIgnoreCase(sec1.get(0).getProductType())) {

				List<MotorDataDetails> motr = motorrepo.findByQuoteNo(qu1);
				if (motr != null && !motr.isEmpty()) {
					for (MotorDataDetails mot : motr) {
						MotorDetailsRes motRes = new MotorDetailsRes();
						motRes.setQuoteNo(mot.getQuoteNo());
						motRes.setCoverId(mot.getCoverId() + "");
						motRes.setChassisNumber(mot.getChassisNumber());
						motRes.setRegistrationNumber(mot.getRegistrationNumber());
						motRes.setVehicleId(mot.getVehicleId());
						motRes.setProductId(mot.getProductId());
						motRes.setSectionId(mot.getSectionId());
						motRes.setBrokerTiraCode(mot.getBrokerTiraCode());
						motRes.setSalePointCode(mot.getSalePointCode());
						motRes.setSectionname(mot.getSectionName());
						motRes.setProductName(mot.getProductName());
						motorResList.add(motRes);
					}
					dto.setMotorResList(motorResList);
				}
			} else {
				List<CommonDataDetails> com = commonrepo.findByQuoteNo(qu1);
				if (com != null && !com.isEmpty()) {
					for (CommonDataDetails human : com) {
						HumanDetailsRes hum = new HumanDetailsRes();
						hum.setRiskId(human.getRiskId());
						hum.setQuoteNo(human.getQuoteNo());
						hum.setCoverId(human.getCoverId() + "");
						hum.setSectionId(human.getSectionId());
						hum.setSectionName(human.getSectionDesc());
						hum.setLocationId(human.getLocationId() + "");
						hum.setLocationName(human.getLocationName());
						hum.setBranchCode(human.getBranchCode());
						hum.setBrokerTiraCode(human.getBrokerTiraCode());
						hum.setSalePointCode(human.getSalePointCode());
						humanList.add(hum);
					}
					dto.setMotorResList(motorResList);
				}

			}

			dtoList.add(dto);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Exception in moblieSeach Method ->" + e.getMessage());
		}
	}

	private PolicyDetailsRes setHomePosition(HomePositionMaster home, QuoteDetailsDto dto) {
		PolicyDetailsRes policy = new PolicyDetailsRes();
		try {
			policy.setQuoteNo(home.getQuoteNo());
			policy.setCustomerId(home.getCustomerId());
			policy.setProductId(home.getProductId()+"");
			policy.setProductName(home.getProductName());
			policy.setNoOfVehicles(home.getNoOfVehicles());
			policy.setCustomerName(home.getCustomerName());
			policy.setPolicyNo(home.getPolicyNo());
			policy.setOriginalPolicyNo(home.getOriginalPolicyNo());
			policy.setStatus(home.getStatus());
			policy.setInceptionDate(home.getInceptionDate());
			policy.setExpiryDate(home.getExpiryDate());
			policy.setEffectiveDate(home.getEffectiveDate());
			policy.setOverallPremiumLc(home.getOverallPremiumLc());
			policy.setOverallPremiumFc(home.getOverallPremiumFc());
			policy.setBranchName(home.getBranchName());
			policy.setPaymentMode(home.getPaymentMode());
			policy.setPaymentStatus(home.getPaymentStatus());
			policy.setCompanyId(home.getCompanyId());
			policy.setBdmcode(home.getBdmCode());	
			policy.setBdmName(home.getBdmName());		
			dto.setPolicyDetails(policy);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return policy;
	}

	private void setPi(QuoteDetailsDto dto, PersonalInfo pi) {
		if (pi != null) {
			CustomerinfoRes cus = new CustomerinfoRes();
			cus.setCustomerId(pi.getCustomerId());
			cus.setCustomerReferenceNo(pi.getCustomerReferenceNo());
			cus.setClientName(pi.getClientName());
			cus.setIdNumber(pi.getIdNumber());
			cus.setAddress1(pi.getAddress1());
			cus.setCityName(pi.getCityName());
			cus.setMobileCode1(pi.getMobileCode1());
			cus.setMobileNo1(pi.getMobileNo1());
			cus.setEmail1(pi.getEmail1());
			dto.setCusInfo(cus);
			
		}
	}

}
