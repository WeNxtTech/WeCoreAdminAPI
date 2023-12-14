package com.maan.eway.embedded;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maan.eway.res.CommonRes;

@RestController
@RequestMapping("/eway/embedded")
public class EmbeddedController {
	
	
	@Autowired
	private EmbeddedService service;
	
	
	@PostMapping("/getEmbeddedDetails")
	public CommonRes getEmbeddedDetails(@RequestBody EmbeddedReq req) {
		return service.getEmbeddedDetails(req);
	}
	
	@GetMapping("/getSearchType")
	public CommonRes getSearchType() {
		return service.getSearchType();
	}
	
	@PostMapping("/getProductDashBoard")
	public CommonRes getProductDashBoard(@RequestBody EmbeddedDashBoardReq req) {
		return service.getProductDashBoard(req);
	}

	@PostMapping("/getProductPlanTypeDashBoard")
	public CommonRes getProductPlanTypeDashBoard(@RequestBody EmbeddedDashBoardReq req) {
		return service.getProductPlanTypeDashBoard(req);
	}
	
	@PostMapping("/getActivePolicy")
	public CommonRes getActivePolicy(@RequestBody EmbeddedDashBoardReq req) {
		return service.getActivePolicy(req);
	}

	@PostMapping("/getAllPolicy")
	public CommonRes getAllPolicy(@RequestBody EmbeddedDashBoardReq req) {
		return service.getAllPolicy(req);
	}

	@PostMapping("/getExpiredPolicy")
	public CommonRes getExpiredPolicy(@RequestBody EmbeddedDashBoardReq req) {
		return service.getExpiredPolicy(req);
	}
	
	@PostMapping("/get/whatsapp/vehicle")
	public Map<String,String>  getWhatsAppVehicle(@RequestBody Map<String,Object> req) {
		return service.getWhatsAppVehicle(req);
	}
}
