package com.maan.eway.vehicleupload;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.maan.eway.batch.entity.EserviceMotorDetailsRaw;
import com.maan.eway.batch.entity.EwayEmplyeeDetailRaw;
import com.maan.eway.batch.entity.EwayUploadTypeMaster;
import com.maan.eway.batch.entity.EwayXlconfigMaster;
import com.maan.eway.batch.entity.SqlSeqNumber;
import com.maan.eway.batch.entity.UgandaVehicleDetailsRaw;
import com.maan.eway.batch.repository.EserviceMotorDetailsRawRepository;
import com.maan.eway.batch.repository.EwayEmplyeeDetailRawRepository;
import com.maan.eway.batch.repository.EwayUploadTypeMasterRepository;
import com.maan.eway.batch.repository.EwayXlconfigMasterRepository;
import com.maan.eway.batch.repository.ProductEmployeesDetailsRepository;
import com.maan.eway.batch.repository.SeqRefnoRepository;
import com.maan.eway.batch.repository.TransactionControlDetailsRepository;
import com.maan.eway.batch.repository.UgandaVehicleDetailsRepository;
import com.maan.eway.batch.req.DeleteRecordReq;
import com.maan.eway.batch.req.EditRecordReq;
import com.maan.eway.batch.req.EmployeeUpdateReq;
import com.maan.eway.batch.req.EwayBatchReq;
import com.maan.eway.batch.req.EwayUploadReq;
import com.maan.eway.batch.req.GetRecordsReq;
import com.maan.eway.batch.req.GetUploadTransactionReq;
import com.maan.eway.batch.req.GetUploadTypeReq;
import com.maan.eway.batch.req.MotorUpdateReq;
import com.maan.eway.batch.req.MoveRecordsReq;
import com.maan.eway.batch.req.SamplFileDownloadReq;
import com.maan.eway.batch.req.SaveUploadTypeReq;
import com.maan.eway.batch.req.UpdateEmployeeRecordReq;
import com.maan.eway.batch.req.UpdateRecordReq;
import com.maan.eway.batch.res.EwayUploadRes;
import com.maan.eway.batch.res.GetEmployeeDetailsRes;
import com.maan.eway.batch.res.GetRecordsRes;
import com.maan.eway.batch.res.GetTransactionStatusRes;
import com.maan.eway.batch.res.GetUploadTransactionRes;
import com.maan.eway.batch.res.GetUploadTypeMasterRes;
import com.maan.eway.batch.res.SaveXlConfigReq;
import com.maan.eway.batch.res.XlConfigData;
import com.maan.eway.bean.ListItemValue;
import com.maan.eway.bean.ProductEmployeeDetails;
import com.maan.eway.error.Error;
import com.maan.eway.res.CommonRes;
import com.maan.eway.springbatch.FactorRateRawMasterRepository;
import com.maan.eway.springbatch.TransactionControlDetails;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import jakarta.transaction.Transactional;
import okhttp3.MediaType;


@Service
public class VehicleBatchServiceImpl implements VehicleBatchService {
	
	
	@Value("${excel.upload.path}")
	private String filePath ;
	
	@Value("${tira.api}")
	private String tiraApi;
	
	@Value("${employee.delete.api}")
	private String empDeleteApi;
	
	@Value("${employee.count.api}")
	private String employeeCountApi;
	
	@Value(value = "${EwayBasicAuthPass}")
	private String EwayBasicAuthPass;
	
	@Value(value = "${EwayBasicAuthName}")
	private String EwayBasicAuthName;
	
	@Value(value = "${SequenceGenerateUrl}")
	private String SequenceGenerateUrl;
	
	@Autowired
	private EwayEmplyeeDetailRawRepository emplyeeDetailRawRepo;;
	
	@Autowired
	private ProductEmployeesDetailsRepository employeesDetailsRepository;
	
	Logger log =LogManager.getLogger(VehicleBatchServiceImpl.class);

	@Autowired
	private VehicleCSVFileConvertion csvFileConvertion;
	@Autowired
	private EwayUploadTypeMasterRepository uploadTypeRepo;
	@Autowired
	private TransactionControlDetailsRepository transRepo;
	@Autowired
	private EserviceMotorDetailsRawRepository eserviceRepository;
	@PersistenceContext
	private EntityManager em;
	@Autowired
	private SeqRefnoRepository sequence;
	@Autowired
	private VehicleAsynchronousProcess asyncProcess;
	@Autowired
	private EwayXlconfigMasterRepository xlConfigMaster;
	
	@Autowired
	private CriteriaQueryServiceImpl criteriaQuery;
	
	@Autowired
	private FactorRateRawMasterRepository repository;
	
	@Autowired
    JobLauncher jobLauncher;
	
    @Autowired
    @Qualifier(value="vehicle_raw_job")
    Job vehicle_raw_job;
    
    @Autowired
    @Qualifier(value="veh_validation_job")
    Job veh_validation_job;
    
    @Autowired
    @Qualifier(value="veh_apicall_job")
    Job veh_apicall_job;
    
    @Autowired
    @Qualifier(value="nonmot_raw_job")
    Job nonmot_raw_job;
    
    @Autowired
    @Qualifier(value="nonmot_validation_job")
    Job nonmot_validation_job;
    
    @Autowired
    private EwayEmplyeeDetailRawRepository employeeRawRepo;
    @Autowired
    private VehicleInputValidation validation;
    
    @Autowired
    private UgandaVehicleDetailsRepository ugandaVehicleDetailsRepo;
    
    @Autowired 
    private Gson printReq;
  
    private static  MediaType mediaType =MediaType.parse("application/json");

    private static SimpleDateFormat sdf =new SimpleDateFormat("dd/MM/yyyy");
    
	@Override
	public CommonRes batchUpload(EwayUploadReq req,String token) {
		EwayUploadRes uploadRes =new EwayUploadRes();
		CommonRes response =new CommonRes();
		try {
			String endorsmentType =StringUtils.isBlank(req.getEndorsementYn())?"":req.getEndorsementYn();
			
			uploadRes.setEndorsementYn(endorsmentType);
			uploadRes.setTypeId(req.getTypeId());
			uploadRes.setProductId(req.getProductId());
			uploadRes.setCompanyId(req.getCompanyId());
			uploadRes.setProgressStatus("P");
			uploadRes.setProgressdesc("Uploading...");
			//uploadRes.setRequestReferenceNo(StringUtils.isBlank(req.getRequestReferenceNo())?generateSeqCall(req)
					//:getTransactionId(req.getRequestReferenceNo(),req.getCompanyId(),req.getProductId()));
			
			uploadRes.setRequestReferenceNo(req.getRequestReferenceNo());
			uploadRes.setToken(token);
			uploadRes.setBrokerBranchCode(StringUtils.isBlank(req.getBrokerBranchCode())?"":req.getBrokerBranchCode());
			uploadRes.setAcExecutiveId(StringUtils.isBlank(req.getAcExecutiveId())?"":req.getAcExecutiveId());
			uploadRes.setApplicationId(StringUtils.isBlank(req.getApplicationId())?"":req.getApplicationId());
			uploadRes.setBeokerCode(StringUtils.isBlank(req.getBeokerCode())?"":req.getBeokerCode());
			uploadRes.setSourceTypeId(StringUtils.isBlank(req.getSourceTypeId())?"":req.getSourceTypeId());
			uploadRes.setBranchCode("");
			uploadRes.setCurrency(StringUtils.isBlank(req.getCurrency())?"":req.getCurrency());
			if("E".equalsIgnoreCase(req.getEndorsementYn()) && "5".equals(req.getProductId())) {
				uploadRes.setEndorsementDate(StringUtils.isBlank(req.getEndorsementDate())?"":req.getEndorsementDate());
				uploadRes.setEndorsementEffectiveDate(StringUtils.isBlank(req.getEndorsementEffectiveDate())?"":req.getEndorsementEffectiveDate());
				uploadRes.setEndorsementRemarks(StringUtils.isBlank(req.getEndorsementRemarks())?"":req.getEndorsementRemarks());
				uploadRes.setEndorsementType(StringUtils.isBlank(req.getEndorsementType())?"":req.getEndorsementType());
				uploadRes.setEndorsementTypeDesc(StringUtils.isBlank(req.getEndorsementTypeDesc())?"":req.getEndorsementTypeDesc());
				uploadRes.setEndorsementYn(StringUtils.isBlank(req.getEndorsementYn())?"":req.getEndorsementYn());
				uploadRes.setEndtCategoryDesc(StringUtils.isBlank(req.getEndtCategoryDesc())?"":req.getEndtCategoryDesc());
				uploadRes.setEndtCount(StringUtils.isBlank(req.getEndtCount())?"":req.getEndtCount());
				uploadRes.setEndtPrevPolicyNo(StringUtils.isBlank(req.getEndtPrevPolicyNo())?"":req.getEndtPrevPolicyNo());
				uploadRes.setEndtStatus(StringUtils.isBlank(req.getEndtStatus())?"":req.getEndtStatus());
			}else if("E".equalsIgnoreCase(req.getEndorsementYn())) {
				List<ProductEmployeeDetails> employeeDetails=employeesDetailsRepository.findByQuoteNo(req.getQuoteNo());
				Integer mainTableRecordCount =employeeDetails.size();
				List<String> insertRecordsRes =employeeDetails.parallelStream()
						.map(p ->insertExistingRecordToRawTable(p,req))
						.collect(Collectors.toList());
				Boolean status =mainTableRecordCount==insertRecordsRes.size();
				log.info("batchUpload || Endorsment existing data status"+status);
				
			}
			uploadRes.setExchangeRate(StringUtils.isBlank(req.getExchangeRate())?"":req.getExchangeRate());
			uploadRes.setHavePromoCode(StringUtils.isBlank(req.getHavePromoCode())?"":req.getHavePromoCode());
			uploadRes.setOwnerCategory(StringUtils.isBlank(req.getOwnerCategory())?"":req.getOwnerCategory());
			uploadRes.setResOwnerName(StringUtils.isBlank(req.getResOwnerName())?"":req.getResOwnerName());
			
			uploadRes.setIsFinanceEndt(StringUtils.isBlank(req.getIsFinanceEndt())?"":req.getIsFinanceEndt());
			uploadRes.setLoginId(StringUtils.isBlank(req.getLoginId())?"":req.getLoginId());
			uploadRes.setNoOfVehicles(StringUtils.isBlank(req.getNoOfVehicles())?"":req.getNoOfVehicles());
			uploadRes.setOrginalPolicyNo(StringUtils.isBlank(req.getOrginalPolicyNo())?"":req.getOrginalPolicyNo());
			uploadRes.setPolicyEndDate(StringUtils.isBlank(req.getPolicyEndDate())?"":req.getPolicyEndDate());
			uploadRes.setPolicyStartDate(StringUtils.isBlank(req.getPolicyStartDate())?"":req.getPolicyStartDate());
			uploadRes.setSubUserType(StringUtils.isBlank(req.getSubUserType())?"":req.getSubUserType());
			uploadRes.setCustomerRefNo(StringUtils.isBlank(req.getCustomerRefNo())?"":req.getCustomerRefNo());
			uploadRes.setBranchCode(StringUtils.isBlank(req.getBranchCode())?"":req.getBranchCode());
			uploadRes.setAgencyCode(StringUtils.isBlank(req.getAgencyCode())?"":req.getAgencyCode());
			uploadRes.setIdnumber(StringUtils.isBlank(req.getIdnumber())?"":req.getIdnumber());
			uploadRes.setUserType(StringUtils.isBlank(req.getUserType())?"":req.getUserType());
			uploadRes.setNcdYn(StringUtils.isBlank(req.getNcdYn())?"N":req.getNcdYn());
			uploadRes.setRiskId(StringUtils.isBlank(req.getRiskId())?"":req.getRiskId());
			uploadRes.setQuoteNo(StringUtils.isBlank(req.getQuoteNo())?"":req.getQuoteNo());
			uploadRes.setSourceType(StringUtils.isBlank(req.getSourceType())?"":req.getSourceType());
			uploadRes.setCustomerCode(StringUtils.isBlank(req.getCustomerCode())?"":req.getCustomerCode());
			uploadRes.setSectionId(StringUtils.isBlank(req.getSectionId())?"":req.getSectionId());
			uploadRes.setRelationId(StringUtils.isBlank(req.getRelationId())?"":req.getRelationId());
			uploadRes.setStateCode(StringUtils.isBlank(req.getStateCode())?"":req.getStateCode());
			uploadRes.setUploadType(StringUtils.isBlank(req.getUploadType())?"Add":req.getUploadType());
			uploadRes.setCustomerName(StringUtils.isBlank(req.getCustomerName())?"":req.getCustomerName());
			uploadRes.setBdmCode(StringUtils.isBlank(req.getBdmCode())?"":req.getBdmCode());
			Integer sectionId =StringUtils.isBlank(req.getSectionId())?0:Integer.valueOf(req.getSectionId());
			
			EwayUploadTypeMaster uploadTypeMaster=uploadTypeRepo.findByCompanyIdAndProductIdAndSectionIdAndStatus
					(Integer.valueOf(req.getCompanyId()),Integer.valueOf(req.getProductId()),sectionId,
					"Y");
			
			uploadRes.setTypeId(uploadTypeMaster.getTypeid().toString());
			uploadRes.setSectionId(uploadTypeMaster.getSectionId().toString());
			String csvFilePath =saveUploadTransactionData(uploadRes);
			uploadRes.setCsvfilepath(csvFilePath);
			
			VehicleThread_CSV_Convertion thread = new VehicleThread_CSV_Convertion(uploadRes,csvFileConvertion,uploadTypeMaster);
			Thread job =new Thread(thread);
			job.setName("VEHILCE_UPLOAD");
			job.setDaemon(false);
			job.start();
			
			 Map<String,String> map = new HashMap<>();
		     map.put("progress_description", "Progressing..");
		     map.put("status", "P");
		     map.put("request_reference_no",req.getRequestReferenceNo());
		        			
		     response.setMessage("Success");
		     response.setErrorMessage(Collections.EMPTY_LIST);
		     response.setIsError(false);
		     response.setCommonResponse(map);
			
		}catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			e.printStackTrace();
			response.setMessage("Failed");
		    response.setErrorMessage(Collections.EMPTY_LIST);
		    response.setIsError(true);
		    response.setCommonResponse(null);
		    return response;
		}
		
		return response;
	}

	private synchronized String getTransactionId(String requestReferenceNo, String companyId, String productId) {
		String referenceNo="";
		try {
			
			if("100019".equals(companyId) && "5".equals(productId)) {
				List<UgandaVehicleDetailsRaw> list =ugandaVehicleDetailsRepo.findByEwayReferenceNo(requestReferenceNo);
				referenceNo=list.isEmpty()?requestReferenceNo:list.get(0).getRequestReferenceNo();
			}else {
				referenceNo=requestReferenceNo;
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return referenceNo;
	}

	private String insertExistingRecordToRawTable(ProductEmployeeDetails p,EwayUploadReq upReq) {
		String status="Success";
		try {
			EwayEmplyeeDetailRaw detailRaw =EwayEmplyeeDetailRaw.builder()
					.companyId(StringUtils.isBlank(p.getCompanyId())?0:Integer.valueOf(p.getCompanyId()))
					.productId(p.getProductId()==null?0:p.getProductId())
					.riskId(p.getRiskId()==null?0:p.getRiskId())
					.quoteNo(StringUtils.isBlank(p.getQuoteNo())?"":p.getQuoteNo())
					.requestReferenceNo(StringUtils.isBlank(p.getRequestReferenceNo())?"":p.getRequestReferenceNo())
					.createdBy(StringUtils.isBlank(p.getCreatedBy())?"":p.getCreatedBy())
					.nationalityId(StringUtils.isBlank(p.getNationalityId())?"":p.getNationalityId())
					.employeeName(StringUtils.isBlank(p.getEmployeeName())?"":p.getEmployeeName())
					.yearOfJoining(p.getDateOfJoiningYear()==null?"":p.getDateOfJoiningYear().toString())
					.monthOfJoiningDesc(p.getDateOfJoiningMonth()==null?"":p.getDateOfJoiningMonth().toLowerCase())
					.dateOfBirth(p.getDateOfBirth()==null?"":sdf.format(p.getDateOfBirth()))
					.occupationId(StringUtils.isBlank(p.getOccupationId())?"":p.getOccupationId())
					.occupationDesc(StringUtils.isBlank(p.getOccupationDesc())?"":p.getOccupationDesc())
					.salary(p.getSalary()==null?"":p.getSalary().toString())
					.endorsmentType(upReq.getEndorsementYn())
					.address(StringUtils.isBlank(p.getAddress())?"":p.getAddress())
					.typeid(Integer.valueOf(upReq.getTypeId()))
					.sectionId(StringUtils.isBlank(p.getSectionId())?"":p.getSectionId())
					.employeeType("E")
					.status("Y")
					.build();
			employeeRawRepo.save(detailRaw);
		}catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			status="Failed";
		}
		return status;
	}

	public void doRawdataInsert(EwayUploadRes uploadResponse, EwayUploadTypeMaster uploadTypeMaster,
			List<EwayXlconfigMaster> xlConfigData) {
		try {
			String typeId = "",uploadedTranId="",rawTable="",mainTable="",errorTable="",fileUploadTypeId="";
			String mandatoryDetails="",dataTypeList="",dateFormatList="",duplicateColumnList="",duplicateExcelColumnList="",newVehMandatoryDetails="";
		
			EwayUploadTypeMaster uploadData = uploadTypeMaster;
        	typeId=uploadData.getTypeid()==null?"":uploadData.getTypeid().toString();
			uploadedTranId=uploadResponse.getRequestReferenceNo();
			rawTable=uploadData.getRawTableName()==null?"":uploadData.getRawTableName();
			fileUploadTypeId=uploadData.getTypeid()==null?"":uploadData.getTypeid().toString();
			log.info("insertRecords - Enter || typeId: " + typeId);
			log.info("TransactionId : "+uploadedTranId+" Fetching Records from CSV....");
        	fileUploadProgress(uploadResponse,"P","","Fetching Records from CSV","50");
			String excelHeaderName="",headerList="";
			for(int i=0;i<xlConfigData.size();i++) {
				EwayXlconfigMaster updatedData = xlConfigData.get(i);
				excelHeaderName = updatedData.getExcelheaderName()==null?"":updatedData.getExcelheaderName();
		    	if(StringUtils.isNotBlank(excelHeaderName)) {
		    		headerList = headerList+"\""+excelHeaderName+ "\""+",";
		    	}
		    }
			String rawtablecolumnslist="",rawtablecolumns="";
			List<XlConfigData> datas =new ArrayList<XlConfigData>();
			StringJoiner dataFieldLength = new StringJoiner("~");
			StringJoiner dataRangeList = new StringJoiner("~");
			List<String> rawTableCol =xlConfigData.stream().map(p -> p.getFieldNameRaw()).collect(Collectors.toList());
			rawTableCol.add(0,"SNO");
			for(int i=0;i<xlConfigData.size();i++) {
				EwayXlconfigMaster updatedData = xlConfigData.get(i);
				
		    	rawtablecolumns = rawTableCol.get(i)==null?"":rawTableCol.get(i);
		    	String excelHeaderDuplicateColumn=updatedData.getExcelheaderName()==null?"":updatedData.getExcelheaderName();
		    	if(StringUtils.isNotBlank(excelHeaderName)) {
		    		rawtablecolumnslist = rawtablecolumnslist+"\""+rawtablecolumns+ "\""+",";
		    		String mandatoryYN = updatedData.getMandatoryyn()==null?"N":updatedData.getMandatoryyn();
		    		//String newVehMandatoryYN = updatedData.getv()==null?"N":updatedData.getNewvehiclemandatoryyn();
		    		String dataType= updatedData.getDataType()==null?"":updatedData.getDataType();
		    		String dateformat= updatedData.getDateFormat()==null?"":updatedData.getDateFormat();
		    		String fieldLength= updatedData.getFieldLength()==null?"":updatedData.getFieldLength().toString();
		    		String dataRange= updatedData.getDataRange()==null?"":updatedData.getDataRange().toString();

		    		mandatoryDetails +=mandatoryYN+"~";
		    		//newVehMandatoryDetails +=newVehMandatoryYN+"~";
		    		if(dataType==null || dataType.isEmpty()) {
		    			dataTypeList +=dataType+"~"+null;
		    		}else {
		    			dataTypeList +=dataType+"~";

		    		}
		    		
		    		dataFieldLength.add(StringUtils.isBlank(fieldLength)?"0":fieldLength);
		    		dataRangeList.add(StringUtils.isBlank(dataRange)?"0":dataRange);
		    		dateFormatList +=dateformat+"~";
		    		String duplicateColumn= updatedData.getDublicateCheck()==null?"":updatedData.getDublicateCheck();
				    	if(StringUtils.isNotBlank(duplicateColumn)&&duplicateColumn.equalsIgnoreCase("Y")) {
		    			duplicateColumnList +=rawtablecolumns+",";
		    			duplicateExcelColumnList +=excelHeaderDuplicateColumn+",";
		    		}
		    	}
		    	
		    	XlConfigData configData =XlConfigData.builder()
		    			.datatype(updatedData.getDataType())
		    			.rawTableColumns(updatedData.getFieldNameRaw())
		    			.build();
		    	datas.add(configData);
		    }
				 
			EwayBatchReq request = new EwayBatchReq();
			uploadResponse.setTypeId(fileUploadTypeId);
			uploadResponse.setExcelrawtablename(rawTable);
			uploadResponse.setExcelrawtablefields(StringUtils.chop((rawtablecolumnslist).replace("\"", "")));
			uploadResponse.setExcelmandatorylist(mandatoryDetails);
			uploadResponse.setTableColumnsDataType(dataTypeList); 
			uploadResponse.setExceldateformatlist(dateFormatList); 
			uploadResponse.setExcelHeaderColumns(headerList);
			uploadResponse.setDataFieldLength(dataFieldLength.toString());
			uploadResponse.setDataRange(dataRangeList.toString());
			uploadResponse.setDuplicatecheckcolumns(StringUtils.chop(duplicateColumnList));
			uploadResponse.setDuplicatecheckexcelcolumns(StringUtils.chop(duplicateExcelColumnList));
			request.setEwayUploadRes(uploadResponse); 
			uploadResponse.setXlConfigData(datas);
			ObjectMapper mapper = new ObjectMapper();
			String ewayBatchReq=mapper.writerWithDefaultPrettyPrinter().writeValueAsString(request);
			
			String uploadType =StringUtils.isBlank(uploadResponse.getUploadType())?"":uploadResponse.getUploadType();
			String productId =StringUtils.isBlank(uploadResponse.getProductId())?"":uploadResponse.getProductId();
			String requestReferenceNo =StringUtils.isBlank(uploadResponse.getRequestReferenceNo())?"":uploadResponse.getRequestReferenceNo();
			String sectionId =StringUtils.isBlank(uploadResponse.getSectionId())?"":uploadResponse.getSectionId();
			if("Add".equalsIgnoreCase(uploadType)) {
				if("5".equals(productId)) {
					eserviceRepository.deleteByRequestReferenceNo(requestReferenceNo);
					eserviceRepository.deleteMotorDetailsByRefNo(requestReferenceNo);
					eserviceRepository.deleteUwQuestionsDetailsByRefNo(requestReferenceNo);
					eserviceRepository.deleteMotorDetailsByRefNo(requestReferenceNo);
					eserviceRepository.deleteMaster_referral_detailsByRefNo(requestReferenceNo);
					eserviceRepository.deletePassengerDetails(requestReferenceNo,sectionId);
					eserviceRepository.deleteUgandaVehicleDetails(requestReferenceNo);
					eserviceRepository.deleteUgandaVehicleDetailsFromMainTable(requestReferenceNo);
				}else {
					eserviceRepository.deleteProductEmployeeDetails(requestReferenceNo,sectionId);
					eserviceRepository.deleteRawEmployeeDetails(requestReferenceNo,sectionId);
					eserviceRepository.deletePassengerDetails(requestReferenceNo,sectionId);
					
				}
			}else {
				eserviceRepository.deleteByRequestReferenceNo(requestReferenceNo,"E");

			}
			
			 Long totalRows =0L;
			 Resource resource = (new FileSystemResource(uploadResponse.getCsvfilepath()));
		     	try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
		        	totalRows = reader.lines().count();
		        }
			  
		     	
		    if("5".equals(uploadResponse.getProductId())) { 	
				JobParameters jobParameters = new JobParametersBuilder()
				     	.addLong("time", System.currentTimeMillis())
				     	.addString("EwayBatchReq", ewayBatchReq)
				     	.addString("RequestReferenceNo", uploadResponse.getRequestReferenceNo())
				     	.addString("ExcelHeaderNames", StringUtils.chop(headerList))
				     	.addLong("totalRecords", totalRows)
						.addLong("gridSize", 10L)
				        .toJobParameters();
						jobLauncher.run(vehicle_raw_job, jobParameters);
		    }else {
		    	
		    	JobParameters jobParameters = new JobParametersBuilder()
				     	.addLong("time", System.currentTimeMillis())
				     	.addString("EwayBatchReq", ewayBatchReq)
				     	.addString("RequestReferenceNo", uploadResponse.getRequestReferenceNo())
				     	.addString("ExcelHeaderNames", StringUtils.chop(headerList))
				     	.addLong("totalRecords", totalRows)
						.addLong("gridSize", 10L)
				        .toJobParameters();
						jobLauncher.run(nonmot_raw_job, jobParameters);
		    	
		    }
					
		}catch (Exception e) {
			log.error(e);
			e.printStackTrace();
		}
		
	}
		
	public void fileUploadProgress(EwayUploadRes uploadResponse,String progressStatus,String errordesc,String progrssDesc,String loading){
		try {
				uploadResponse.setProgressdesc(progrssDesc); 
				uploadResponse.setProgressErrordesc(errordesc); 
				uploadResponse.setProgressStatus(progressStatus);
				saveUploadTransactionData(uploadResponse);
			}catch (Exception e) {log.error(e);}
	}
	
	public String saveUploadTransactionData(EwayUploadRes res ) {
		try {
			TransactionControlDetails tcd =transRepo.findByRequestReferenceNo(res.getRequestReferenceNo());
			tcd.setStatus("P");
			tcd.setProgressDescription("Progressing..");
			tcd.setCompanyId(Integer.valueOf(res.getCompanyId()));
			transRepo.saveAndFlush(tcd);
			return tcd.getCsvFilePath();
		}catch (Exception e) {
			log.error(e);
			e.printStackTrace();
		}
		return "";
	}

	public void validateRawTableRecords(EwayUploadRes uploadResponse) {
		Long totalRecords =0L;
		Long validRecords =0L;
		Long errorRecords =0L;
		Long deletedRecords = 0L ;
		try {
			String product =uploadResponse.getProductId();
			Integer companyId =Integer.valueOf(uploadResponse.getCompanyId());
			Integer productId =Integer.valueOf(uploadResponse.getProductId());
			Integer typeId =Integer.valueOf(uploadResponse.getTypeId());
			String requestReferenceNo =uploadResponse.getRequestReferenceNo();
			String sectionId =StringUtils.isBlank(uploadResponse.getSectionId())?"":uploadResponse.getSectionId();

			String quoteNo =StringUtils.isBlank(uploadResponse.getQuoteNo())?"":uploadResponse.getQuoteNo();
			fileUploadProgress(uploadResponse,"P","Uploading","Validating raw table records","50");
			
			// for motor validation block
			if("5".equals(product) &&( "100002".equals(companyId.toString()) || "100019".equals(companyId.toString()))) {
			
				//eserviceRepository.updateInsuranceTypeId(companyId, productId, typeId, requestReferenceNo);
				criteriaQuery.updateInsuranceType(companyId, productId, typeId, requestReferenceNo);
				//eserviceRepository.updateSectionIdByRequestRefNo(companyId, productId, typeId, requestReferenceNo);
				//criteriaQuery.updateSectionId(companyId, productId, typeId, requestReferenceNo);
				//eserviceRepository.updateBodyTypeId(companyId, productId, typeId, requestReferenceNo);
				criteriaQuery.updateBodyTypeId(companyId, productId, typeId, requestReferenceNo);
				//eserviceRepository.updateInsuranceClassId(companyId, productId, typeId, requestReferenceNo);
				criteriaQuery.updateInsuranceClassId(companyId, productId, typeId, requestReferenceNo);
				//eserviceRepository.updateMotorUsageId(companyId, productId, typeId, requestReferenceNo);
				//eserviceRepository.updateSuminsuredValidationByPolicyType(companyId, productId, typeId, requestReferenceNo);
				eserviceRepository.updateBankCode(companyId,requestReferenceNo);

				if("100002".equals(companyId.toString())) {
					List<String> status =Arrays.asList(new String[] {"Y","E"});
					List<EserviceMotorDetailsRaw> list =eserviceRepository.findByCompanyIdAndProductIdAndRequestReferenceNoAndStatusIgnoreCaseInAndApiStatusIsNull(Integer.valueOf(uploadResponse.getCompanyId()),
							Integer.valueOf(uploadResponse.getProductId()),uploadResponse.getRequestReferenceNo(),status);
					
					log.info("validateRawTableRecords || Partitions size "+list.size());
					List<List<EserviceMotorDetailsRaw>> partitionsList =nPartition(list,list.size()>10?list.size()/10:10);
					for(List<EserviceMotorDetailsRaw> eList :partitionsList) {
						//parallel call
						List<CompletableFuture<String>> comFutures = eList.parallelStream()
								.map(e -> asyncProcess.validateTira(e, uploadResponse))
								.collect(Collectors.toList());
								
						@SuppressWarnings("unchecked")
						CompletableFuture<String> [] array =new CompletableFuture[comFutures.size()];
						comFutures.toArray(array);
						CompletableFuture.allOf(array).join();
					}
					
					//eserviceRepository.updateEmptyErrorStatus(companyId, productId, typeId, requestReferenceNo);
					eserviceRepository.updateDupicateSearchBydata(companyId, productId, typeId, requestReferenceNo);
					//criteriaQuery.updateDuplicateData(companyId, productId, typeId, requestReferenceNo);
					eserviceRepository.overrideExistingErrorRecord(typeId, requestReferenceNo, companyId, productId);		
					//criteriaQuery.overirdeExistingErrorRecord(typeId, requestReferenceNo, companyId, productId);
					criteriaQuery.updateMotorUsageId(companyId, productId, typeId, requestReferenceNo);

				}else if("100019".equals(companyId.toString())) {
					
					ugandaVehicleDetailsRepo.updateDuplicateRegistrationNo(requestReferenceNo);
					ugandaVehicleDetailsRepo.updateMotorUsageId(requestReferenceNo);

					//criteriaQuery.updateMotorUsageId(companyId, productId, typeId, requestReferenceNo);
					

				}
				
				eserviceRepository.updateMotorCatgeoryId(requestReferenceNo,companyId);
				//criteriaQuery.updateSuminsuredValidation(companyId, productId, typeId, requestReferenceNo);
				//eserviceRepository.updateCollateralValidation(companyId, productId, typeId, requestReferenceNo);
				//criteriaQuery.updateColleteralValidation(companyId, productId, typeId, requestReferenceNo);
				eserviceRepository.updateMasterIdEmptyValidation(companyId, productId, typeId, requestReferenceNo);
				criteriaQuery.updateEmptyDataError(companyId, productId, typeId, requestReferenceNo);
				criteriaQuery.updateMotorErrorStatus(companyId, productId, typeId, requestReferenceNo,"0");
				List<EserviceMotorDetailsRaw> dlist =eserviceRepository.findByCompanyIdAndProductIdAndRequestReferenceNo(companyId,productId,requestReferenceNo);
				validRecords =dlist.stream().filter(p ->"100002".equals(companyId.toString())?"Y".equals(p.getStatus()) && "Y".equals(p.getTiraStatus()):"Y".equals(p.getStatus())).count();
				errorRecords =dlist.stream().filter(p ->"100002".equals(companyId.toString())?"E".equals(p.getStatus()) ||  "E".equals(p.getTiraStatus()):"E".equals(p.getStatus())).count();
				totalRecords =validRecords + errorRecords;
			
			} /*else if("5".equals(product) && "100019".equals(companyId.toString())) {
				
			    ugandaVehicleDetailsRepo.updateDuplicateRegistrationNo(requestReferenceNo);
				ugandaVehicleDetailsRepo.updateMotorCategory(requestReferenceNo);
				criteriaQuery.updateEmptyDataError(companyId, productId, typeId, requestReferenceNo);
				criteriaQuery.updateErrorStatus(companyId, productId, typeId, requestReferenceNo,sectionId);
			
				List<EserviceMotorDetailsRaw> dlist =eserviceRepository.findByCompanyIdAndProductIdAndRequestReferenceNo(companyId,productId,requestReferenceNo);
				validRecords =dlist.stream().filter(p ->"Y".equals(p.getStatus()) && "Y".equals(p.getTiraStatus())).count();
				errorRecords =dlist.stream().filter(p ->"E".equals(p.getStatus()) ||  "E".equals(p.getTiraStatus())).count();
				totalRecords =validRecords + errorRecords;
		
			}*/else if("46".equals(product) && "100002".equals(companyId.toString())) {
				
			    ugandaVehicleDetailsRepo.updateDuplicateRegistrationNo(requestReferenceNo);
				//ugandaVehicleDetailsRepo.updateMotorCategory(requestReferenceNo);
				criteriaQuery.updateBodyTypeId(companyId, productId, typeId, requestReferenceNo);
				//criteriaQuery.updateMotorUsageId(companyId, productId, typeId, requestReferenceNo);
				eserviceRepository.updateVehUsageId(companyId, productId, typeId, requestReferenceNo);

				
				List<UgandaVehicleDetailsRaw> vehiList =ugandaVehicleDetailsRepo.findByRequestReferenceNo(requestReferenceNo);
				
				if(!CollectionUtils.isEmpty(vehiList)) {
					
					errorRecords =vehiList.stream().filter(f -> "E".equalsIgnoreCase(f.getStatus()))
							.count();
					validRecords =vehiList.stream().filter(f -> "Y".equalsIgnoreCase(f.getStatus()))
							.count();
					totalRecords =errorRecords + validRecords;
					
					
				}
			}
			// For Employee MASTER validation block
			else if("14".equals(product) || "15".equals(product) || "32".equals(product) || "19".equals(product)) {
				
				//employeeRawRepo.updateOccupationId(companyId,productId,
						//Integer.valueOf(uploadResponse.getRiskId()),requestReferenceNo,quoteNo);
				criteriaQuery.updateOccupationId(companyId, productId, quoteNo, requestReferenceNo,sectionId);
				employeeRawRepo.updateDateOfMonth(companyId, productId, quoteNo, requestReferenceNo,sectionId);
				employeeRawRepo.updateLocationId(requestReferenceNo,productId.toString(),sectionId);
				criteriaQuery.updateEmpErrorDesc(companyId, productId, quoteNo, requestReferenceNo,sectionId);
				criteriaQuery.updateErrorStatus(companyId, productId, typeId, requestReferenceNo,sectionId);
				employeeRawRepo.updateDuplicateNationalityId(companyId,productId,requestReferenceNo,quoteNo,sectionId);
				Map<String,String> map =new HashMap<String,String>();
				map.put("QuoteNo", quoteNo);
				map.put("ProductId", product);
				map.put("SectionId", sectionId);
				String request =printReq.toJson(map);
				log.info("Employee cout request || requestReferenceNo : "+requestReferenceNo+" || "+request);
				Map<String,Object> response =asyncProcess.callApi(request, uploadResponse.getToken(), mediaType, employeeCountApi);
				log.info("Employee cout response || requestReferenceNo : "+requestReferenceNo+" || "+printReq.toJson(response));
				Map<String,Object> result =response.get("Result")==null?null:(Map<String,Object>) response.get("Result");
				Long expectedCount =result.get("ExpectedCount")==null?0L:Long.valueOf(result.get("ExpectedCount").toString());
				Long actualCount =result.get("ActualCount")==null?0L:Long.valueOf(result.get("ActualCount").toString());
				Long newEmpCount =employeeRawRepo.getCountRecords(companyId,productId,requestReferenceNo,sectionId);
				Long totalEmpCount =actualCount + newEmpCount;
				if(totalEmpCount>expectedCount) {
				    String errorMsg="The employees limt has exceeded more than your setup ("+expectedCount+")";
					Integer updateCount=employeeRawRepo.updateEmployeeExceededCount(errorMsg,companyId,productId,requestReferenceNo,sectionId);
					log.info("validateRawTableRecords :: updateEmployeeExceededCount : "+updateCount);
				}
				
				List<EwayEmplyeeDetailRaw> emp_list =employeeRawRepo.findByCompanyIdAndProductIdAndQuoteNoAndRiskIdAndSectionIdAndRequestReferenceNo(
						companyId,productId,quoteNo,
						Integer.valueOf(uploadResponse.getRiskId()),sectionId,requestReferenceNo);
				
				if(!CollectionUtils.isEmpty(emp_list)) {
					
					errorRecords =emp_list.stream().filter(f -> "E".equalsIgnoreCase(f.getStatus()))
							.count();
					validRecords =emp_list.stream().filter(f -> "Y".equalsIgnoreCase(f.getStatus()))
							.count();
					totalRecords =errorRecords + validRecords;
					
					
				}
			}
			
			else if("4".equals(product) ) {
				
				employeeRawRepo.updateDuplicatePassportNo(companyId.toString(), productId.toString(), requestReferenceNo, quoteNo);
				employeeRawRepo.updateNationlityId(companyId, productId,requestReferenceNo);
				employeeRawRepo.updateGender(requestReferenceNo);
				criteriaQuery.updateRelationId(companyId, productId, quoteNo, requestReferenceNo);
				criteriaQuery.updateTravelErrorDesc(companyId, productId, quoteNo, requestReferenceNo);
				criteriaQuery.updateErrorStatus(companyId, productId, typeId, requestReferenceNo,sectionId);
				List<EwayEmplyeeDetailRaw> passList=employeeRawRepo.findByCompanyIdAndProductIdAndRequestReferenceNo(companyId, productId, requestReferenceNo);
				if(!CollectionUtils.isEmpty(passList)) {
					
					errorRecords =passList.stream().filter(f -> "E".equalsIgnoreCase(f.getStatus()))
							.count();
					validRecords =passList.stream().filter(f -> "Y".equalsIgnoreCase(f.getStatus()))
							.count();
					totalRecords =errorRecords + validRecords;
					
					
				}
			}else if("59".equals(product) ||"26".equals(product) || "24".equals(product)) {
				employeeRawRepo.updateDuplicateSerialNo(requestReferenceNo);
			//	employeeRawRepo.updateContentAndAllriskSumInsured(requestReferenceNo);
				employeeRawRepo.updateLocationId(requestReferenceNo,product,sectionId);
				employeeRawRepo.updateContentTypeId(requestReferenceNo);
				employeeRawRepo.updateErrorStatusAndErrorDesc(requestReferenceNo);
				List<EwayEmplyeeDetailRaw> passList=employeeRawRepo.findByCompanyIdAndProductIdAndRequestReferenceNo(companyId, productId, requestReferenceNo);
				if(!CollectionUtils.isEmpty(passList)) {
					
					errorRecords =passList.stream().filter(f -> "E".equalsIgnoreCase(f.getStatus()))
							.count();
					validRecords =passList.stream().filter(f -> "Y".equalsIgnoreCase(f.getStatus()))
							.count();
					totalRecords =errorRecords + validRecords;
						
				}
			}else {
				employeeRawRepo.updateLocationId(requestReferenceNo,productId.toString(),sectionId);
				criteriaQuery.updateOccupationId(companyId, productId, quoteNo, requestReferenceNo,sectionId);
				employeeRawRepo.updateHealthRelation(companyId.toString(), requestReferenceNo);
				List<EwayEmplyeeDetailRaw> passList=employeeRawRepo.findByCompanyIdAndProductIdAndRequestReferenceNo(companyId, productId, requestReferenceNo);
				if(!CollectionUtils.isEmpty(passList)) {
					
					errorRecords =passList.stream().filter(f -> "E".equalsIgnoreCase(f.getStatus()))
							.count();
					validRecords =passList.stream().filter(f -> "Y".equalsIgnoreCase(f.getStatus()))
							.count();
					totalRecords =errorRecords + validRecords;
						
				}
			}
			
			uploadResponse.setToatalRows(String.valueOf(totalRecords));
			uploadResponse.setErrorRecords(String.valueOf(errorRecords));
			uploadResponse.setValidRecords(String.valueOf(validRecords));
			uploadResponse.setDeletedRecords(String.valueOf(deletedRecords));
			fileUploadProgress(uploadResponse,"S","BatchUploaded","Batch inserted successfully","50");

		}catch (Exception e) {
			fileUploadProgress(uploadResponse,"E","Failed","Raw table Insert Batch Failed","50");
			log.error(e);
			e.printStackTrace();
		}
		
	}

	private <T> List<List<T>> nPartition(List<T> objs, final int N) {
	    return new ArrayList<>(IntStream.range(0, objs.size()).boxed().collect(
	            Collectors.groupingBy(e->e/N,Collectors.mapping(e->objs.get(e), Collectors.toList())
	                    )).values());
	    }

	@Override
	public CommonRes getUploadTransaction(GetUploadTransactionReq req) {
		CommonRes res = new CommonRes();
		try {
			TransactionControlDetails d =transRepo.findByCompanyIdAndProductIdAndRequestReferenceNo(
					Integer.valueOf(req.getCompanyId()),Integer.valueOf(req.getProductId()),req.getRequestRefNo());
			if(d!=null) {
				GetUploadTransactionRes transactionRes =GetUploadTransactionRes.builder()
					.companyId(d.getCompanyId()==null?"":d.getCompanyId().toString())
					.productId(d.getProductId()==null?"":d.getProductId().toString())
					.requestRefNo(StringUtils.isBlank(d.getRequestReferenceNo())?"":d.getRequestReferenceNo())
					.uploadDate(d.getEntryDate()==null?"":sdf.format(d.getEntryDate()))
					.totalRecords(d.getTotalRecords()==null?"0":d.getTotalRecords().toString())	
					.validRecords(d.getValidRecords()==null?"0":d.getValidRecords().toString())
					.errorRecords(d.getErrorRecords()==null?"0":d.getErrorRecords().toString())
					.movedRecords(d.getMovedRecords()==null?"0":d.getMovedRecords().toString())
					.sectionId(d.getSectionId()==null?"0":d.getSectionId().toString())
					.build();
				
				res.setCommonResponse(transactionRes);
				res.setMessage("SUCCESS");
			}else {
				res.setCommonResponse(null);
				res.setMessage("FAILED");
			}
		}catch (Exception e) {
			log.error(e);
			e.printStackTrace();
		}
		return res;
	}

	@Override
	public CommonRes getTransactionStatus(GetUploadTransactionReq req) {
		CommonRes res = new CommonRes();
		try {
			TransactionControlDetails data =transRepo.findByCompanyIdAndProductIdAndRequestReferenceNo(
					Integer.valueOf(req.getCompanyId()),Integer.valueOf(req.getProductId()),req.getRequestRefNo());
			if(data!=null) {
				GetTransactionStatusRes transactionStatusRes =GetTransactionStatusRes.builder()
						.status(data.getStatus())
						.statusDesc(data.getProgressDescription())
						.build();
				res.setCommonResponse(transactionStatusRes);
				res.setMessage("SUCCESS");
			}else {
				res.setCommonResponse(null);
				res.setMessage("FAILED");
			}
		}catch (Exception e) {
			log.error(e);
			e.printStackTrace();
		}
		return res;
	}

	@Override
	public CommonRes getRecordByStatus(GetRecordsReq req) {
		CommonRes res = new CommonRes();
		ArrayList<GetRecordsRes> arrayList =new ArrayList<GetRecordsRes>();
		try {			
			//motor grid
			if("5".equals(req.getProductId())) {
				List<EserviceMotorDetailsRaw> list =eserviceRepository.getErrorRecordsByRefNo(Integer.valueOf(req.getCompanyId()), Integer.valueOf(req.getProductId()), req.getRequestRefNo());
				if(!CollectionUtils.isEmpty(list)) {
					list.forEach(p ->{
						GetRecordsRes recordsRes =GetRecordsRes.builder()
								.companyId(p.getCompanyId().toString())
								.productId(p.getProductId().toString())
								.requestRefNo(p.getRequestReferenceNo())
								.sectionId(p.getSectionId()==null?"":p.getSectionId().toString())
								.searchByData(StringUtils.isBlank(p.getSearchByData())?"":p.getSearchByData())
								.insuranceType(StringUtils.isBlank(p.getInsuranceTypeDesc())?"":p.getInsuranceTypeDesc())
								.insuranceClass(StringUtils.isBlank(p.getInsuranceClassDesc())?"":p.getInsuranceClassDesc())
								.bodyType(StringUtils.isBlank(p.getBodyTypeDesc())?"":p.getBodyTypeDesc())
								.rowNum(p.getRowNum()==null?"":p.getRowNum().toString())
								.policyStartDate(StringUtils.isBlank(p.getPolicyStartDate())?"":p.getPolicyStartDate())
								.policyEndDate(StringUtils.isBlank(p.getPolicyEndDate())?"":p.getPolicyEndDate())
								.inputStatus(StringUtils.isBlank(p.getStatus())?"":p.getStatus())
								.inputErrorDesc(StringUtils.isBlank(p.getErrorDesc())?"":p.getErrorDesc()) 
								.tiraStatus(StringUtils.isBlank(p.getTiraStatus())?"":p.getTiraStatus())
								.tiraErrorDesc(StringUtils.isBlank(p.getTiraErrorDesc())?"":p.getTiraErrorDesc()) 
								.build();	
						arrayList.add(recordsRes);
					});
					res.setCommonResponse(arrayList);
					res.setMessage("SUCCESS");
				}else {
					res.setCommonResponse(null);
					res.setMessage("FAILED");
				}
				
				// employee grid
			}else if("14".equals(req.getProductId()) || "15".equals(req.getProductId()) || "32".equals(req.getProductId())) {
				
				List<EwayEmplyeeDetailRaw> list =new ArrayList<EwayEmplyeeDetailRaw>();
				
				if("E".equalsIgnoreCase(req.getStatus())) {
					list =employeeRawRepo.findByCompanyIdAndProductIdAndRequestReferenceNoAndQuoteNoAndRiskIdAndStatusIgnoreCase(
							Integer.valueOf(req.getCompanyId()),Integer.valueOf(req.getProductId()),req.getRequestRefNo(),
							req.getQuoteNo(),Integer.valueOf(req.getRiskId()),req.getStatus());
				}
				
				ArrayList<GetEmployeeDetailsRes> arrayList2 =new ArrayList<GetEmployeeDetailsRes>();
				
				if(!CollectionUtils.isEmpty(list)) {
					list.forEach(p ->{
						GetEmployeeDetailsRes recordsRes =GetEmployeeDetailsRes.builder()
								.companyId(p.getCompanyId().toString())
								.productId(p.getProductId().toString())
								.requestRefNo(p.getRequestReferenceNo())
								.nationalityId(StringUtils.isBlank(p.getNationalityId())?"":p.getNationalityId())
								.employeeName(StringUtils.isBlank(p.getEmployeeName())?"":p.getEmployeeName())
								.dateOfBirth(StringUtils.isBlank(p.getDateOfBirth())?"":p.getDateOfBirth())
								.dateOfJoiningYear(StringUtils.isBlank(p.getYearOfJoining())?"":p.getYearOfJoining())
								.dateOfJoiningMonth(StringUtils.isBlank(p.getMonthOfJoining())?"":p.getMonthOfJoining())
								.occupationDesc(StringUtils.isBlank(p.getOccupationDesc())?"":p.getOccupationDesc())
								.salary(StringUtils.isBlank(p.getSalary())?"":p.getSalary())
								.occupationId(StringUtils.isBlank(p.getOccupationId())?"":p.getOccupationId())
								.status(p.getStatus())
								.errorDesc(StringUtils.isBlank(p.getErrorDesc())?"":p.getErrorDesc())
								.rowNum(p.getRowNum().toString())
								.quoteNo(p.getQuoteNo())
								.riskId(p.getRiskId().toString())
								.build();	
						arrayList2.add(recordsRes);
					});
					res.setCommonResponse(arrayList2);
					res.setMessage("SUCCESS");
				}else {
					res.setCommonResponse(null);
					res.setMessage("FAILED");
				}
				
			}else if("4".equals(req.getProductId())) {
				List<EwayEmplyeeDetailRaw> list =new ArrayList<EwayEmplyeeDetailRaw>();
				
				if("E".equalsIgnoreCase(req.getStatus())) {
					list =employeeRawRepo.findByCompanyIdAndProductIdAndRequestReferenceNoAndQuoteNoAndRiskIdAndStatusIgnoreCase(
							Integer.valueOf(req.getCompanyId()),Integer.valueOf(req.getProductId()),req.getRequestRefNo(),
							req.getQuoteNo(),Integer.valueOf(req.getRiskId()),req.getStatus());
				}
				
				ArrayList<GetEmployeeDetailsRes> arrayList2 =new ArrayList<GetEmployeeDetailsRes>();
				
				if(!CollectionUtils.isEmpty(list)) {
					list.forEach(p ->{
						GetEmployeeDetailsRes recordsRes =GetEmployeeDetailsRes.builder()
								.companyId(p.getCompanyId().toString())
								.productId(p.getProductId().toString())
								.requestRefNo(p.getRequestReferenceNo())
								.firstName(StringUtils.isBlank(p.getFirstName())?"":p.getFirstName())
								.lastName(StringUtils.isBlank(p.getLastName())?"":p.getLastName())
								.passportNumber(StringUtils.isBlank(p.getPassportNo())?"":p.getPassportNo())
								.relation(StringUtils.isBlank(p.getRelationDesc())?"":p.getRelationDesc())
								.dateOfBirth(StringUtils.isBlank(p.getDateOfBirth())?"":p.getDateOfBirth())
								.status(p.getStatus())
								.errorDesc(StringUtils.isBlank(p.getErrorDesc())?"":p.getErrorDesc())
								.rowNum(p.getRowNum().toString())
								.quoteNo(p.getQuoteNo())
								.riskId(p.getRiskId().toString())
								.build();	
						arrayList2.add(recordsRes);
					});
					res.setCommonResponse(arrayList2);
					res.setMessage("SUCCESS");
				}else {
					res.setCommonResponse(null);
					res.setMessage("FAILED");
				}
			}
		}catch (Exception e) {
			log.error(e);
			e.printStackTrace();
		}
		return res;
	}

	@Override
	public CommonRes editRecord(EditRecordReq req) {
		CommonRes res = new CommonRes();
		try {
			if("14".equals(req.getProductId()) || "15".equals(req.getProductId()) || "32".equals(req.getProductId())) {
				
				Optional<EwayEmplyeeDetailRaw> raw =employeeRawRepo.findById(Integer.valueOf(req.getRowId()));
				if(raw.isPresent()) {
					EwayEmplyeeDetailRaw p =raw.get();
					GetEmployeeDetailsRes employeeDetailsRes =GetEmployeeDetailsRes.builder()
							.companyId(p.getCompanyId().toString())
							.productId(p.getProductId().toString())
							.requestRefNo(p.getRequestReferenceNo())
							.nationalityId(StringUtils.isBlank(p.getNationalityId())?"":p.getNationalityId())
							.employeeName(StringUtils.isBlank(p.getEmployeeName())?"":p.getEmployeeName())
							.dateOfBirth(StringUtils.isBlank(p.getDateOfBirth())?"":p.getDateOfBirth())
							.dateOfJoiningYear(StringUtils.isBlank(p.getYearOfJoining())?"":p.getYearOfJoining())
							.occupationDesc(StringUtils.isBlank(p.getOccupationDesc())?"":p.getOccupationDesc())
							.salary(StringUtils.isBlank(p.getSalary())?"":p.getSalary())
							.status(p.getStatus())
							.errorDesc(StringUtils.isBlank(p.getErrorDesc())?"":p.getErrorDesc())
							.occupationId(StringUtils.isBlank(p.getOccupationId())?"":p.getOccupationId())
							.rowNum(p.getRowNum().toString())
							.quoteNo(p.getQuoteNo())
							.riskId(p.getRiskId().toString())
							.build();
					res.setCommonResponse(employeeDetailsRes);
					res.setMessage("FAILED");
				}else{
					res.setCommonResponse(null);
					res.setMessage("FAILED");
				}
			}
		}catch (Exception e) {
			log.error(e);
			e.printStackTrace();
		}
		return res;
	}

	@Override
	public CommonRes insertRecords(GetRecordsReq req,String auth) {
		CommonRes res = new CommonRes();
		try {
			
			// Motor main table insert block
			if("5".equalsIgnoreCase(req.getProductId())) {
				List<EserviceMotorDetailsRaw> list =eserviceRepository.findByCompanyIdAndProductIdAndRequestReferenceNo(Integer.valueOf(req.getCompanyId()),Integer.valueOf(req.getProductId()),
						req.getRequestRefNo());
				if(!CollectionUtils.isEmpty(list)) {
					
					// filter valid records
					List<EserviceMotorDetailsRaw> data =list.stream()
							.filter(p -> "Y".equalsIgnoreCase(p.getStatus()) )
							.filter(p -> "Y".equalsIgnoreCase(p.getTiraStatus()))
							.filter(p -> StringUtils.isBlank(p.getApiStatus()))
							.collect(Collectors.toList());
					
					// made the partitions based by 10
					List<List<EserviceMotorDetailsRaw>> partitions =nPartition(data, data.size()>10?data.size()/10:10);						
					List<Map<String,Object>> listOfError = new ArrayList<>();
					for(List<EserviceMotorDetailsRaw> eservice :partitions) {
						List<CompletableFuture<List<Map<String,Object>>>> comFuture = new ArrayList<CompletableFuture<List<Map<String,Object>>>>();
						Long maxVehId =transRepo.getVehicleId(eservice.get(0).getRequestReferenceNo(), eservice.get(0).getProductId().toString());
						Long countVeh =1L;
						for(EserviceMotorDetailsRaw raw :eservice) {
							 Long vehicleId =maxVehId+countVeh;
							 CompletableFuture<List<Map<String,Object>>> asyncList =asyncProcess.createQuote(raw, auth,vehicleId);
							 comFuture.add(asyncList);
							 countVeh++;
						}							
						@SuppressWarnings("unchecked")
						CompletableFuture<List<Map<String,Object>>>[] comArray =new CompletableFuture[comFuture.size()];
						comFuture.toArray(comArray);
						CompletableFuture.allOf(comArray).join();
						List<List<Map<String, Object>>> array_list = 
					            new ArrayList<List<Map<String,Object>>>((Collection<? extends List<Map<String, Object>>>) Arrays.asList(comArray));

						System.out.println(new Gson().toJson(array_list));
						}
						
					

			        
						res.setCommonResponse("SUCCESS");
						res.setMessage("SUCCESS");
				}else {
					res.setCommonResponse("FAILED");
					res.setMessage("FAILED");
				}
		
		}	
			
		// Employee main table insert block	
		else if("14".equals(req.getProductId()) || "15".equals(req.getProductId()) || "32".equals(req.getProductId())) {
			
			List<Map<String,Object>> list =employeeRawRepo.getEmployeRawDetails(Integer.valueOf(req.getCompanyId()),Integer.valueOf(req.getProductId()),
					req.getRequestRefNo());
			if(!CollectionUtils.isEmpty(list)) {
				Map<String,Object> apiResponse =asyncProcess.createEmployee(list,auth);
				@SuppressWarnings("unchecked")
				List<Map<String,Object>> errors =apiResponse.get("ErrorMessage")==null?null:(List<Map<String,Object>>)apiResponse.get("ErrorMessage");
				if(CollectionUtils.isEmpty(errors)) {
					res.setCommonResponse("SUCCESS");
					res.setMessage("SUCCESS");
				}else{
					res.setCommonResponse(errors);
					res.setMessage("FAILED");
				}				
			}else {
				res.setCommonResponse("FAILED");
				res.setMessage("FAILED");
			}
			
		// Travel data insert block	
		 }else if("4".equals(req.getProductId())){
			 List<Map<String,Object>> list =employeeRawRepo.getPassengersList(req.getCompanyId(),req.getProductId(),
						req.getRequestRefNo());			 
			 if(!CollectionUtils.isEmpty(list)) {
				 Map<String,Object> apiResponse=asyncProcess.createPassenger(list,auth);
				 List<Map<String,Object>> errors =apiResponse.get("ErrorMessage")==null?null:(List<Map<String,Object>>)apiResponse.get("ErrorMessage");
					if(CollectionUtils.isEmpty(errors)) {
						res.setCommonResponse("SUCCESS");
						res.setMessage("SUCCESS");
					}else{
						res.setCommonResponse(errors);
						res.setMessage("FAILED");
					}	
					res.setCommonResponse("SUCCESS");
					res.setMessage("SUCCESS");
			 }else{
					res.setCommonResponse("FAILED");
					res.setMessage("FAILED");
			 }	
		 }
		}catch (Exception e) {
			log.error(e);
			e.printStackTrace();
		}
		return res;
	}

	@Override
	public CommonRes updateRecords(UpdateRecordReq updateReq,String token) {
		CommonRes res = new CommonRes();
		try {
			if("14".equals(updateReq.getProductId()) || "15".equals(updateReq.getProductId()) || "32".equals(updateReq.getProductId())) {				
				List<Error> errorList=validation.validateEmployee(updateReq);
				EmployeeUpdateReq req =updateReq.getEmployeeUpdateReq();
				if(CollectionUtils.isEmpty(errorList)) {
					Optional<EwayEmplyeeDetailRaw> emp =employeeRawRepo.findById(Integer.valueOf(req.getRowNum()));
					if(emp.isPresent()) {
						EwayEmplyeeDetailRaw detailRaw =emp.get();
						detailRaw.setEmployeeName(req.getEmployeeName());
						detailRaw.setDateOfBirth(req.getDateOfBirth());
						//detailRawdetailRaw.setDateOfJoining(req.getDateOfJoiningYear());
						//detailRaw.setDateOfJoinMonth(req.getDateOfJoiningMonth());
						detailRaw.setNationalityId(req.getNationalityId());
						detailRaw.setOccupationId(req.getOccupationId());
						detailRaw.setOccupationDesc(req.getOccupationDesc());
						detailRaw.setSalary(req.getSalary());
						detailRaw.setStatus("Y");
						detailRaw.setErrorDesc("");
						EwayEmplyeeDetailRaw result =employeeRawRepo.saveAndFlush(detailRaw);
						Integer count =0;//employeeRawRepo.checkSuminsuredValidation(updateReq.getQuoteNo(), updateReq.getRequestRefNo(), updateReq.getCompanyId(),
								//updateReq.getRiskId(), updateReq.getProductId());
						if(result!=null && count ==0) {
							
							TransactionControlDetails tDetails=	transRepo.findByCompanyIdAndProductIdAndRequestReferenceNo(Integer.valueOf(updateReq.getCompanyId()),
									Integer.valueOf(updateReq.getProductId()), updateReq.getRequestRefNo());
							Integer validRecords =tDetails.getValidRecords();
							Integer errorRecords =tDetails.getErrorRecords();
							tDetails.setErrorRecords(errorRecords-1);
							tDetails.setValidRecords(validRecords+1);
							transRepo.saveAndFlush(tDetails);
						}
						
						res.setCommonResponse("SUCCESS");
						res.setMessage("SUCCESS");
					}else {
						
						res.setCommonResponse("FAILED");
						res.setMessage("FAILED");
					}
				}else {
					res.setCommonResponse(errorList);
					res.setMessage("ERROR");
				}
				
			}else if("5".equals(updateReq.getProductId())) {
				List<Error> erroList =validation.validateVehicle(updateReq);
				if(CollectionUtils.isEmpty(erroList)) {
					MotorUpdateReq req =updateReq.getMotorRequest();
					EserviceMotorDetailsRaw raw	= eserviceRepository.findByRowNum(Integer.valueOf(req.getRowNum()));
					raw.setInsuranceTypeDesc(req.getInsuranceType());
					raw.setInsuranceTypeId(req.getInsuranceTypeId());
					raw.setInsuranceClassDesc(req.getInsuranceClass());
					raw.setInsuranceClassId(req.getInsuranceClassId());
					raw.setBodyTypeDesc(req.getBodyType());
					raw.setBodyTypeId(req.getBodyTypeId());
					raw.setMotorUsageDesc(req.getMotorUsage());
					raw.setMotorUsageId(req.getMotorUsageId());
					raw.setClaimYn(req.getClaimYn());
					raw.setVehicleSuminsured(req.getVehcileSuminsured());
					raw.setExtendedSuminsured(req.getExtendedTPPDSuminsured());
					raw.setWindshieldSuminsured(req.getWinShieldSuminsured());
					raw.setStatus("Y");
					raw.setErrorDesc("");
					if("Y".equals(req.getCollateralYn())) {
						raw.setBorrowerType(req.getBorrowType());
						raw.setFirstLossPayee(req.getFirstLossPayee());
					}
					raw.setCollateral(req.getCollateralYn());
					raw.setSearchByData(req.getSearchByData());
					raw.setAccessoriesSuminsured(req.getAccessoriesSuminured());
					raw.setGpsTrackingEnabled(req.getGpsYn());
					
					EserviceMotorDetailsRaw result =eserviceRepository.saveAndFlush(raw);
					if(result!=null) {
						
						TransactionControlDetails tDetails=	transRepo.findByCompanyIdAndProductIdAndRequestReferenceNo(Integer.valueOf(updateReq.getCompanyId()),
								Integer.valueOf(updateReq.getProductId()), updateReq.getRequestRefNo());
						Integer validRecords =tDetails.getValidRecords();
						Integer errorRecords =tDetails.getErrorRecords();
						tDetails.setErrorRecords(errorRecords-1);
						tDetails.setValidRecords(validRecords+1);
						transRepo.saveAndFlush(tDetails);
					}
					
					EwayUploadRes uploadReq = new EwayUploadRes();
					uploadReq.setToken(token);
					asyncProcess.validateTira(result, uploadReq);
					
				}else {
					res.setCommonResponse(erroList);
					res.setMessage("ERROR");
				}
				
			}
			
		}catch (Exception e) {
			log.error(e);
			e.printStackTrace();
		}
		return res;
	}

	private String getRequestRefNo(String companyId,String branchCode,String productId) {
		String refNo ="";
		try {
			String refCode=getListItem(companyId, branchCode, "PRODUCT_SHORT_CODE", productId);
			refNo =refCode +"-" +generateRefNo() ; 
		}catch (Exception e) {
			log.error(e);
			e.printStackTrace();
		}
		
		return refNo;
	}
	
	
	public synchronized String generateSeqCall(EwayUploadReq req ) {
	       try { 	   
	    	Map<String,Object> request = new HashMap<String,Object>();   
	    	request.put("ProductId", req.getProductId());
	    	request.put("InsuranceId", req.getCompanyId());
	    	request.put("Type", "2");
	    	request.put("TypeDesc", "REQUEST_REFERENCE_NO");
	    	
	    	String url = SequenceGenerateUrl;
	   		String auth = EwayBasicAuthName +":"+ EwayBasicAuthPass;
	   		byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes(StandardCharsets.US_ASCII));
	         String authHeader = "Basic " + new String( encodedAuth );
	      
	   		RestTemplate restTemplate = new RestTemplate();
	   		HttpHeaders headers = new HttpHeaders();
	   		headers.setAccept(Arrays.asList(new org.springframework.http.MediaType[] { org.springframework.http.MediaType.APPLICATION_JSON }));
	   		headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
	   		 headers.set("Authorization",authHeader);
	   		HttpEntity<Object> entityReq = new HttpEntity<Object>(request, headers);

	   		ResponseEntity<CommonRes> response = restTemplate.postForEntity(url, entityReq, CommonRes.class);
	   		ObjectMapper mapper = new ObjectMapper();
	   		SequenceGenerateRes res = mapper.convertValue(response.getBody().getCommonResponse() ,new TypeReference<SequenceGenerateRes>(){});

	   		String seq = res.getGeneratedValue();
	   		System.out.println("Generated Sequence --> " + seq );
	    	 return seq ;
	        } catch (Exception e) {
				e.printStackTrace();
				log.info( "Exception is ---> " + e.getMessage());
	            return null;
	        }
	       
	 }
	@Override
	public CommonRes deleteRecords(DeleteRecordReq req) {
		CommonRes res = new CommonRes();
		try {
			if("5".equals(req.getProductId())) {
				Integer count =eserviceRepository.deleteByRowNum(Integer.valueOf(req.getRowNUm()));
				if(count>0) {
					res.setMessage("SUCCESS");
					res.setCommonResponse("SUCCESS");
				}else {
					res.setMessage("FAILED");
					res.setCommonResponse("FAILED");
				}
			}
		}catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			res.setMessage("Exception");
			res.setCommonResponse("Somthing went wrong contact Admin...!");
		}
		return res;
	}

	@Override
	public CommonRes sampleDownload(SamplFileDownloadReq req) {
		CommonRes res =new CommonRes();
		Map<String,Object> map =new HashMap<String,Object>();
		try {
			
			Integer sectionId =StringUtils.isBlank(req.getSectionId())?0:Integer.valueOf(req.getSectionId());
			EwayUploadTypeMaster typeMaster =uploadTypeRepo.findByCompanyIdAndProductIdAndSectionIdAndStatus(
					Integer.valueOf(req.getCompanyId()),Integer.valueOf(req.getProductId()),sectionId,"Y");
			if(typeMaster!=null) {
			    Integer companyId =typeMaster.getCompanyId();
			    Integer productId =typeMaster.getProductId();
			    Integer typeId =typeMaster.getTypeid();
			    String fileName =typeMaster.getTypename();
			    
			    if("N".equals(typeMaster.getFilePathYn())) {
				    	List<EwayXlconfigMaster> master =xlConfigMaster.findByCompanyIdAndProductIdAndTypeidAndStatusOrderByFieldid(companyId, productId, typeId, "Y");
				    
					List<String> excelHeaderColumns =master.stream().map(p ->p.getExcelheaderName())
							.collect(Collectors.toList());
					
					String [] strArray =new String[excelHeaderColumns.size()];
					excelHeaderColumns.toArray(strArray);
					
					// Create a new workbook
			        Workbook workbook = new XSSFWorkbook();
			        
			        Sheet sheet =workbook.createSheet(fileName);
			        
			        Font font = workbook.createFont();
			        font.setFontName("Arial");
			        font.setFontHeightInPoints((short) 12);
			        font.setBold(true);
			        font.setColor(IndexedColors.BLACK.getIndex());
			        
			        sheet.autoSizeColumn(0);
			        
	
			        // Create a cell style with the font
			        CellStyle style = workbook.createCellStyle();
			        style.setFont(font);
			        
			        style.setFillForegroundColor(IndexedColors.GREEN.getIndex()); 
		            style.setFillPattern(FillPatternType.FINE_DOTS); 
		            
		           
	
			        Row rowm =sheet.createRow(0);
			        rowm.setHeightInPoints(20);
	
			       // String [] excelColArray =excelHeaderColumns.split("~");
	
			        int col =0;
			        for(String str :strArray) {
			        	Cell cell =rowm.createCell(col);
			        	cell.setCellValue(str);
			        	cell.setCellStyle(style);
			        	
			        	col++;
			        }
			        
			     // Auto-size the cells in the first row
			        for (int i = 0; i <strArray.length; i++) {
			            sheet.autoSizeColumn(i);
			            
			        }
					
					ByteArrayOutputStream bos = new ByteArrayOutputStream();
			        workbook.write(bos);
			        workbook.close();
			        byte [] byteArray =bos.toByteArray();
			        String base64 =Base64.getEncoder().encodeToString(byteArray);
			        String prefix = "data:application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;base64,";
	
			        res.setMessage("SUCCESS");
					map.put("Base64", prefix+base64);
					map.put("FileName", typeMaster.getTypename());
					map.put("Message", "SUCCESS");
					res.setCommonResponse(map);
			    }else {
			    	
			    	String filePath = typeMaster.getFilePath();
			    	Path paths = Paths.get(filePath);
			    	byte [] array =Files.readAllBytes(paths);
			    	String base64 =Base64.getEncoder().encodeToString(array);
				    String prefix = "data:application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;base64,";
		
				    res.setMessage("SUCCESS");
				    map.put("Base64", prefix+base64);
					map.put("FileName", typeMaster.getTypename());
					map.put("Message", "SUCCESS");
					res.setCommonResponse(map);
			    }
			}else {
				res.setMessage("FAILED");
				map.put("Base64", "");
				map.put("FileName", "");
				map.put("Message", "FAILED");
				res.setCommonResponse(map);
			}
		}catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			res.setMessage("FAILED");

		}
		return res;
	}
	

public synchronized String getListItem(String insuranceId , String branchCode, String itemType, String itemCode) {
	String itemDesc = "" ;
	List<ListItemValue> list = new ArrayList<ListItemValue>();
	try {
		Date today = new Date();
		Calendar cal = new GregorianCalendar();
		cal.setTime(today);
		today = cal.getTime();
		Date todayEnd = cal.getTime();
		
		// Criteria
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<ListItemValue> query=  cb.createQuery(ListItemValue.class);
		// Find All
		Root<ListItemValue> c = query.from(ListItemValue.class);
		
		//Select
		query.select(c);
		// Order By
		List<Order> orderList = new ArrayList<Order>();
		orderList.add(cb.asc(c.get("branchCode")));
		
		
		// Effective Date Start Max Filter
		Subquery<Timestamp> effectiveDate = query.subquery(Timestamp.class);
		Root<ListItemValue> ocpm1 = effectiveDate.from(ListItemValue.class);
		effectiveDate.select(cb.greatest(ocpm1.get("effectiveDateStart")));
		Predicate a1 = cb.equal(c.get("itemId"),ocpm1.get("itemId"));
		Predicate a2 = cb.lessThanOrEqualTo(ocpm1.get("effectiveDateStart"), today);
		Predicate b1= cb.equal(c.get("branchCode"),ocpm1.get("branchCode"));
		Predicate b2 = cb.equal(c.get("companyId"),ocpm1.get("companyId"));
		effectiveDate.where(a1,a2,b1,b2);
		
		// Effective Date End Max Filter
		Subquery<Timestamp> effectiveDate2 = query.subquery(Timestamp.class);
		Root<ListItemValue> ocpm2 = effectiveDate2.from(ListItemValue.class);
		effectiveDate2.select(cb.greatest(ocpm2.get("effectiveDateEnd")));
		Predicate a3 = cb.equal(c.get("itemId"),ocpm2.get("itemId"));
		Predicate a4 = cb.greaterThanOrEqualTo(ocpm2.get("effectiveDateEnd"), todayEnd);
		Predicate b3= cb.equal(c.get("companyId"),ocpm2.get("companyId"));
		Predicate b4= cb.equal(c.get("branchCode"),ocpm2.get("branchCode"));
		effectiveDate2.where(a3,a4,b3,b4);
					
		// Where
		Predicate n1 = cb.equal(c.get("status"),"Y");
		Predicate n12 = cb.equal(c.get("status"),"R");
		Predicate n13 = cb.or(n1,n12);
		Predicate n2 = cb.equal(c.get("effectiveDateStart"),effectiveDate);
		Predicate n3 = cb.equal(c.get("effectiveDateEnd"),effectiveDate2);	
		Predicate n4 = cb.equal(c.get("companyId"), insuranceId);
		Predicate n5 = cb.equal(c.get("companyId"), "99999");
		Predicate n6 = cb.equal(c.get("branchCode"), branchCode);
		Predicate n7 = cb.equal(c.get("branchCode"), "99999");
		Predicate n8 = cb.or(n4,n5);
		Predicate n9 = cb.or(n6,n7);
		Predicate n10 = cb.equal(c.get("itemType"),itemType );
		Predicate n11 = cb.equal(c.get("itemCode"), itemCode);
		
		if(itemType.equalsIgnoreCase("PRODUCT_SHORT_CODE"))          //not company based
			query.where(n13,n2,n3,n8,n9,n10,n11).orderBy(orderList);
		else
			query.where(n13,n2,n3,n4,n9,n10,n11).orderBy(orderList);
			
			
		// Get Result
		TypedQuery<ListItemValue> result = em.createQuery(query);
		list = result.getResultList();
		
		itemDesc = list.size() > 0 ? list.get(0).getItemValue() : "" ; 
	} catch (Exception e) {
		e.printStackTrace();
		log.info("Exception is ---> " + e.getMessage());
		return null;
	}
	return itemDesc ;
}


public synchronized String generateRefNo() {
    try {
    	 SqlSeqNumber entity;
         entity = sequence.save(new SqlSeqNumber());          
         return String.format("%05d",entity.getRequestReferenceNo()) ;
     } catch (Exception e) {
			e.printStackTrace();
			log.info( "Exception is ---> " + e.getMessage());
         return null;
     }
    
}

@Override
public CommonRes saveUploadMaster(SaveUploadTypeReq req) {
	CommonRes returnRes = new CommonRes();
	try {
		Integer companyId =Integer.valueOf(req.getCompanyId());
		Integer productId =Integer.valueOf(req.getProductId());
		//Integer typeId =uploadTypeRepo.findByCompanyIdAndProductId(companyId, productId);
	    //Integer type_id =StringUtils.isBlank(req.getRawTableId())?typeId==null?101:typeId :Integer.valueOf(req.getRawTableId());
		
	    EwayUploadTypeMaster uploadMaster =EwayUploadTypeMaster.builder()
	    		.companyId(companyId)
	    		.productId(productId)
	    		.typeid(null)
	    		.filePath(req.getSampleXlFilePath())
	    		.typename(req.getFileName())
	    		.sectionId(0)
	    		.entryDate(new Date())
	    		.status(StringUtils.isBlank(req.getStatus())?"N":req.getStatus())
	    		.productDesc(StringUtils.isBlank(req.getProductDesc())?null:req.getProductDesc())
	    		.rawTableId(req.getRawTableId())
	    		.apiMethod(req.getApiMethod())
	    		.rawTableName(req.getRawTableName())
	    		.build();
	    uploadTypeRepo.save(uploadMaster);
	    returnRes.setMessage("SUCCESS");
	}catch (Exception e) {
		e.printStackTrace();
		log.info( "Exception is ---> " + e);
	    returnRes.setMessage("FAILED");

	}
	return returnRes;
}

@Override
public CommonRes getUploadMaster(GetUploadTypeReq req) {
	CommonRes returnRes = new CommonRes();
	try {
		ArrayList<GetUploadTypeMasterRes> returnList =new ArrayList<GetUploadTypeMasterRes>();
		List<EwayUploadTypeMaster> list =uploadTypeRepo.findByCompanyIdAndStatus(Integer.valueOf(req.getCompanyId()),"Y");
		if(list.size()>0 && !list.isEmpty()) {
			list.forEach(p ->{
				GetUploadTypeMasterRes response = GetUploadTypeMasterRes.builder()
						.companyId(p.getCompanyId().toString())
						.fileName(p.getTypename())
						.typeId(p.getTypeid().toString())
						.productDesc(StringUtils.isBlank(p.getProductDesc())?"":p.getProductDesc())
						.productId(p.getProductId().toString())
						.status(p.getStatus())
						.rawTableId(p.getRawTableId().toString())
						.rawTableName(p.getRawTableName())
						.sampleExcelFilePath(p.getFilePath())
						.entryDate(new SimpleDateFormat("dd/MM/yyyy").format(p.getEntryDate()))
						.build();
				returnList.add(response);
			});
			returnRes.setCommonResponse(returnList);
			returnRes.setMessage("SUCCESS");
		}
	}catch (Exception e) {
		e.printStackTrace();
		log.info( "Exception is ---> " + e);
		returnRes.setCommonResponse(null);
		returnRes.setMessage("FAILED");
	}
	return returnRes;
}



@Override
@Transactional
 public Object moveRecords(MoveRecordsReq req, String token) {
	LinkedHashMap<Object,Object> request =new LinkedHashMap<Object,Object>();
	LinkedHashMap<Object,Object> mapRes =new LinkedHashMap<Object,Object>();
	VehicleUploadErrorMsg response = new VehicleUploadErrorMsg();
	String ewayReferenceNo ="";
	Object generateReq =null;
	try {
         log.info("MoveRecords request || "+printReq.toJson(req));
		 Integer companyId=Integer.valueOf(req.getCompanyId());
		 Integer productId=Integer.valueOf(req.getProductId());
		// Motor main table insert block
		if(("5".equalsIgnoreCase(req.getProductId()) || "46".equalsIgnoreCase(req.getProductId()))) {
			List<EserviceMotorDetailsRaw> list =eserviceRepository.findByCompanyIdAndProductIdAndRequestReferenceNo(companyId,productId,req.getRequestRefNo());
								
			if(!CollectionUtils.isEmpty(list)) {
					
				
				List<EserviceMotorDetailsRaw> data = new ArrayList<>();
				
				if("5".equalsIgnoreCase(req.getProductId())) {
				// filter valid records
					data =list.stream()
					.filter(p -> "Y".equalsIgnoreCase(p.getStatus()) )
					.filter(p -> "100002".equals(companyId.toString())?"Y".equalsIgnoreCase(p.getTiraStatus()):StringUtils.isBlank(p.getTiraStatus()))
					.filter(p -> StringUtils.isBlank(p.getApiStatus()))
					.collect(Collectors.toList());
				}else if("46".equalsIgnoreCase(req.getProductId())) {
					// filter valid records
					data =list.stream()
					.filter(p -> "Y".equalsIgnoreCase(p.getStatus()) )
					.filter(p -> StringUtils.isBlank(p.getApiStatus()))
					.collect(Collectors.toList());
				}
							
			// made the partitions based by 10
			List<List<EserviceMotorDetailsRaw>> partitions =nPartition(data, data.size()>10?data.size():10);						
			List<List<CompletableFuture<List<Map<String,Object>>>>> listOfList = new ArrayList<>();
			for(List<EserviceMotorDetailsRaw> eservice :partitions) {
				List<CompletableFuture<List<Map<String,Object>>>> comFuture = new ArrayList<CompletableFuture<List<Map<String,Object>>>>();
				Long maxVehId =transRepo.getVehicleId(eservice.get(0).getRequestReferenceNo(), eservice.get(0).getProductId().toString());
				Long countVeh =1L;
				for(EserviceMotorDetailsRaw raw :eservice) {
					Long vehicleId =maxVehId+countVeh;
					CompletableFuture<List<Map<String,Object>>> asyncList =asyncProcess.createQuote(raw, token,vehicleId);
					comFuture.add(asyncList);
					countVeh++;
				}							
			@SuppressWarnings("unchecked")
			CompletableFuture<List<Map<String,Object>>>[] comArray =new CompletableFuture[comFuture.size()];
			comFuture.toArray(comArray);
			CompletableFuture.allOf(comArray).join();
			
			listOfList.add(comFuture);
									
			}
			
			List<Map<String, Object>> flatList =listOfList.stream()
					.flatMap(a -> a.stream())
					.flatMap(b ->{
						try {
							return b.get().stream();
						} catch (Exception e) {
							e.printStackTrace();
						}
						return null;
					})
					.collect(Collectors.toList());
			
			
			if(!flatList.isEmpty()) {
				response.setCommonResponse(null);
				response.setMessage("FAILED");
				response.setIsError(true);
				response.setErrorMessage(flatList);
			}else {
				
				mapRes.put("Message", "SUCCESS");
				mapRes.put("RequestRefNo",req.getRequestRefNo());
				response.setCommonResponse(mapRes);
				response.setMessage("SUCCESS");
				response.setIsError(false);
				response.setErrorMessage(Collections.emptyList());
			}
			
			}else {
				mapRes.put("Message", "FAILED");
				mapRes.put("RequestRefNo",req.getRequestRefNo());
				response.setCommonResponse(mapRes);
				response.setMessage("FAILED");
			}
				
			}else {	
				
				if("100019".equals(String.valueOf(companyId)) && "5".equals(String.valueOf(productId))) {
					List<UgandaVehicleDetailsRaw> ugandaList =ugandaVehicleDetailsRepo.findByRequestReferenceNoAndEwayReferenceNoNotNull(req.getRequestRefNo());
					ewayReferenceNo =ugandaList.isEmpty()?"":ugandaList.get(0).getEwayReferenceNo();
				}
				
				Integer sectionId =StringUtils.isBlank(req.getSectionId())?0:Integer.valueOf(req.getSectionId());
				
				EwayUploadTypeMaster typeMaster=uploadTypeRepo.findByCompanyIdAndProductIdAndSectionIdAndStatus
						 (companyId, productId,sectionId, "Y");
				 
				Integer typeId=typeMaster.getTypeid();
				 
				 
				List<EwayXlconfigMaster> list =xlConfigMaster.findByCompanyIdAndProductIdAndSectionIdAndTypeidOrderByFieldid
						 (companyId,productId,sectionId,typeId);
				 
			
				 StringJoiner arrayDynQuery=new StringJoiner(",");
				 StringJoiner arrayJsonFields=new StringJoiner(",");
				 StringJoiner objectDynQuery=new StringJoiner(",");
				 StringJoiner objectJsonKey=new StringJoiner(",");
				 StringJoiner objectListJsonKey=new StringJoiner(",");
				
		         for(EwayXlconfigMaster config : list) {
		        	 if("Y".equalsIgnoreCase(config.getIsArray()) ) {
			        	 if("Y".equalsIgnoreCase(config.getIsMainDefauVal())) {
			        		 arrayDynQuery.add("'"+config.getSelColName()+"' as "+config.getApiJsonKey()+"");
			        	 }else {
			        		 arrayDynQuery.add(config.getSelColName());
			        	 }
			        	 arrayJsonFields.add(config.getApiJsonKey());
		        	 }if("Y".equalsIgnoreCase(config.getIsObject())) {
		        		 if("Y".equalsIgnoreCase(config.getObjDefaulVal())) {
		        			 objectDynQuery.add("'"+config.getObjSelcolKey()+"' as "+config.getObjApijsonKey()+"");
			        	 }else {
			        		 objectDynQuery.add(config.getObjSelcolKey());
			        	 }
		        		 objectJsonKey.add(config.getObjApijsonKey());
		        	 }else if("L".equalsIgnoreCase(config.getIsObject())) {
		        		 objectListJsonKey.add(config.getObjApijsonKey());
		        	 }
		         }
				 
		         String rawTableName =typeMaster.getRawTableName().trim();
		         String apiUrl =typeMaster.getApiName().trim();
		        // String method =StringUtils.isBlank(typeMaster.getApiMethod())?"POST":typeMaster.getApiMethod();
		         ArrayList<LinkedHashMap<Object,Object>> arrayList =new ArrayList<LinkedHashMap<Object,Object>>();
		         if(StringUtils.isNotBlank(arrayDynQuery.toString())) {
					 String [] apiJsonFields=arrayJsonFields.toString().split(",");
			         String  selectCol ="select "+arrayDynQuery.toString()+" from "+rawTableName+" where REQUEST_REFERENCE_NO=?1 and STATUS='Y' and (API_STATUS IS NULL OR API_STATUS='N')";
			         log.info("Array query select columns || "+selectCol);
			         log.info("Array json key columns || "+arrayJsonFields);
			         Query query =em.createNativeQuery(selectCol);
			         query.setParameter(1, req.getRequestRefNo());
			         @SuppressWarnings("unchecked")
					 List<Object[]> queryResult =query.getResultList();
			         Object[][] resultArray = new Object[queryResult.size()][];
			         for (int i = 0; i < queryResult.size(); i++) {
			             resultArray[i] = queryResult.get(i);
			         }
			         
			         for (Object [] ar :resultArray) {
			    		 LinkedHashMap<Object,Object> map = new LinkedHashMap<Object,Object>();
			    		 int key=0;
			        	 for(Object obj :ar) {
			        		 map.put(apiJsonFields[key], obj==null?"":obj.toString());
			        		 key++;
			        	 }
			        	 arrayList.add(map); 
			         }
			         
			         generateReq =arrayList;
		         }if(StringUtils.isNotBlank(objectDynQuery.toString())){
		        	 
		        	 String [] apiJsonFields=objectJsonKey.toString().split(",");
			         String  selectCol ="select "+objectDynQuery.toString()+" from "+rawTableName+" where REQUEST_REFERENCE_NO=?1 and STATUS='Y' and (API_STATUS IS NULL OR API_STATUS='N')";
			         log.info("Object query select columns || "+selectCol);
			         log.info("Object json query columns || "+objectDynQuery);
			         Query query =em.createNativeQuery(selectCol);
			         query.setParameter(1, req.getRequestRefNo());
			         @SuppressWarnings("unchecked")
					 List<Object[]> queryResult =query.getResultList();
					 Object [] array = new Object[1];
					 
					 array =queryResult.get(0) instanceof Object[]?array=queryResult.get(0):queryResult.get(0);
					
					 int key =0;
					 for(Object o : array) {
						 request.put(apiJsonFields[key], o==null?"":o.toString());
						 key++;
					 }
			         
					 if(arrayList.size()>0) {
			        	 request.put(objectListJsonKey.toString(), arrayList);
			         }
					 
					 generateReq =request;
		         }
		        
		        
		         
		         if("100019".equals(String.valueOf(companyId)) && "5".equals(String.valueOf(productId))) {
		        	 request.put("RequestReferenceNo", ewayReferenceNo);
		         }
		         
		        String apiReq =printReq.toJson(generateReq);
		        Map<String,Object> apiRes= asyncProcess.callApi(apiReq, token, mediaType, apiUrl);
		        log.info("Api Response ==>" +printReq.toJson(apiRes));
		        String statusCode=apiRes.get("StatusCode")==null?"": apiRes.get("StatusCode").toString();
				String status ="N";
				if("201".equals(statusCode))
					status ="Y";
				else
					status ="N";
				
				if("100019".equals(String.valueOf(companyId)) && "5".equals(String.valueOf(productId))) {
					
					List<Map<String,Object>> errorList =apiRes.get("ErrorMessage")==null?null:(List<Map<String,Object>>)apiRes.get("ErrorMessage");
					
					if(errorList!=null) {
						Map<String,Object> result =apiRes.get("Result")==null?null:(Map<String,Object>)apiRes.get("Result");
						ewayReferenceNo=result.get("RequestReferenceNo")==null?"":result.get("RequestReferenceNo").toString();
					}else {
						status ="N";
					}
					log.info("EWAY ReferenceNo || "+ewayReferenceNo);
					String updateQuery ="update "+rawTableName+" set API_STATUS=?1,EWAY_REFERENCE_NO=?2 where REQUEST_REFERENCE_NO=?3";
					 em.createNativeQuery(updateQuery)
					.setParameter(1, status)
					.setParameter(2, StringUtils.isBlank(ewayReferenceNo)?null:ewayReferenceNo)
					.setParameter(3, req.getRequestRefNo())
					.executeUpdate();
		         }else {
					 String updateQuery ="update "+rawTableName+" set API_STATUS=?1 where REQUEST_REFERENCE_NO=?2";
					 Integer count= em.createNativeQuery(updateQuery)
					.setParameter(1, status)
					.setParameter(2, req.getRequestRefNo())
					.executeUpdate();
		         }
				
				 
				mapRes.put("Message", "SUCCESS");
				mapRes.put("RequestRefNo", "100019".equals(req.getCompanyId()) && "5".equals(req.getProductId()
						)?ewayReferenceNo:req.getRequestRefNo());
				response.setCommonResponse(mapRes);
				response.setMessage("SUCCESS");
			}
	 }catch (Exception e) {
		 mapRes.put("Message", "FAILED");
			mapRes.put("RequestRefNo", "100019".equals(req.getCompanyId()) && "5".equals(req.getProductId()
					)?ewayReferenceNo:req.getRequestRefNo());
		e.printStackTrace();
		response.setMessage("FAILED");
		return response;
	}

	return response;
	 
 }

@Override
public CommonRes saveExcelField(List<SaveXlConfigReq> req) {
	CommonRes response = new CommonRes();
	try {
		//List<Error> validation =validateInputFields(req);
		Integer companyId =Integer.valueOf(req.get(0).getCompanyId());
		Integer productId =Integer.valueOf(req.get(0).getProductId());
		Integer typeId =Integer.valueOf(req.get(0).getTypeId());
		uploadTypeRepo.deleteByCompanyIdAndProductIdAndTypeid(companyId,productId,typeId);
	    List<EwayXlconfigMaster> list =req.stream().map(p ->{
	    	EwayXlconfigMaster m = EwayXlconfigMaster.builder()
	    			.companyId(companyId)
	    			.productId(productId)
	    			.sectionId(0)
	    			.typeid(typeId)
	    			.fieldid(Integer.valueOf(p.getSno()))
	    			.excelheaderName(StringUtils.isBlank(p.getExcelHeaderName())?null:p.getExcelHeaderName())
	    			.mandatoryyn(StringUtils.isBlank(p.getMandatoryYn())?null:p.getMandatoryYn())
	    			.dataType(StringUtils.isBlank(p.getDataType())?null:p.getDataType())
	    			.status(StringUtils.isBlank(p.getStatus())?"Y":p.getStatus())
	    			.excelColumnIndex(Integer.valueOf(p.getSno()))
	    			.fieldNameRaw(StringUtils.isBlank(p.getRawColumnName())?null:p.getRawColumnName())
	    			.fieldLength(StringUtils.isBlank(p.getFieldLength())?null:Integer.valueOf(p.getFieldLength()))
	    			.dataRange(StringUtils.isBlank(p.getDataRange())?null:p.getDataRange())
	    			.arrayJsonDefaultVal(StringUtils.isBlank(p.getArrayDefaultValYn())?null:p.getArrayDefaultValYn())
	    			.arrayJsonKey(StringUtils.isBlank(p.getArrayJsonKey())?null:p.getArrayJsonKey())
	    			.arrayTableCol(StringUtils.isBlank(p.getArrayTableColumn())?null:p.getArrayTableColumn())
	    			.isArrayVal(StringUtils.isBlank(p.getIsArrayYn())?null:p.getIsArrayYn())
	    			.isObjectVal(StringUtils.isBlank(p.getIsObjectYn())?null:p.getIsObjectYn())
	    			.objJsonKey(StringUtils.isBlank(p.getObjectJsonKey())?null:p.getObjectJsonKey())
	    			.objJsonDefaultVal(StringUtils.isBlank(p.getObjectDefaultValYn())?null:p.getObjectDefaultValYn())
	    			.objTableCol(StringUtils.isBlank(p.getObjectTablecolumn())?null:p.getObjectTablecolumn())
	    			.build();
	    	
	    	return xlConfigMaster.save(m);
	    }).collect(Collectors.toList());
	
	    Integer inputSize=req.size();
	    Integer saveSize =list.size();
	    
	    if(inputSize==saveSize) {
	    	response.setCommonResponse("SUCCESS");
	    	response.setMessage("Records saved successfully..");
	    }else {
	    	response.setCommonResponse("FAILED");
	    	response.setMessage("Records saved failed.. Contact Admin..");
	    }
	    
	}catch (Exception e) {
		e.printStackTrace();
		log.error(e);
    	response.setCommonResponse("FAILED");
    	response.setMessage(e.getMessage());
	}
	return response;
}

@Override
public CommonRes getUploadRecord(MoveRecordsReq req) {
	CommonRes response = new CommonRes();
	try {
		List<EwayEmplyeeDetailRaw> list =employeeRawRepo.findByRequestReferenceNo(req.getRequestRefNo());
		
	    Map<String,List<EwayEmplyeeDetailRaw>>  groupData=
	    		list.stream().collect(Collectors.groupingBy(EwayEmplyeeDetailRaw::getStatus));
			    		
	    List<HashMap<String,String>> validRecords =groupData
	    		.entrySet().stream().filter(p ->p.getKey().equalsIgnoreCase("Y"))
	    		.map(p ->p.getValue())
	    		.flatMap(p ->p.parallelStream())
	    		.map(p ->{
	    			HashMap<String,String> res = new HashMap<String,String>();
	    			res.put("CompanyId", p.getCompanyId() ==null?"":p.getCompanyId().toString());
	    			res.put("ProductId", p.getProductId()==null?"":p.getProductId().toString());
	    			res.put("RequestReferenceNo", StringUtils.isBlank(p.getRequestReferenceNo())?"":p.getRequestReferenceNo());
	    			res.put("LocationDesc", StringUtils.isBlank(p.getLocationDesc())?"":p.getLocationDesc());
	    			res.put("ContentTypeDesc", StringUtils.isBlank(p.getContentTypeDesc())?"":p.getContentTypeDesc());
	    			res.put("LocationId", StringUtils.isBlank(p.getLocationId())?"":p.getLocationId());
	    			res.put("ContentTypeId", StringUtils.isBlank(p.getContentTypeId())?"":p.getContentTypeId());
	    			res.put("SumInsured",StringUtils.isBlank(p.getSumInsured())?"":p.getSumInsured());
	    			res.put("Description", StringUtils.isBlank(p.getDescription())?"":p.getDescription());
	    			res.put("Status", "Y");
	    			res.put("RowNum", p.getRowNum().toString());
	    			res.put("SerialNumber", p.getSerialNumber());
	    			return res;
	    		})
	    		.collect(Collectors.toList());
	  
	    List<HashMap<String,String>> errorRecords =groupData
	    	    		.entrySet().stream().filter(p ->p.getKey().equalsIgnoreCase("E"))
	    	    		.map(p ->p.getValue())
	    	    		.flatMap(p ->p.parallelStream())
	    	    		.map(p ->{
	    	    			HashMap<String,String> res = new HashMap<String,String>();
	    	    			res.put("CompanyId", p.getCompanyId() ==null?"":p.getCompanyId().toString());
	    	    			res.put("ProductId", p.getProductId()==null?"":p.getProductId().toString());
	    	    			res.put("RequestReferenceNo", StringUtils.isBlank(p.getRequestReferenceNo())?"":p.getRequestReferenceNo());
	    	    			res.put("LocationDesc", StringUtils.isBlank(p.getLocationDesc())?"":p.getLocationDesc());
	    	    			res.put("ContentTypeDesc", StringUtils.isBlank(p.getContentTypeDesc())?"":p.getContentTypeDesc());
	    	    			res.put("LocationId", StringUtils.isBlank(p.getLocationId())?"":p.getLocationId());
	    	    			res.put("ContentTypeId", StringUtils.isBlank(p.getContentTypeId())?"":p.getContentTypeId());
	    	    			res.put("SumInsured",StringUtils.isBlank(p.getSumInsured())?"":p.getSumInsured());
	    	    			res.put("Description", StringUtils.isBlank(p.getDescription())?"":p.getDescription());
	    	    			res.put("ErrorDesc", StringUtils.isBlank(p.getErrorDesc())?"":p.getErrorDesc());
	    	    			res.put("Status", "E");
	    	    			res.put("RowNum", p.getRowNum().toString());
	    	    			res.put("SerialNumber", p.getSerialNumber());
	    	    			return res;
	    	    		})
	    	    		.collect(Collectors.toList());
	    
	    HashMap<String,Object> map = new HashMap<>();
	    map.put("SuccessRecords", validRecords);		
	    map.put("ErrorRecords", errorRecords);		
	    response.setErroCode(0);
	    response.setErrorMessage(Collections.emptyList());
	    response.setIsError(false);
	    response.setMessage("SUCCESS");
	    response.setCommonResponse(map);
	    	  
	}catch (Exception e) {
		e.printStackTrace();
		log.error(e);
	}
	return response;
}

@Override
public CommonRes updateEmployeeRecord(UpdateEmployeeRecordReq req) {
	CommonRes response = new CommonRes();
	try {
		List<Error> error =validateDomestic(req);
		if(error.isEmpty()) {
			EwayEmplyeeDetailRaw  detailRaw =EwayEmplyeeDetailRaw.builder()
					.companyId(Integer.valueOf(req.getCompanyId()))
					.productId(Integer.valueOf(req.getProductId()))
					.requestReferenceNo(req.getRequestReferenceNo())
					.contentTypeDesc(req.getContentTypeDesc())
					.contentTypeId(req.getContentTypeId())
					.locationDesc(req.getLocationDesc())
					.locationId(req.getLocationId())
					.sumInsured(req.getSumInsured())
					.description(req.getDescription())
					.status("Y")
					.errorDesc(null)
					.rowNum(Integer.valueOf(req.getRowNum()))
					.serialNumber(req.getSerialNumber())
					.build();
			employeeRawRepo.save(detailRaw);
			response.setErrorMessage(Collections.emptyList());
			response.setIsError(false);
			response.setMessage("Records updated Success");
		}else {
			response.setErrorMessage(error);
			response.setIsError(true);
			response.setMessage("Records Failed");
		}
	}catch (Exception e) {
		e.printStackTrace();
		log.error(e);
	}
	return response;
}

 
 private List<Error> validateDomestic(UpdateEmployeeRecordReq req){
	 
	 List<Error> list =new ArrayList<>();
	 
	 if(StringUtils.isBlank(req.getLocationDesc())) {
		 list.add(new Error("","LocationDesc","Please enter LocationDesc")) ;
	 }if(StringUtils.isBlank(req.getLocationId())) {
		 list.add(new Error("","LocationId","Please enter LocationId")) ;
	 }if(StringUtils.isBlank(req.getContentTypeDesc())) {
		 list.add(new Error("","ContentTypeDesc","Please enter ContentTypeDesc")) ;
	 }if(StringUtils.isBlank(req.getContentTypeId())) {
		 list.add(new Error("","ContentTypeId","Please enter ContentTypeId")) ;
	 }if(StringUtils.isBlank(req.getSumInsured())) {
		 list.add(new Error("","SumInsured","Please enter SumInsured")) ;

	 }if(StringUtils.isBlank(req.getDescription())) {
		 list.add(new Error("","Description","Please enter Description")) ;

	 }if(StringUtils.isBlank(req.getSerialNumber())) {
		 list.add(new Error("","SerialNumber","Please enter SerialNumber")) ;

	 }
	 
	
	 
	 return list;
	 
 }

 public void testDepreciations() {
	 try {
		
		 LocalDate manufacturedate =LocalDate.now().minusYears(20);
		 LocalDate todayDate =LocalDate.now();
		 long years =ChronoUnit.YEARS.between(manufacturedate, todayDate);
		 
		 
	 }catch (Exception e) {
		e.printStackTrace();
	}
 }

	@Override
	public Object batchCreateQuote(String request_ref_no, String authorization) {
		CommonRes response = new CommonRes();
		try {
			Integer total_records =eserviceRepository.countByRequestReferenceNoAndStatusNot(request_ref_no,"E");
			
			TransactionControlDetails controlDetails=transRepo.findByRequestReferenceNo(request_ref_no);
			controlDetails.setProgressDescription("Progressing..");
			controlDetails.setStatus("P");

			TransactionControlDetails tcd= transRepo.save(controlDetails);
			
			JobParameters jobParameters=new JobParametersBuilder()
					.addString("request_ref_no", request_ref_no)
					.addString("Authorization", authorization)
					.addLong("totalRecords", total_records.longValue())
					.addLong("gridSize", 10L)
					.addLocalDateTime("time", LocalDateTime.now())
					.toJobParameters();
			jobLauncher.run(veh_apicall_job, jobParameters);
			
			
			 Map<String,String> map = new HashMap<>();
		     map.put("progress_description", tcd.getProgressDescription());
		     map.put("status", tcd.getStatus());
		     map.put("request_reference_no",request_ref_no);
		        			
		     response.setMessage("Success");
		     response.setErrorMessage(Collections.EMPTY_LIST);
		     response.setIsError(false);
		     response.setCommonResponse(map);
			
		}catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			response.setMessage("Failed");
		    response.setErrorMessage(Collections.EMPTY_LIST);
		    response.setIsError(true);
		    response.setCommonResponse(null);
		    return response;
		}
		return response;
	}
	
	@Override
	public CommonRes vehicleValidation(String request_ref_no) {
		CommonRes response =new CommonRes();
		try {
			
			
			TransactionControlDetails controlDetails=transRepo.findByRequestReferenceNo(request_ref_no);
			controlDetails.setProgressDescription("Progressing..");
			controlDetails.setStatus("P");

			TransactionControlDetails tcd= transRepo.save(controlDetails);
		
			if(5 == tcd.getProductId()) {
				
				Integer total_records =eserviceRepository.countByRequestReferenceNoAndStatusNot(request_ref_no,"E");

				JobParameters jobParameters = new JobParametersBuilder()
						.addString("company_id", tcd.getCompanyId().toString())
						.addString("productId", tcd.getProductId().toString())
						.addString("request_ref_no", request_ref_no)
						.addLong("totalRecords", total_records.longValue())
						.addLong("gridSize",10L)
						.addLocalDateTime("time", LocalDateTime.now())
						.toJobParameters();
				jobLauncher.run(veh_validation_job, jobParameters);
			}else {
				
				Integer total_records =emplyeeDetailRawRepo.countByRequestReferenceNoAndStatusNot(request_ref_no,"E");
				
				List<EwayEmplyeeDetailRaw> list = emplyeeDetailRawRepo.findByRequestReferenceNo(request_ref_no) ; 
				
				JobParameters jobParameters = new JobParametersBuilder()
						.addString("company_id", tcd.getCompanyId().toString())
						.addString("productId", tcd.getProductId().toString())
						.addString("quote_no", list.get(0).getQuoteNo())
						.addString("section_id", list.get(0).getSectionId())
						.addString("request_ref_no", request_ref_no)
						.addLong("totalRecords", total_records.longValue())
						.addLong("gridSize",10L)
						.addLocalDateTime("time", LocalDateTime.now())
						.toJobParameters();
				jobLauncher.run(nonmot_validation_job, jobParameters);
			}
			
			
			
			
			 Map<String,String> map = new HashMap<>();
		     map.put("progress_description", tcd.getProgressDescription());
		     map.put("status", tcd.getStatus());
		     map.put("request_reference_no",request_ref_no);
		        			
		     response.setMessage("Success");
		     response.setErrorMessage(Collections.EMPTY_LIST);
		     response.setIsError(false);
		     response.setCommonResponse(map);
			
		}catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			response.setMessage("Failed");
		    response.setErrorMessage(Collections.EMPTY_LIST);
		    response.setIsError(true);
		    response.setCommonResponse(null);
		    return response;
		}
		return response;
	}

	@Override
	public void deleteRawData(String request_ref_no) {
		try {
			
			Thread_RawData_Delete thread = new Thread_RawData_Delete(request_ref_no, repository);
			thread.setDaemon(false);
			thread.setPriority(Thread.MIN_PRIORITY);
			thread.setName("DELETE-"+request_ref_no+"");
			
		}catch (Exception e) {
			log.error(e);
			e.printStackTrace();
		}
		
	}
   
}
