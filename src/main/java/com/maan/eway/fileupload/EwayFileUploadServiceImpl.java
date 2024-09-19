package com.maan.eway.fileupload;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.util.CollectionUtils;

import com.google.gson.Gson;
import com.maan.eway.bean.FactorTypeDetails;
import com.maan.eway.bean.PremiaConfigDataMaster;
import com.maan.eway.bean.PremiaConfigMaster;
import com.maan.eway.bean.SectionCoverMaster;
import com.maan.eway.error.Error;
import com.maan.eway.master.req.FactorParamsInsert;
import com.maan.eway.master.req.FactorRateSaveReq;
import com.maan.eway.master.service.FactorRateMasterService;
import com.maan.eway.repository.FactorRateMasterRepository;
import com.maan.eway.repository.ListItemValueRepository;
import com.maan.eway.res.CommonRes;
import com.maan.eway.res.SuccessRes;

@Service
public class EwayFileUploadServiceImpl implements EwayFileUploadService {
	
	
	Logger log =LogManager.getLogger(getClass());
	
	private static Gson json = new Gson();
	
	@Autowired
	private JpqlQueryServiceImpl queryService;
	
	@Autowired
	private FactorRateMasterService entityService;

	@PersistenceContext
	private EntityManager em;

	@Override
	public com.maan.eway.res.CommonRes download(FileDownloadRequest req)  {
		log.info("File download request : "+json.toJson(req));
		byte byteArry[] =null ;
		com.maan.eway.res.CommonRes response = new com.maan.eway.res.CommonRes();
		FileDownloadRes  res =new FileDownloadRes();
		List<Error> errors = new ArrayList<Error>();
		try {
			String branchCode=StringUtils.isBlank(req.getBranchCode())?"99999":req.getBranchCode();
			String subCoverId=StringUtils.isBlank(req.getSubCoverId())?"0":req.getSubCoverId();
			req.setBranchCode(branchCode);
			req.setSubCoverId(subCoverId);
			Map<String,Object> object=queryService.getFactorXlColumns(req);
			if(object!=null) {
				String excel_columns ="";
				String minimum_rateyn =object.get("MINIMUM_RATEYN").toString();
				String columns =object.get("QUERY_COLUMNS").toString();
				String query_columns ="";
				if("Y".equalsIgnoreCase(minimum_rateyn)) {			
					excel_columns =",Rate,MinimumRate,CalcType,MinimumPremium,RegulatoryCode,ExcessPercent,ExcessAmount,ExcessDesc,Status";
					query_columns="agencyCode," +columns+ ",COALESCE(rate,'0'),COALESCE(minimumRate,'0'),calcType,COALESCE(minPremium,'0'),regulatoryCode,COALESCE(excessPercent,'0'),COALESCE(excessAmount,'0'),excessDesc,status";
				}else {
					excel_columns =",Rate,CalcType,MinimumPremium,RegulatoryCode,ExcessPercent,ExcessAmount,ExcessDesc,Status";
					query_columns="agencyCode," +columns+ ",rate,calcType,minPremium,regulatoryCode,excessPercent,excessAmount,excessDesc,status";
				}	
							
				String factorId =object.get("FACTOR_ID").toString();
				//String defaultColumns =",Rate,MinimumRate,CalcType,MinimumPremium,RegulatoryCode,ExcessPercent,ExcessAmount,ExcessDesc,Status";
				String xlColumns ="AgencyCode,"+object.get("XL_COLUMNS").toString()+excel_columns;
				List<Object[][]> obj =queryService.getFactorRateDetails(req, query_columns, factorId);
				
					
				XSSFWorkbook workbook = new XSSFWorkbook();
				XSSFSheet sheet =workbook.createSheet("FACTOR_RATE_DETAILS");
				CreationHelper createHelper = workbook.getCreationHelper();

				XSSFCellStyle cellStyle =workbook.createCellStyle();
				XSSFFont font = workbook.createFont();

				cellStyle.setDataFormat(createHelper.createDataFormat().getFormat("####"));

				font.setBold(true);
				font.setFontHeight(10);
				font.setFontName("Arial");
				cellStyle.setFont(font);
					
				String[] headers =xlColumns.split(",");
					
				int rowNum = 1;
					
				Row row =sheet.createRow(0);
					
				for (int i =0;i<headers.length;i++) {
					Cell cell =row.createCell(i);
					cell.setCellValue(headers[i]);
					row.getCell(i).setCellStyle(cellStyle);
						
				}
				
				 // Auto-size the cells in the first row
		        for (int i = 0; i <headers.length; i++) {
		            sheet.autoSizeColumn(i);
		            
		        }
				
				if(obj.size()>0) { 
					for(Object [] ob :obj) {
						row =sheet.createRow(rowNum++);
						int col =0;
						for(Object str : ob) {
							Cell cell =row.createCell(col++);							
							NumberFormat nf = NumberFormat.getInstance();
					        if(str instanceof Integer) {
								cell.setCellValue(str==null?"":nf.format(str));
					        }else if(str instanceof Double){
					            nf.setMinimumFractionDigits(2);
								cell.setCellValue(str==null?"":nf.format(str));
					        }else if(str instanceof BigDecimal){
					        	cell.setCellValue(str==null?"0":new BigDecimal(str.toString()).stripTrailingZeros().toPlainString());
					        }else {
					        	cell.setCellValue(str==null?"":str.toString());
					        }

						}
					}
				}
					ByteArrayOutputStream bos = new ByteArrayOutputStream();
					workbook.write(bos);
					workbook.close();
					byteArry = bos.toByteArray();
					String prefix = "data:application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;base64,";
					String base64 =Base64.getEncoder().encodeToString(byteArry);
					res.setFile(prefix+base64);
					response.setCommonResponse(res);
				
				
			}else {
				errors.add(new Error("101", "FileDownload", "Excle header columns not found.."));
				response.setErrorMessage(errors);
				return 	response;
			}
		}catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		}
		return response;
	}

	public String formatCellData(String input) {
		String output="";
		try {
			if(input.matches("[0-9.]*")) {  
				double number = Double.valueOf(input);
				number = Math.round(number * 100);
				number = number/100;
				output=String.valueOf(number);
			}else {
				output=input;
			}
		}catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		}
		return output;
	}

	@Override
	public com.maan.eway.res.CommonRes upload(String filePath, FileUploadInputRequest request, String token) {
		com.maan.eway.res.CommonRes comRes = new com.maan.eway.res.CommonRes ();
		List<Error> validation = new ArrayList<Error>();
		String factorTypeId ="";Date effectiveDate=null;String remarks=""; 
		try {
			FileInputStream excelFile = new FileInputStream(new File(filePath));
            @SuppressWarnings("resource")
			Workbook workbook = new XSSFWorkbook(excelFile);
            Sheet worksheet = workbook.getSheetAt(0);
            List<Map<String,Object>> dataList = new ArrayList<Map<String,Object>>();
            List<Map<Object,Object>> resultList = new ArrayList<Map<Object,Object>>();
            DataFormatter formatter = new DataFormatter();
            for(int i=1;i<worksheet.getPhysicalNumberOfRows() ;i++) {
            	Row headers=worksheet.getRow(0);
                int maxNumOfCells = worksheet.getRow(0).getLastCellNum(); // The the maximum number of columns
                Row row = worksheet.getRow(i);
                if(row==null)
                	continue;
        
                Map<String,Object> object =new HashMap<String,Object>();
	                for (int j=0;j<maxNumOfCells;j++) {
	             	    Cell cell =row.getCell(j);
	                    if(cell==null){
	                    	object.put(headers.getCell(j).getStringCellValue(), "");
	                    }else if( cell!=null && cell.getCellType()==CellType.STRING) {
	                		object.put(headers.getCell(j).getStringCellValue(), StringUtils.isBlank(cell.getStringCellValue())?"N/A":cell.getStringCellValue());
	                	}else  if (cell!=null && cell.getCellType()==CellType.NUMERIC ){
	                		object.put(headers.getCell(j).getStringCellValue(), String.valueOf(cell.getNumericCellValue()).equals("")?"N/A":formatter.formatCellValue(cell));
	                	}
	                	log.info("Cells || "+cell+" || "+ headers.getCell(j));
	                }
	             dataList.add(object);
            }
            if(!CollectionUtils.isEmpty(dataList)) {
            	List<SectionCoverMaster> sectionCov=queryService.getSectionCoverMaster(request); 
            	if(CollectionUtils.isEmpty(sectionCov)) {
            		validation.add(new Error("500", "SectionCoverMaster", "No records found in SectionCoverMaster "));
            		comRes.setErrorMessage(validation);
            		return comRes;
            	}
            	 factorTypeId = StringUtils.isBlank(sectionCov.get(0).getFactorTypeId().toString())?"":sectionCov.get(0).getFactorTypeId().toString();
            	 effectiveDate =sectionCov.get(0).getEffectiveDateStart().toString()==null?null:sectionCov.get(0).getEffectiveDateStart();            	
            	 remarks = StringUtils.isBlank(sectionCov.get(0).getRemarks())?"":sectionCov.get(0).getRemarks();            	
            	 List<FactorTypeDetails> flist=queryService.getFactorRateColumns(request,factorTypeId);
            		
            	if(CollectionUtils.isEmpty(flist)) {
            		validation.add(new Error("500", "FactorTypeDetails", "No records found in FactorTypeDetails "));
            		comRes.setErrorMessage(validation);
            		return comRes;
            	}            
            	if(!CollectionUtils.isEmpty(flist)) {            	
	            	Map<String,Object> dbkeys =new HashMap<String,Object>();	            	
	            	for(int i=0;i<flist.size();i++) {	            		
	            		FactorTypeDetails fac =flist.get(i);	            	
	            		if(fac.getRangeYn().equalsIgnoreCase("Y")) {
	            			String dbfrom =fac.getRangeFromColumn();
	            			String dbto =fac.getRangeToColumn();
	            			String fromdisplay =fac.getFromDisplayName();
	            			String toDisplay =fac.getToDisplayName();
	            			dbkeys.put(fromdisplay, dbfrom);
	            			dbkeys.put(toDisplay, dbto);	            			
	            		}else if(fac.getRangeYn().equalsIgnoreCase("N")) {	            			
	            			dbkeys.put(fac.getDiscreteDisplayName(), fac.getDiscreteColumn());
	            		}	            		
	            	}
	            	for(int i =0;i<dataList.size();i++) {	            		
	            		Map<String,Object> p =dataList.get(i);	            		
	                	Map<Object,Object> result =new HashMap<Object,Object>();	
	            		for (Entry<String, Object> xl:p.entrySet()) {
	            			for(Entry<String, Object> db:dbkeys.entrySet()) {	            				
	            				if(xl.getKey().equalsIgnoreCase( db.getKey()) ) {	            					
	            					result.put(db.getValue(), xl.getValue()==null?"":xl.getValue());	            					
	            				}
	            			}	            			
	            			if(xl.getKey().equalsIgnoreCase("Rate")) {
	            				result.put(xl.getKey().trim(), xl.getValue()==null?"":xl.getValue());
	            			}
	            			else if (xl.getKey().equalsIgnoreCase("Calctype")){
	            				result.put(xl.getKey().trim(), xl.getValue()==null?"":xl.getValue());
	            			}else if (xl.getKey().equalsIgnoreCase("RegulatoryCode")){
	            				result.put(xl.getKey().trim(), xl.getValue()==null?"":xl.getValue());
	            			}else if (xl.getKey().equalsIgnoreCase("MinimumPremium")){
	            				result.put(xl.getKey().trim(), xl.getValue()==null?"":xl.getValue());
	            			}else if(xl.getKey().equalsIgnoreCase("Status")) {
	            				result.put(xl.getKey().trim(), xl.getValue()==null?"":xl.getValue());

	            			}
	            		}	            		
	            		resultList.add(result);	            		
	            	}
	            log.info("Response || "+json.toJson(resultList));
	           
	            DozerBeanMapper mapper =new DozerBeanMapper();
	            List<FactorParamsInsert> factorParamsInserts = new ArrayList<FactorParamsInsert>();
	            int sno =1;
	            for(Map<Object,Object> datas:resultList) {   
	            	UploadFactorRequest data=  mapper.map(datas , UploadFactorRequest.class );	            	
		            FactorParamsInsert factor =mapper.map(data , FactorParamsInsert.class );		            	 
		            factor.setMinimumPremium(datas.get("MinimumPremium")==null?"":datas.get("MinimumPremium").toString());
		            factor.setRate(datas.get("Rate")==null?"":datas.get("Rate").toString());
		            factor.setCalType(datas.get("CalcType")==null?"":datas.get("CalcType").toString());
		            factor.setRegulatoryCode(datas.get("RegulatoryCode")==null?"":datas.get("RegulatoryCode").toString());
		            factor.setSno(String.valueOf(sno));
		            factor.setStatus(datas.get("Status")==null?"":datas.get("Status").toString());
		            factorParamsInserts.add(factor);		            	 
		            	sno++;	            	
	            }
	     
	            // frame FactorRateRequest
	            log.info("Xl Data Response || "+json.toJson(factorParamsInserts));
	            FactorRateSaveReq req = new FactorRateSaveReq();
	            req.setAgencyCode(StringUtils.isBlank(request.getAgencyCode())?"":request.getAgencyCode());
	            req.setBranchCode(StringUtils.isBlank(request.getBranchCode())?"":request.getBranchCode());
	            req.setCompanyId(request.getInsuranceId());
	            req.setProductId(request.getProductId());
	            req.setCoverId(request.getCoverId());
	            req.setSectionId(request.getSectionId());
	            req.setSubCoverId(request.getSectionId());
	            req.setEffectiveDateStart(effectiveDate);
	            req.setFactorTypeId(factorTypeId);
	            req.setRemarks(remarks);
	            req.setSubCoverYn(StringUtils.isBlank(request.getSubCoverId())?"0":request.getSubCoverId().equals("0")?"N":"Y");
	            req.setSubCoverId(request.getSubCoverId());
	            req.setCreatedBy(request.getCreatedBy());
	            req.setStatus(request.getStatus());
	            req.setFactorParams(factorParamsInserts);
	            
	            log.info("Factor Insert Request || "+json.toJson(req));
	
	            CommonRes data = new CommonRes();
	            
	            validation= entityService.validateFactorRateDetails(req,token);

	    		if (validation != null && validation.size() != 0) {
	    			data.setCommonResponse(null);
	    			data.setIsError(true);
	    			data.setErrorMessage(validation);
	    			data.setMessage("Failed");
	    			comRes.setErrorMessage(validation);
	    			return comRes;

	    		} else {
	    			
	    			SuccessRes res = entityService.insertFactorRateDetails(req);
	    			comRes.setCommonResponse(res);
	    			comRes.setIsError(false);
	    			comRes.setMessage("Success");
	    			res.setFactorTypeId(factorTypeId);
	    			return comRes;

	    		}
	            
            	}else {
            		comRes.setMessage("No Record Found in FactorTypeDetails");
            		return comRes;
            	}
            	
           }else {
        	   
        	comRes.setMessage("No Record Found in Xl Sheet");
       		return comRes;
       		
           }
		}catch (Exception e) {
			e.printStackTrace();
			log.error(e);
            comRes.setMessage("Failed");
            comRes.setIsError(true);

		}
		return comRes;
		
		
	}

	//Premia table Download
	@Override
	public CommonRes premiaDownload(PremiaFileDownloadRequest req) {
		log.info("File download request : "+json.toJson(req));
		byte byteArry[] =null ;
		com.maan.eway.res.CommonRes response = new com.maan.eway.res.CommonRes();
		FileDownloadRes  res =new FileDownloadRes();
		List<Error> errors = new ArrayList<Error>();
		try {
			System.out.println("Premia Intergration Excel Download Starts............................");
			Map<String,Object> premia=getallPremiaConfig(req);
			System.out.println("Premia Config Master  : "+premia);
			int count=0;
			String[] tableList=null;
			if(premia!=null) {
				String premiaId =premia.get("PREMIA_ID").toString();
				String tableName =premia.get("TABLE_NAME").toString();
				count= countPremiaId(premiaId); 
				tableList=tableList( tableName);
			}
			Integer premiaId=0;
			String tableString=null;
			String table1=null;
			XSSFWorkbook workbook = new XSSFWorkbook();
			for(String data: tableList ) {
			table1=data;
			premiaId=premiaId+1;
			
			tableString=capitalizeWordsWithoutUnderscore(table1.replaceAll("\\s", ""));
			System.out.println("Premia Id  : "+premiaId);
			System.out.println("Premia Table  : "+tableString);
			Map<String,Object> object=queryService.getPremiaXlColumns(req,premiaId);
			if(object!=null) {
				String input =object.get("QUERY_COLUMNS").toString();
				String columns = convertToCamelCase(input);
				String xlColumns =object.get("XL_COLUMNS").toString();
				List<Object[][]> obj =queryService.getPremiaDetails(req,columns,tableString);
			
				
				
				Sheet sheet =workbook.createSheet(table1);
				
				XSSFCellStyle cellStyle =workbook.createCellStyle();
				XSSFFont font = workbook.createFont();
					
				font.setBold(true);
				font.setFontHeight(10);
				font.setFontName("Arial");
				cellStyle.setFont(font);
					
				String[] headers =xlColumns.split(",");
					
				int rowNum = 1;
					
				Row row =sheet.createRow(0);
					
				for (int i =0;i<headers.length;i++) {
					Cell cell =row.createCell(i);
					cell.setCellValue(headers[i]);
					row.getCell(i).setCellStyle(cellStyle);
						
				}
				
				 // Auto-size the cells in the first row
		        for (int i = 0; i <headers.length; i++) {
		            sheet.autoSizeColumn(i);
		            
		        }
				
				if(!CollectionUtils.isEmpty(obj)) { 
					
					for(Object [] ob :obj) {
						row =sheet.createRow(rowNum++);
						int col =0;
						for(Object str : ob) {
							Cell cell =row.createCell(col++);
							cell.setCellValue(str==null?"":str.toString());
						}
					}
				}

					
				
				
			}else {
				errors.add(new Error("101", "FileDownload", "Excle header columns not found.."));
				response.setErrorMessage(errors);
				return 	response;
			}
			}
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();
			byteArry = bos.toByteArray();
			String prefix = "data:application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;base64,";
			String base64 = Base64Utils.encodeToString(byteArry);
			res.setFile(prefix+base64);
			response.setCommonResponse(res);
		}catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		}
		return response;
	}

	public static String convertToCamelCase(String input) {
        String[] parts = input.toLowerCase().split("_");
        for (int i = 1; i < parts.length; i++) {
            parts[i] = parts[i].substring(0, 1).toUpperCase() + parts[i].substring(1);
        }
        return String.join("", parts);
    }
	public int  countPremiaId(String premiaId) {
        String[] parts = premiaId.split(",");
        for (int i = 1; i < parts.length; i++) {
            parts[i] = parts[i];
        }
        int count= parts.length;
        return count;
        
    }
	public String[]  tableList(String tableName) {
		
        String[] parts = tableName.split(",");
        for (int i = 0; i < parts.length; i++) {
        	 if ( parts[i].startsWith("[")) {
        		 parts[i] =  parts[i].substring(1);
 	        }else  if ( parts[i].endsWith("]")){
 	        	 parts[i] =  parts[i].substring(0,  parts[i].length() - 1);
 	        }
            parts[i] = parts[i];
        }
        return parts;
        
    }

//	 private static String capitalizeWordsWithoutUnderscore(String input) {
//	        return Arrays.stream(input.split("_"))
//	                .map(word -> Character.toUpperCase(word.charAt(0)) + word.substring(1).toLowerCase())
//	                .collect(Collectors.joining());
//	    }
	 private static String capitalizeWordsWithoutUnderscore(String input) {
	        StringBuilder result = new StringBuilder();

	        boolean capitalizeNext = true;

	        for (char c : input.toLowerCase().toCharArray()) {
	            if (c == '_') {
	                capitalizeNext = true;
	            } else {
	                result.append(capitalizeNext ? Character.toUpperCase(c) : c);
	                capitalizeNext = false;
	            }
	        }

	        return result.toString();
	    }

	
	
	public Map<String,Object> getallPremiaConfig(PremiaFileDownloadRequest req) {
		List<String> tableName = new ArrayList<String>();
		List<Integer> premiaId =new ArrayList<Integer>();
		Map<String,Object> res =new HashMap<String,Object>();
		try {
			Date today = new Date();
			Calendar cal = new GregorianCalendar();
			cal.setTime(today);
			cal.set(Calendar.HOUR_OF_DAY, 23);
			cal.set(Calendar.MINUTE, 1);
			today = cal.getTime();

			List<PremiaConfigMaster> list = new ArrayList<PremiaConfigMaster>();
		
			// Find Latest Record
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<PremiaConfigMaster> query = cb.createQuery(PremiaConfigMaster.class);

			// Find All
			Root<PremiaConfigMaster> b = query.from(PremiaConfigMaster.class);

			// Select
			query.select(b);

			// Amend ID Max Filter
			Subquery<Long> amendId = query.subquery(Long.class);
			Root<PremiaConfigMaster> ocpm1 = amendId.from(PremiaConfigMaster.class);
			amendId.select(cb.max(ocpm1.get("amendId")));
			Predicate a1 = cb.equal(ocpm1.get("companyId"), b.get("companyId"));
			Predicate a2 = cb.equal(ocpm1.get("branchCode"),b.get("branchCode"));
			Predicate a3 = cb.equal(ocpm1.get("premiaId"), b.get("premiaId"));
			Predicate a4 = cb.equal(ocpm1.get("productId"), b.get("productId"));

			amendId.where(a1, a2,a3,a4);

			// Order By
			List<Order> orderList = new ArrayList<Order>();
			orderList.add(cb.asc(b.get("branchCode")));

			
			// Where
			Predicate n1 = cb.equal(b.get("amendId"), amendId);
			Predicate n2 = cb.equal(b.get("companyId"), req.getCompanyId());
			Predicate n6 = cb.equal(b.get("productId"),req.getProductId());
			Predicate n7 = cb.equal(b.get("productId"), "99999");
			Predicate n8 = cb.or(n6,n7);
			if((StringUtils.isNotBlank(req.getProductId()))) {
			query.where(n1,n2,n8).orderBy(orderList);
			}
			
			else {
				query.where(n1,n2).orderBy(orderList);
				
			}
			
			// Get Result
			TypedQuery<PremiaConfigMaster> result = em.createQuery(query);

			list = result.getResultList();
			if(!CollectionUtils.isEmpty(list)) {
				for (PremiaConfigMaster fac :list) {
					tableName.add(fac.getPremiaTableName());
					premiaId.add(fac.getPremiaId());
					}
		}
			res.put("TABLE_NAME", tableName.toString());
			res.put("PREMIA_ID", premiaId.toString());
			log.info("getPremiaTable Response || "+json.toJson(res));
	
		}catch (Exception e) {
			e.printStackTrace();
			log.info("Exception is ---> " + e.getMessage());
			return null;
		}
		return res;
	}
}
