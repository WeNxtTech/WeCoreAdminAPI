package com.maan.eway.ttrncloseing.service.impl;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
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

	@Autowired
	private TTrnClosingRepo ttrnrepo;
	
	@Autowired
	private HomePositionMasterRepository hemoRepo;
	
	@Override
	public CommonRes insertTTRNDetails(TtrnReq req) {
		CommonRes res = new CommonRes();
		try {
			TTrnClosing ttrn = new TTrnClosing();
		//	ttrn = ttrnrepo.findByBranchCodeAndProductIdAndCompanyidAndTranCode(req.getBranchCode(), req.getProductCoreCode(),req.getCompanyId(),req.get);
			if (ttrn == null) {
				ttrn = new TTrnClosing();
			}
			LocalDate startDate = req.getDateOpened().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			LocalDate endDate = req.getCloDateClosed().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			ttrn.setTranCode(1);
//			ttrn.setDateClosed(req.getCloDateClosed()); // current date
//			ttrn.setPreparedDt(req.getPreparedDt()); // current date
//			ttrn.setDateOpened(req.getDateOpened()); // current date
//			ttrn.setMonthendDt(calculateMonthEnd(startDate,endDate));
//			ttrn.setMonthendDt(req.getMonthEnddate());
			ttrn.setRemarks(req.getRemarks());
			ttrn.setPreparedBy(StringUtils.isBlank(req.getPreparedBy()) ? null : Integer.valueOf(req.getPreparedBy()));
			ttrn.setModifiedBy(req.getModifiedBy());
			ttrn.setBranchCode(req.getBranchCode());
			ttrn.setProductId(req.getProductCoreCode());
			ttrn.setSetUpMonth(null);
			ttrnrepo.save(ttrn);
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

	private Date calculateMonthEnd(LocalDate startDate, LocalDate endDate) {

		LocalDate endMonthLastDate = endDate.withDayOfMonth(endDate.lengthOfMonth());

		if (endDate.equals(endMonthLastDate)) {
			return Date.from(endDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
		}

		if (endDate.isBefore(endMonthLastDate)) { // Return first day of next month
			LocalDate nextMonthFirstDay = endDate.plusMonths(1).withDayOfMonth(1);
			return Date.from(nextMonthFirstDay.atStartOfDay(ZoneId.systemDefault()).toInstant());
		}

		LocalDate previousMonthLastDay = endDate.minusMonths(1).withDayOfMonth(endDate.minusMonths(1).lengthOfMonth());
		return Date.from(previousMonthLastDay.atStartOfDay(ZoneId.systemDefault()).toInstant());
	}

	@Override
	public CommonRes updateHPM(HomePositionReq req) {
		CommonRes res = new CommonRes();
		try
		{
			TTrnClosing ttrn = new TTrnClosing();
			HomePositionMaster home = hemoRepo.findByQuoteNo(req.getQuoteNo());
		//	ttrn = ttrnrepo.findByBranchCodeAndProductIdAndCompanyid(req.getBranchCode(), req.getProductCoreCode(),req.getCompanyId());
			if(home!=null && ttrn!=null)
			{
//				LocalDate startDate = ttrn.getDateOpened().toInstant()
//	                    .atZone(ZoneId.systemDefault()).toLocalDate();
//
//	            LocalDate endDate = ttrn.getDateClosed().toInstant()
//	                    .atZone(ZoneId.systemDefault()).toLocalDate();
				if("Y".equalsIgnoreCase(req.getStatus()))
				{
				//	home.setEffectiveDate(ttrn.getMonthendDt());
					hemoRepo.save(home);
					
				}else
				{
					LocalDate today = LocalDate.now();
//					if (!today.isBefore(startDate) && !today.isAfter(endDate)) {
//						home.setEffectiveDate(Date.from(today.atStartOfDay(ZoneId.systemDefault()).toInstant()));
//						hemoRepo.save(home);
//				    }
//					else
//					{
//				//		home.setEffectiveDate(ttrn.getMonthendDt());
//						hemoRepo.save(home);
//					}
					
	               
				}
				res.setErroCode(0);
				res.setIsError(false);
				res.setMessage("Sucess");
				
			}
			else
			{
				res.setErroCode(1);
				res.setIsError(true);
				res.setMessage("No Data Fount");
			}
		}catch(Exception e)
		{
			
		}
		
		return res;
	}

	@Override
	public CommonRes getTTrnList(TtrnGetReq req) {
		TtrnGetResForMonth resall = new TtrnGetResForMonth();
		
		List<TtrnRes> resList = new ArrayList<>();
		CommonRes resc= new CommonRes();
		DozerBeanMapper dozerMapper = new DozerBeanMapper();
		try
		{
			Date setUpMonth = java.sql.Date.valueOf(req.getSetUpMonth());
		//	List<TTrnClosing> ttrnlist = ttrnrepo.findByBranchCodeAndProductIdAndCompanyid(req.getBranchCode(), req.getProductId(),req.getCompanyId());
			TtrnGetResForMonth getmonth = findRelevantMonth(req.getBranchCode(), req.getProductId(),req.getCompanyId(),req.getSetUpMonth());	
			if(getmonth!=null) {
			List<TTrnClosing>	ttrnlist =getmonth.getTtrnlist();
			if(ttrnlist!= null)
			{
				for(TTrnClosing ttrn :ttrnlist) {
				// res = dozerMapper.map(ttrn, TtrnRes.class);
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
			resall.setResList(resList);
			resall.setStatus(getmonth.getStatus());
			resc.setCommonResponse(resall);
			resc.setMessage("Success");
			resc.setIsError(false);
		}catch(Exception e)
		{
			e.printStackTrace();
			resc.setCommonResponse(null);
			resc.setMessage("failed");
			resc.setIsError(false);
		}
		return resc;
	}
	
	

	    @PersistenceContext
	    private EntityManager em;

	   
		public TtrnGetResForMonth findRelevantMonth(String branchCode, String productId, String companyId,LocalDate setUpMonth) {

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

			currentQry.where(cb.and(cb.equal(currentRoot.get("branchCode"), branchCode),
					cb.equal(currentRoot.get("productId"), productId),
					cb.equal(currentRoot.get("companyid"), companyId),
					cb.between(currentRoot.get("setUpMonth"), currMonthStart, currMonthEnd)));

			currentQry.orderBy(cb.desc(currentRoot.get("setUpMonth")));

			List<TTrnClosing> currentMonthList = em.createQuery(currentQry).setMaxResults(1).getResultList();

			/*
			 * ========================================================= 2. IF CURRENT MONTH
			 * EXISTS → CHECK STATUS
			 * =========================================================
			 */
			if (!currentMonthList.isEmpty()) {

				TTrnClosing currentMonth = currentMonthList.get(0);
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

					prevQry.where(cb.and(cb.equal(prevRoot.get("branchCode"), branchCode),
							cb.equal(prevRoot.get("productId"), productId),
							cb.equal(prevRoot.get("companyid"), companyId),
							cb.between(prevRoot.get("setUpMonth"), prevMonthStart, prevMonthEnd)));

					prevQry.orderBy(cb.desc(prevRoot.get("setUpMonth")));

					List<TTrnClosing> prevMonthList = em.createQuery(prevQry).setMaxResults(1).getResultList();
					TTrnClosing prev = prevMonthList.get(0);
					result.add(prev);
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

					nextQry.where(cb.and(cb.equal(nextRoot.get("branchCode"), branchCode),
							cb.equal(nextRoot.get("productId"), productId),
							cb.equal(nextRoot.get("companyid"), companyId),
							cb.between(nextRoot.get("setUpMonth"), nextMonthStart, nextMonthEnd)));

					nextQry.orderBy(cb.desc(nextRoot.get("setUpMonth")));

					List<TTrnClosing> nextMonthList = em.createQuery(nextQry).setMaxResults(1).getResultList();
					TTrnClosing next = nextMonthList.get(0);
					result.add(next);
					resall.setTtrnlist(result);
					resall.setStatus("N");
				}
			}
			return resall;
		}
	    
	    
	}

	

	


