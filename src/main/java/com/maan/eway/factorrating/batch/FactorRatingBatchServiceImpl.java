package com.maan.eway.factorrating.batch;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
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
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;
import com.maan.eway.batch.repository.TransactionControlDetailsRepository;
import com.maan.eway.bean.FactorTypeDetails;
import com.maan.eway.bean.ListItemValue;
import com.maan.eway.bean.SectionCoverMaster;
import com.maan.eway.common.res.CommonRes;
import com.maan.eway.factorrating.batch.configuration.Grouping_Thread_Job;
import com.maan.eway.factorrating.batch.configuration.MainInsert_Thread_Job;
import com.maan.eway.factorrating.batch.configuration.RawInsert_Thread_Job;
import com.maan.eway.fileupload.FileUploadInputRequest;
import com.maan.eway.fileupload.JpqlQueryServiceImpl;
import com.maan.eway.master.req.FactorRateSaveReq;
import com.maan.eway.master.service.impl.FactorRateMasterServiceImpl;
import com.maan.eway.res.DropDownRes;
import com.maan.eway.springbatch.FactorRateRawInsert;
import com.maan.eway.springbatch.FactorRateRawMasterRepository;
import com.maan.eway.springbatch.TransactionControlDetails;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
public class FactorRatingBatchServiceImpl implements FactorRatingBatchService {

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
    
    
    @Autowired
	private TransactionControlDetailsRepository controlDetailsRepository;
    
    @Autowired
	private FactorRateMasterServiceImpl service; 
	
	
	
	@Override
	public CommonRes convertExcelToCSV(MultipartFile file,Integer product_id) {
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
	        
	        TransactionControlDetails controlDetails =null;
			Long count=controlDetailsRepository.count();
    		Long tranId =count==0?1:count+1;
    		String refeNo ="FACTOR_"+String.valueOf(tranId);
	        
	        JobParameters jobParameters = new JobParametersBuilder()
	        		.addString("csv_file_path", csv_file)
	        		.addString("excel_file_path", xlfilePath)
	        		.addString("file_extension", extension)
	        		.addString("factor_id", refeNo)
                    .addLong("startAt", System.currentTimeMillis()).toJobParameters();	        		
	        jobLauncher.run(excelToCsvJob, jobParameters);
       
			log.info("convertExcelToCSV end time : "+new Date());
			
			
			 controlDetails = TransactionControlDetails.builder()
					.branchCode(null)
					.companyId(null)
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
			String effDate =new SimpleDateFormat("dd/MM/yyyy hh:MM:ss").format(effectiveDate);
			String remarks = StringUtils.isBlank(sectionCov.get(0).getRemarks())?"":sectionCov.get(0).getRemarks();  
			String createdBy = StringUtils.isBlank(sectionCov.get(0).getCreatedBy())?"":sectionCov.get(0).getCreatedBy();  

			List<FactorTypeDetails> flist=queryService.getFactorRateColumns(res,factorTypeId);
			        	
			StringJoiner entityColumns =new StringJoiner("~");
		    StringJoiner xlheaderCol =new StringJoiner("~");
		    StringJoiner discreateColumns =new StringJoiner("~");
					         	
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
			        }else if(fac.getRangeYn().equalsIgnoreCase("N")) {	            			
			        		entityColumns.add(fac.getDiscreteColumn());
			        		xlheaderCol.add(fac.getDiscreteDisplayName());
			        		discreateColumns.add(fac.getDiscreteColumn());
			        }	
			      }
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
			req.setAgencyCode(frri.getAgencyCode());
			req.setBranchCode(frri.getBranchCode());
			req.setCompanyId(frri.getCompanyId());
			req.setProductId(frri.getProductId().toString());
			req.setSectionId(frri.getSectionId().toString());
			req.setSubCoverId(frri.getSubCoverId().toString());
			req.setCoverId(frri.getCoverId().toString());
			req.setFactorTypeId(frri.getFactorTypeId().toString());
			req.setCreatedBy(frri.getCreatedBy());
			req.setEffectiveDateStart(frri.getEffectiveDateStart());
			
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

			List<FactorTypeDetails> flist=queryService.getFactorRateColumns(req,frri.getFactorTypeId().toString());
        	
        	String discreate_columns =flist.stream().filter(p -> "N".equals(p.getRangeYn())).map(p -> p.getDiscreteColumn())
        	            .collect(Collectors.joining("~"));
        	
        	String isDiscreate =StringUtils.isBlank(discreate_columns)?"N":"Y";
			Map<String,List<DropDownRes>> dropDownList =new HashMap<String,List<DropDownRes>>();

        	FactorRateSaveReq factorRateSaveReq = new FactorRateSaveReq();
			factorRateSaveReq.setAgencyCode(frri.getAgencyCode());
			factorRateSaveReq.setBranchCode(frri.getBranchCode());
			factorRateSaveReq.setCompanyId(frri.getCompanyId());
			factorRateSaveReq.setCoverId(frri.getCoverId().toString());
			factorRateSaveReq.setSectionId(frri.getSectionId().toString());
			factorRateSaveReq.setProductId(frri.getProductId().toString());
			factorRateSaveReq.setFactorTypeId(frri.getFactorTypeId().toString());
			dropDownList= rateMasterServiceImpl.masterDiscreteApiCall(factorRateSaveReq,token.replaceAll("Bearer ", "").split(",")[0]);						
        	  
			String dropwon_data =print.toJson(dropDownList);
			Grouping_Thread_Job thread_Job = new Grouping_Thread_Job(tran_id,discreate_columns,isDiscreate
					,groupingJob,jobLauncher,total_records,dropwon_data);
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
		
}
