package com.maan.eway.ttrncloseing.service.impl;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.maan.eway.bean.HomePositionMaster;
import com.maan.eway.common.res.CommonRes;
import com.maan.eway.repository.HomePositionMasterRepository;
import com.maan.eway.ttrncloseing.bean.TTrnClosing;
import com.maan.eway.ttrncloseing.dto.HomePositionReq;
import com.maan.eway.ttrncloseing.dto.TtrnReq;
import com.maan.eway.ttrncloseing.repository.TTrnClosingRepo;
import com.maan.eway.ttrncloseing.service.TTrnCloseingService;

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
			ttrn = ttrnrepo.findByBranchCodeAndProductCoreCode(req.getBranchCode(), req.getProductCoreCode());
			if (ttrn == null) {
				ttrn = new TTrnClosing();
			}
			LocalDate startDate = req.getDateOpened().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			LocalDate endDate = req.getCloDateClosed().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			ttrn.setTranCode(1);
			ttrn.setDateClosed(req.getCloDateClosed()); // current date
			ttrn.setPreparedDt(req.getPreparedDt()); // current date
			ttrn.setDateOpened(req.getDateOpened()); // current date
			ttrn.setMonthendDt(calculateMonthEnd(startDate,endDate));
			ttrn.setRemarks(req.getRemarks());
			ttrn.setPreparedBy(StringUtils.isBlank(req.getPreparedBy()) ? null : Integer.valueOf(req.getPreparedBy()));
			ttrn.setModifiedBy(req.getModifiedBy());
			ttrn.setBranchCode(req.getBranchCode());
			ttrn.setProductCoreCode(req.getProductCoreCode());
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
			ttrn = ttrnrepo.findByBranchCodeAndProductCoreCode(req.getBranchCode(), req.getProductCoreCode());
			if(home!=null && ttrn!=null)
			{
				LocalDate startDate = ttrn.getDateOpened().toInstant()
	                    .atZone(ZoneId.systemDefault()).toLocalDate();

	            LocalDate endDate = ttrn.getDateClosed().toInstant()
	                    .atZone(ZoneId.systemDefault()).toLocalDate();
				if("Y".equalsIgnoreCase(req.getStatus()))
				{
					home.setEffectiveDate(ttrn.getMonthendDt());
					hemoRepo.save(home);
					
				}else
				{
					LocalDate today = LocalDate.now();
					if (!today.isBefore(startDate) && !today.isAfter(endDate)) {
						home.setEffectiveDate(Date.from(today.atStartOfDay(ZoneId.systemDefault()).toInstant()));
						hemoRepo.save(home);
				    }
					else
					{
						home.setEffectiveDate(ttrn.getMonthendDt());
						hemoRepo.save(home);
					}
					
	               
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

	

}
