package com.maan.eway.ttrncloseing.service.impl;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.maan.eway.bean.HomePositionMaster;
import com.maan.eway.common.res.CommonRes;
import com.maan.eway.repository.HomePositionMasterRepository;
import com.maan.eway.ttrncloseing.bean.TTrnClosing;
import com.maan.eway.ttrncloseing.dto.HomePositionReq;
import com.maan.eway.ttrncloseing.dto.TtrnGetReq;
import com.maan.eway.ttrncloseing.dto.TtrnGetResForMonth;
import com.maan.eway.ttrncloseing.dto.TtrnReq;
import com.maan.eway.ttrncloseing.dto.TtrnRes;
import com.maan.eway.ttrncloseing.repository.TTrnClosingRepo;
import com.maan.eway.ttrncloseing.service.TTrnCloseingService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

@Service
public class TTrnCloseingServiceImpl implements TTrnCloseingService{

	private Logger log = LogManager.getLogger(TTrnCloseingServiceImpl.class);
	@Autowired
	private TTrnClosingRepo ttrnrepo;
	
	@Autowired
	private HomePositionMasterRepository hemoRepo;
	
	@Override
	public CommonRes insertTTRNDetails(TtrnReq req) {
		CommonRes res = new CommonRes();
		try {
			
			TTrnClosing ttrn = new TTrnClosing();
			if(StringUtils.isNotBlank(req.getTranCode())) {
				Integer tr = Integer.valueOf(req.getTranCode());
			  ttrn = ttrnrepo.findByCompanyidAndTranCode(req.getCompanyId(),tr);
			
		}
			if (ttrn == null) {
				ttrn = new TTrnClosing();
			}
			ttrn.setDateClosed(req.getCloDateClosed()); // current date
			ttrn.setPreparedDt(req.getPreparedDt()); // current date
			ttrn.setDateOpened(req.getDateOpened()); // current date
		//	ttrn.setMonthendDt(calculateMonthEnd(startDate,endDate));
			ttrn.setMonthendDt(req.getMonthEnddate());
			ttrn.setRemarks(req.getRemarks());
			ttrn.setPreparedBy(StringUtils.isBlank(req.getPreparedBy()) ? null : req.getPreparedBy());
			ttrn.setModifiedBy(req.getModifiedBy());
			ttrn.setBranchCode(req.getBranchCode());
			ttrn.setProductId(req.getProductCoreCode());
			ttrn.setCompanyid(req.getCompanyId());
			ttrn.setSetUpMonth(req.getDateOpened());
			if (req.getDateOpened() != null) {
			    ttrn.setYear(String.valueOf(req.getDateOpened().getYear()));
			}
			ttrnrepo.saveAndFlush(ttrn);
			res.setCommonResponse(ttrn);
			res.setMessage("Success");
			res.setErroCode(0);
			res.setIsError(false);
		} catch (Exception e) {
			res.setCommonResponse(null);
			res.setMessage("Failed" + e.getMessage());
			res.setErroCode(1);
			res.setIsError(true);
		}
		return res;
	}
	
	@Override
	public CommonRes getTTrnAllYear(HomePositionReq req) {
		CommonRes rescom = new CommonRes();
		List<TtrnRes> reslist = new ArrayList<TtrnRes>();
		List<TTrnClosing> list = new ArrayList<>();
		try {
			if (StringUtils.isBlank(req.getYear()) || StringUtils.isBlank(req.getCompanyId())) {
				rescom.setCommonResponse(null);
				rescom.setMessage("Please Enter Year and CompanyId");
				rescom.setErroCode(0);
				rescom.setIsError(false);
				return rescom;
			}
			
			list = ttrnrepo.findByCompanyidAndYear(req.getCompanyId(), req.getYear());
			if (list != null) {
				for (TTrnClosing ttrn : list) {
					TtrnRes res = new TtrnRes();
					res.setYear(ttrn.getYear());
					res.setDateOpened(ttrn.getDateOpened());
					res.setDateClosed(ttrn.getDateClosed());
					res.setMonthendDt(ttrn.getMonthendDt());
					res.setProductCoreCode(ttrn.getProductId());
					res.setSetUpMonth(ttrn.getSetUpMonth());
					res.setCompanyid(ttrn.getCompanyid());
					res.setBranchCode(ttrn.getBranchCode());
					res.setTranCode(ttrn.getTranCode());
					res.setRemarks(ttrn.getRemarks());
					res.setBranchCode(ttrn.getBranchCode());
					res.setProductCoreCode(ttrn.getProductId());
					reslist.add(res);
				}
				rescom.setCommonResponse(reslist);
				rescom.setMessage("Success");
				rescom.setErroCode(0);
				rescom.setIsError(false);
			} else {
				rescom.setCommonResponse(null);
				rescom.setMessage("No data available For this Year :" + req.getYear());
				rescom.setErroCode(0);
				rescom.setIsError(false);
			}

		} catch (Exception e) {
			rescom.setCommonResponse(null);
			rescom.setMessage("Failed" + e.getMessage());
			rescom.setErroCode(1);
			rescom.setIsError(true);
		}

		return rescom;
	}

	
	public CommonRes getTTrnTran(HomePositionReq req) {
		CommonRes rescom = new CommonRes();
		List<TtrnRes> reslist = new ArrayList<TtrnRes>();
		TTrnClosing ttrn = new TTrnClosing();
		try {
			if ((req.getTranId()==null) || StringUtils.isBlank(req.getCompanyId())) {
				rescom.setCommonResponse(null);
				rescom.setMessage("Please Enter TranId and CompanyId");
				rescom.setErroCode(0);
				rescom.setIsError(false);
				return rescom;
			}
			
			ttrn = ttrnrepo.findByCompanyidAndTranCode(req.getCompanyId(), req.getTranId());
			if (ttrn != null) {
				
					TtrnRes res = new TtrnRes();
					res.setYear(ttrn.getYear());
					res.setDateOpened(ttrn.getDateOpened());
					res.setDateClosed(ttrn.getDateClosed());
					res.setMonthendDt(ttrn.getMonthendDt());
					res.setProductCoreCode(ttrn.getProductId());
					res.setSetUpMonth(ttrn.getSetUpMonth());
					res.setCompanyid(ttrn.getCompanyid());
					res.setBranchCode(ttrn.getBranchCode());
					res.setTranCode(ttrn.getTranCode());
					res.setRemarks(ttrn.getRemarks());
					res.setBranchCode(ttrn.getBranchCode());
					res.setProductCoreCode(ttrn.getProductId());
					reslist.add(res);
				
				rescom.setCommonResponse(reslist);
				rescom.setMessage("Success");
				rescom.setErroCode(0);
				rescom.setIsError(false);
			} else {
				rescom.setCommonResponse(null);
				rescom.setMessage("No data available For this Year :" + req.getYear());
				rescom.setErroCode(0);
				rescom.setIsError(false);
			}

		} catch (Exception e) {
			rescom.setCommonResponse(null);
			rescom.setMessage("Failed" + e.getMessage());
			rescom.setErroCode(1);
			rescom.setIsError(true);
		}

		return rescom;
	}

	@Override
	public CommonRes updateHPM(HomePositionReq req) {
		CommonRes res = new CommonRes();
		try {
			TTrnClosing ttrn = new TTrnClosing();
			HomePositionMaster home = hemoRepo.findByQuoteNo(req.getQuoteNo());
			if (home != null) {
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
				LocalDate localDate = LocalDate.parse(req.getDate(), formatter);
				Date effectiveDate = java.sql.Date.valueOf(localDate);

				home.setEffectiveDate(effectiveDate);
				hemoRepo.save(home);
				res.setCommonResponse(home);
				res.setErroCode(0);
				res.setIsError(false);
				res.setMessage("Sucess");

			} else {
				res.setErroCode(1);
				res.setIsError(true);
				res.setMessage("No Data Fount");
			}
		} catch (Exception e) {

		}

		return res;
	}

	@Override
	public CommonRes getTTrnList(TtrnGetReq req) {
		TtrnGetResForMonth resall = new TtrnGetResForMonth();
		List<TtrnRes> resList = new ArrayList<>();
		CommonRes resc = new CommonRes();
		try {

			TtrnGetResForMonth getmonth = findRelevantMonth(req.getBranchCode(), req.getProductId(), req.getCompanyId(),
					req.getSetUpMonth());
			if (getmonth != null) {
				List<TTrnClosing> ttrnlist = getmonth.getTtrnlist();
				if (ttrnlist != null) {
					for (TTrnClosing ttrn : ttrnlist) {
						
						TtrnRes res = new TtrnRes();
						res.setDateOpened(ttrn.getDateOpened());
						res.setDateClosed(ttrn.getDateClosed());
						res.setMonthendDt(ttrn.getMonthendDt());
						res.setProductCoreCode(ttrn.getProductId());
						res.setSetUpMonth(ttrn.getSetUpMonth());
						res.setCompanyid(ttrn.getCompanyid());
						res.setBranchCode(ttrn.getBranchCode());
						res.setTranCode(ttrn.getTranCode());
						res.setRemarks(ttrn.getRemarks());
						resList.add(res);
					}
				}
			}
			if (StringUtils.isNoneBlank(req.getQuoteNo())) {
				HomePositionMaster home = hemoRepo.findByQuoteNo(req.getQuoteNo());
				String formattedDate = "";
				if (home != null) {
					if (home.getEffectiveDate() != null) {
						formattedDate = new SimpleDateFormat("dd/MM/yyyy").format(home.getEffectiveDate());
						resall.setEffDate(formattedDate);
					}
				}
			}

			resall.setResList(resList);
			resall.setStatus(getmonth.getStatus());
			resc.setCommonResponse(resall);
			resc.setMessage("Success");
			resc.setIsError(false);
		} catch (Exception e) {
			e.printStackTrace();
			resc.setCommonResponse(null);
			resc.setMessage("failed");
			resc.setIsError(false);
		}
		return resc;
	}
	
	

	    @PersistenceContext
	    private EntityManager em;

	   
		public TtrnGetResForMonth findRelevantMonth(String branchCode, String productId, String companyId,
				LocalDate setUpMonth) {
			try {
				TtrnGetResForMonth resall = new TtrnGetResForMonth();
				CriteriaBuilder cb = em.getCriteriaBuilder();
				List<TTrnClosing> result = new ArrayList<>();

				/*
				 * ========================================================= 1. FETCH CURRENT
				 * MONTH RECORD =========================================================
				 */
				CriteriaQuery<TTrnClosing> currentQry = cb.createQuery(TTrnClosing.class);
				Root<TTrnClosing> currentRoot = currentQry.from(TTrnClosing.class);

				Date currMonthStart = java.sql.Date.valueOf(setUpMonth.withDayOfMonth(1));
				Date currMonthEnd = java.sql.Date.valueOf(setUpMonth.withDayOfMonth(setUpMonth.lengthOfMonth()));

				currentQry.where(cb.equal(currentRoot.get("companyid"), companyId),
						cb.between(currentRoot.get("setUpMonth"), currMonthStart, currMonthEnd));

				currentQry.orderBy(cb.desc(currentRoot.get("setUpMonth")));

				List<TTrnClosing> currentMonthList = em.createQuery(currentQry).setMaxResults(1).getResultList();

				/*
				 * ========================================================= 2. IF CURRENT MONTH
				 * EXISTS → CHECK STATUS
				 * =========================================================
				 */
				if (!currentMonthList.isEmpty()) {

					TTrnClosing currentMonth = currentMonthList.get(0);
					currentMonth.setMonthendDt(LocalDate.now());
					result.add(currentMonth);

					/*
					 * ----------------------------------------------------- CASE 1 : CURRENT MONTH
					 * IS CLOSED → FETCH PREVIOUS MONTH
					 * -----------------------------------------------------
					 */
					if (setUpMonth.isBefore(currentMonth.getDateClosed())) {

						CriteriaQuery<TTrnClosing> prevQry = cb.createQuery(TTrnClosing.class);
						Root<TTrnClosing> prevRoot = prevQry.from(TTrnClosing.class);

						LocalDate prevMonth = setUpMonth.minusMonths(1);

						Date prevMonthStart = java.sql.Date.valueOf(prevMonth.withDayOfMonth(1));
						Date prevMonthEnd = java.sql.Date.valueOf(prevMonth.withDayOfMonth(prevMonth.lengthOfMonth()));

						prevQry.where(
								// cb.and(cb.equal(prevRoot.get("branchCode"), branchCode),
								// cb.equal(prevRoot.get("productId"), productId),
								cb.equal(prevRoot.get("companyid"), companyId),
								cb.between(prevRoot.get("setUpMonth"), prevMonthStart, prevMonthEnd));

						prevQry.orderBy(cb.desc(prevRoot.get("setUpMonth")));

						List<TTrnClosing> prevMonthList = em.createQuery(prevQry).setMaxResults(1).getResultList();
						if (!prevMonthList.isEmpty()) {
							TTrnClosing prev = prevMonthList.get(0);
							if (setUpMonth.isBefore(prev.getDateClosed())) {
								result.add(prev);
							}
						}
						resall.setTtrnlist(result);
						resall.setStatus("Y");
						return resall;
					}

					/*
					 * ----------------------------------------------------- CASE 2 : CURRENT MONTH
					 * IS OPEN → FETCH NEXT (FUTURE) MONTH
					 * -----------------------------------------------------
					 */
					else {

						CriteriaQuery<TTrnClosing> nextQry = cb.createQuery(TTrnClosing.class);
						Root<TTrnClosing> nextRoot = nextQry.from(TTrnClosing.class);

						LocalDate nextMonth = setUpMonth.plusMonths(1);

						Date nextMonthStart = java.sql.Date.valueOf(nextMonth.withDayOfMonth(1));
						Date nextMonthEnd = java.sql.Date.valueOf(nextMonth.withDayOfMonth(nextMonth.lengthOfMonth()));

						nextQry.where(
								// cb.and(cb.equal(nextRoot.get("branchCode"), branchCode),
								// cb.equal(nextRoot.get("productId"), productId),
								cb.equal(nextRoot.get("companyid"), companyId),
								cb.between(nextRoot.get("setUpMonth"), nextMonthStart, nextMonthEnd));

						nextQry.orderBy(cb.desc(nextRoot.get("setUpMonth")));

						List<TTrnClosing> nextMonthList = em.createQuery(nextQry).setMaxResults(1).getResultList();
						TTrnClosing next = nextMonthList.get(0);
						result.add(next);
						resall.setTtrnlist(result);
						resall.setStatus("N");
					}
				}

				return resall;
			} catch (Exception e) {
				e.printStackTrace();
				log.info("Log Details => findRelevantMonth" + e.getMessage());
				return null;
			}
		}



		
		
	    
	    
	}

	

	


