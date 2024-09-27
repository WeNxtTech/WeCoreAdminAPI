package com.maan.eway.common.service.impl;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.dozer.DozerBeanMapper;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.maan.eway.batch.repository.TirraErrorHistoryRepository;
import com.maan.eway.bean.HomePositionMaster;
import com.maan.eway.bean.LoginMaster;
import com.maan.eway.bean.MailDataDetails;
import com.maan.eway.bean.MotorVehicleInfo;
import com.maan.eway.bean.PaymentDetail;
import com.maan.eway.bean.PersonalInfo;
import com.maan.eway.bean.SmsDataDetails;
import com.maan.eway.bean.TiraTrackingDetails;
import com.maan.eway.bean.TirraErrorHistory;
import com.maan.eway.common.req.DataManipulationReq;
import com.maan.eway.common.req.DeleteTiraSearchedVehicleReq;
import com.maan.eway.common.req.GetAllTirraErrorHistory;
import com.maan.eway.common.req.GetTirraEorrorHistoryReq;
import com.maan.eway.common.req.PolicyRevertReq;
import com.maan.eway.common.req.TiraGetReq;
import com.maan.eway.common.req.TiraPushedDetailsReq;
import com.maan.eway.common.req.TransactionCheckStatusReq;
import com.maan.eway.common.res.GetTirraEorrorHistoryRes;
import com.maan.eway.common.res.LastConvertedEndtPolicyRes;
import com.maan.eway.common.res.LastConvertedPolicyRes;
import com.maan.eway.common.res.LastMailSentRes;
import com.maan.eway.common.res.LastSmSSentRes;
import com.maan.eway.common.res.LastTiraSearchedVehicleRes;
import com.maan.eway.common.res.LastestOnlinePaymentRes;
import com.maan.eway.common.res.LatestCashPaymentRes;
import com.maan.eway.common.res.LatestChequePaymentRes;
import com.maan.eway.common.res.LatestCreditPaymentRes;
import com.maan.eway.common.res.LatestRefundPaymentRes;
import com.maan.eway.common.res.TiraErrorHistoryTotalRes;
import com.maan.eway.common.res.TiraPusehDetailsREs2;
import com.maan.eway.common.res.TiraPushedDetailsRes;
import com.maan.eway.common.res.TiraPushedListDetailsRes;
import com.maan.eway.common.res.TransactionCheckStatusRes;
import com.maan.eway.common.service.ReportsService;
import com.maan.eway.error.Error;
import com.maan.eway.repository.HomePositionMasterRepository;
import com.maan.eway.repository.LoginMasterRepository;
import com.maan.eway.repository.MailDataDetailsRepository;
import com.maan.eway.repository.MotorVehicleInfoRepository;
import com.maan.eway.repository.PaymentDetailRepository;
import com.maan.eway.repository.PersonalInfoRepository;
import com.maan.eway.repository.SmsDataDetailsRepository;
import com.maan.eway.repository.TiraTrackingDetailsRepository;
import com.maan.eway.res.LogDetailsRes;
import com.maan.eway.res.SuccessRes2;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;

@Service
//@Transactional(noRollbackFor = { SQLException.class , SQLGrammarException.class ,UnexpectedRollbackException.class ,PersistenceException.class })
public class ReportsServiceImple implements ReportsService {
  
	@Autowired
	private TirraErrorHistoryRepository repo;
	
	@Autowired
	private TiraTrackingDetailsRepository tiraRepo;

	@Autowired
	private MotorVehicleInfoRepository motVehRepo;

	@Autowired
	private SmsDataDetailsRepository smsRepo ;
	
	@Autowired
	private MailDataDetailsRepository mailRepo ;
	
	@Autowired
	private HomePositionMasterRepository homeRepo ;
	
	@Autowired
	private PersonalInfoRepository personalRepo ;
	
	@Autowired
	private PaymentDetailRepository paymentRepo ;
	
	@Autowired
	private LoginMasterRepository loginRepo ;
	
	@PersistenceContext
	private EntityManager em;
	
	@Value("${datamanipulation.password}")
	private String datamanipulationPassword;
	
	@Override
	public TiraErrorHistoryTotalRes getTirraEorrorHistory(GetTirraEorrorHistoryReq req) {
		TiraErrorHistoryTotalRes res2 = new TiraErrorHistoryTotalRes();
		List<GetTirraEorrorHistoryRes> response = new ArrayList<GetTirraEorrorHistoryRes>();
		DozerBeanMapper mapper = new DozerBeanMapper();
		
		try {
			// Limit , Offset
			int limit = StringUtils.isBlank(req.getLimit()) ? 0 : Integer.valueOf(req.getLimit());
			int offset = StringUtils.isBlank(req.getOffset()) ? 100 : Integer.valueOf(req.getOffset());
			
			Pageable paging = PageRequest.of(limit, offset, Sort.by("entryDate").descending());
			
			Page<TirraErrorHistory> list = repo.findByReqRegNumberAndEntryDateBetween(paging, req.getRegistrationNumber(), req.getStartDate(), req.getEndDate());
						
			if(list.getContent().size()>0) {
				for(TirraErrorHistory data : list.getContent()) {
					GetTirraEorrorHistoryRes res = new GetTirraEorrorHistoryRes();
					mapper.map(data, res);
					res.setRegistrationNumber(data.getReqRegNumber());
					
					if(data.getResponseStatus().equalsIgnoreCase("TIRA001"))
						res.setResponseStatusDesc("Successfull");
					else
						res.setResponseStatusDesc("Failed");
					
					response.add(res);
				}
			}
			
			res2.setTiraHistory(response);
			res2.setTotalCount(response==null ?0L :Long.valueOf(response.size()) );
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		return res2;
	}

	@Override
	public TiraErrorHistoryTotalRes getAllTirraErrorHistory(GetAllTirraErrorHistory req) {
		TiraErrorHistoryTotalRes res2 = new TiraErrorHistoryTotalRes();
		List<GetTirraEorrorHistoryRes> response = new ArrayList<GetTirraEorrorHistoryRes>();
		DozerBeanMapper mapper = new DozerBeanMapper();
		
		try {
			// Limit , Offset
			int limit = StringUtils.isBlank(req.getLimit()) ? 0 : Integer.valueOf(req.getLimit());
			int offset = StringUtils.isBlank(req.getOffset()) ? 100 : Integer.valueOf(req.getOffset());
			
			Pageable paging = PageRequest.of(limit, offset, Sort.by("entryDate").descending());
			
			Page<TirraErrorHistory> list = repo.findByEntryDateBetween(paging, req.getStartDate(), req.getEndDate());
			if(list.getContent().size()>0) {
				for(TirraErrorHistory data : list.getContent()) {
					GetTirraEorrorHistoryRes res = new GetTirraEorrorHistoryRes();
					mapper.map(data, res);
					res.setRegistrationNumber(data.getReqRegNumber());
					
					if(data!=null && data.getResponseStatus().equalsIgnoreCase("TIRA001"))
						res.setResponseStatusDesc("Successfull");
					else
						res.setResponseStatusDesc("Failed");
					
					response.add(res);
				}
			}

			res2.setTiraHistory(response);
			res2.setTotalCount(response==null ?0L :Long.valueOf(response.size()) );
			
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		return res2;
	
	}

	@Override
	public TiraPushedListDetailsRes getallTiraIntegrationPushedDetails(TiraPushedDetailsReq req) {
		TiraPushedListDetailsRes res2 = new TiraPushedListDetailsRes();
		List<TiraPusehDetailsREs2> reslist = new ArrayList<TiraPusehDetailsREs2>();
		DozerBeanMapper mapper = new DozerBeanMapper();
		
		try {
			// Limit , Offset
			int limit = StringUtils.isBlank(req.getLimit()) ? 0 : Integer.valueOf(req.getLimit());
			int offset = StringUtils.isBlank(req.getOffset()) ? 100 : Integer.valueOf(req.getOffset());
			
			Pageable paging = PageRequest.of(limit, offset, Sort.by("entryDate").descending());
			
			Page<TiraTrackingDetails> list = tiraRepo.findByEntryDateBetween(paging, req.getStartDate(), req.getEndDate());
			if(list.getContent().size()>0) {
				for(TiraTrackingDetails data : list.getContent()) {
					TiraPusehDetailsREs2 res = new TiraPusehDetailsREs2();
					mapper.map(data, res);
					res.setChassisNumber(data.getChassisNo());
					res.setQuoteNo(data.getPolicyNo());
					reslist.add(res);
				}
			}

			res2.setTiraHistory(reslist);
			res2.setTotalCount(reslist==null ?0L :Long.valueOf(reslist.size()) );
			
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		return res2;
	
	}

	@Override
	public List<TiraPushedDetailsRes> getTiraIntegrationPushedDetails(TiraGetReq req) {
		List<TiraPushedDetailsRes> resList = new ArrayList<TiraPushedDetailsRes>();
		DozerBeanMapper mapper = new DozerBeanMapper();
		
		try {
			
			List<TiraTrackingDetails> list = tiraRepo.findByPolicyNo(req.getQuoteNo());
			if(list.size()>0) {
				for(TiraTrackingDetails data : list) {
					TiraPushedDetailsRes res = new TiraPushedDetailsRes();
					mapper.map(data, res);
					res.setChassisNumber(data.getChassisNo());
					
					// Request File 
					if (StringUtils.isNotBlank(res.getRequestFileimgurl()) && new File(res.getRequestFileimgurl()).exists()) {
						res.setRequestFileimgurl(new GetFileFromPath(res.getRequestFileimgurl()).call().getImgUrl());
					} else
						System.out.println("Request File is Not found!!" + res.getRequestFileimgurl());
					
					// Response File 
					if (StringUtils.isNotBlank(res.getResponseFileimgurl()) && new File(res.getResponseFileimgurl()).exists()) {
						res.setRequestFileimgurl(new GetFileFromPath(res.getResponseFileimgurl()).call().getImgUrl());
					} else
						System.out.println("Response File is Not found!!" + res.getResponseFileimgurl());
					
					resList.add(res);
				}
			}
			
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		return resList;
	
	}

	@Override
	public SuccessRes2 deleteTiraSearchVehicle(DeleteTiraSearchedVehicleReq req) {
		SuccessRes2 res = new SuccessRes2();
		
		try {
			
			MotorVehicleInfo vehInfo = motVehRepo.findTop1ByResRegNumberAndSavedFromAndCompanyIdOrderByEntryDateDesc(req.getRegisterNumber(),"API", req.getInsuranceId());
			if(vehInfo!=null) {
				motVehRepo.delete(vehInfo);
				res.setSuccessId(req.getRegisterNumber());;
				res.setResponse("Deleted Successfully");
			} else {
				res.setSuccessId(req.getRegisterNumber());
				res.setResponse("No Data Found");
			}
			
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		return res;
	
	}

	@Override
	public TransactionCheckStatusRes getTransactionCheckStatusDetails(TransactionCheckStatusReq req) {
		TransactionCheckStatusRes res = new TransactionCheckStatusRes();
		DozerBeanMapper mapper = new DozerBeanMapper();
		
		try {
			// Last Tira Searched Vehicle
			TirraErrorHistory tiraSearchedVehicle = repo.findTop1ByOrderByEntryDateDesc();
			LastTiraSearchedVehicleRes lstTiraVehRes = new LastTiraSearchedVehicleRes();
			mapper.map(tiraSearchedVehicle, lstTiraVehRes) ;
			lstTiraVehRes.setRegistrationNumber(tiraSearchedVehicle.getReqRegNumber());
			
			if(tiraSearchedVehicle!=null && tiraSearchedVehicle.getResponseStatus().equalsIgnoreCase("TIRA001"))
				lstTiraVehRes.setResponseStatusDesc("Successfull");
			else
				lstTiraVehRes.setResponseStatusDesc("Failed");
			
			// Last Mail Sent Details 
			MailDataDetails mailData = mailRepo.findTop1ByOrderByPushedEntryDateDesc();
			LastMailSentRes mailRes = new LastMailSentRes(); 
			mapper.map(mailData , mailRes) ;
			
			// Last Sms Sent Details
			SmsDataDetails smsData = smsRepo.findTop1ByOrderByEntryDateDesc();
			LastSmSSentRes smsRes = new LastSmSSentRes();
			mapper.map(smsData , smsRes) ;
					
			// Last Converted Policy Details 
			LastConvertedPolicyRes  policyRes = getLastConvertedPolicy(req);
			
			// Last Converted Endt Policy Details 
			LastConvertedEndtPolicyRes endtPolicyRes = getLastConvertedEndtPolicy(req);
			
			// Last Online Payment
			PaymentDetail onlineData = paymentRepo.findTop1ByPaymentTypedescIgnoreCaseAndPaymentsIgnoreCaseAndCompanyIdOrderByUpdatedDateDesc("Online","Charge",req.getInsuranceId());
			LastestOnlinePaymentRes onlineRes = new LastestOnlinePaymentRes();
			if(onlineData!=null)  mapper.map(onlineData , onlineRes) ;
			
			// Last Cash Payment
			PaymentDetail cashData = paymentRepo.findTop1ByPaymentTypedescIgnoreCaseAndPaymentsIgnoreCaseAndCompanyIdOrderByUpdatedDateDesc("Cash","Charge",req.getInsuranceId());
			LatestCashPaymentRes cashRes = new LatestCashPaymentRes();
			if(cashData!=null)  mapper.map(cashData , cashRes) ;
			
			// Last Cheque Payment
			PaymentDetail chqueData = paymentRepo.findTop1ByPaymentTypedescIgnoreCaseAndPaymentsIgnoreCaseAndCompanyIdOrderByUpdatedDateDesc("Cheque","Charge",req.getInsuranceId());
			LatestChequePaymentRes chequeRes = new LatestChequePaymentRes();
			if(chqueData!=null) mapper.map(chqueData , chequeRes ) ;
			
			// Last Credit Payment
			PaymentDetail creditData = paymentRepo.findTop1ByPaymentTypedescIgnoreCaseAndPaymentsIgnoreCaseAndCompanyIdOrderByUpdatedDateDesc("Credit","Charge",req.getInsuranceId());
			LatestCreditPaymentRes creditRes = new LatestCreditPaymentRes();
			if(creditData!=null)  mapper.map(creditData , creditRes) ;
			
			// Last Refund Payment
			PaymentDetail refundData = paymentRepo.findTop1ByPaymentTypedescIgnoreCaseAndPaymentsIgnoreCaseAndCompanyIdOrderByUpdatedDateDesc("Cheque","Refund",req.getInsuranceId());
			LatestRefundPaymentRes refundRes = new LatestRefundPaymentRes();
			if(refundData!=null)  mapper.map(refundData , refundRes) ;
			
//			// Update Policy No
//			List<String> quoteNos = new ArrayList<String>();
//			quoteNos.add(onlineRes.getQuoteNo());
//			quoteNos.add(cashRes.getQuoteNo());
//			quoteNos.add(chequeRes.getQuoteNo());
//			quoteNos.add(creditRes.getQuoteNo());
//			quoteNos.add(refundRes.getQuoteNo());
//			List<HomePositionMaster> homeDatas = homeRepo.findByQuoteNoIn(quoteNos);
//			if(homeDatas.size() > 0 ) {
//				onlineRes.setPolicyNo( homeDatas.stream().filter( o -> o.getQuoteNo().equalsIgnoreCase(onlineRes.getQuoteNo()) ).collect(Collectors.toList()).size() > 0 ?
//					homeDatas.stream().filter( o -> o.getQuoteNo().equalsIgnoreCase(onlineRes.getQuoteNo()) ).collect(Collectors.toList()).get(0).getPolicyNo(): "");
//				
//				cashRes.setPolicyNo( homeDatas.stream().filter( o -> o.getQuoteNo().equalsIgnoreCase(cashRes.getQuoteNo()) ).collect(Collectors.toList()).size() > 0 ?
//					homeDatas.stream().filter( o -> o.getQuoteNo().equalsIgnoreCase(cashRes.getQuoteNo()) ).collect(Collectors.toList()).get(0).getPolicyNo(): "");
//				
//				chequeRes.setPolicyNo( homeDatas.stream().filter( o -> o.getQuoteNo().equalsIgnoreCase(chequeRes.getQuoteNo()) ).collect(Collectors.toList()).size() > 0 ?
//					homeDatas.stream().filter( o -> o.getQuoteNo().equalsIgnoreCase(chequeRes.getQuoteNo()) ).collect(Collectors.toList()).get(0).getPolicyNo(): "");
//				
//				creditRes.setPolicyNo( homeDatas.stream().filter( o -> o.getQuoteNo().equalsIgnoreCase(creditRes.getQuoteNo()) ).collect(Collectors.toList()).size() > 0 ?
//					homeDatas.stream().filter( o -> o.getQuoteNo().equalsIgnoreCase(creditRes.getQuoteNo()) ).collect(Collectors.toList()).get(0).getPolicyNo() : "");
//				
//				refundRes.setPolicyNo( homeDatas.stream().filter( o -> o.getQuoteNo().equalsIgnoreCase(refundRes.getQuoteNo()) ).collect(Collectors.toList()).size() > 0 ?
//					homeDatas.stream().filter( o -> o.getQuoteNo().equalsIgnoreCase(refundRes.getQuoteNo()) ).collect(Collectors.toList()).get(0).getPolicyNo()  : "") ;
//			}
			
			
			//Response
			res.setLatestTiraSearchedVehicle(lstTiraVehRes);
			res.setLatestSentMail(mailRes);
			res.setLatestSentSms(smsRes);
			res.setLatestConvertedPolicy(policyRes);
			res.setLatestendtPolicyRes(endtPolicyRes);
			res.setLatestOnlinePaymentRes(onlineRes);
			res.setLatestChequePaymentRes(chequeRes);
			res.setLatestCashPaymentRes(cashRes);
			res.setLatestCreditPaymentRes(creditRes);
			res.setLatestRefundPaymentRes(refundRes);
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		return res;
	
	}

	public LastConvertedPolicyRes getLastConvertedPolicy(TransactionCheckStatusReq req) {
		LastConvertedPolicyRes  policyRes = new LastConvertedPolicyRes();
	//	DozerBeanMapper mapper = new DozerBeanMapper();
		try {
					
			// Last Converted Policy Details 
			HomePositionMaster homeData = homeRepo.findTop1ByCompanyIdAndPolicyNoIsNotNullAndIntegrationStatusAndStatusAndEndtTypeIdIsNullOrderByPolicyCovertedDateDesc(req.getInsuranceId(),"S" ,"P");
			PersonalInfo   persInfo =  personalRepo.findByCustomerId(homeData.getCustomerId());
			policyRes.setAdminRemarks(homeData.getAdminRemarks());
			policyRes.setCount(homeData.getNoOfVehicles()==null ? "" :homeData.getNoOfVehicles().toString());
			policyRes.setCurrency(homeData.getCurrency());
			policyRes.setExchangeRate( homeData.getExchangeRate());
			policyRes.setCustomerId(homeData.getCustomerId());
			if(persInfo!=null ) {
				policyRes.setClientName(persInfo.getClientName());
				policyRes.setCustomerReferenceNo(persInfo.getCustomerReferenceNo());
				policyRes.setIdNumber(persInfo.getIdNumber());
				policyRes.setPolicyHolderTypeDesc(persInfo.getPolicyHoderTypeDesc());
				policyRes.setIdTypeDesc(persInfo.getIdTypeDesc());
			}
			
			policyRes.setEntryDate(homeData.getEntryDate());
			policyRes.setExchangeRate(homeData.getExchangeRate());
			policyRes.setOverallPremiumFc(homeData.getOverallPremiumFc());
			policyRes.setOverallPremiumLc(homeData.getOverallPremiumLc());
			policyRes.setPolicyStartDate(homeData.getInceptionDate());
			policyRes.setPolicyEndDate(homeData.getExpiryDate());
			policyRes.setProductName(homeData.getProductName());
			policyRes.setQuoteNo(homeData.getQuoteNo());
			policyRes.setReferalRemarks(homeData.getReferralDescription());
			policyRes.setRequestReferenceNo(homeData.getRequestReferenceNo());
			policyRes.setCoverNoteNumber(homeData.getCoverNoteNumber()==null?"" : homeData.getCoverNoteNumber().toString());
			policyRes.setTiraCoverNoteNo(homeData.getTiraCoverNoteNo());
			policyRes.setDebitNoteNo(homeData.getDebitNoteNo()==null?"":homeData.getDebitNoteNo());
			policyRes.setCreditNo(homeData.getCreditNo()==null?"":homeData.getCreditNo());
			policyRes.setPolicyNo(homeData.getPolicyNo());
			policyRes.setPolicyCovertedDate(homeData.getPolicyCovertedDate());
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		return policyRes;
	
	}
	
	public LastConvertedEndtPolicyRes getLastConvertedEndtPolicy(TransactionCheckStatusReq req) {
		LastConvertedEndtPolicyRes  policyEndtRes = new LastConvertedEndtPolicyRes();
	//	DozerBeanMapper mapper = new DozerBeanMapper();
		try {
					
			// Last Converted Policy Details 
			HomePositionMaster homeData = homeRepo.findTop1ByCompanyIdAndPolicyNoIsNotNullAndIntegrationStatusAndStatusAndEndtTypeIdIsNotNullOrderByPolicyCovertedDateDesc(req.getInsuranceId(),"S" ,"P");
			PersonalInfo   persInfo =  personalRepo.findByCustomerId(homeData.getCustomerId());
			policyEndtRes.setAdminRemarks(homeData.getAdminRemarks());
			policyEndtRes.setCount(homeData.getNoOfVehicles()==null ? "" :homeData.getNoOfVehicles().toString());
			policyEndtRes.setCurrency(homeData.getCurrency());
			policyEndtRes.setExchangeRate( homeData.getExchangeRate());
			policyEndtRes.setCustomerId(homeData.getCustomerId());
			if(persInfo!=null ) {
				policyEndtRes.setClientName(persInfo.getClientName());
				policyEndtRes.setCustomerReferenceNo(persInfo.getCustomerReferenceNo());
				policyEndtRes.setIdNumber(persInfo.getIdNumber());
				policyEndtRes.setPolicyHolderTypeDesc(persInfo.getPolicyHoderTypeDesc());
				policyEndtRes.setIdTypeDesc(persInfo.getIdTypeDesc());
			}
			
			policyEndtRes.setEntryDate(homeData.getEntryDate());
			policyEndtRes.setExchangeRate(homeData.getExchangeRate());
			policyEndtRes.setOverallPremiumFc(homeData.getOverallPremiumFc());
			policyEndtRes.setOverallPremiumLc(homeData.getOverallPremiumLc());
			policyEndtRes.setPolicyStartDate(homeData.getInceptionDate());
			policyEndtRes.setPolicyEndDate(homeData.getExpiryDate());
			policyEndtRes.setProductName(homeData.getProductName());
			policyEndtRes.setQuoteNo(homeData.getQuoteNo());
			policyEndtRes.setReferalRemarks(homeData.getReferralDescription());
			policyEndtRes.setRequestReferenceNo(homeData.getRequestReferenceNo());
			policyEndtRes.setCoverNoteNumber(homeData.getCoverNoteNumber()==null?"" : homeData.getCoverNoteNumber().toString());
			policyEndtRes.setTiraCoverNoteNo(homeData.getTiraCoverNoteNo());
			policyEndtRes.setDebitNoteNo(homeData.getDebitNoteNo()==null?"":homeData.getDebitNoteNo());
			policyEndtRes.setCreditNo(homeData.getCreditNo()==null?"":homeData.getCreditNo());
			policyEndtRes.setPolicyNo(homeData.getPolicyNo());
			policyEndtRes.setPolicyCovertedDate(homeData.getPolicyCovertedDate());
			policyEndtRes.setEndorsementDate(homeData.getEndtDate());
			policyEndtRes.setEndorsementEffdate(homeData.getEndorsementEffdate());
			policyEndtRes.setEndorsementRemarks(homeData.getEndorsementRemarks());
			policyEndtRes.setEndorsementType(homeData.getEndtTypeId());
			policyEndtRes.setEndorsementTypeDesc(homeData.getEndtTypeDesc());
			policyEndtRes.setEndtCategDesc(homeData.getEndtCategDesc());
			policyEndtRes.setEndtCount(homeData.getEndtCount()==null?"" :homeData.getEndtCount().toString() );
			policyEndtRes.setEndtPremium(homeData.getEndtPremium()==null?"":homeData.getEndtPremium().toPlainString() );
			policyEndtRes.setEndtPrevPolicyNo(homeData.getEndtPrevPolicyNo());
			policyEndtRes.setEndtPrevQuoteNo(homeData.getEndtPrevQuoteNo());
			policyEndtRes.setEndtStatus(homeData.getEndtStatus());
			
			
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		return policyEndtRes;
	
	}

	@Override
	public SuccessRes2 policyRevertToQuote(PolicyRevertReq req) {
		try {
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	
	}

	@Override
	@Transactional
	//@Transactional(noRollbackFor = { SQLException.class , SQLGrammarException.class , UnexpectedRollbackException.class ,PersistenceException.class })
	public List<Map<String, Object>> dataManipulation(DataManipulationReq req) {
		List<Map<String,Object>> list = new ArrayList<>();
		String listquery[];
		try {
			listquery=req.getQuery().split(";");
			if(listquery == null || listquery.length <= 0)
	        {
				listquery = new String[1];
				listquery[0] = req.getQuery();
	        }
			for(int q = 0; q < listquery.length; q++)
            {
                if(listquery[q] == null)
                {
                	listquery[q] = "";
                } else
                {
                	listquery[q] = listquery[q].replaceAll(";", "");
                }
				Query query=null;
				query = em.createNativeQuery(listquery[q]);
				if( listquery[q].toLowerCase().contains("select")) {
					query.unwrap(NativeQuery.class)
		            .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		            list = query.getResultList();
				}else {
					query.executeUpdate();
				}
            }
		
		} catch(Exception e ) {
		   e.printStackTrace();
		   return null;
		} 
		return list;
	}

	@Override
	public List<Error> validatedataManipulation(DataManipulationReq req) {
		List<Error> errorList = new ArrayList<Error>();
		LoginMaster loginData = null ;
		try {
			if (StringUtils.isBlank(req.getUserId())) {
				errorList.add(new Error("01", "UserId", "Please Enter UserId"));
			}else if(StringUtils.isNotBlank(req.getUserId())) {
				loginData = loginRepo.findByLoginId(req.getUserId()) ;
			}
//			
			if (StringUtils.isBlank(req.getQuery())) {
				errorList.add(new Error("02", "Query", "Please Enter Valid Query"));
			} else if( !req.getQuery().toLowerCase().contains("select")) {
				if(loginData!=null && !"Y".equals(loginData.getDmlYN()))
				errorList.add(new Error("02", "Query", "Cannot Run Insert/Delete/Update Query"));
			}
			
//			if (StringUtils.isBlank(req.getPassword())) {
//				errorList.add(new Error("02", "Password", "Please Enter Password"));
//				
//			} else if (! datamanipulationPassword.equalsIgnoreCase(req.getPassword())) {
//				errorList.add(new Error("02", "Password", "Please Enter Valid Password"));
//			}
//			
		} catch (Exception e) {
			//log.error(e);
			e.printStackTrace();
		}
		return errorList;
	}

	//@Override
	public List<LogDetailsRes> LogDetails1(String filePath) {
		List<LogDetailsRes> resp=new ArrayList<>();
		filePath = filePath.substring(0, filePath.lastIndexOf("\\"));
		filePath = filePath.substring(0, filePath.lastIndexOf("\\"));
		filePath = filePath.substring(0, filePath.lastIndexOf("\\"));
		filePath = filePath +"\\logs";
		System.out.println(filePath);
		File file = new File(filePath);
		if(file.exists() && file.isDirectory()){
			File[] files = file.listFiles();
			/*Arrays.sort(files, new Comparator<File>()
            {
                public int compare(File f1, File f2)
                {
                    return Long.valueOf(f2.lastModified()).compareTo
                            (
                            f1.lastModified());
                }
            });
            //System.out.println(Arrays.asList(files));
            for(int i=0, length=Math.min(files.length, 3); i<length; i++) {
            	System.out.println(files[i]);
        
            }*/
			
			
			
			
			
			for(int i=0;i<files.length;i++){
				LogDetailsRes res=new LogDetailsRes();
				res.setFileName(files[i].getName());
				res.setFilePath(filePath+"\\"+files[i].getName());
				resp.add(res);
			}
				
		}
		return resp;
	}
	
	public List<LogDetailsRes> LogDetails(String filePath) {
		List<LogDetailsRes> resp=new ArrayList<>();
		try {
		//filePath = filePath.substring(0, filePath.lastIndexOf("\\"));
		//filePath = filePath.substring(0, filePath.lastIndexOf("\\"));
		//filePath = filePath.substring(0, filePath.lastIndexOf("\\"));
		//filePath = filePath +"/logs";
		System.out.println(filePath);
		File file = new File(filePath);
		if(file.exists() && file.isDirectory()){
			Path dir = Paths.get(filePath);
	        LocalDateTime tenDaysAgo = LocalDateTime.now().minus(10, ChronoUnit.DAYS);
	        Instant tenDaysAgoInstant = tenDaysAgo.atZone(ZoneId.systemDefault()).toInstant();
	        try (Stream<Path> files = Files.list(dir)) {
	        	Stream<Path> files1=   files.filter(path -> { 
	                try {
	                    // Get file attributes
	                    BasicFileAttributes attrs = Files.readAttributes(path, BasicFileAttributes.class);
	                    Instant fileTime = attrs.lastModifiedTime().toInstant();
	                   // System.out.println(""+fileTime) ;                  // Check if the file was modified in the last 10 days
	                    return fileTime.isAfter(tenDaysAgoInstant);
	                } catch (Exception e) {
	                    e.printStackTrace();
	                    return false;
	                }
	            });
	        	List<Path> pathList = files1.collect(Collectors.toList());
	        	for (Path path : pathList) {
	        		LogDetailsRes res=new LogDetailsRes();
					res.setFileName(path.getFileName().toString());
					res.setFilePath(path.toString());
					resp.add(res);
	        	   // System.out.println(path);
	        	}
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
		}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return resp;
    }
}
