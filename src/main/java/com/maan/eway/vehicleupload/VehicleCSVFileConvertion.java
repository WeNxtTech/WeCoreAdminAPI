package com.maan.eway.vehicleupload;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.maan.eway.batch.entity.EwayUploadTypeMaster;
import com.maan.eway.batch.entity.EwayXlconfigMaster;
import com.maan.eway.batch.repository.EwayXlconfigMasterRepository;
import com.maan.eway.batch.repository.TransactionControlDetailsRepository;
import com.maan.eway.batch.res.EwayUploadRes;
import com.maan.eway.springbatch.TransactionControlDetails;
import com.maan.eway.springbatch.TransactionControlDetailsId;

@Component
@Transactional
public class VehicleCSVFileConvertion {
	
	static Logger log =LogManager.getLogger(VehicleCSVFileConvertion.class);
	
	@Value("${csv.upload.path}")
    private String csvFilePath;
	
	@PersistenceContext
	EntityManager em;
	
	private static String OUTPUT_DATE_FORMAT="dd/MM/yyyy";
	private static String NEW_LINE_CHARACTER = "\r\n";
	private static String CVS_SEPERATOR_CHAR = "\t";
	
	@Autowired
	private EwayXlconfigMasterRepository xlConfigMaster;
	
	@Autowired
	private TransactionControlDetailsRepository transRepo;
	
	@Autowired
	private VehicleBatchServiceImpl batchServiceImpl;
	
	public void doCSVCovertion(EwayUploadRes uploadRes, EwayUploadTypeMaster uploadTypeMaster) {
		try {
			if(StringUtils.isNotBlank(uploadRes.getCsvfilepath())) {
				File csvFile = new File(uploadRes.getCsvfilepath());
				if(csvFile.exists() && csvFile.canRead()) {
					if(uploadTypeMaster!=null) {
						
						List<EwayXlconfigMaster> list = xlConfigMaster.findByCompanyIdAndProductIdAndSectionIdAndTypeidAndStatusIgnoreCaseOrderByExcelColumnIndex(
								Integer.valueOf(uploadRes.getCompanyId()),Integer.valueOf(uploadRes.getProductId()),Integer.valueOf(uploadRes.getSectionId()),
								Integer.valueOf(uploadRes.getTypeId()),"Y");
								
						
						List<EwayXlconfigMaster> xlConfigData =list.stream().filter(p ->StringUtils.isNotBlank(p.getExcelheaderName()))
								.filter(p ->StringUtils.isNotBlank(p.getFieldNameRaw()))
								.filter(p ->"Y".equalsIgnoreCase(p.getStatus()))
								.collect(Collectors.toList());
						
						if(xlConfigData!=null&&xlConfigData.size()>0) {
							Map<String,Object> errorList = getRecordsList(csvFile,xlConfigData,uploadRes.getRequestReferenceNo());
							if(errorList.size()>0) {
								String errorDesc = (String) errorList.get("ERROR");
								uploadRes.setProgressdesc(errorDesc);
								batchServiceImpl.fileUploadProgress(uploadRes,"E",(String) errorList.get("ERROR"),"Excel Header UnMatched","20");
							}else {
								batchServiceImpl.fileUploadProgress(uploadRes,"P","","Moving to raw table","30");
								
								//Reading recordsfrom csv file and inserting into Raw table and updating duplicates in raw table
								VehicleEwayUploadBatchThread batchInsertThread = new VehicleEwayUploadBatchThread(batchServiceImpl,uploadRes,xlConfigData,uploadTypeMaster);
								Thread threadJob=new Thread(batchInsertThread);
								threadJob.setName("BATCH_SAVE_THREAD");
								threadJob.setDaemon(false);
								threadJob.start();
							}
						}else {
							batchServiceImpl.fileUploadProgress(uploadRes,"E","Excel Header Configuration Details not found for Uploaded FileType...","Validating Excel and Csv Header Columns...","ERROR 500");

						}
					}else {
						batchServiceImpl.fileUploadProgress(uploadRes,"E","Excel Configuration Data Not found in Fieldtypemaster for Uploaded File...","Reading Excel Header Column from Database...","ERROR 500");
					}
				}
			}
			
		}catch (Exception e) {
			log.error(e);
			e.printStackTrace();
		}
		
	}
	
	public EwayUploadRes convertExceltoCsv(EwayUploadRes response) {//,UploadFileResponse response
		try {
				String csvFileName = response.getExcelFileName().replace(".xlsx", ".csv").replace(".xls", ".csv").replace(".XLSX", ".csv").replace(".XLS", ".csv");
				LocalDateTime time = LocalDateTime.now();
				String csvFilePath =  this.csvFilePath+time.getNano()+csvFileName;
				File csvFile = new File(csvFilePath);
				if(csvFile.exists()) {
					if(csvFile.delete()) 
					{ log.info("File deleted successfully"); } 
				}
				int tolalNoofRowsinFile = csvConvertion(response,csvFilePath, "MM/dd/yyyy", "\t");
				response.setCsvfilepath(csvFilePath);
				response.setCsvFileName(csvFileName);
				if(tolalNoofRowsinFile>0) {
					tolalNoofRowsinFile = tolalNoofRowsinFile-1;
						TransactionControlDetailsId id =TransactionControlDetailsId.builder()
								.productId(Integer.valueOf(response.getProductId()))
								.requestReferenceNo(response.getRequestReferenceNo())
								.build();
						Optional<TransactionControlDetails> tranList = transRepo.findById(id);
						if(tranList.isPresent()) {
							String existingTotalRecords = tranList.get().getTotalRecords()==null?"0":tranList.get().getTotalRecords().toString();
							tolalNoofRowsinFile = tolalNoofRowsinFile + Integer.valueOf(existingTotalRecords);
						}
					
				}
				response.setToatalRows(String.valueOf(tolalNoofRowsinFile));
				response.setCsvfilepath(csvFilePath);
				response.setCsvFileName(csvFileName);
				if(!(csvFile.exists() && csvFile.canRead())) {
					response.setProgressStatus("E");
					response.setProgressErrordesc("CSV File Not Found ..."); 
					response.setProgressdesc("Error Occurred While CSV convertion...");
				}
			}catch(Exception e) {
				log.error(e);
				e.printStackTrace();
				response.setProgressStatus("E");
				response.setProgressErrordesc(e.getMessage().length()<1000?e.getMessage():e.getMessage().substring(0, 1000)); 
				response.setProgressdesc("Error Occurred While CSV convertion...");
			}
			return response;
		}
	
	public int csvConvertion(EwayUploadRes response, String csvFileName, String dateFormat, String seperatorChar){
		int tolalNoofRowsinFile=0;
        try
        {
        	String excelFileName=response.getExcelFilePath();
           //String OUTPUT_DATE_FORMAT = dateFormat;
          // String CVS_SEPERATOR_CHAR = seperatorChar;
           String fileType=FilenameUtils.getExtension(excelFileName);
           if("xlsx".equals(fileType))
           {
           	tolalNoofRowsinFile=excelXToCSV(excelFileName, csvFileName,response);
           } else
           if("xls".equals(fileType))
           {
           	tolalNoofRowsinFile=excelToCSV(excelFileName, csvFileName,response);
           }
  
        }catch(Exception e){log.error(e);
        	batchServiceImpl.fileUploadProgress(response,"E","CSV Conversion Failed","Reading Excel Header Column from Database...","ERROR 500");
        
        }
        return tolalNoofRowsinFile;
    }
	
	 //XLSX TO CSV
	 public  int excelXToCSV(String excelFileName, String csvFileName, EwayUploadRes response) throws  Exception {
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
		         // String vehicleId =getVehilceId(response.getRequestReferenceNo(), response.getProductId());
		          for(int eachRow = 0; eachRow < rows; eachRow++){
		              String csvData = "";
		            //  String snoAndVehNo = String.valueOf(eachRow);
		              XSSFRow myRow = mySheet.getRow(eachRow);
		              
		               //System.out.println("RowNo : " + eachRow);
		              System.out.println("myRow is Empty"+ myRow!=null); 
		              if(++count == 1){
		                  firstCount = myRow.getLastCellNum();
		              }
		              for(int i = 0; i < firstCount; i++){
		            	  csvData = (String.valueOf(csvData)+getXLSXCellData(myRow.getCell(i))+"~")
			                		  .replace("\t", "").replace("\n", "").replace("\r", "").replace("'", "").replaceAll("^\"|\"$", "").trim();
		              }
		             /* if(StringUtils.isNotBlank(csvData)) {
							
		            	  if( eachRow== 0) {
			                	
		            		  csvData="Sno~"+csvData;
			                	
			  	          }else {
			  	    
			                 csvData=Long.valueOf(vehicleId)+eachRow+"~"+csvData;	
			                 
			  	            }*/
			              writeCSV(csvFileName, (String.valueOf(csvData.substring(0, csvData.length() - 1))).trim()+NEW_LINE_CHARACTER);
		              }
		            
		    }catch(Exception e){e.printStackTrace();
		    }finally{
	        	log.info("xls File Converted to csv.....");
	        	batchServiceImpl.fileUploadProgress(response,"E","CSV Conversion Failed","Reading Excel Header Column from Database...","ERROR 500");
	        }
		 return tolalNoofRowsinFile;
		 }
	 
	 public int excelToCSV(String excelFileName, String csvFileName, EwayUploadRes response)
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
	          //  int rowcount = 0;
	            int firstCount = 0;
	            String csvData;
		       //  String vehicleId =getVehilceId(response.getRequestReferenceNo(), response.getProductId());
	            for(; rowIter.hasNext(); writeCSV(csvFileName, (new StringBuilder(String.valueOf(csvData.substring(0, csvData.length() - 1)).trim())).append(NEW_LINE_CHARACTER).toString()))
	            {
	                csvData = "";
	                HSSFRow myRow = (HSSFRow)rowIter.next();
	                
	                if(myRow==null) {
	                	continue;
	                }
	                
	                if(++count == 1)
	                {
	                    firstCount = myRow.getLastCellNum();
	                }
	                for(int i = 0; i < firstCount; i++)
	                {
	                    csvData = String.valueOf(csvData)+getCellData(myRow.getCell(i)).replace("\t", "").replace("\n", "").replace("\r", "").replace("'", "").replaceAll("^\"|\"$", "")+"~".trim();
	                }
	               // int rownum = ++rowcount;
	                
	                /*if( rownum== 1) {
	                	
		                csvData="Sno~"+csvData;
	                
	  	            }else {
	  	            	
	  	            	 csvData=Long.valueOf(vehicleId)+rownum-1+"~"+csvData;		  	            		                		  	              
	  	            }*/
	             
	           }
	            tolalNoofRowsinFile=count;
	        } catch(Exception e) {e.printStackTrace();
	        }finally{
	        	log.info("xls File Converted to csv.....");
	        	batchServiceImpl.fileUploadProgress(response,"E","CSV Conversion Failed","Reading Excel Header Column from Database...","ERROR 500");
	        }
	        return tolalNoofRowsinFile;
	    }

	 
	 private static String getXLSXCellData(XSSFCell myCell)
	            throws Exception
	        {
	            String cellData = "";
	            if(myCell == null)
	            {
	                cellData = String.valueOf(cellData)+CVS_SEPERATOR_CHAR;
	            } else
	            {
	            	//log.info("CellType==>"+myCell.getCellType());
	                switch(myCell.getCellType())
	                {
	                case STRING: 
	                	cellData = String.valueOf(cellData)+myCell.getRichStringCellValue()+CVS_SEPERATOR_CHAR;
	                    break;
	                case BOOLEAN:               cellData = String.valueOf(cellData)+myCell.getBooleanCellValue()+CVS_SEPERATOR_CHAR;
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
	            	try{
	            		cellData = String.valueOf(cellData)+myCell.getRawValue()+CVS_SEPERATOR_CHAR;
	            	}catch(Exception e){
	            		cellData = "";
	            		cellData = String.valueOf(cellData)+myCell.getNumericCellValue()+CVS_SEPERATOR_CHAR;
	            	}
	            }
	            return cellData;
	        }

	    private static String getXLSXFormulaValue(XSSFCell myCell)
	            throws Exception
	        {
	            String cellData = "";
	            if(myCell.getCachedFormulaResultType() == CellType.STRING || myCell.getCellType() == CellType.BOOLEAN)
	            {
	                cellData = String.valueOf(cellData)+myCell.getRichStringCellValue()+CVS_SEPERATOR_CHAR;
	            } else
	            if(myCell.getCachedFormulaResultType() == CellType.NUMERIC)
	            {
	                cellData = String.valueOf(cellData)+getXLSXNumericValue(myCell)+CVS_SEPERATOR_CHAR;
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
		        DataFormatter dataFormatter = new DataFormatter();
		        if(DateUtil.isCellDateFormatted(myCell))
		        {
		        	String formattedCellStr = dataFormatter.formatCellValue(myCell);
		        	cellData=formattedCellStr;
		        } else
		        {
		        	String formattedCellStr = dataFormatter.formatCellValue(myCell);
		        	cellData=formattedCellStr;
		        }
		        return cellData;
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
		
	    private static void writeCSV(String csvFileName, String csvData)
		        throws Exception
		    {
		        FileOutputStream writer = new FileOutputStream(csvFileName, true);
		        writer.write(csvData.getBytes());
		        writer.close();
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
	    
	    
	    
	    private Map<String,Object> getRecordsList(File inputFile, List<EwayXlconfigMaster> dbColumns, String typeId) {
			String record = "", result = "", unmatched = "";
			long totalLinesProcessed = 0l;
			String[] excelHeaders = null;
			Map<String,Object> resultMap = null;
			boolean cont = true; 
			BufferedReader in = null;
			try {
				in = new BufferedReader(new FileReader(inputFile));
				while ((record = in.readLine()) != null && cont) {
					///System.out.println("status----->" + record);
					totalLinesProcessed++;
					
					if (totalLinesProcessed == 1) {
						//record=record.replaceAll("~Sno~VehicleNo", "");
						excelHeaders = record.split("~");
						//System.out.println(excelHeaders.length);
						resultMap = matchColumns(excelHeaders, dbColumns);
						if (!resultMap.isEmpty()) {
							unmatched = (String) resultMap.get("UNMATCHED");
							if (unmatched != null && unmatched != "" && unmatched.length() > 0) {
								result += unmatched;
								cont = false;
								break;
							}
							if(!cont) break;
						}
					}else {///Added new
						cont = false;
						return resultMap;
					}
					record = record.replaceAll("\'", "");
					record = record.replaceAll("\"", "");
					record = record.replaceAll("[*]", "");
					if ((record.trim()).length() == 0)
						break;
				}
			}catch (Exception e) {log.error(e);} 
			 finally {
				try {
					in.close();
				} catch (IOException e) {log.error(e);}
			}
			if(result!="" && result!=null) {
				resultMap.put("ERROR", result);
			}
			return resultMap;
		}
	    
	    
	 // Getting match and unmatched Columns
		public  Map<String,Object> matchColumns(String[] excelColumns, List<EwayXlconfigMaster> dbColumns) {
			System.out.println("matchColumns() - Enter");
			String unmatchedColumns = "";
			
			Long dbColumnLength = Long.valueOf(dbColumns.size());
			Long excelColumnLength = Long.valueOf(excelColumns.length);
			EwayXlconfigMaster columnInfo = new EwayXlconfigMaster();
			HashMap<String,Object> columns = new HashMap<String, Object>();
			if(excelColumnLength==dbColumnLength) {
				for(int i=0;i<excelColumnLength;i++) {
				//	int headerIndex = Integer.valueOf(i);
					columnInfo = dbColumns.get(i);
					String dbheader=columnInfo.getExcelheaderName().trim().toUpperCase().replaceAll("\\s","");
					String excelheaderName = excelColumns[i].trim().toUpperCase().replaceAll("\\s","");
					if (!((excelheaderName.replaceAll("[^a-zA-Z]+", "")).equalsIgnoreCase((dbheader.replaceAll("[^a-zA-Z]+", ""))))){ // && headerIndex == Integer.valueOf(columnInfo.getExcelcolumnindex().toString())){
						unmatchedColumns += excelheaderName + ", ";
					}
				}
				if (unmatchedColumns.length() > 1) {
					unmatchedColumns = unmatchedColumns.substring(0,unmatchedColumns.length() - 2);
					columns.put("UNMATCHED", "Unknown Columns Found ["+ unmatchedColumns+ "]");
					log.info("matchColumns() - Exit || UnMatched: "+ unmatchedColumns);
				}
			}else {
				columns.put("UNMATCHED", "Excel and Configured Header Columns Length Not Matched, Configured Column are " + dbColumnLength + " & Excel Column are " + excelColumnLength);//Excel and DataBase Header Columns Length Not Matched,  DbColumn Length : 
			}
			return columns;
		}

		
		/*private String getVehilceId(String refNo,String productId) {
			String vehicleId="";
			try {
				synchronized(this) {
					if("5".equals(productId)) {
						vehicleId =transRepo.getVehicleId(refNo, productId);
					}
				}
			}catch (Exception e) {
				e.printStackTrace();
			}
			return vehicleId;
		}*/
	    
}
