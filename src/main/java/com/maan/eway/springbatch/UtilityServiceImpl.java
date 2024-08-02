package com.maan.eway.springbatch;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.StringJoiner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import org.apache.commons.collections4.ListUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.dozer.DozerBeanMapper;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.maan.eway.batch.repository.TransactionControlDetailsRepository;
import com.maan.eway.bean.FactorTypeDetails;
import com.maan.eway.bean.SectionCoverMaster;
import com.maan.eway.error.Error;
import com.maan.eway.fileupload.FileUploadInputRequest;
import com.maan.eway.fileupload.JpqlQueryServiceImpl;
import com.maan.eway.master.req.FactorParamsInsert;
import com.maan.eway.master.req.FactorRateSaveReq;
import com.maan.eway.master.service.FactorRateMasterService;
import com.maan.eway.master.service.impl.FactorRateMasterServiceImpl;
import com.maan.eway.res.CommonRes;
import com.maan.eway.res.DropDownRes;

@Service
@Lazy
public class UtilityServiceImpl {
	
	Logger log =LogManager.getLogger(getClass());
	
	@Value("${excel.upload.path}")
	private String xlpath;
	
	@Value("${csv.upload.path}")
	private String csvPath;
	
	@PersistenceContext
	private EntityManager em;
	
	@Autowired
	private JpqlQueryServiceImpl queryService;
	@Autowired
	private FactorRateRawMasterRepository rawMasterRepository;
	@Autowired
	private FactorRateMasterService entityService;
	@Autowired
	private AsyncProcessThread processThread;
	@Autowired
	private FactorRateMasterServiceImpl rateMasterServiceImpl;
	@Autowired
	private TransactionControlDetailsRepository controlDetailsRepository;
	
	public static  int totalRecordCount =0;
	
	public static DozerBeanMapper mapper =new DozerBeanMapper();
	
	private static final DataFormatter ORIGINAL_VALUE = new DataFormatter();


	@Autowired
    JobLauncher jobLauncher;
	
    @Autowired
	@Qualifier("ewayJobProcess")
    Job processJob;
    
    @Autowired
    @Qualifier("MainTableJob")
    Job job;
    
   static private Gson print  = new Gson();
    
   static ObjectMapper objectMapper = new ObjectMapper();

	private  static String NEW_LINE_CHARACTER = "\r\n";
	private final static String OUTPUT_DATE_FORMAT="dd/MM/yyyy";
	private final static String CVS_SEPERATOR_CHAR = "\t";
	
	SimpleDateFormat sdf =new SimpleDateFormat("dd/mm/yyyy") ;
	
	public FileUploadInputRequest saveExcelfile(FileUploadInputRequest request, MultipartFile file) {
		try {
			log.info("saveExcelfile to path");
			String fileName =FilenameUtils.getBaseName(file.getOriginalFilename());
			String extension="."+FilenameUtils.getExtension(file.getOriginalFilename());
			String currentDateTime = sdf.format(new Date()).replaceAll("[^0-9]", "");
			Random random = new Random();
			random.nextInt();
			String xlfilePath =xlpath+fileName+currentDateTime+random.nextInt()+extension;			
			// save file into path
			log.info("Enter || xlfilePath || "+xlfilePath);
			File copyFile =new File(xlfilePath);
			file.transferTo(copyFile);
			request.setXlFilePath(xlfilePath);
			request.setFileName(fileName);
			request.setProgressDesc("Progressing");
			request.setProgressStatus("P");
			request.setStatus("P");
		}catch (Exception e) {
			log.error(e);
			e.printStackTrace();
		}
		return request;
		
	}

	public void convertToCsvFile(UtilityServiceImpl utilService, FileUploadInputRequest request) {
		log.info("Enter || convertToCsvFile method ||");
		updateBatchTransaction (request.getTranId(), "Csvfile covertion start" ,"","Progressing",null);
		try {
			//CSV File convertion start
			//String csvFileName = request.get.replace(".xlsx", ".csv").replace(".xls", ".csv").replace(".XLSX", ".csv").replace(".XLS", ".csv");
			String currentDateTime = sdf.format(new Date()).replaceAll("[^0-9]", "");
			String csvFilePath =  csvPath+request.getFileName()+currentDateTime+".csv";
			File csvFile = new File(csvFilePath);
			if(csvFile.exists()) {
				if(csvFile.delete()) 
				{ log.info("File deleted successfully"); } 
			}
			
			int toltalNoRows=csvConvertion(request ,csvFilePath);
		 	log.info("ConvertToCsvFile || toltalNoRows || :" +toltalNoRows);
			totalRecordCount=toltalNoRows;
			//CSV File convertion end
			request.setCsvFilePath(csvFilePath);
			
			updateBatchTransaction (request.getTranId(), "getting entity columns from table" ,"","Progressing","P");

			// get dynamic columns from DB
			List<SectionCoverMaster> sectionCov=queryService.getSectionCoverMaster(request); 
        	String factorTypeId = StringUtils.isBlank(sectionCov.get(0).getFactorTypeId().toString())?"":sectionCov.get(0).getFactorTypeId().toString();
        	Date effectiveDate =sectionCov.get(0).getEffectiveDateStart().toString()==null?null:sectionCov.get(0).getEffectiveDateStart(); 
        	String effDate =new SimpleDateFormat("dd/MM/yyyy hh:MM:ss").format(effectiveDate);
        	String remarks = StringUtils.isBlank(sectionCov.get(0).getRemarks())?"":sectionCov.get(0).getRemarks();  
        	String createdBy = StringUtils.isBlank(sectionCov.get(0).getCreatedBy())?"":sectionCov.get(0).getCreatedBy();  

        	List<FactorTypeDetails> flist=queryService.getFactorRateColumns(request,factorTypeId);
        	
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
        	
        	request.setExcelHeaderColumns(xlheaderCol.toString());
        	request.setColumns(entityColumns.toString());
        	request.setRemarks(remarks);
        	request.setFactorTypeId(factorTypeId);
        	request.setEffectiveDate(effDate);
        	request.setRemarks(remarks);
        	request.setCreatedBy(createdBy);
        	request.setDiscreteColumn(discreateColumns.toString());
        	request.setTotalRecordsCount(String.valueOf(toltalNoRows));
        	Boolean columnStatus =checkMismatchedColumns(request);
        	
        	if(columnStatus) {
				updateBatchTransaction (request.getTranId(), "spring batch process calling" ,"","Progressing","P");
	
	        	log.info("Spring batch job started ");
	        	JobParameters jobParameters = new JobParametersBuilder()
						.addLong("time", System.currentTimeMillis())
				     	.addString("ewayBatchData", print.toJson(request))
				        .toJobParameters();
						jobLauncher.run(processJob, jobParameters);
        	}
  
		}catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			updateBatchTransaction (request.getTranId(), e.getMessage() ,"Error","Error","E");

		}
		
	}

	
	private Boolean checkMismatchedColumns(FileUploadInputRequest request) {
		updateBatchTransaction (request.getTranId(), "checking columns mismatching columns" ,"","Progressing",null);
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
				updateBatchTransaction (request.getTranId(), "checking mismatching columns" ,"Xl Heder columns is not matched","Progressing","E");

				return false;
			}
				
		}catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			updateBatchTransaction (request.getTranId(), e.getMessage() ,"Error","Error","E");

		}
		return null;
		
	}

	public int csvConvertion(FileUploadInputRequest request, String csvFilePath){
		int tolalNoofRowsinFile=0;
        try
        {
           String excelFileName=request.getXlFilePath();
           String fileType=FilenameUtils.getExtension(excelFileName);
           if("xlsx".equals(fileType))
           {
           	tolalNoofRowsinFile=excelXToCSV(excelFileName, csvFilePath,request);
           } else
           if("xls".equals(fileType))
           {
           	tolalNoofRowsinFile=excelToCSV(excelFileName, csvFilePath,request);
           }
        }catch(Exception e){log.error(e);
		updateBatchTransaction (request.getTranId(), e.getMessage() ,"Error","Error","E");
		e.printStackTrace();

        }
        return tolalNoofRowsinFile;
    }
	
	//XLSX TO CSV
	 public  int excelXToCSV(String excelFileName, String csvFileName, FileUploadInputRequest request) throws  Exception {
		 int tolalNoofRowsinFile=0;
		 try
		    {
		          checkValidFile(excelFileName);
		          File file = new File(excelFileName);
		          OPCPackage opcPackage = OPCPackage.open(file.getAbsolutePath());
		          @SuppressWarnings("resource")
		          XSSFWorkbook myWorkBook = new XSSFWorkbook(opcPackage);
		          XSSFSheet mySheet = myWorkBook.getSheetAt(0);
		          int count = 0;
		          int firstCount = 0;
		          int rows = mySheet.getPhysicalNumberOfRows();
		          tolalNoofRowsinFile = rows;
		          for(int eachRow = 0; eachRow < rows; eachRow++){
		              String csvData = "";
		              String snoAndVehNo = String.valueOf(eachRow);
		              XSSFRow myRow = mySheet.getRow(eachRow);
		              
		              //System.out.println("RowNo : " + eachRow);
		              System.out.println("myRow is Empty"+ myRow!=null); 
		              if(++count == 1){
		                  firstCount = myRow.getLastCellNum();
		              }
		              for(int i = 0; i < firstCount; i++){

		            	  csvData = (String.valueOf(csvData)+getXLSXCellData(myRow.getCell(i))+"~")
			                		  .replace("\t", "").replace("\n", "").replace("\r", "").replace("'", "").replaceAll("^\"|\"$", "").replace(",", "");
		              }
		              if(StringUtils.isNotBlank(csvData)) {
		            	  if(eachRow==0) {
		            		  csvData= "sNo~"+csvData;
		            	  }else {
		            		  csvData=String.valueOf(eachRow)+"~"+csvData;
		            	  }
							
			              writeCSV(csvFileName, (String.valueOf(csvData.substring(0, csvData.length() - 1))).trim()+"\r\n");
		              }
		            }
		    }catch(Exception e){e.printStackTrace();
			updateBatchTransaction (request.getTranId(), e.getMessage() ,"Error","Error","E");

		    }finally{
				updateBatchTransaction (request.getTranId(), "Csvfile covertion completed" ,"","Progressing",null);
	        }
		 return tolalNoofRowsinFile;
		 }
	 
	 public int excelToCSV(String excelFileName, String csvFileName, FileUploadInputRequest request)
	    {
			int tolalNoofRowsinFile=0;
	        try
	        {
	        	log.info("xls File Start Coverting to csv.....");
	            checkValidFile(excelFileName);
	            HSSFWorkbook myWorkBook = new HSSFWorkbook(new POIFSFileSystem(new FileInputStream(excelFileName)));
	            HSSFSheet mySheet = myWorkBook.getSheetAt(0);
	            Iterator rowIter = mySheet.rowIterator();
	            int count = 0;
	            int rowcount = 0;
	            int firstCount = 0;
	            String csvData;
	            for(; rowIter.hasNext(); writeCSV(csvFileName, (new StringBuilder(String.valueOf(csvData.substring(0, csvData.length() - 1)).trim())).append("\r\n").toString()))
	            {
	                csvData = "";
	                HSSFRow myRow = (HSSFRow)rowIter.next();
	                if(++count == 1)
	                {
	                    firstCount = myRow.getLastCellNum();
	                }
	                for(int i = 0; i < firstCount; i++)
	                {
	                    csvData = String.valueOf(csvData)+getCellData(myRow.getCell(i)).replace("\t", "").replace("\n", "").replace("\r", "").replace("'", "").replaceAll("^\"|\"$", "").replace(",", "")+"~";
	                }
	                int rownum = ++rowcount;
	                if( rownum== 1) {
	                	
	                	csvData="Sno~"+csvData;
	                	
	  	            }else {
	  	            	
	  	            	csvData=String.valueOf(rownum-1)+"~"+csvData;
	                	
	  	              

	  	            }

	  	            }
	              
	            tolalNoofRowsinFile=count;
	        } catch(Exception e) {e.printStackTrace();
			updateBatchTransaction (request.getTranId(), e.getMessage() ,"Error","Error","E");

	        }finally{
				updateBatchTransaction (request.getTranId(), "Csvfile covertion completed" ,"","Progressing",null);
	        }
	        return tolalNoofRowsinFile;
	    }
		
	 
	 	private void writeCSV(String csvFileName, String csvData)
		        throws Exception
		    	{
	    		//String removedWhilteSpace =csvData.replaceAll("\\s", "");
	    		//log.info("removedWhilteSpace data : "+removedWhilteSpace);
		        FileOutputStream writer = new FileOutputStream(csvFileName, true);
		        writer.write(csvData.getBytes());
		        writer.close();
		    	}
	 	
	 	
	 	private static void checkValidFile(String fileName)
	    {
	        boolean valid = true;
	        try
	        {
	            File f = new File(fileName);
	            if(!f.exists() || f.isDirectory())
	            {
	                valid = false;
	            }
	        }
	        catch(Exception e)
	        {
	            valid = false;
	        }
	        if(!valid)
	        {
	            System.out.println("File doesn't exist: "+fileName);
	            System.exit(0);
	        }
	    }
	 	
	 	
	 	private  String getXLSXCellData(XSSFCell myCell)
	            throws Exception
	        {
	 		  
	            String cellData = "";
	            if(myCell == null)
	            {
	                cellData = String.valueOf(cellData)+CVS_SEPERATOR_CHAR;
	            } else
	            {
	                switch(myCell.getCellType())
	                {
	                case STRING: 
	                	cellData = String.valueOf(cellData)+ORIGINAL_VALUE.formatCellValue(myCell)+CVS_SEPERATOR_CHAR;
	                    break;
	                case BOOLEAN:               cellData = String.valueOf(cellData)+ORIGINAL_VALUE.formatCellValue(myCell)+CVS_SEPERATOR_CHAR;
	                    break;

	                case NUMERIC:  
	                    cellData = String.valueOf(cellData)+getXLSXNumericValue(myCell)+CVS_SEPERATOR_CHAR;
	                    break;

	                case FORMULA: 
	                    cellData = String.valueOf(cellData)+getXLSXFormulaValue(myCell)+CVS_SEPERATOR_CHAR;
	                case BLANK: 
	                default:
	                    cellData = String.valueOf(cellData)+CVS_SEPERATOR_CHAR;
	                    break;
	                }
	            }
	            return cellData.trim();
	        }
	    
		private static String getXLSXNumericValue(XSSFCell myCell)
	            throws Exception
	        {
	            String cellData = "";
	            
	            
	            if(DateUtil.isCellDateFormatted(myCell))
	            {
	                java.util.Date obj = myCell.getDateCellValue();
	                cellData = String.valueOf(cellData)+new SimpleDateFormat(OUTPUT_DATE_FORMAT).format(obj)+CVS_SEPERATOR_CHAR;//new SimpleDateFormat(OUTPUT_DATE_FORMAT).format(
	            } else
	            {
	                //cellData = String.valueOf(cellData)+myCell.getNumericCellValue()+CVS_SEPERATOR_CHAR;
	            	String cellValue =ORIGINAL_VALUE.formatCellValue(myCell);
		            System.out.println("cellValue || "+cellValue);

	            	try{
	            		cellData = cellData+cellValue+CVS_SEPERATOR_CHAR;
	            	}catch(Exception e){
	            		cellData = "";
	            		cellData = cellData+ORIGINAL_VALUE.formatCellValue(myCell)+CVS_SEPERATOR_CHAR;
	            	}
	            }
	            
	            System.out.println("Last || "+cellData);
	            return cellData;
	        }

	    private static String getXLSXFormulaValue(XSSFCell myCell)
	            throws Exception
	        {
	            String cellData = "";
	            if(myCell.getCachedFormulaResultType() == CellType.STRING || myCell.getCellType() == CellType.BOOLEAN)
	            {
	                cellData = cellData+ORIGINAL_VALUE.formatCellValue(myCell)+CVS_SEPERATOR_CHAR;
	            } else
	            if(myCell.getCachedFormulaResultType() == CellType.NUMERIC)
	            {
	                cellData = cellData+getXLSXNumericValue(myCell)+CVS_SEPERATOR_CHAR;
	            }
	            return cellData;
	        }
	    
	    
	    private static String getCellData(HSSFCell myCell)
		        throws Exception
		    {
		        String cellData = "";
		        if(myCell == null)
		        {
		            cellData = String.valueOf(cellData)+CVS_SEPERATOR_CHAR;
		        } else
		        {
		            switch(myCell.getCellType())
		            {
		            case STRING: 
		            	cellData = String.valueOf(cellData)+myCell.getRichStringCellValue()+CVS_SEPERATOR_CHAR;
		                break;
		            case BOOLEAN: 
		                cellData = String.valueOf(cellData)+myCell.getBooleanCellValue()+CVS_SEPERATOR_CHAR;
		                break;

		            case NUMERIC: 
		                cellData = String.valueOf(cellData)+getNumericValue(myCell);
		                break;

		            case FORMULA: 
		                cellData = String.valueOf(cellData)+getFormulaValue(myCell);

		            case BLANK: 
		            default:
		                cellData = String.valueOf(cellData)+CVS_SEPERATOR_CHAR;
		                break;
		            }
		        }
		        return cellData.trim();
		    }
	    
	    
	    private static String getNumericValue(HSSFCell myCell)
		        throws Exception
		    {
		        String cellData = "";
		        if(DateUtil.isCellDateFormatted(myCell))
		        {
		            java.util.Date obj = myCell.getDateCellValue();
		            cellData = String.valueOf(cellData)+new SimpleDateFormat(OUTPUT_DATE_FORMAT).format(obj)+CVS_SEPERATOR_CHAR;
		        } else
		        {
		            //DataFormatter formatter = new DataFormatter();
		            //cellData = String.valueOf(cellData)+formatter.formatCellValue(myCell)+CVS_SEPERATOR_CHAR;
		        	cellData = String.valueOf(cellData)+(long)myCell.getNumericCellValue()+CVS_SEPERATOR_CHAR;
		        }
		        return cellData;
		    }
	    
	    
	    private static String getFormulaValue(HSSFCell myCell)
		        throws Exception
		    {
		        String cellData = "";
		        if(myCell.getCachedFormulaResultType() == CellType.STRING ||(myCell.getCachedFormulaResultType() == CellType.BOOLEAN))
		        {
		            cellData = String.valueOf(cellData)+myCell.getRichStringCellValue()+CVS_SEPERATOR_CHAR;
		        } else
		        if(myCell.getCachedFormulaResultType()== CellType.NUMERIC)
		        {
		            cellData = String.valueOf(cellData)+getNumericValue(myCell)+CVS_SEPERATOR_CHAR;
		        }
		        return cellData;
		    }
	    
	    
	    
	    public void updateBatchTransaction(String tranId,String progressStatus,String errordesc,String progrssDesc,String loading){
	    	TransactionControlDetails t =null;
	    	try {
				Long total=0L;
				Long error_records=0L;
				Long valid_records =0L;
				t =controlDetailsRepository.findByRequestReferenceNo(tranId);
				t.setProgressDescription(progrssDesc); 
				t.setErrorDescription(errordesc); 
				t.setStatus(loading);
				
				List<FactorRateRawInsert> list =rawMasterRepository.findByTranId(tranId);
				if(!CollectionUtils.isEmpty(list)) {
					
					 total =list.stream().count();
					 error_records =list.stream().filter(p ->"E".equalsIgnoreCase(p.getErrorStatus()) || "E".equals(p.getStatus()))
							.count();
					 valid_records =total - error_records;
					
				}
				t.setTotalRecords(total.intValue());
				t.setErrorRecords(error_records.intValue());
				t.setValidRecords(valid_records.intValue());
				controlDetailsRepository.saveAndFlush(t);
				//saveUploadTransactionData(uploadResponse);
				}catch (Exception e) {
					t.setErrorDescription(e.getMessage());
					t.setStatus("E");
					t.setProgressDescription(e.getMessage());
					controlDetailsRepository.saveAndFlush(t);
					log.error(e);e.printStackTrace();}
		}
		
		public void saveUploadTransactionData(FileUploadInputRequest res ) {
			TransactionControlDetails controlDetails =null;
			try {
				Long count=controlDetailsRepository.count();
	    		Long tranId =count==0?1:count+1;
	    		String refeNo ="FACTOR_"+String.valueOf(tranId);
				 controlDetails = TransactionControlDetails.builder()
						.branchCode(StringUtils.isBlank(res.getBranchCode())?"":res.getBranchCode())
						.companyId(StringUtils.isBlank(res.getInsuranceId())?null:Integer.valueOf(res.getInsuranceId()))
						.entryDate(new Date())
						.errorDescription(StringUtils.isBlank(res.getProgressErrorDesc())?"":res.getProgressErrorDesc())
						//.errorRecords(StringUtils.isBlank(res.getErrorRecords())?0:Integer.valueOf(res.getErrorRecords()))
						//.validRecords(StringUtils.isBlank(res.get)?0:Integer.valueOf(res.getValidRecords()))
						//.totalRecords(StringUtils.isBlank(res.getTotalRecordsCount()())?0:Integer.valueOf(res.getTotalRecordsCount()))
						.fileName(StringUtils.isBlank(res.getFileName())?"":res.getFileName())
						.filePath(StringUtils.isBlank(res.getXlFilePath())?"":res.getXlFilePath())
						.lastUpdatedDate(new Date())
						.loadPercentage(null)
						.loginName(StringUtils.isBlank(res.getCreatedBy())?"":res.getCreatedBy())
						.productId(StringUtils.isBlank(res.getProductId())?null:Integer.valueOf(res.getProductId()))
						.progressDescription(StringUtils.isBlank(res.getProgressDesc())?"":res.getProgressDesc())
						.requestReferenceNo(refeNo)
						.sectionId(StringUtils.isBlank(res.getSectionId())?null:Integer.valueOf(res.getSectionId()))
						.status(StringUtils.isBlank(res.getProgressStatus())?"":res.getProgressStatus())
						.typeId(Long.valueOf(0))
						.tranDate(new Date())
						.build();
				TransactionControlDetails result =controlDetailsRepository.saveAndFlush(controlDetails);
				res.setTranId(result.getRequestReferenceNo());
			}catch (Exception e) {
				log.error(e);
				e.printStackTrace();
				controlDetails.setErrorDescription(e.getMessage());
				controlDetails.setStatus("E");
				controlDetails.setProgressDescription(e.getMessage());
				controlDetailsRepository.saveAndFlush(controlDetails);
			}
		}
	    
	    
	   public String updateFactorRawRecordByTranId(String tranId, String discreateColumns, String auth) {
		   String response="";
		   try {
			  Map<String,List<FactorRateRawInsert>> loadList =new HashMap<String,List<FactorRateRawInsert>>();
			
			  AtomicInteger uniqueId =new AtomicInteger(0);

			  List<FactorRateRawInsert> errorList = rawMasterRepository.findByTranIdAndStatus(tranId,"E");
			  
			  if(errorList.size()==0 && errorList.isEmpty()) {
				  
				  List<FactorRateRawInsert> list = rawMasterRepository.findByTranId(tranId);

				  Map<String,List<DropDownRes>> dropDownList =new HashMap<String,List<DropDownRes>>();
				  if(list.size()>0) {
					// get master validation apis details
					log.info("Master api validation block calling ....");  
					FactorRateSaveReq factorRateSaveReq = new FactorRateSaveReq();
					factorRateSaveReq.setAgencyCode(list.get(0).getAgencyCode());
					factorRateSaveReq.setBranchCode(list.get(0).getBranchCode());
					factorRateSaveReq.setCompanyId(list.get(0).getCompanyId());
					factorRateSaveReq.setCoverId(list.get(0).getCoverId().toString());
					factorRateSaveReq.setSectionId(list.get(0).getSectionId().toString());
					factorRateSaveReq.setProductId(list.get(0).getProductId().toString());
					factorRateSaveReq.setFactorTypeId(list.get(0).getFactorTypeId().toString());
					dropDownList= rateMasterServiceImpl.masterDiscreteApiCall(factorRateSaveReq, auth.replaceAll("Bearer ", "").split(",")[0]);						
					log.info("Master api validation block completed ..");  
					 if(StringUtils.isNotBlank(discreateColumns)) {
						 String [] discreateColArr =discreateColumns.split("~");
						 log.info("Grouping the records block calling based on the discreate columns ||" +discreateColumns);  
						 if(discreateColArr.length==1) {
							 
							Map<Object,List<FactorRateRawInsert>> groupdata =list.stream()
						        .collect(Collectors.groupingBy(getGroupFields().get(discreateColArr[0])));  
							
							for (Entry<Object, List<FactorRateRawInsert>> entry :groupdata.entrySet()) {
								 List<FactorRateRawInsert> data= entry.getValue();
								 loadList.put(String.valueOf(uniqueId.getAndIncrement()),data);	
								
								}												
						 }else if(discreateColArr.length==2) {
							 
							 Map<Object, Map<Object, List<FactorRateRawInsert>>> groupdata =list.stream()
								       .collect(
						                        Collectors.groupingBy(getGroupFields().get(discreateColArr[0]),
						                                Collectors.groupingBy(getGroupFields().get(discreateColArr[1])
						                                       )));
							 
							 	
							 for(Entry<Object, Map<Object, List<FactorRateRawInsert>>> entry : groupdata.entrySet()) {
								 Map<Object, List<FactorRateRawInsert>> d1 =entry.getValue();
								 for(Entry<Object,List<FactorRateRawInsert>> entry2:d1.entrySet()) {
									 List<FactorRateRawInsert> data= entry2.getValue();
									 loadList.put(String.valueOf(uniqueId.getAndIncrement()),data);	
								 }
							 }					 						 
						 }else if(discreateColArr.length==3) {						 
							 Map<Object, Map<Object, Map<Object, List<FactorRateRawInsert>>>> groupdata =list.stream()
								       .collect(
						                        Collectors.groupingBy(getGroupFields().get(discreateColArr[0]),
						                                Collectors.groupingBy(getGroupFields().get(discreateColArr[1]),
						                                		Collectors.groupingBy(getGroupFields().get(discreateColArr[2])
						                                       ))));
							 
							 for( Entry<Object, Map<Object, Map<Object, List<FactorRateRawInsert>>>>entry1 : groupdata.entrySet()) {
								 Map<Object, Map<Object, List<FactorRateRawInsert>>> d1 =entry1.getValue();							 
								 for(Entry<Object, Map<Object, List<FactorRateRawInsert>>> entry2:d1.entrySet()) {								 
									 Map<Object, List<FactorRateRawInsert>> d2 =entry2.getValue();								 
									 for(Entry<Object, List<FactorRateRawInsert>> entry3 : d2.entrySet()) {
										 List<FactorRateRawInsert> data= entry3.getValue();
										 loadList.put(String.valueOf(uniqueId.getAndIncrement()),data);	
	
									 }}}              												 						 
						 }else if(discreateColArr.length==4) {
						
							 Map<Object, Map<Object, Map<Object, Map<Object, List<FactorRateRawInsert>>>>> groupdata =list.stream()
								       .collect(
						                        Collectors.groupingBy(getGroupFields().get(discreateColArr[0]),
						                                Collectors.groupingBy(getGroupFields().get(discreateColArr[1]),
						                                		Collectors.groupingBy(getGroupFields().get(discreateColArr[2]),
						                                				Collectors.groupingBy(getGroupFields().get(discreateColArr[3])
						                                       )))));	
							 						 
							 for(Entry<Object, Map<Object, Map<Object, Map<Object, List<FactorRateRawInsert>>>>> entry1 :groupdata.entrySet()) {
							 		Map<Object, Map<Object, Map<Object, List<FactorRateRawInsert>>>> d1 =entry1.getValue();				 		
							 		for(Entry<Object, Map<Object, Map<Object, List<FactorRateRawInsert>>>> entry2 :d1.entrySet()) {						 			
							 			 Map<Object, Map<Object, List<FactorRateRawInsert>>> d2 =entry2.getValue();						 			 
							 			 for( Entry<Object, Map<Object, List<FactorRateRawInsert>>> entry3 :d2.entrySet()) {						 				 
							 				Map<Object, List<FactorRateRawInsert>> d3 =entry3.getValue();						 				
							 				for(Map.Entry<Object, List<FactorRateRawInsert>> entry4:d3.entrySet()) {						 					
							 					List<FactorRateRawInsert> data =entry4.getValue();						 					
							 					loadList.put(String.valueOf(uniqueId.getAndIncrement()),data);						 					
							 					
							 				}						 				 						 				
							 			 }						 									 			
							 		}					 								 								 		
							 	 }  
					 }else if(discreateColArr.length==12){
							    	
							 								 			
							 		 Map<String, Map<String, Map<String, Map<String, Map<String, Map<String, Map<String, Map<String, Map<String, Map<String, Map<String, 
							 		 Map<String, List<FactorRateRawInsert>>>>>>>>>>>>> groupData=list.stream()
										       .collect(
								                        Collectors.groupingBy(getGroupFields().get(discreateColArr[0]),
								                                Collectors.groupingBy(getGroupFields().get(discreateColArr[1]),
								                                		Collectors.groupingBy(getGroupFields().get(discreateColArr[2]),
								                                				Collectors.groupingBy(getGroupFields().get(discreateColArr[3]),
								                                						Collectors.groupingBy(getGroupFields().get(discreateColArr[4]),
																                                Collectors.groupingBy(getGroupFields().get(discreateColArr[5]),
																                                		Collectors.groupingBy(getGroupFields().get(discreateColArr[6]),
																                                				Collectors.groupingBy(getGroupFields().get(discreateColArr[7]),
																                                						Collectors.groupingBy(getGroupFields().get(discreateColArr[8]),
																								                                Collectors.groupingBy(getGroupFields().get(discreateColArr[9]),
																								                                		Collectors.groupingBy(getGroupFields().get(discreateColArr[10]),
																								                                				Collectors.groupingBy(getGroupFields().get(discreateColArr[11])
								                           )))))))))))));	
							 	
							 				
							 		// Loop through the nested map
							         groupData.forEach((a, b) -> {
							             b.forEach((c, d) -> {
							            	 d.forEach((e, f) -> {
							            		 f.forEach((g, h) -> {
							            			 h.forEach((l, m) -> {
							            				 m.forEach((n, o) -> {
							            					 o.forEach((p, q) -> {
							            						 q.forEach((r, s) -> {
							            							 s.forEach((t, u) -> {
							            								 u.forEach((v, x) -> {
							            									 x.forEach((w, y) -> { 
							            										 y.forEach((z, z1) -> {
																		               loadList.put(String.valueOf(uniqueId.getAndIncrement()), z1);
																	             });
																             });   
															             });     
														             });    
													             });   
												             });  
											             });   
										             });    
									             }); 
								             });
							             });
							         }); 	
							         
							         								
							 	}else if(discreateColArr.length==8){
							    	
							 								 			
							 		 Map<String, Map<String, Map<String, Map<String, Map<String, Map<String, Map<String, Map<String, List<FactorRateRawInsert>>>>>>>>> groupData=list.stream()
										       .collect(
								                        Collectors.groupingBy(getGroupFields().get(discreateColArr[0]),
								                                Collectors.groupingBy(getGroupFields().get(discreateColArr[1]),
								                                		Collectors.groupingBy(getGroupFields().get(discreateColArr[2]),
								                                				Collectors.groupingBy(getGroupFields().get(discreateColArr[3]),
								                                						Collectors.groupingBy(getGroupFields().get(discreateColArr[4]),
																                                Collectors.groupingBy(getGroupFields().get(discreateColArr[5]),
																                                		Collectors.groupingBy(getGroupFields().get(discreateColArr[6]),
																                                				Collectors.groupingBy(getGroupFields().get(discreateColArr[7])
																                                						
								                           )))))))));	
							 	
							 				
							 		// Loop through the nested map
							         groupData.forEach((a, b) -> {
							             b.forEach((c, d) -> {
							            	 d.forEach((e, f) -> {
							            		 f.forEach((g, h) -> {
							            			 h.forEach((l, m) -> {
							            				 m.forEach((n, o) -> {
							            					 o.forEach((p, q) -> {
							            						 q.forEach((r, s) -> {
														            loadList.put(String.valueOf(uniqueId.getAndIncrement()), s);
 
													             });   
												             });  
											             });   
										             });    
									             }); 
								             });
							             });
							         }); 	
							         
							         								
							 	}else if(discreateColArr.length==5) {
									
									 Map<String, Map<String, Map<String, Map<String, Map<String, List<FactorRateRawInsert>>>>>> 
									 groupdata =list.stream()
										       .collect(
								                        Collectors.groupingBy(getGroupFields().get(discreateColArr[0]),
								                                Collectors.groupingBy(getGroupFields().get(discreateColArr[1]),
								                                		Collectors.groupingBy(getGroupFields().get(discreateColArr[2]),
								                                				Collectors.groupingBy(getGroupFields().get(discreateColArr[3]),
								                                				Collectors.groupingBy(getGroupFields().get(discreateColArr[4])
								                                       ))))));	
									 
										
								 		// Loop through the nested map
									 groupdata.forEach((a, b) -> {
								             b.forEach((c, d) -> {
								            	 d.forEach((e, f) -> {
								            		 f.forEach((g, h) -> {
								            			 h.forEach((l, m) -> {
													            loadList.put(String.valueOf(uniqueId.getAndIncrement()), m);

											             });    
										             }); 
									             });
								             });
								         }); 	
									
							 }else if(discreateColArr.length==6) {
									
								 Map<String, Map<String, Map<String, Map<String, Map<String, Map<String, List<FactorRateRawInsert>>>>>>>
								 groupData =list.stream()
									       .collect(
							                        Collectors.groupingBy(getGroupFields().get(discreateColArr[0]),
							                                Collectors.groupingBy(getGroupFields().get(discreateColArr[1]),
							                                		Collectors.groupingBy(getGroupFields().get(discreateColArr[2]),
							                                				Collectors.groupingBy(getGroupFields().get(discreateColArr[3]),
							                                						Collectors.groupingBy(getGroupFields().get(discreateColArr[4]),
							                                								Collectors.groupingBy(getGroupFields().get(discreateColArr[5])
							                                       )))))));	
								 
								// Loop through the nested map
						         groupData.forEach((a, b) -> {
						             b.forEach((c, d) -> {
						            	 d.forEach((e, f) -> {
						            		 f.forEach((g, h) -> {
						            			 h.forEach((l, m) -> {
						            				 m.forEach((n, o) -> {
												            loadList.put(String.valueOf(uniqueId.getAndIncrement()), o);

										             });   
									             });    
								             }); 
							             });
						             });
						         }); 	
						         
								 						 
								
						 }else if(discreateColArr.length==7) {
						
							 Map<String, Map<String, Map<String, Map<String, Map<String, Map<String, Map<String, List<FactorRateRawInsert>>>>>>>> 
							 groupData =list.stream()
								       .collect(
						                        Collectors.groupingBy(getGroupFields().get(discreateColArr[0]),
						                                Collectors.groupingBy(getGroupFields().get(discreateColArr[1]),
						                                		Collectors.groupingBy(getGroupFields().get(discreateColArr[2]),
						                                				Collectors.groupingBy(getGroupFields().get(discreateColArr[3]),
						                                						Collectors.groupingBy(getGroupFields().get(discreateColArr[4]),
						                                								Collectors.groupingBy(getGroupFields().get(discreateColArr[5]),
						                                										Collectors.groupingBy(getGroupFields().get(discreateColArr[6])
						                                       ))))))));	
							 						 
							// Loop through the nested map
					         groupData.forEach((a, b) -> {
					             b.forEach((c, d) -> {
					            	 d.forEach((e, f) -> {
					            		 f.forEach((g, h) -> {
					            			 h.forEach((l, m) -> {
					            				 m.forEach((n, o) -> {
					            					 o.forEach((p, q) -> {
												            loadList.put(String.valueOf(uniqueId.getAndIncrement()), q);

										             });  
									             });   
								             });    
							             }); 
						             });
					             });
					         }); 	
					         
							
					 }else if(discreateColArr.length==9) {
						 
						 
						 Map<String, Map<String, Map<String, Map<String, Map<String, Map<String, Map<String, Map<String, Map<String, List<FactorRateRawInsert>>>>>>>>>> groupData=list.stream()
							       .collect(
					                        Collectors.groupingBy(getGroupFields().get(discreateColArr[0]),
					                                Collectors.groupingBy(getGroupFields().get(discreateColArr[1]),
					                                		Collectors.groupingBy(getGroupFields().get(discreateColArr[2]),
					                                				Collectors.groupingBy(getGroupFields().get(discreateColArr[3]),
					                                						Collectors.groupingBy(getGroupFields().get(discreateColArr[4]),
													                                Collectors.groupingBy(getGroupFields().get(discreateColArr[5]),
													                                		Collectors.groupingBy(getGroupFields().get(discreateColArr[6]),
													                                				Collectors.groupingBy(getGroupFields().get(discreateColArr[7]),
													                                						Collectors.groupingBy(getGroupFields().get(discreateColArr[8])
																					                                		
					                           ))))))))));	
				 	
				 				
				 		// Loop through the nested map
				         groupData.forEach((a, b) -> {
				             b.forEach((c, d) -> {
				            	 d.forEach((e, f) -> {
				            		 f.forEach((g, h) -> {
				            			 h.forEach((l, m) -> {
				            				 m.forEach((n, o) -> {
				            					 o.forEach((p, q) -> {
				            						 q.forEach((r, s) -> {
				            							 s.forEach((t, u) -> {
													            loadList.put(String.valueOf(uniqueId.getAndIncrement()), u);
  
											             });    
										             });   
									             });  
								             });   
							             });    
						             }); 
					             });
				             });
				         }); 	
				         
						 
					 }
						 
						log.info("Grouping the records block completed based on the discreate columns ||" +discreateColumns);  
						
						
						 List<List<FactorRateRawInsert>> data =loadList.entrySet().stream()
								  .map(p -> p.getValue())
								  .collect(Collectors.toList());
						  
						processThread.asyncProcess(data, discreateColumns,auth,dropDownList);
						
					 }else {
						log.info("Calling non discreate columns block ||" +discreateColumns);  
						 callNonDiscreate(list,auth,dropDownList);
					 }
				  }	
				}
			 updateBatchTransaction (tranId, "Rawtable data vaildation completed" ,"","Progressing","P");				 
		   }catch (Exception e) {
			   e.printStackTrace();
				log.error(e);
				updateBatchTransaction ( tranId,"Error",e.getMessage(),"Error","E");
		}
		return response;
	   }
	  
	  
	
	   private void callNonDiscreate(List<FactorRateRawInsert> list,String auth ,Map<String,List<DropDownRes>> dropDownList) {
		try {
			List<List<FactorRateRawInsert>> discreateList =ListUtils.partition(list, list.size());
		
			processThread.asyncProcess(discreateList,"N/A",auth,dropDownList);		
		}catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		}
		
	}

	public void updateFactorRawRecords(FactorRateSaveReq req,List<Error> errors, String tranId, String discreateColumns) {
		try {			
			List<FactorRateRawInsert> saveData=req.getFactorParams().stream().map(p ->{
				FactorRateRawInsert entity = new FactorRateRawInsert();
				entity.setParam1(StringUtils.isBlank(p.getParam1())?"0":p.getParam1());
				entity.setParam2(StringUtils.isBlank(p.getParam2())?"0":p.getParam2());
				entity.setParam3(StringUtils.isBlank(p.getParam3())?"0":p.getParam3());
				entity.setParam4(StringUtils.isBlank(p.getParam4())?"0":p.getParam4());
				entity.setParam5(StringUtils.isBlank(p.getParam5())?"0":p.getParam5());
				entity.setParam6(StringUtils.isBlank(p.getParam6())?"0":p.getParam6());
				entity.setParam7(StringUtils.isBlank(p.getParam7())?"0":p.getParam7());
				entity.setParam8(StringUtils.isBlank(p.getParam8())?"0":p.getParam8());
				entity.setParam9(StringUtils.isBlank(p.getParam9())?"0":p.getParam9());
				entity.setParam10(StringUtils.isBlank(p.getParam10())?"0":p.getParam10());
				entity.setParam11(StringUtils.isBlank(p.getParam11())?"0":p.getParam11());
				entity.setParam12(StringUtils.isBlank(p.getParam12())?"0":p.getParam12());
				entity.setSno(Integer.valueOf(p.getSno()));
				entity.setApiUrl(StringUtils.isBlank(p.getApiUrl())?"":p.getApiUrl());
				entity.setCalcType(StringUtils.isBlank(p.getCalType())?"":p.getCalType());
				entity.setMasterYn(StringUtils.isBlank(p.getMasterYn())?"":p.getMasterYn());
				entity.setMinPremium(StringUtils.isBlank(p.getMinimumPremium())?"0":p.getMinimumPremium());
				entity.setRegulatoryCode(StringUtils.isBlank(p.getRegulatoryCode())?"":p.getRegulatoryCode());
				entity.setRate(StringUtils.isBlank(p.getRate())?"0":p.getRate());
				entity.setAgencyCode(req.getAgencyCode());
				entity.setAmendId(0);
				entity.setBranchCode(req.getBranchCode());
				entity.setCompanyId(req.getCompanyId());
				entity.setFactorTypeId(Integer.valueOf(req.getFactorTypeId()));
				entity.setProductId(Integer.valueOf(req.getProductId()));
				entity.setSectionId(Integer.valueOf(req.getSectionId()));
				entity.setCoverId(Integer.valueOf(req.getCoverId()));
				entity.setSubCoverId(Integer.valueOf(req.getSubCoverId()));
				entity.setEffectiveDateStart(req.getEffectiveDateStart());
				entity.setEffectiveDateEnd(req.getEffectiveDateEnd());
				entity.setRemarks(req.getRemarks());
				entity.setStatus(p.getStatus());
				entity.setCreatedBy(StringUtils.isBlank(req.getCreatedBy())?"":req.getCreatedBy());
				entity.setTranId(tranId);
				entity.setGroupingColumn(discreateColumns);
				entity.setErrorDesc(errors.size()>0?print.toJson(errors).length()>10000?print.toJson(errors).substring(0,10000):print.toJson(errors)
					:null);
				entity.setErrorStatus(CollectionUtils.isEmpty(errors)?"Y":"E");
				entity.setEntryDate(new Date());
				entity.setXlAgencyCode(StringUtils.isBlank(p.getXlAgencyCode())?"":p.getXlAgencyCode());
				entity.setParam13(StringUtils.isBlank(p.getParam13())?null:p.getParam13());
				entity.setParam14(StringUtils.isBlank(p.getParam14())?null:p.getParam14());
				entity.setParam15(StringUtils.isBlank(p.getParam15())?null:p.getParam15());
				entity.setParam16(StringUtils.isBlank(p.getParam16())?null:p.getParam16());
				entity.setParam17(StringUtils.isBlank(p.getParam17())?null:p.getParam17());
				entity.setParam18(StringUtils.isBlank(p.getParam18())?null:p.getParam18());
				entity.setParam19(StringUtils.isBlank(p.getParam19())?null:p.getParam19());
				entity.setParam20(StringUtils.isBlank(p.getParam20())?null:p.getParam20());
				entity.setParam21(StringUtils.isBlank(p.getParam21())?null:p.getParam21());
				entity.setParam22(StringUtils.isBlank(p.getParam22())?null:p.getParam22());
				entity.setParam23(StringUtils.isBlank(p.getParam23())?null:p.getParam23());
				entity.setParam24(StringUtils.isBlank(p.getParam24())?null:p.getParam24());
				entity.setParam25(StringUtils.isBlank(p.getParam25())?null:p.getParam25());
				entity.setParam26(StringUtils.isBlank(p.getParam26())?null:p.getParam26());
				entity.setParam27(StringUtils.isBlank(p.getParam27())?null:p.getParam27());
				entity.setParam28(StringUtils.isBlank(p.getParam28())?null:p.getParam28());
				entity.setExcessAmount(StringUtils.isBlank(p.getExcessAmount())?null:p.getExcessAmount());
				entity.setExcessPercent(StringUtils.isBlank(p.getExcessPercent())?null:p.getExcessPercent());
				entity.setExcessDesc(StringUtils.isBlank(p.getExcessDesc())?"":p.getExcessDesc());
				return entity;
			}).collect(Collectors.toList());
			
			rawMasterRepository.saveAllAndFlush(saveData);
				
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	private Map<String, Function<FactorRateRawInsert, String>> getGroupFields() {
		   Map<String, Function<FactorRateRawInsert, String>> map = new HashMap<String, Function<FactorRateRawInsert, String>>();
		   Function<FactorRateRawInsert, String> param9 = FactorRateRawInsert::getParam9;
		   Function<FactorRateRawInsert, String> param10 = FactorRateRawInsert::getParam10;
		   Function<FactorRateRawInsert, String> param11 = FactorRateRawInsert::getParam11;
		   Function<FactorRateRawInsert, String> param12 = FactorRateRawInsert::getParam12;
		   Function<FactorRateRawInsert, String> param13 = FactorRateRawInsert::getParam13;
		   Function<FactorRateRawInsert, String> param14 = FactorRateRawInsert::getParam14;
		   Function<FactorRateRawInsert, String> param15 = FactorRateRawInsert::getParam15;
		   Function<FactorRateRawInsert, String> param16 = FactorRateRawInsert::getParam16;
		   Function<FactorRateRawInsert, String> param17 = FactorRateRawInsert::getParam17;
		   Function<FactorRateRawInsert, String> param18 = FactorRateRawInsert::getParam18;
		   Function<FactorRateRawInsert, String> param19 = FactorRateRawInsert::getParam19;
		   Function<FactorRateRawInsert, String> param20 = FactorRateRawInsert::getParam20;
		   
		   map.put("param10", param10);
		   map.put("param11", param11);
		   map.put("param9", param9);
		   map.put("param12", param12);
		   map.put("param13", param13);
		   map.put("param14", param14);
		   map.put("param15", param15);
		   map.put("param16", param16);
		   map.put("param17", param17);
		   map.put("param18", param18);
		   map.put("param19", param19);
		   map.put("param20", param20);
		   return map;
	   }
	  
	public void insertFactorRecordsToMainTable(String tranId, String totalRecord) {
		ObjectMapper objMapper = new ObjectMapper();
		try {
			List<FactorRateRawInsert> list=rawMasterRepository.findByTranIdAndErrorDescIsNull(tranId);
			if(!CollectionUtils.isEmpty(list)) {
				FactorRateSaveReq factorRateSaveReq=mapper.map(list.get(0), FactorRateSaveReq.class);
				List<FactorParmsRequestMapping> parmasMapping = new ArrayList<FactorParmsRequestMapping>();
				try {
				 parmasMapping =list.stream()
						.map(from -> {
							try {
								return objMapper.readValue(print.toJson(from), FactorParmsRequestMapping.class);
							} catch (JsonProcessingException e) {
								e.printStackTrace();
							}
							return null;
						})
				.collect(Collectors.toList());
				}catch (Exception e) {
					e.printStackTrace();
				}

				List<FactorParamsInsert> params=parmasMapping.stream()
					.map(d ->mapper.map(d, FactorParamsInsert.class))
						.collect(Collectors.toList());
				
				factorRateSaveReq.setSubCoverYn(factorRateSaveReq.getSubCoverId().equals("0")?"N":"Y");
				
				factorRateSaveReq.setFactorParams(params);
				
				log.info(print.toJson(factorRateSaveReq));
				
				entityService.insertFactorRateDetails(factorRateSaveReq);
				
				log.info("Process Completed at " +new Date());
				
			}
		
			
		}catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		}
		
	}
		
	
	public CommonRes doMainJob(String tranId) throws Exception {
		Boolean status =false;
		Long count=0L;
		log.info("Mainjob batch request ||tranId :"+tranId+" ");
		CommonRes response = new CommonRes();
		try {
			count=rawMasterRepository.countByTranIdAndErrorStatus(tranId,"E");
			log.info("FactorRateRawInsert  Error Records count : "+count);
			if(count==0) {
				status =true;	
				count=rawMasterRepository.countByTranId(tranId);
				log.info("Maintable batch job calling .... : "+count);
				JobParameters jobParameters = new JobParametersBuilder()
						.addLong("time", System.currentTimeMillis())
						.addString("MainTable", tranId+"~"+count)
				        .toJobParameters();
						jobLauncher.run(job, jobParameters);
						log.info("Maintable batch job completed: "+count);

						response.setMessage("SUCCESS");
						response.setCommonResponse("Records moved Successfully");	
			}else {
				response.setMessage("SUCCESS");
				response.setCommonResponse("Please update or delete error records");	
			}
		}catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		}
		return response;
	}

	public void updateMasterValidation(SpringBatchMapperResponse res) {
		try {
			Integer count =rawMasterRepository.updateAgencyCodeValidation(res.getTranId(),res.getInsuranceId(),res.getProductId());
			log.info("updateMasterValidation update count is :  "+count);
		}catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		}
		
	}
	
}
