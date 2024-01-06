package com.maan.eway.excelconfig2;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maan.eway.batch.repository.EwayXlconfigMasterRepository;
import com.maan.eway.master.req.ColumnNameDropDownlReq;
import com.maan.eway.res.DropDownRes;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/xlconfig")
public class XLConfigMasterController {

	@Autowired
	XLConfigMasterServiceImpl service;

	@Autowired
	EwayXlconfigMasterRepository repo;

	@PostMapping("/save")
	public ResponseEntity<CommonResponse> save(@RequestBody List<XLConfigMasterSaveReq> req) {

//		List<XLConfigMasterSaveReq> saveList=service.getList(req);

//		List<List<Errors>> eList = service.validateList(saveList);

		//List<Errors> eList = Collections.emptyList();//service.validate(req);
//				
		List<Errors> errorList =service.validateConfigMaster(req);//service.validate(req);

		CommonResponse res = new CommonResponse();

		if (errorList.size() != 0) {

			res.setError(true);
			res.setMessage("Insertion Failed");
			res.setErrorMessage(errorList);
			res.setResult(null);

			return new ResponseEntity<CommonResponse>(res, HttpStatus.OK);
		} else {
			SuccessResponse sRes = service.saveList(req);

			if (sRes != null) {

				res.setError(false);
				res.setMessage("Insertion Successfull");
				res.setErrorMessage(Collections.emptyList());
				res.setResult(sRes);

				return new ResponseEntity<CommonResponse>(res, HttpStatus.CREATED);

			} else
				return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("/get")
	public ResponseEntity<CommonResponse> getByPK(@RequestBody XLConfigGetReq req) {

		CommonResponse res = new CommonResponse();

		XLConfigMasterResponse resp = service.getByPK(req);

		if (resp != null) {

			res.setError(false);
			res.setMessage("Details Found");
			res.setErrorMessage(Collections.emptyList());
			res.setResult(resp);

			return new ResponseEntity<CommonResponse>(res, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("/getall")
	public ResponseEntity<CommonResponse> getAll(@RequestBody XLConfigGetReq req) {

		CommonResponse res = new CommonResponse();

		List<XLConfigMasterResponse> resp = service.getAll(req);

		if (resp != null) {

			res.setError(false);
			res.setMessage("Details Found");
			res.setErrorMessage(Collections.emptyList());
			res.setResult(resp);

			return new ResponseEntity<CommonResponse>(res, HttpStatus.OK);
		} else {
			
			res.setError(false);
			res.setMessage("Details Not Found");
			res.setErrorMessage(Collections.emptyList());
			res.setResult(null);
			
			return new ResponseEntity<CommonResponse>(res, HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/getdatatype")
	public ResponseEntity<CommonResponse> getDataType() {

		List<DataType> dataList = service.getDataType();

		CommonResponse res = new CommonResponse();

		res.setResult(dataList);
		res.setError(false);
		res.setErrorMessage(Collections.emptyList());
		res.setMessage("Success");

		if (dataList.size() != 0) {
			return new ResponseEntity<CommonResponse>(res, HttpStatus.CREATED);
		} else {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("/columnname")
	@ApiOperation(value = "This method is to Column Name Drop Down")
	public ResponseEntity<CommonResponse> columnName(@RequestBody ColumnNameDropDownlReq req) {

		CommonResponse res = new CommonResponse();

		List<DropDownRes> dropList = service.columnName(req);

		res.setResult(dropList);
		res.setError(false);
		res.setErrorMessage(Collections.emptyList());
		res.setMessage("Success");

		if (dropList != null) {
			return new ResponseEntity<CommonResponse>(res, HttpStatus.CREATED);
		} else {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}

	}

	@PostMapping("/delete")
	public ResponseEntity<CommonResponse> deleteByPk(@RequestBody XLConfigGetReq req) {

		SuccessResponse sRes = service.deleteByPk(req);

		CommonResponse res = new CommonResponse();
		if (sRes != null) {
			res.setError(false);
			res.setErrorMessage(Collections.emptyList());
			res.setMessage("Deleted Successfully");
			res.setResult(sRes);

			return new ResponseEntity<CommonResponse>(res, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
	}

}
//	@PostMapping("/save")
//	public ResponseEntity<CommonResponse> save(@RequestBody List<Map<String, Object>> req) {
//		List<Errors> elist = new ArrayList<>();
//		for (Map<String, Object> map : req) {
//			Errors e = new Errors();
//
//			e.setCode((String)map.get("Code"));
//			e.setField((String)map.get("Field"));
//			e.setMessage((String)map.get("Message"));
//			elist.add(e);
////			LinkedHashMap<String, String> map= ;
//		}
//		CommonResponse res = new CommonResponse();
//		res.setResult(elist);
//		return new ResponseEntity<CommonResponse>(res, HttpStatus.CREATED);
//	}
