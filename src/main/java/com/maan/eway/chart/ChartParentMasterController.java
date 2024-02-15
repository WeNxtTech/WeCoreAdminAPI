package com.maan.eway.chart;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maan.eway.res.CommonRes;

@RestController
@RequestMapping("/chart")
public class ChartParentMasterController {
	
	
	@Autowired
	private ChartParentMasterService service;
	
	@PostMapping("/parent/insert")
	public CommonRes parentSave(@RequestBody ChartParentRequest req) {
		return service.parentSave(req);
	}
	
	
	@GetMapping("/getAllChartByCompanyId/{companyId}")
	public CommonRes getAllChartByCompanyId(@PathVariable("companyId") Integer companyId) {
		return service.getAllChartByCompanyId(companyId);
	}
	
	@PostMapping("/edit/chartDetails")
	public CommonRes editChartDetails(@RequestBody EditChartDetailsReq req) {
		return service.editChartDetails(req);
	}
	
	@GetMapping("/accountType/dropdown")
	public CommonRes accountType() {
		return service.accountType();
	}
	
	@GetMapping("/characterstic/dropdown")
	public CommonRes characterstic() {
		return service.characterstic();
	}
	
	//@PostMapping("/childChartInsert")
	public CommonRes childChartInsert(@RequestBody List<ChartChildRequest> req) {
		return service.childChartInsert(req);
	}

	@PostMapping("/getAllChildChartInsert")
	public CommonRes getAllChildChartInsert(@RequestBody GetAllChildChartRequest req) {
		return service.getAllChildChartInsert(req);
	}

	@PostMapping("/edit/childchart")
	public CommonRes editChildChart(@RequestBody EditChartChildRequest req) {
		return service.editChildChart(req);
	}
	
	@GetMapping("/policy/credit/debit/entry/{quoteNo}")
	public CommonRes policyCredittDebitEntry(@PathVariable("quoteNo") String quoteNo) {
		return service.policyCredittDebitEntry(quoteNo);
	}
	
	@PostMapping("/childChartInsert")
	public CommonRes childChartInsert1(@RequestBody ChildChartInfoReq req) {
		return service.childChartInsert1(req);
	}
		
}
