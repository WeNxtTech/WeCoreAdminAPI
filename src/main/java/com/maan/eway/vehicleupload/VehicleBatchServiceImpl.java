package com.maan.eway.vehicleupload;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.maan.eway.batch.entity.EserviceMotorDetailsRaw;
import com.maan.eway.batch.entity.EwayEmplyeeDetailRaw;
import com.maan.eway.batch.entity.EwayUploadTypeMaster;
import com.maan.eway.batch.entity.EwayXlconfigMaster;
import com.maan.eway.batch.entity.SqlSeqNumber;
import com.maan.eway.batch.repository.EserviceMotorDetailsRawRepository;
import com.maan.eway.batch.repository.EwayEmplyeeDetailRawRepository;
import com.maan.eway.batch.repository.EwayUploadTypeMasterRepository;
import com.maan.eway.batch.repository.ProductEmployeesDetailsRepository;
import com.maan.eway.batch.repository.SeqRefnoRepository;
import com.maan.eway.batch.repository.TransactionControlDetailsRepository;
import com.maan.eway.batch.req.DeleteRecordReq;
import com.maan.eway.batch.req.EditRecordReq;
import com.maan.eway.batch.req.EmployeeUpdateReq;
import com.maan.eway.batch.req.EwayBatchReq;
import com.maan.eway.batch.req.EwayUploadReq;
import com.maan.eway.batch.req.GetRecordsReq;
import com.maan.eway.batch.req.GetUploadTransactionReq;
import com.maan.eway.batch.req.MotorUpdateReq;
import com.maan.eway.batch.req.SamplFileDownloadReq;
import com.maan.eway.batch.req.UpdateRecordReq;
import com.maan.eway.batch.res.EwayUploadRes;
import com.maan.eway.batch.res.GetEmployeeDetailsRes;
import com.maan.eway.batch.res.GetRecordsRes;
import com.maan.eway.batch.res.GetTransactionStatusRes;
import com.maan.eway.batch.res.GetUploadTransactionRes;
import com.maan.eway.batch.res.XlConfigData;
import com.maan.eway.bean.ListItemValue;
import com.maan.eway.bean.ProductEmployeeDetails;
import com.maan.eway.error.Error;
import com.maan.eway.res.CommonRes;
import com.maan.eway.springbatch.TransactionControlDetails;

import okhttp3.MediaType;


@Service
public class VehicleBatchServiceImpl implements VehicleBatchService {
	
	
	@Value("${eway.xl.path}")
	private String filePath ;
	
	@Value("${tira.api}")
	private String tiraApi;
	
	@Value("${employee.delete.api}")
	private String empDeleteApi;
	
	@Value("${employee.count.api}")
	private String employeeCountApi;
	
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
	private CriteriaQueryServiceImpl criteriaQuery;
	
	@Autowired
    JobLauncher jobLauncher;
	
    @Autowired
    @Qualifier(value="VehicleJob")
    Job processJob;
    
    @Autowired
    private EwayEmplyeeDetailRawRepository employeeRawRepo;
    @Autowired
    private VehicleInputValidation validation;
    @Autowired 
    private Gson printReq;
  
    private static  MediaType mediaType =MediaType.parse("application/json");

    private static SimpleDateFormat sdf =new SimpleDateFormat("dd/MM/yyyy");
    
	@Override
	public EwayUploadRes batchUpload(MultipartFile file,EwayUploadReq req,String token) {
		EwayUploadRes uploadRes =new EwayUploadRes();
		try {
			String fileName =FilenameUtils.getBaseName(file.getOriginalFilename());
			String extension =FilenameUtils.getExtension(file.getOriginalFilename());
			String endorsmentType =StringUtils.isBlank(req.getEndorsementYn())?"":req.getEndorsementYn();
			
			uploadRes.setEndorsementYn(endorsmentType);
			uploadRes.setTypeId(req.getTypeId());
			uploadRes.setProductId(req.getProductId());
			uploadRes.setCompanyId(req.getCompanyId());
			uploadRes.setExcelFileName(file.getOriginalFilename());
			uploadRes.setProgressStatus("P");
			uploadRes.setProgressdesc("Uploading...");
			uploadRes.setRequestReferenceNo(StringUtils.isBlank(req.getRequestReferenceNo())?getRequestRefNo(req.getCompanyId(), req.getBranchCode(), req.getProductId()):req.getRequestReferenceNo());
			uploadRes.setToken(token);
			uploadRes.setBrokerBranchCode(StringUtils.isBlank(req.getBrokerBranchCode())?"":req.getBrokerBranchCode());
			uploadRes.setAcExecutiveId(StringUtils.isBlank(req.getAcExecutiveId())?"":req.getAcExecutiveId());
			uploadRes.setApplicationId(StringUtils.isBlank(req.getApplicationId())?"":req.getApplicationId());
			uploadRes.setBeokerCode(StringUtils.isBlank(req.getBeokerCode())?"":req.getBeokerCode());
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
			
			LocalDateTime dateTime =LocalDateTime.now();
			String excelFilePath=filePath+fileName+dateTime.getNano()+"."+extension;
			Path path =Paths.get(excelFilePath);
			file.transferTo(path);
			
			uploadRes.setExcelFilePath(excelFilePath);
			
			EwayUploadTypeMaster uploadTypeMaster=uploadTypeRepo.findByCompanyIdAndProductIdAndTypeidAndStatus(Integer.valueOf(req.getCompanyId()),Integer.valueOf(req.getProductId()),
					Integer.valueOf(req.getTypeId()),"Y");
			
			saveUploadTransactionData(uploadRes);
			
			VehicleThread_CSV_Convertion thread = new VehicleThread_CSV_Convertion(uploadRes,csvFileConvertion,uploadTypeMaster);
			Thread job =new Thread(thread);
			job.setName("EWAY_BATCH_UPLOAD");
			job.setDaemon(false);
			job.start();
			
			
		}catch (Exception e) {
			log.error(e);
			e.printStackTrace();
		}
		
		return uploadRes;
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
					.dateOfJoining(p.getDateOfJoiningYear()==null?"":p.getDateOfJoiningYear().toString())
					.dateOfJoiningMonth(p.getDateOfJoiningMonth()==null?"":p.getDateOfJoiningMonth().toLowerCase())
					.dateOfBirth(p.getDateOfBirth()==null?"":sdf.format(p.getDateOfBirth()))
					.occupationId(StringUtils.isBlank(p.getOccupationId())?"":p.getOccupationId())
					.occupatonDesc(StringUtils.isBlank(p.getOccupationDesc())?"":p.getOccupationDesc())
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

			for(int i=0;i<xlConfigData.size();i++) {
				EwayXlconfigMaster updatedData = xlConfigData.get(i);
		    	rawtablecolumns = updatedData.getFieldNameRaw()==null?"":updatedData.getFieldNameRaw();
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

			if("Add".equalsIgnoreCase(uploadType)) {
				if("5".equals(productId)) {
					eserviceRepository.deleteByRequestReferenceNo(requestReferenceNo);
					eserviceRepository.deleteMotorDetailsByRefNo(requestReferenceNo);
					eserviceRepository.deleteUwQuestionsDetailsByRefNo(requestReferenceNo);
					eserviceRepository.deleteMotorDetailsByRefNo(requestReferenceNo);
					eserviceRepository.deleteMaster_referral_detailsByRefNo(requestReferenceNo);
				}else if("14".equals(productId) || "15".equals(productId) || "32".equals(productId)){
					eserviceRepository.deleteProductEmployeeDetails(requestReferenceNo);
					eserviceRepository.deleteRawEmployeeDetails(requestReferenceNo);
				}else if("4".equals(productId)){
					eserviceRepository.deleteRawEmployeeDetails(requestReferenceNo);
					eserviceRepository.deletePassengerDetails(requestReferenceNo);
				}
			}else {
				eserviceRepository.deleteByRequestReferenceNo(requestReferenceNo,"E");

			}
			
			JobParameters jobParameters = new JobParametersBuilder()
			     	.addLong("time", System.currentTimeMillis())
			     	.addString("EwayBatchReq", ewayBatchReq)
			     	.addString("RequestReferenceNo", uploadResponse.getRequestReferenceNo())
			     	.addString("ExcelHeaderNames", StringUtils.chop(headerList))
			        .toJobParameters();
					jobLauncher.run(processJob, jobParameters);
					
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
	
	public void saveUploadTransactionData(EwayUploadRes res ) {
		try {
			TransactionControlDetails controlDetails = TransactionControlDetails.builder()
					.branchCode(StringUtils.isBlank(res.getBranchCode())?"":res.getBranchCode())
					.companyId(StringUtils.isBlank(res.getCompanyId())?null:Integer.valueOf(res.getCompanyId()))
					.entryDate(new Date())
					.errorDescription(StringUtils.isBlank(res.getProgressErrordesc())?"":res.getProgressErrordesc())
					.errorRecords(StringUtils.isBlank(res.getErrorRecords())?0:Integer.valueOf(res.getErrorRecords()))
					.validRecords(StringUtils.isBlank(res.getValidRecords())?0:Integer.valueOf(res.getValidRecords()))
					.totalRecords(StringUtils.isBlank(res.getToatalRows())?0:Integer.valueOf(res.getToatalRows()))
					.fileName(StringUtils.isBlank(res.getExcelFileName())?"":res.getExcelFileName())
					.filePath(StringUtils.isBlank(res.getExcelFilePath())?"":res.getExcelFilePath())
					.lastUpdatedDate(new Date())
					.loadPercentage(null)
					.loginName(StringUtils.isBlank(res.getUploadedBy())?"":res.getUploadedBy())
					.productId(StringUtils.isBlank(res.getProductId())?null:Integer.valueOf(res.getProductId()))
					.progressDescription(StringUtils.isBlank(res.getProgressdesc())?"":res.getProgressdesc())
					.requestReferenceNo(res.getRequestReferenceNo())
					.sectionId(StringUtils.isBlank(res.getSectionId())?null:Integer.valueOf(res.getSectionId()))
					.status(StringUtils.isBlank(res.getProgressStatus())?"":res.getProgressStatus())
					.typeId(Long.valueOf(res.getTypeId()))
					.tranDate(new Date())
					.build();
			transRepo.saveAndFlush(controlDetails);
		}catch (Exception e) {
			log.error(e);
			e.printStackTrace();
		}
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
			String quoteNo =StringUtils.isBlank(uploadResponse.getQuoteNo())?"":uploadResponse.getQuoteNo();
			fileUploadProgress(uploadResponse,"P","Uploading","Validating raw table records","50");
			
			// for motor validation block
			if("5".equals(product)) {
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
				//eserviceRepository.updateInsuranceTypeId(companyId, productId, typeId, requestReferenceNo);
				criteriaQuery.updateInsuranceType(companyId, productId, typeId, requestReferenceNo);
				//eserviceRepository.updateSectionIdByRequestRefNo(companyId, productId, typeId, requestReferenceNo);
				//criteriaQuery.updateSectionId(companyId, productId, typeId, requestReferenceNo);
				//eserviceRepository.updateBodyTypeId(companyId, productId, typeId, requestReferenceNo);
				criteriaQuery.updateBodyTypeId(companyId, productId, typeId, requestReferenceNo);
				//eserviceRepository.updateInsuranceClassId(companyId, productId, typeId, requestReferenceNo);
				criteriaQuery.updateInsuranceClassId(companyId, productId, typeId, requestReferenceNo);
				//eserviceRepository.updateMotorUsageId(companyId, productId, typeId, requestReferenceNo);
				criteriaQuery.updateMotorUsageId(companyId, productId, typeId, requestReferenceNo);
				//eserviceRepository.updateSuminsuredValidationByPolicyType(companyId, productId, typeId, requestReferenceNo);
				criteriaQuery.updateSuminsuredValidation(companyId, productId, typeId, requestReferenceNo);
				//eserviceRepository.updateCollateralValidation(companyId, productId, typeId, requestReferenceNo);
				criteriaQuery.updateColleteralValidation(companyId, productId, typeId, requestReferenceNo);
				//eserviceRepository.updateMasterIdEmptyValidation(companyId, productId, typeId, requestReferenceNo);
				criteriaQuery.updateEmptyDataError(companyId, productId, typeId, requestReferenceNo);
				criteriaQuery.updateErrorStatus(companyId, productId, typeId, requestReferenceNo);
				//eserviceRepository.updateEmptyErrorStatus(companyId, productId, typeId, requestReferenceNo);
				eserviceRepository.updateDupicateSearchBydata(companyId, productId, typeId, requestReferenceNo);
				//criteriaQuery.updateDuplicateData(companyId, productId, typeId, requestReferenceNo);
				eserviceRepository.overrideExistingErrorRecord(typeId, requestReferenceNo, companyId, productId);		
				//criteriaQuery.overirdeExistingErrorRecord(typeId, requestReferenceNo, companyId, productId);
				List<EserviceMotorDetailsRaw> dlist =eserviceRepository.findByCompanyIdAndProductIdAndRequestReferenceNo(companyId,productId,requestReferenceNo);
				validRecords =dlist.stream().filter(p ->"Y".equals(p.getStatus()) && "Y".equals(p.getTiraStatus())).count();
				errorRecords =dlist.stream().filter(p ->"E".equals(p.getStatus()) ||  "E".equals(p.getTiraStatus())).count();
				totalRecords =validRecords + errorRecords;
			}
			                                            
			
			// For Employee MASTER validation block
			else if("14".equals(product) || "15".equals(product) || "32".equals(product)) {
				
				//employeeRawRepo.updateOccupationId(companyId,productId,
						//Integer.valueOf(uploadResponse.getRiskId()),requestReferenceNo,quoteNo);
				criteriaQuery.updateOccupationId(companyId, productId, quoteNo, requestReferenceNo);
				employeeRawRepo.updateDateOfMonth(companyId, productId, quoteNo, requestReferenceNo);
				employeeRawRepo.updateLocationId(requestReferenceNo);
				criteriaQuery.updateEmpErrorDesc(companyId, productId, quoteNo, requestReferenceNo);
				criteriaQuery.updateErrorStatus(companyId, productId, typeId, requestReferenceNo);
				employeeRawRepo.updateDuplicateNationalityId(companyId,productId,requestReferenceNo,quoteNo);
				String sectionId =StringUtils.isBlank(uploadResponse.getSectionId())?"":uploadResponse.getSectionId();
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
				Long newEmpCount =employeeRawRepo.getCountRecords(companyId,productId,requestReferenceNo);
				Long totalEmpCount =actualCount + newEmpCount;
				if(totalEmpCount>expectedCount) {
				    String errorMsg="The employees limt has exceeded more than your setup ("+expectedCount+")";
					Integer updateCount=employeeRawRepo.updateEmployeeExceededCount(errorMsg,companyId,productId,requestReferenceNo);
					log.info("validateRawTableRecords :: updateEmployeeExceededCount : "+updateCount);
				}
				
				List<EwayEmplyeeDetailRaw> emp_list =employeeRawRepo.findByCompanyIdAndProductIdAndQuoteNoAndRiskIdAndRequestReferenceNo(
						companyId,productId,quoteNo,
						Integer.valueOf(uploadResponse.getRiskId()),requestReferenceNo);
				
				if(!CollectionUtils.isEmpty(emp_list)) {
					
					errorRecords =emp_list.stream().filter(f -> "E".equalsIgnoreCase(f.getStatus()))
							.count();
					validRecords =emp_list.stream().filter(f -> "Y".equalsIgnoreCase(f.getStatus()))
							.count();
					totalRecords =errorRecords + validRecords;
					
					
				}
			}
			
			else if("4".equals(product)) {
				
				employeeRawRepo.updateDuplicatePassportNo(companyId.toString(), productId.toString(), requestReferenceNo, quoteNo);
				employeeRawRepo.updateNationlityId(companyId, productId,requestReferenceNo);
				criteriaQuery.updateRelationId(companyId, productId, quoteNo, requestReferenceNo);
				criteriaQuery.updateTravelErrorDesc(companyId, productId, quoteNo, requestReferenceNo);
				criteriaQuery.updateErrorStatus(companyId, productId, typeId, requestReferenceNo);
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
								.dateOfJoiningYear(StringUtils.isBlank(p.getDateOfJoining())?"":p.getDateOfJoining())
								.dateOfJoiningMonth(StringUtils.isBlank(p.getDateOfJoiningMonth())?"":p.getDateOfJoiningMonth())
								.occupationDesc(StringUtils.isBlank(p.getOccupatonDesc())?"":p.getOccupatonDesc())
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
							.dateOfJoiningYear(StringUtils.isBlank(p.getDateOfJoining())?"":p.getDateOfJoining())
							.occupationDesc(StringUtils.isBlank(p.getOccupatonDesc())?"":p.getOccupatonDesc())
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
					for(List<EserviceMotorDetailsRaw> eservice :partitions) {
						List<CompletableFuture<Object>> comFuture = new ArrayList<CompletableFuture<Object>>();
						Long maxVehId =transRepo.getVehicleId(eservice.get(0).getRequestReferenceNo(), eservice.get(0).getProductId().toString());
						Long countVeh =1L;
						for(EserviceMotorDetailsRaw raw :eservice) {
							 Long vehicleId =maxVehId+countVeh;
							 CompletableFuture<Object> asyncList =asyncProcess.createQuote(raw, auth,vehicleId);
							 comFuture.add(asyncList);
							 countVeh++;
						}							
						@SuppressWarnings("unchecked")
						CompletableFuture<Object>[] comArray =new CompletableFuture[comFuture.size()];
						comFuture.toArray(comArray);
						CompletableFuture.allOf(comArray).join();
							
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
						detailRaw.setDateOfJoining(req.getDateOfJoiningYear());
						detailRaw.setDateOfJoiningMonth(req.getDateOfJoiningMonth());
						detailRaw.setNationalityId(req.getNationalityId());
						detailRaw.setOccupationId(req.getOccupationId());
						detailRaw.setOccupatonDesc(req.getOccupationDesc());
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
		try {
			EwayUploadTypeMaster typeMaster =uploadTypeRepo.findByCompanyIdAndProductIdAndStatus(Integer.valueOf(req.getCompanyId()),Integer.valueOf(req.getProductId()),"Y");
			String file_path =typeMaster.getFilePath();
			String dataUri ="data:application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;base64,";
			Path path =Paths.get(file_path);
			byte [] bytes =Files.readAllBytes(path);
			String encodeString =Base64.getEncoder().encodeToString(bytes);
			String base64 =dataUri+encodeString;
			res.setMessage("SUCCESS");
			Map<String,Object> map =new HashMap<String,Object>();
			map.put("Base64", base64);
			map.put("FileName", typeMaster.getTypename());
			map.put("Message", "SUCCESS");
			res.setCommonResponse(map);
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
		Subquery<Long> effectiveDate = query.subquery(Long.class);
		Root<ListItemValue> ocpm1 = effectiveDate.from(ListItemValue.class);
		effectiveDate.select(cb.max(ocpm1.get("effectiveDateStart")));
		Predicate a1 = cb.equal(c.get("itemId"),ocpm1.get("itemId"));
		Predicate a2 = cb.lessThanOrEqualTo(ocpm1.get("effectiveDateStart"), today);
		effectiveDate.where(a1,a2);
		// Effective Date End Max Filter
		Subquery<Long> effectiveDate2 = query.subquery(Long.class);
		Root<ListItemValue> ocpm2 = effectiveDate2.from(ListItemValue.class);
		effectiveDate2.select(cb.max(ocpm2.get("effectiveDateEnd")));
		Predicate a3 = cb.equal(c.get("itemId"),ocpm2.get("itemId"));
		Predicate a4 = cb.greaterThanOrEqualTo(ocpm2.get("effectiveDateEnd"), todayEnd);
		effectiveDate2.where(a3,a4);
					
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




}
