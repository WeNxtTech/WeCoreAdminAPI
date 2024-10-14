package com.maan.eway.factorrating.batch;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.StringJoiner;
import java.util.stream.Collectors;

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
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;
import com.maan.eway.batch.repository.TransactionControlDetailsRepository;
import com.maan.eway.batch.req.EwayUploadReq;
import com.maan.eway.bean.FactorTypeDetails;
import com.maan.eway.bean.ListItemValue;
import com.maan.eway.bean.SectionCoverMaster;
import com.maan.eway.common.res.CommonRes;
import com.maan.eway.error.Error;
import com.maan.eway.factorrating.batch.configuration.Grouping_Thread_Job;
import com.maan.eway.factorrating.batch.configuration.MainInsert_Thread_Job;
import com.maan.eway.factorrating.batch.configuration.RawInsert_Thread_Job;
import com.maan.eway.fileupload.FileDownloadRequest;
import com.maan.eway.fileupload.FileUploadInputRequest;
import com.maan.eway.fileupload.JpqlQueryServiceImpl;
import com.maan.eway.master.req.FactorParamsInsert;
import com.maan.eway.master.req.FactorRateSaveReq;
import com.maan.eway.master.service.impl.FactorRateMasterServiceImpl;
import com.maan.eway.res.DropDownRes;
import com.maan.eway.springbatch.FactorRateRawInsert;
import com.maan.eway.springbatch.FactorRateRawMasterRepository;
import com.maan.eway.springbatch.TransactionControlDetails;
import com.maan.eway.vehicleupload.VehicleBatchServiceImpl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
public class FactorRatingBatchServiceImpl implements FactorRatingBatchService,Serializable {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Logger log = LogManager.getLogger(FactorRatingBatchServiceImpl.class);
	
	private static Gson print = new Gson();
	
	@Value("${excel.upload.path}")
	private String excel_upload_path;
	
	@Value("${csv.upload.path}")
	private String csv_upload_path;
	
	
	
	@PersistenceContext
	private EntityManager em;
	
	@Autowired
	private FactorRateRawMasterRepository rawMasterRepository;
	
	@Autowired
	private JpqlQueryServiceImpl queryService;
	
	@Autowired
	private FactorRateMasterServiceImpl rateMasterServiceImpl;
	
	@Autowired
	private VehicleBatchServiceImpl sequence;
	
	@Autowired
    JobLauncher jobLauncher;
	
    @Autowired
	@Qualifier("excelToCsvJob")
    Job excelToCsvJob;
    
    @Autowired
	@Qualifier("rawdataInsertBatchJob")
    Job rawdataInsertBatchJob;
    
    @Autowired
   	@Qualifier("mainDataInsertJob")
    Job mainDataInsertJob;
    
    @Autowired
   	@Qualifier("groupingJob")
    Job groupingJob;
    
	public static Map<String,Map<String, List<FactorRateRawInsert>>> LOCAL_DATA_STORAGE= new HashMap<>();

    @Autowired
	private TransactionControlDetailsRepository controlDetailsRepository;
    
    @Autowired
	private FactorRateMasterServiceImpl service; 
	
	

	
	@Override
	public CommonRes convertExcelToCSV(MultipartFile file,Integer product_id,Integer company_id,String request_ref_no) {
		CommonRes response = new CommonRes();
		try {
			
			log.info("convertExcelToCSV start time : "+new Date());
			
			String file_name =FilenameUtils.getBaseName(file.getOriginalFilename());
			String extension =FilenameUtils.getExtension(file.getOriginalFilename());
			String xlfilePath =excel_upload_path+file_name+System.currentTimeMillis()+"."+extension;
			byte[] bytes = file.getBytes();
	        Path path = Paths.get(xlfilePath);
	        Files.write(path, bytes);
	        String csv_file =csv_upload_path+file_name+System.currentTimeMillis()+".csv";
	        File f = new File(csv_file);
	        
	        if(f.exists()) {
	        	f.delete();
	        	log.info(csv_file+" file has been deleted");
	        }else {
	        	f.createNewFile();
	        }
	        
	    
	        String refeNo="";
	        if("0".equals(request_ref_no)|| StringUtils.isBlank(request_ref_no)) {
	    		EwayUploadReq seqReq = new EwayUploadReq();
	    		seqReq.setCompanyId(company_id.toString());
	    		seqReq.setProductId(product_id.toString());
	    		
	    		refeNo =sequence.generateSeqCall(seqReq);
	        }else {
	        	refeNo = request_ref_no;
	        }
    		
	        JobParameters jobParameters = new JobParametersBuilder()
	        		.addString("csv_file_path", csv_file)
	        		.addString("excel_file_path", xlfilePath)
	        		.addString("file_extension", extension)
	        		.addString("factor_id", refeNo)
                    .addLong("startAt", System.currentTimeMillis()).toJobParameters();	        		
	        jobLauncher.run(excelToCsvJob, jobParameters);
       
			log.info("convertExcelToCSV end time : "+new Date());
						
			TransactionControlDetails controlDetails = TransactionControlDetails.builder()
					.branchCode(null)
					.companyId(company_id)
					.entryDate(new Date())
					.errorDescription(null)
					.errorRecords(0)
					.validRecords(0)
					.totalRecords(0)
					.fileName(file_name)
					.filePath(xlfilePath)
					.csvFilePath(csv_file)
					.lastUpdatedDate(new Date())
					.loadPercentage(null)
					.productId(product_id)
					.progressDescription("Excel to Csv Converting...")
					.requestReferenceNo(refeNo)
					.sectionId(0)
					.status("P")
					.typeId(Long.valueOf(0))
					.tranDate(new Date())
					.build();
			TransactionControlDetails result =controlDetailsRepository.saveAndFlush(controlDetails);

	        Map<String,String> map = new HashMap<>();
	        map.put("progress_description", result.getProgressDescription() );
	        map.put("status", result.getStatus());
	        map.put("tran_id", result.getRequestReferenceNo());
	        			
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
	public CommonRes rawdataInsert(FileUploadInputRequest res) {
		log.info("rawdataInsert request : "+print.toJson(res));
		CommonRes response = new CommonRes();

		try {
			TransactionControlDetails controlDetails=controlDetailsRepository.findByRequestReferenceNo(res.getTranId());
			controlDetails.setBranchCode(StringUtils.isBlank(res.getBranchCode())?"":res.getBranchCode());
			controlDetails.setCompanyId(StringUtils.isBlank(res.getInsuranceId())?null:Integer.valueOf(res.getInsuranceId()));
			controlDetails.setLastUpdatedDate(new Date());
			controlDetails.setLoginName(StringUtils.isBlank(res.getCreatedBy())?"":res.getCreatedBy());
			controlDetails.setProgressDescription("RawData is inserting...");
			controlDetails.setSectionId(StringUtils.isBlank(res.getSectionId())?null:Integer.valueOf(res.getSectionId()));
			controlDetails.setStatus("P");
			controlDetails.setErrorRecords(0);
			controlDetails.setTotalRecords(0);
			controlDetails.setValidRecords(0);
			controlDetails.setTypeid(0);
			TransactionControlDetails tcd =controlDetailsRepository.save(controlDetails);
						
			res.setCsvFilePath(tcd.getCsvFilePath());
			res.setProgressStatus(tcd.getStatus());
			res.setProgressDesc(tcd.getProgressDescription());
			
			// get dynamic columns from DB
			List<SectionCoverMaster> sectionCov=queryService.getSectionCoverMaster(res); 
			String factorTypeId = StringUtils.isBlank(sectionCov.get(0).getFactorTypeId().toString())?"":sectionCov.get(0).getFactorTypeId().toString();
			Date effectiveDate =sectionCov.get(0).getEffectiveDateStart().toString()==null?null:sectionCov.get(0).getEffectiveDateStart(); 
			
			//String effDate =new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(effectiveDate);
			
			String effDate =new SimpleDateFormat("dd/MM/yyyy").format(effectiveDate);
			
			String remarks = StringUtils.isBlank(sectionCov.get(0).getRemarks())?"":sectionCov.get(0).getRemarks();  
			String createdBy = StringUtils.isBlank(sectionCov.get(0).getCreatedBy())?"":sectionCov.get(0).getCreatedBy();  
			String minimum_rateyn =StringUtils.isBlank(sectionCov.get(0).getMinimumRateYn())?"N":sectionCov.get(0).getMinimumRateYn();  
			List<FactorTypeDetails> flist=queryService.getFactorRateColumns(res,factorTypeId);
			        	
			StringJoiner entityColumns =new StringJoiner("~");
		    StringJoiner xlheaderCol =new StringJoiner("~");
		    StringJoiner discreateColumns =new StringJoiner("~");
		    StringJoiner range_columns =new StringJoiner("~");
		

			entityColumns.add("sNo");
			xlheaderCol.add("AgencyCode");// default XL columns
			entityColumns.add("xlAgencyCode");// default entity columns
				for(int i=0;i<flist.size();i++) {	            		
					FactorTypeDetails fac =flist.get(i);	            	
			        if(fac.getRangeYn().equalsIgnoreCase("Y")) {
			        		entityColumns.add(fac.getRangeFromColumn());
			        		entityColumns.add(fac.getRangeToColumn());
			        		xlheaderCol.add(fac.getFromDisplayName());
			        		xlheaderCol.add(fac.getToDisplayName());
			        		range_columns.add(fac.getRangeFromColumn());
			        		range_columns.add(fac.getRangeToColumn());

			        }else if(fac.getRangeYn().equalsIgnoreCase("N")) {	            			
			        		entityColumns.add(fac.getDiscreteColumn());
			        		xlheaderCol.add(fac.getDiscreteDisplayName());
			        		discreateColumns.add(fac.getDiscreteColumn());
			        }	
			      }
			        
					if("Y".equalsIgnoreCase(minimum_rateyn)) {

						// entity columns,// default entity columns
					       entityColumns.add("rate");
					       entityColumns.add("minimumRate");
					       entityColumns.add("calcType");
					       entityColumns.add("minPremium");
					       entityColumns.add("regulatoryCode");
					       entityColumns.add("excessPercent");
					       entityColumns.add("excessAmount");
					       entityColumns.add("excessDesc");
					       entityColumns.add("status");
					        	// default xl headercolumns
					       xlheaderCol.add("Rate");
					       xlheaderCol.add("MinimumRate");
					       xlheaderCol.add("CalcType");
					       xlheaderCol.add("MinimumPremium");
					       xlheaderCol.add("RegulatoryCode");
					       xlheaderCol.add("ExcessPercent");
					       xlheaderCol.add("ExcessAmount");
					       xlheaderCol.add("ExcessDesc");
					       xlheaderCol.add("Status");
					}else {
						
						// entity columns,// default entity columns
					       entityColumns.add("rate");
					       entityColumns.add("calcType");
					       entityColumns.add("minPremium");
					       entityColumns.add("regulatoryCode");
					       entityColumns.add("excessPercent");
					       entityColumns.add("excessAmount");
					       entityColumns.add("excessDesc");
					       entityColumns.add("status");
					        	// default xl headercolumns
					       xlheaderCol.add("Rate");
					       xlheaderCol.add("CalcType");
					       xlheaderCol.add("MinimumPremium");
					       xlheaderCol.add("RegulatoryCode");
					       xlheaderCol.add("ExcessPercent");
					       xlheaderCol.add("ExcessAmount");
					       xlheaderCol.add("ExcessDesc");
					       xlheaderCol.add("Status");
					}
			        	
				   res.setMinimumRateYn(minimum_rateyn);
			       res.setExcelHeaderColumns(xlheaderCol.toString());
			       res.setColumns(entityColumns.toString());
			       res.setRemarks(remarks);
			       res.setFactorTypeId(factorTypeId);
			       res.setEffectiveDate(effDate);
			       res.setRemarks(remarks);
			       res.setCreatedBy(createdBy);
			       res.setDiscreteColumn(discreateColumns.toString());
			       res.setTotalRecordsCount(String.valueOf(0));
			       res.setProgressStatus("P");
			       res.setStatus("P");
			       res.setProgressDesc("Pending");
			       res.setProgressErrorDesc("");
			       res.setRangeColumns(range_columns.toString());
			       Boolean columnStatus =checkMismatchedColumns(res);
			     
			  if(columnStatus) {  
				  
				  Long totalRows =0L;
				  Resource resource = (new FileSystemResource(res.getCsvFilePath()));
			        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
			        	totalRows = reader.lines().count();
			        }
				  
			      log.info("totalRows : "+totalRows);
				  RawInsert_Thread_Job thread_Job = new RawInsert_Thread_Job(res,jobLauncher,rawdataInsertBatchJob,totalRows,res.getTranId());
				  Thread thread = new Thread(thread_Job);
				  thread.setName("RAWDATA_BATCH");
				  thread.setPriority(Thread.MAX_PRIORITY);
				  thread.setDaemon(false);
				  thread.start();
				//  updateBatchTransaction (res.getTranId(), "Rawdata insert batch strated" ,"","Progressing","P");

			  }
			  
			 Map<String,String> map = new HashMap<>();
		     map.put("progress_description", tcd.getProgressDescription());
		     map.put("status", tcd.getStatus());
		     map.put("tran_id", tcd.getRequestReferenceNo());
			  
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
	
	private Boolean checkMismatchedColumns(FileUploadInputRequest request) {
		updateBatchTransaction (request.getTranId(), "Validating Excel Header Columns.." ,"","Progressing",null);
		try {
			File csvFile = new File(request.getCsvFilePath());
			String [] excelHeaders=null;
			BufferedReader in = null;
			String record = "", result = "", unmatched = "";
			long totalLinesProcessed = 0l;
			if(csvFile.exists() && csvFile.canRead()) {
				in = new BufferedReader(new FileReader(request.getCsvFilePath()));
				while ((record = in.readLine()) != null) {
					///System.out.println("status----->" + record);
					totalLinesProcessed++;
					
					if (totalLinesProcessed == 1) {
						//record=record.replaceAll("~Sno~VehicleNo", "");
						excelHeaders = record.split("~");
						//System.out.println(excelHeaders.length);
						}
					}
			}
			System.out.println(excelHeaders.length +" || "+1+request.getExcelHeaderColumns().split("~").length);
			Boolean columnLength =excelHeaders.length==request.getExcelHeaderColumns().split("~").length+1?true:false;
			if(columnLength) {
				
				List<String> s1 =new ArrayList<String>(Arrays.asList(excelHeaders));
				List<String> s2 =new ArrayList<String>(Arrays.asList(request.getExcelHeaderColumns()));
				
				boolean status =s1.toString().replaceAll("\\s", "").contentEquals(s2.toString().replaceAll("\\s", ""))?true:false; 
				
				return true;
			}else {
				updateBatchTransaction (request.getTranId(), "Validating Excel Header Columns.." ,"Xl Heder columns is not matched","Progressing","E");

				return false;
			}
				
		}catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			updateBatchTransaction (request.getTranId(), e.getMessage() ,"Error","Error","E");

		}
		return null;
		
	}

	  public void updateBatchTransaction(String tranId,String progressStatus,String errordesc,String progrssDesc,String loading){
	    	TransactionControlDetails t =null;
	    	try {
				Long total=0L;
				Long error_records=0L;
				Long valid_records =0L;
				t =controlDetailsRepository.findByRequestReferenceNo(tranId);
				t.setProgressDescription(progressStatus); 
				t.setErrorDescription(errordesc); 
				t.setStatus(loading);				
				error_records=rawMasterRepository.countByTranIdAndErrorStatus(tranId, "E");
				total=rawMasterRepository.countByTranId(tranId);
				valid_records=total-error_records;				
				t.setTotalRecords(total.intValue());				
				t.setErrorRecords(error_records.intValue());
				t.setValidRecords(valid_records.intValue());
				controlDetailsRepository.saveAndFlush(t);
				}catch (Exception e) {
					t.setErrorDescription(e.getMessage());
					t.setStatus("E");
					t.setProgressDescription(e.getMessage());
					controlDetailsRepository.saveAndFlush(t);
					log.error(e);e.printStackTrace();}
		}

	  
	  public void updateMainDataDetails(String tranId,String progressStatus,String errordesc,String progrssDesc,String loading){
	    	TransactionControlDetails t =null;
	    	try {				
				t =controlDetailsRepository.findByRequestReferenceNo(tranId);
				t.setProgressDescription(progressStatus); 
				t.setErrorDescription(errordesc); 
				t.setStatus(loading);				
				controlDetailsRepository.saveAndFlush(t);
				}catch (Exception e) {
					t.setErrorDescription(e.getMessage());
					t.setStatus("E");
					t.setProgressDescription(e.getMessage());
					controlDetailsRepository.saveAndFlush(t);
					log.error(e);e.printStackTrace();}
		}
	  
	  public void updateCsvRecordDetails(String tranId,String progressStatus,String errordesc,String progrssDesc,String loading){
	    	TransactionControlDetails t =null;
	    	try {
				
				t =controlDetailsRepository.findByRequestReferenceNo(tranId);
				t.setProgressDescription(progressStatus); 
				t.setErrorDescription(errordesc); 
				t.setStatus(loading);	
				
				Long totalRows =0L;
				  Resource resource = (new FileSystemResource(t.getCsvFilePath()));
			        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
			        	totalRows = reader.lines().count();
			        }catch (Exception e) {
						e.printStackTrace();
					}				
				t.setTotalRecords(totalRows.intValue()-1);
				controlDetailsRepository.saveAndFlush(t);
				}catch (Exception e) {
					t.setErrorDescription(e.getMessage());
					t.setStatus("E");
					t.setProgressDescription(e.getMessage());
					controlDetailsRepository.saveAndFlush(t);
					log.error(e);e.printStackTrace();}
		}


	  public void updateRawDataRecords(String tranId,String progressStatus,String errordesc,String progrssDesc,String loading){
	    	TransactionControlDetails t =null;
	    	try {			
					t =controlDetailsRepository.findByRequestReferenceNo(tranId);
					t.setProgressDescription(progrssDesc); 
					t.setErrorDescription(errordesc); 
					t.setStatus(loading);				
					Long total=rawMasterRepository.countByTranId(tranId);
					t.setTotalRecords(total.intValue());				
					controlDetailsRepository.saveAndFlush(t);
				}catch (Exception e) {
					t.setErrorDescription(e.getMessage());
					t.setStatus("E");
					t.setProgressDescription(e.getMessage());
					controlDetailsRepository.saveAndFlush(t);
					log.error(e);e.printStackTrace();}
		}

	@Override
	public CommonRes maindataInsert(String tran_id) {
		CommonRes response = new CommonRes();
		try {
			
			TransactionControlDetails controlDetails=controlDetailsRepository.findByRequestReferenceNo(tran_id);
			controlDetails.setLastUpdatedDate(new Date());
			controlDetails.setProgressDescription("Records are inserting into main table...");
			controlDetails.setStatus("P");
			TransactionControlDetails tcd =controlDetailsRepository.save(controlDetails);
			
			Long total_records =rawMasterRepository.countByTranIdAndErrorStatusIsNull(tran_id);
			FactorRateRawInsert frri =rawMasterRepository.findByTranIdAndSno(tran_id,1);
			FactorRateSaveReq req = new FactorRateSaveReq();
			//req.setAgencyCode(frri.get);
			req.setBranchCode(frri.getBranchCode());
			req.setCompanyId(frri.getCompanyId());
			req.setProductId(frri.getProductId().toString());
			req.setSectionId(frri.getSectionId().toString());
			req.setSubCoverId(frri.getSubCoverId().toString());
			req.setCoverId(frri.getCoverId().toString());
			req.setFactorTypeId(frri.getFactorTypeId().toString());
			req.setCreatedBy(frri.getCreatedBy());
			req.setEffectiveDateStart(frri.getEffectiveDateStart());
			
			List<String> list = rawMasterRepository.getXlAgencyCode(tran_id);
			String agency_codes = list.stream().collect(Collectors.joining("~"));
			req.setAgencyCode(agency_codes);
			Integer amendId = service.upadateOldFactor(req);
			List<ListItemValue> itemValues=service.getListItem("99999",req.getBranchCode(),"CALCULATION_TYPE");
			Map<String,Object> coverDetails=service.coverMasterDetails(req);
			
			Long gridSize =20L;
			
			MainInsert_Thread_Job mainInsert= new MainInsert_Thread_Job(amendId,coverDetails,itemValues,
					total_records,tran_id,gridSize,jobLauncher,mainDataInsertJob);
			Thread thread = new Thread(mainInsert);
			thread.setName("MainInsert");
			thread.setDaemon(false);
			thread.setPriority(Thread.MAX_PRIORITY);
			thread.start();
			
			Map<String,String> map = new HashMap<String, String>();
			map.put("progress_description", tcd.getProgressDescription());
		    map.put("status", tcd.getStatus());
		    map.put("tran_id", tcd.getRequestReferenceNo());
					
			response.setErrorMessage(Collections.EMPTY_LIST);
			response.setErroCode(0);
			response.setIsError(false);
			response.setMessage("Success");
			response.setCommonResponse(map);
			
		}catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			response.setErrorMessage(Collections.EMPTY_LIST);
			response.setErroCode(0);
			response.setIsError(false);
			response.setMessage("Failed");
			response.setCommonResponse(null);
			return response;
		}
		return response;
	}



	@Override
	public CommonRes validateReocrds(String tran_id,String token) {
		CommonRes response = new CommonRes();
		try {
			
			TransactionControlDetails controlDetails=controlDetailsRepository.findByRequestReferenceNo(tran_id);
			controlDetails.setLastUpdatedDate(new Date());
			controlDetails.setProgressDescription("Validating Records...");
			controlDetails.setStatus("P");
			TransactionControlDetails tcd =controlDetailsRepository.save(controlDetails);
			
			
			Long total_records =rawMasterRepository.countByTranIdAndErrorStatusIsNull(tran_id);
			FactorRateRawInsert frri =rawMasterRepository.findByTranIdAndSno(tran_id,1);
			FileUploadInputRequest req = new FileUploadInputRequest();
			req.setAgencyCode(frri.getAgencyCode());
			req.setBranchCode(frri.getBranchCode());
			req.setInsuranceId(frri.getCompanyId());
			req.setProductId(frri.getProductId().toString());
			req.setSectionId(frri.getSectionId().toString());
			req.setSubCoverId(frri.getSubCoverId().toString());
			req.setCoverId(frri.getCoverId().toString());
			req.setFactorTypeId(frri.getFactorTypeId().toString());
			req.setCreatedBy(frri.getCreatedBy());

			FileDownloadRequest getMinimumRateYnReq = new FileDownloadRequest();
			getMinimumRateYnReq.setAgencyCode(frri.getAgencyCode());
			getMinimumRateYnReq.setBranchCode(frri.getBranchCode());
			getMinimumRateYnReq.setCompanyId(frri.getCompanyId());
			getMinimumRateYnReq.setProductId(frri.getProductId().toString());
			getMinimumRateYnReq.setSectionId(frri.getSectionId().toString());
			getMinimumRateYnReq.setSubCoverId(frri.getSubCoverId().toString());
			getMinimumRateYnReq.setCoverId(frri.getCoverId().toString());
			
			Map<String,Object> factorMap =queryService.getFactorXlColumns(getMinimumRateYnReq);
			String minimumRateYn = factorMap.get("MINIMUM_RATEYN").toString();
			
			List<FactorTypeDetails> flist=queryService.getFactorRateColumns(req,frri.getFactorTypeId().toString());
        	
        	String discreate_columns =flist.stream().filter(p -> "N".equals(p.getRangeYn())).map(p -> p.getDiscreteColumn())
        	            .collect(Collectors.joining("~"));
        	
        	String rageColumns =flist.stream().filter(p -> "Y".equals(p.getRangeYn())).map(p -> p.getRangeFromColumn()+"~"+ p.getRangeToColumn())
    	            .collect(Collectors.joining("~"));
        	
        	String isDiscreate =StringUtils.isBlank(discreate_columns)?"N":"Y";
			

        	FactorRateSaveReq factorRateSaveReq = new FactorRateSaveReq();
			factorRateSaveReq.setAgencyCode(frri.getAgencyCode());
			factorRateSaveReq.setBranchCode(frri.getBranchCode());
			factorRateSaveReq.setCompanyId(frri.getCompanyId());
			factorRateSaveReq.setCoverId(frri.getCoverId().toString());
			factorRateSaveReq.setSectionId(frri.getSectionId().toString());
			factorRateSaveReq.setProductId(frri.getProductId().toString());
			factorRateSaveReq.setFactorTypeId(frri.getFactorTypeId().toString());
			//dropDownList= rateMasterServiceImpl.masterDiscreteApiCall(factorRateSaveReq,token.replaceAll("Bearer ", "").split(",")[0]);						
        	  
			//String dropwon_data =print.toJson(dropDownList);
			Grouping_Thread_Job thread_Job = new Grouping_Thread_Job(tran_id,discreate_columns,isDiscreate
					,groupingJob,jobLauncher,total_records,token,rageColumns,minimumRateYn);
			Thread thread = new Thread(thread_Job);
			thread.setName("GROUP_RECORDS");
			thread.setPriority(Thread.MAX_PRIORITY);
			thread.setDaemon(false);
			thread.start();
			
			Map<String,String> map = new HashMap<String, String>();
			map.put("progress_description", tcd.getProgressDescription());
		    map.put("status", tcd.getStatus());
		    map.put("tran_id", tcd.getRequestReferenceNo());
					
			response.setErrorMessage(Collections.EMPTY_LIST);
			response.setErroCode(0);
			response.setIsError(false);
			response.setMessage("Success");
			response.setCommonResponse(map);
		}catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			response.setErrorMessage(Collections.EMPTY_LIST);
			response.setErroCode(0);
			response.setIsError(false);
			response.setMessage("Failed");
			response.setCommonResponse(null);
			return response;
		}
		return response;
	}
	
	@Transactional
	public List<FactorRateRawInsert> factorRangeValidation(List<FactorRateRawInsert> list,String rangeColumns,String discreateColumns,String factor_id, Map<String, List<DropDownRes>> drop,String minimum_rate_yn) {
		try {
			
			
			// RANGE VALIDATION BLOCK			
			String [] range_array=rangeColumns.split("~");
			List<Integer> sno =list.parallelStream().map(s -> s.getSno()).collect(Collectors.toList());
			Double[][] values =getValues(range_array, list);	
			Integer[][] paramColumns =getParamValues(range_array);
			List<Integer> checkForDuplicates=checkForDuplicates(values,paramColumns,sno);			
			
			if(!checkForDuplicates.isEmpty()) {				
				queryService.updateErrorRecords(factor_id, checkForDuplicates, "Duplicate range or between range has found with this row in your uploaded excel");											
			}
							
			// DUPLICATE ROWS FIND BLOCK			
			if (StringUtils.isNotBlank(discreateColumns)) {
				
				List<String> groupByColumns =new ArrayList<>();
				List<String> range = new ArrayList<>(5);
				List<String> discreate = new ArrayList<>(5);
				
				if(StringUtils.isNotBlank(discreateColumns))
					discreate =Arrays.stream(discreateColumns.split("~")).collect(Collectors.toList());
				
				if(StringUtils.isNotBlank(rangeColumns))
					range =Arrays.stream(rangeColumns.split("~")).collect(Collectors.toList());
	
				groupByColumns.addAll(range);				
				groupByColumns.addAll(discreate);
				
				List<FactorRateRawInsert> duplicateRecordsList =new ArrayList<>();
			    Map<List<Object>,List<FactorRateRawInsert>> groupByData=groupByRecords(list, groupByColumns);
			    groupByData.entrySet().parallelStream().forEach(gp ->{		    	
			    	if(gp.getValue().size()>1) {
			    		duplicateRecordsList.addAll(gp.getValue());
			    		System.out.println(duplicateRecordsList.size());
			    	}			    	
			    });
			    
			    List<Integer> snoIds =duplicateRecordsList.parallelStream().map(map -> map.getSno()).collect(Collectors.toList());
				if(!snoIds.isEmpty())
					queryService.updateErrorRecords(factor_id, snoIds, "Duplicate rows have found with this row in your uploaded excel");											
			
			
				
				// Discreate Coulmns Checking
				
				List<String> originalKeys =new ArrayList<>(drop.keySet());
				
				Map<String, List<DropDownRes>> filterKeyData =discreate.stream().filter(p ->originalKeys.contains(p))
						.collect(Collectors.toMap(k -> k, drop ::get));
				
				//List<Integer> invalidSnoIds = new ArrayList<>();
							
				
				// Process entries
		        for (Map.Entry<String, List<DropDownRes>> entry : filterKeyData.entrySet()) {
		            String keyName = entry.getKey();
		            Set<String> masterValues = entry.getValue().stream()
		                                               .map(DropDownRes::getCode)
		                                               .collect(Collectors.toSet());
		            		            
		            List<String> rawMasterIds = list.stream().map(p -> getMapValues(p,keyName)).distinct().collect(Collectors.toList());
		            		
		            List<String> inValidMasterId =rawMasterIds.stream()
		            		.filter(p ->!masterValues.contains(p) && !"99999".equals(p)).distinct()
		            		.collect(Collectors.toList());
		            
		            if(!inValidMasterId.isEmpty()) {
						List<String> filterDuplicateSno =inValidMasterId.parallelStream().distinct().collect(Collectors.toList());
						queryService.updateErrorRecords(keyName,factor_id, filterDuplicateSno, "Invalid Discreate values found in this row");											
					}
				
		            
		        }
			    	      
		   
			}else if(StringUtils.isBlank(discreateColumns)) {
				String groupbyColumns ="";
				if("Y".equalsIgnoreCase(minimum_rate_yn))
					groupbyColumns ="xlAgencyCode~"+rangeColumns+"~rate~minimumRate~calcType~minPremium";
				else
					groupbyColumns ="xlAgencyCode~"+rangeColumns+"~rate~calcType~minPremium";

				List<String> groupbyColumnsList =Arrays.stream(groupbyColumns.split("~")).collect(Collectors.toList());
				List<FactorRateRawInsert> duplicateRecordsList =new ArrayList<>();
			    Map<List<Object>,List<FactorRateRawInsert>> groupByData=groupByRecords(list, groupbyColumnsList);
			    groupByData.entrySet().parallelStream().forEach(gp ->{		    	
			    	if(gp.getValue().size()>1) {
			    		duplicateRecordsList.addAll(gp.getValue());
			    	}			    	
			    });
			    
			    List<Integer> snoIds =duplicateRecordsList.parallelStream().map(map -> map.getSno()).collect(Collectors.toList());
				if(!snoIds.isEmpty())
					queryService.updateErrorRecords(factor_id, snoIds, "Duplicate rows have found with this row in your uploaded excel");											
			
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
    private static <T> boolean isInvalid(T item, String fieldName, Set<String> masterValues) {
        try {
            Field field = item.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            String fieldValue = field.get(item).toString();
            return !masterValues.contains(fieldValue);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static <T> String getMapValues(T item, String fieldName) {
        try {
            Field field = item.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            String fieldValue = field.get(item).toString();
            return fieldValue;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
		return fieldName;
    }

	
	
	
	 public static List<Integer> checkForDuplicates1(Double[][] table, Integer[][] paramColumns, List<Integer> sno) {
	    	List<Integer> list = new ArrayList<>();
	    	List<Double[]> ranges = new ArrayList<>();
	    	for (int i = 0; i < table.length; i++) {
	    		Double[] row = table[i];	            
	        	for (Double[] range : ranges) {
	                if (isOverlapping(range, row, paramColumns)) {
	                	list.add(sno.get(i));
	                }
	            }
	         
	        	
	        		ranges.add(row);
	        	
	        }
	        return list; // No duplicates found
	    }

	    public static boolean isOverlapping(Double[] oldRow, Double[] newRow, Integer[][] paramColumns) {
	        for (Integer[] cols : paramColumns) {
	            if (oldRow[cols[0]] < newRow[cols[1]] && newRow[cols[0]] < oldRow[cols[1]]) {
	                return true;
	            }
	        }
	        return false;
	    }
	    
	    public static Double[][] getValues(String[] fieldNames, List<FactorRateRawInsert> list) throws Exception {
	        // Initialize base array with appropriate dimensions
	    	Double[][] base = new Double[list.size()][fieldNames.length];
	        int index = 0;
	        for (FactorRateRawInsert obj : list) {
	        	Double[] data = new Double[fieldNames.length];
	            for (int i = 0; i < fieldNames.length; i++) {
	                try {
	                    Field field = FactorRateRawInsert.class.getDeclaredField(fieldNames[i]);
	                    field.setAccessible(true);
	                    
	                    data[i] = Double.valueOf(field.get(obj)==null||field.get(obj).toString().isEmpty() ?"0":field.get(obj).toString());
	                } catch (NoSuchFieldException | IllegalAccessException e) {
	                    // Handle or log the exception as needed
	                    data[i] = null; // or handle the error case appropriately
	                }
	            }
	            base[index] = data;
	            index++;
	        }
	        
	        return base;
	    }
	    
	    public static Double[][] getValuesForFactorParamInsert(String[] fieldNames, List<FactorParamsInsert> list) throws Exception {
	        // Initialize base array with appropriate dimensions
	    	Double[][] base = new Double[list.size()][fieldNames.length];
	        int index = 0;
	        for (FactorParamsInsert obj : list) {
	        	Double[] data = new Double[fieldNames.length];
	            for (int i = 0; i < fieldNames.length; i++) {
	                try {
	                    Field field = FactorParamsInsert.class.getDeclaredField(fieldNames[i]);
	                    field.setAccessible(true);
	                    
	                    data[i] = Double.valueOf(field.get(obj)==null||field.get(obj).toString().isEmpty() ?"0":field.get(obj).toString());
	                } catch (NoSuchFieldException | IllegalAccessException e) {
	                    // Handle or log the exception as needed
	                    data[i] = null; // or handle the error case appropriately
	                }
	            }
	            base[index] = data;
	            index++;
	        }
	        
	        return base;
	    }
	    
	    public static Integer[][] getParamValues(String [] inputArray){
	    	int length =inputArray.length/2;
	    	Integer[][] output = new Integer[length][2];
	    	int start =0;
	    	for(int i =0;i<length;i++) {
	    		int end = start + 1; // End index is start + 1
	            
	            // Initialize the inner array and assign start and end values
	            output[i] = new Integer[] { start, end };
	            
	            // Update start for the next iteration
	            start = end + 1;
	    		
	    	}
	    	
	    	return output;
	    }
	    
	    public static Map<List<Object>, List<FactorRateRawInsert>> groupByRecords(List<FactorRateRawInsert> list, List<String> fieldNames) {
	        return list.stream().collect(Collectors.groupingBy(obj -> {
	          List<Object> key =new ArrayList<>();
	            for (String fieldName : fieldNames) {
	                try {
	                    Field field = obj.getClass().getDeclaredField(fieldName);
	                    field.setAccessible(true);
	                    key.add(field.get(obj));
	                } catch (Exception e) {
	                    throw new RuntimeException(e);
	                }
	            }
	            return key;
	        }));
	    }
	    
	    
	    public static Map<List<Object>, List<FactorParamsInsert>> groupByRecords_1(List<FactorParamsInsert> list, List<String> fieldNames) {
	        return list.stream().collect(Collectors.groupingBy(obj -> {
	          List<Object> key =new ArrayList<>();
	            for (String fieldName : fieldNames) {
	                try {
	                    Field field = obj.getClass().getDeclaredField(fieldName);
	                    field.setAccessible(true);
	                    key.add(field.get(obj));
	                } catch (Exception e) {
	                    throw new RuntimeException(e);
	                }
	            }
	            return key;
	        }));
	    }
	    
	    public static List<Integer> checkForDuplicates(Double[][] table, Integer[][] paramColumns, List<Integer> sno) {
	        List<Range> ranges = new ArrayList<>();
	        for (int i = 0; i < table.length; i++) {
	            Double[] row = table[i];
	            ranges.add(new Range(row, sno.get(i)));
	        }

	        // Sort ranges by their start position
	        ranges.sort((r1, r2) -> Double.compare(r1.row[0], r2.row[0]));

	        List<Integer> result = new ArrayList<>();
	        PriorityQueue<Range> activeRanges = new PriorityQueue<>((r1, r2) -> Double.compare(r1.row[1], r2.row[1]));

	        for (Range current : ranges) {
	            // Remove inactive ranges
	            while (!activeRanges.isEmpty() && activeRanges.peek().row[1] < current.row[0]) {
	                activeRanges.poll();
	            }

	            // Check for overlaps
	            for (Range active : activeRanges) {
	                if (isOverlapping(active, current, paramColumns)) {
	                    result.add(current.sno);
	                    break;
	                }
	            }

	            // Add the current range to active ranges
	            activeRanges.add(current);
	        }

	        return result;
	    }

	    private static boolean isOverlapping(Range r1, Range r2, Integer[][] paramColumns) {
	        Double[] oldRow = r1.row;
	        Double[] newRow = r2.row;

	        for (Integer[] cols : paramColumns) {
	            double oldStart = oldRow[cols[0]];
	            double oldEnd = oldRow[cols[1]];
	            double newStart = newRow[cols[0]];
	            double newEnd = newRow[cols[1]];
	            
	            if((newStart>oldStart && newStart<oldEnd) || (newEnd>oldStart &&  newEnd<oldEnd)) {	            	
	            	return true;
	            }

	        }
			return false;
	       
	    }

	    private static class Range {
	        Double[] row;
	        Integer sno;

	        Range(Double[] row, Integer sno) {
	            this.row = row;
	            this.sno = sno;
	        }
	    }

		@Override
		public CommonRes getErrorRecords(String tranId) {
			CommonRes response = new CommonRes();
			try {
				
				FactorRateRawInsert frri = rawMasterRepository.findByTranIdAndSno(tranId, 1);
				
				FileUploadInputRequest req = new FileUploadInputRequest();
				req.setInsuranceId(frri.getCompanyId());
				req.setProductId(frri.getProductId().toString());
				req.setCoverId(frri.getCoverId().toString());
				req.setSectionId(frri.getSectionId().toString());
				req.setSubCoverId(frri.getSubCoverId().toString());
				
				// get dynamic columns from DB
				List<SectionCoverMaster> sectionCov=queryService.getSectionCoverMaster(req); 
				String factorTypeId = StringUtils.isBlank(sectionCov.get(0).getFactorTypeId().toString())?"":sectionCov.get(0).getFactorTypeId().toString();
				
				//String effDate =new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(effectiveDate);
				String minimum_rateyn =StringUtils.isBlank(sectionCov.get(0).getMinimumRateYn())?"N":sectionCov.get(0).getMinimumRateYn();  
				List<FactorTypeDetails> flist=queryService.getFactorRateColumns(req,factorTypeId);
				        	
				StringJoiner entityColumns =new StringJoiner(",");
			    StringJoiner xlheaderCol =new StringJoiner("~");
			  
			    
				xlheaderCol.add("ErrorDescription");
				xlheaderCol.add("RowNumber");
				xlheaderCol.add("AgencyCode");// default XL columns
					for(int i=0;i<flist.size();i++) {	            		
						FactorTypeDetails fac =flist.get(i);	            	
				        if(fac.getRangeYn().equalsIgnoreCase("Y")) {
				        		entityColumns.add(fac.getRangeFromColumn());
				        		entityColumns.add(fac.getRangeToColumn());
				        		xlheaderCol.add(fac.getFromDisplayName());
				        		xlheaderCol.add(fac.getToDisplayName());
				        	

				        }else if(fac.getRangeYn().equalsIgnoreCase("N")) {	            			
				        		entityColumns.add(fac.getDiscreteColumn());
				        		xlheaderCol.add(fac.getDiscreteDisplayName());
				        }	
				      }
				        
						if("Y".equalsIgnoreCase(minimum_rateyn)) {

							// entity columns,// default entity columns
						       entityColumns.add("rate");
						       entityColumns.add("minimumRate");
						       entityColumns.add("calcType");
						       entityColumns.add("minPremium");
						       entityColumns.add("regulatoryCode");
						       entityColumns.add("excessPercent");
						       entityColumns.add("excessAmount");
						       entityColumns.add("excessDesc");
						       entityColumns.add("status");
						        	// default xl headercolumns
						       xlheaderCol.add("Rate");
						       xlheaderCol.add("MinimumRate");
						       xlheaderCol.add("CalcType");
						       xlheaderCol.add("MinimumPremium");
						       xlheaderCol.add("RegulatoryCode");
						       xlheaderCol.add("ExcessPercent");
						       xlheaderCol.add("ExcessAmount");
						       xlheaderCol.add("ExcessDesc");
						       xlheaderCol.add("Status");
						}else {
							
							// entity columns,// default entity columns
						       entityColumns.add("rate");
						       entityColumns.add("calcType");
						       entityColumns.add("minPremium");
						       entityColumns.add("regulatoryCode");
						       entityColumns.add("excessPercent");
						       entityColumns.add("excessAmount");
						       entityColumns.add("excessDesc");
						       entityColumns.add("status");
						        	// default xl headercolumns
						       xlheaderCol.add("Rate");
						       xlheaderCol.add("CalcType");
						       xlheaderCol.add("MinimumPremium");
						       xlheaderCol.add("RegulatoryCode");
						       xlheaderCol.add("ExcessPercent");
						       xlheaderCol.add("ExcessAmount");
						       xlheaderCol.add("ExcessDesc");
						       xlheaderCol.add("Status");
						}
						
						
				String [] table_header = xlheaderCol.toString().split("~");
				List<Object[]> data = queryService.getRawErrorRecords(tranId, entityColumns.toString());
				Map<String,Object> map = new HashMap<String, Object>();
				map.put("Headers", table_header);
				map.put("Values", data);
				
				response.setMessage("Success");
				response.setErrorMessage(Collections.emptyList());
				response.setCommonResponse(map);
				
			}catch (Exception e) {
				e.printStackTrace();
				response.setMessage("Failed");
				response.setErrorMessage(Collections.emptyList());
				response.setCommonResponse(e.getMessage());
				return response;
			}
			return response;
		}
	    
		
		//@Transactional
		public List<Error> factorRangeValidationForInsertFactorRate(List<FactorParamsInsert> list,String rangeColumns,String discreateColumns,String factor_id, Map<String, List<DropDownRes>> drop,String minimum_rate_yn) {
			List<Error> errorList = new ArrayList<Error>();
			try {
				
				// RANGE VALIDATION BLOCK			
				String [] range_array=rangeColumns.split("~");
				List<Integer> sno =list.parallelStream().map(s -> Integer.valueOf(s.getSno())).collect(Collectors.toList());
				Double[][] values =getValuesForFactorParamInsert(range_array, list);	
				Integer[][] paramColumns =getParamValues(range_array);
				List<Integer> checkForDuplicates=checkForDuplicates(values,paramColumns,sno);			
				
				if(!checkForDuplicates.isEmpty()) {				
					//queryService.updateErrorRecords(factor_id, checkForDuplicates, "Duplicate range or between range has found with this row in your uploaded excel");																
					String str = checkForDuplicates.parallelStream().map(m -> m.toString()).collect(Collectors.joining(","));
					
					errorList.add(new Error("100","DUPLICATE RANGE VALUES HAVE FOUND BELOW ROWS ::",""+str+" "));
				
				}
								
				// DUPLICATE ROWS FIND BLOCK			
				if (StringUtils.isNotBlank(discreateColumns)) {
					
					List<String> groupByColumns =new ArrayList<>();
					List<String> range = new ArrayList<>(5);
					List<String> discreate = new ArrayList<>(5);
					
					if(StringUtils.isNotBlank(discreateColumns))
						discreate =Arrays.stream(discreateColumns.split("~")).collect(Collectors.toList());
					
					if(StringUtils.isNotBlank(rangeColumns))
						range =Arrays.stream(rangeColumns.split("~")).collect(Collectors.toList());
		
					groupByColumns.addAll(range);				
					groupByColumns.addAll(discreate);
					
					List<FactorParamsInsert> duplicateRecordsList =new ArrayList<>();
				    Map<List<Object>,List<FactorParamsInsert>> groupByData=groupByRecords_1(list, groupByColumns);
				    groupByData.entrySet().parallelStream().forEach(gp ->{		    	
				    	if(gp.getValue().size()>1) {
				    		duplicateRecordsList.addAll(gp.getValue());
				    	}			    	
				    });
				    
				    List<Integer> snoIds =duplicateRecordsList.parallelStream().map(map -> Integer.valueOf(map.getSno())).collect(Collectors.toList());
					if(!snoIds.isEmpty()) {
						String str = snoIds.parallelStream().map(m -> m.toString()).collect(Collectors.joining(","));
						errorList.add(new Error("100","DUPLICATE ROWS VALUES HAVE FOUND BELOW ROWS::",""+str+" "));

						//queryService.updateErrorRecords(factor_id, snoIds, "Duplicate rows have found with this row in your uploaded excel");											
					}
				
					
					// Discreate Coulmns Checking
					
					List<String> originalKeys =new ArrayList<>(drop.keySet());
					
					Map<String, List<DropDownRes>> filterKeyData =discreate.stream().filter(p ->originalKeys.contains(p))
							.collect(Collectors.toMap(k -> k, drop ::get));
					
					//List<Integer> invalidSnoIds = new ArrayList<>();
								
					
					// Process entries
			        for (Map.Entry<String, List<DropDownRes>> entry : filterKeyData.entrySet()) {
			            String keyName = entry.getKey();
			            Set<String> masterValues = entry.getValue().stream()
			                                               .map(DropDownRes::getCode)
			                                               .collect(Collectors.toSet());
			            		            
			            List<String> rawMasterIds = list.stream().map(p -> getMapValues(p,keyName)).distinct().collect(Collectors.toList());
			            		
			            List<String> inValidMasterId =rawMasterIds.stream()
			            		.filter(p ->!masterValues.contains(p) && !"99999".equals(p)).distinct()          		
			            		.collect(Collectors.toList());
			            
			            if(!inValidMasterId.isEmpty()) {
							List<String> filterDuplicateSno =inValidMasterId.parallelStream().distinct().collect(Collectors.toList());
							String str = filterDuplicateSno.parallelStream().map(m -> m).collect(Collectors.joining(","));
							errorList.add(new Error("100","INVALID DISCREATE VALUES HAVE FOUND FOR BELOW VALUES ::",""+str+" "));

							//queryService.updateErrorRecords(keyName,factor_id, filterDuplicateSno, "Invalid Discreate values found in this row");											
						}
					
			            
			        }
				    	      
			   
				}else if(StringUtils.isBlank(discreateColumns)) {
					String groupbyColumns ="";
					if("Y".equalsIgnoreCase(minimum_rate_yn))
						groupbyColumns ="xlAgencyCode~"+rangeColumns+"~rate~minimumRate~calcType~minPremium";
					else
						groupbyColumns ="xlAgencyCode~"+rangeColumns+"~rate~calcType~minPremium";

					List<String> groupbyColumnsList =Arrays.stream(groupbyColumns.split("~")).collect(Collectors.toList());
					List<FactorParamsInsert> duplicateRecordsList =new ArrayList<>();
				    Map<List<Object>,List<FactorParamsInsert>> groupByData=groupByRecords_1(list, groupbyColumnsList);
				    groupByData.entrySet().parallelStream().forEach(gp ->{		    	
				    	if(gp.getValue().size()>1) {
				    		duplicateRecordsList.addAll(gp.getValue());
				    	}			    	
				    });
				    
				    List<Integer> snoIds =duplicateRecordsList.parallelStream().map(map -> Integer.valueOf(map.getSno())).collect(Collectors.toList());
					if(!snoIds.isEmpty()) {
						String str = snoIds.parallelStream().map(m -> m.toString()).collect(Collectors.joining(","));
						errorList.add(new Error("100","DUPLICATE ROWS HAVE FOUND BELOW ROWS::",""+str+" "));
					}
						//queryService.updateErrorRecords(factor_id, snoIds, "Duplicate rows have found with this row in your uploaded excel");											
				
				}
				
			}catch (Exception e) {
				e.printStackTrace();
			}
			
			return errorList;
		}
	  
	  
	
}
		

