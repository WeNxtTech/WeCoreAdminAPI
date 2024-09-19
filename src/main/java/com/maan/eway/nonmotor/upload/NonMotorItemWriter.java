package com.maan.eway.nonmotor.upload;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.StringJoiner;

import org.apache.commons.lang3.Range;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ParameterizedPreparedStatementSetter;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.maan.eway.batch.repository.TransactionControlDetailsRepository;
import com.maan.eway.batch.req.EwayBatchReq;
import com.maan.eway.batch.res.EwayUploadRes;
import com.maan.eway.batch.res.XlConfigData;
import com.maan.eway.vehicleupload.Record;

@Configuration
@Component
public class NonMotorItemWriter {

Logger log =LogManager.getLogger(NonMotorItemWriter.class);
Gson print =new Gson();

String batchRequest ="";

public void setEwayRequest(String batchRequest) {
	this.batchRequest=batchRequest;
	
}

@Bean
@JobScope
public ItemWriter<Record> itemWriter(TransactionControlDetailsRepository fleetTempRepo, JdbcTemplate jdbcTemplate) {
	return new ItemWriter<Record>() {/// EntityManager,DataSource dataSource, EntityManagerFactory em,
		public void write(Chunk<? extends Record> items) throws Exception {
			try {
				@SuppressWarnings("unchecked")
				List<Record> recordsList = (List<Record>) items.getItems();
				EwayUploadRes response = new EwayUploadRes();
				ObjectMapper mapper = new ObjectMapper();
				EwayBatchReq request = new EwayBatchReq();
				request = mapper.readValue(batchRequest, EwayBatchReq.class);
				response = request.getEwayUploadRes();
				String productId =response.getProductId();
				log.info("Eway batch write start with productId:" +productId);
				if(("5".equals(productId) || "46".equals(productId))){
					batchInsert_1(recordsList, jdbcTemplate, response,fleetTempRepo);
				}//else if ("100019".equalsIgnoreCase(companyId)){
				//	batchInsert_3(recordsList, jdbcTemplate, response,fleetTempRepo);// for ugandaCompany
				//}
				else {
					batchInsert_2(recordsList, jdbcTemplate, response,fleetTempRepo);

				}
				log.info("Eway batch write endwith productId:" +productId);
				
			} catch (JsonParseException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	};
}

protected int[][] batchInsert_1(List<Record> records, JdbcTemplate jdbcTemplate,	EwayUploadRes response, TransactionControlDetailsRepository fleetTempRepo) {
	try {
		System.out.println("batchInsert:");
		String excelTableName = response.getExcelrawtablename();
		int batchSize = records.size();
		
		HashMap<String,String> map =getTableColumns(response.getXlConfigData());
		String rawTableFields = map.get("RAW_COLUMNS").toString();
		String[] listcol = rawTableFields.split(",");
		int length = listcol.length;
		String prepareValues =map.get("PREPARE_VALUES").toString();
		
	
					
		String finalquery = "INSERT INTO " + excelTableName + "(COMPANY_ID,PRODUCT_ID,SECTION_ID,REQUEST_REFERENCE_NO,TYPEID,ERROR_DESC,STATUS,"
				+ "BROKER_BRANCHCODE,AC_EXECUTIVEID,BROKER_CODE,LOGIN_ID,SUB_USERTYPE,APPLICATION_ID,CUSTOMER_REFERENCENO,ENDORSEMENT_YN,"
				+ "ENDORSEMENT_DATE,ENDORSEMENT_EFFECTIVE_DATE,ENDORSEMENT_REMARKS,ENDORSEMENT_TYPE,ENDORSEMENT_TYPE_DESC,ENDT_CATEGORY_DESC,ENDT_COUNT,"
				+ "ENDT_PREV_POLICYNO,ENDT_STATUS,IS_FINANCE_ENDT,ORGINAL_POLICYNO,EXCHANGE_RATE,HAVE_PROMOCODE,NO_OF_VEHICLES,"
				+ "POLICY_START_DATE,POLICY_END_DATE,PROMOCODE,CURRENCY,BRANCH_CODE,AGENCY_CODE,ID_NUMBER,USER_TYPE,NCD_YN,SOURCE_TYPE,CUSTOMER_CODE,CUSTOMER_NAME,BDM_CODE,OWNER_CATEGORY,SOURCE_TYPEID,"
				+ "" + rawTableFields + ") VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,"
				+ "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,"
				+prepareValues + ")";
		
			int[][] updateCounts = jdbcTemplate.batchUpdate(finalquery, records, batchSize,
				new ParameterizedPreparedStatementSetter<Record>() {
					public void setValues(PreparedStatement ps, Record argument) throws SQLException {
						//log.info("Eway batch data========>"+argument==null?"":print.toJson(argument));
						
						final String error =validateDetails(argument,response);
						DateFormat dateformat = new SimpleDateFormat("dd/MM/yyyy");
						ps.setInt(1, Integer.valueOf(response.getCompanyId()));
						ps.setInt(2, Integer.valueOf(response.getProductId()));
						ps.setInt(3, StringUtils.isBlank(response.getSectionId())?0:Integer.valueOf(response.getSectionId()));
						ps.setString(4,  StringUtils.isBlank(response.getRequestReferenceNo())?null:response.getRequestReferenceNo());
						ps.setInt(5, Integer.valueOf(response.getTypeId()));
						ps.setString(6,  StringUtils.isBlank(error) ?null:error);
						ps.setString(7,  StringUtils.isBlank(error) ?"Y":"E");
						ps.setString(8, StringUtils.isBlank(response.getBrokerBranchCode())?null:response.getBrokerBranchCode());
						ps.setString(9, StringUtils.isBlank(response.getAcExecutiveId())?null:response.getAcExecutiveId());
						ps.setString(10, StringUtils.isBlank(response.getBeokerCode())?null:response.getBeokerCode());
						ps.setString(11, StringUtils.isBlank(response.getLoginId())?null:response.getLoginId());
						ps.setString(12, StringUtils.isBlank(response.getSubUserType())?null:response.getSubUserType());
						ps.setString(13, StringUtils.isBlank(response.getApplicationId())?null:response.getApplicationId());
						ps.setString(14, StringUtils.isBlank(response.getCustomerRefNo())?null:response.getCustomerRefNo());
						ps.setString(15, StringUtils.isBlank(response.getEndorsementYn())?null:response.getEndorsementYn());
						ps.setString(16, StringUtils.isBlank(response.getEndorsementDate())?null:response.getEndorsementDate());
						ps.setString(17, StringUtils.isBlank(response.getEndorsementEffectiveDate())?null:response.getEndorsementEffectiveDate());
						ps.setString(18, StringUtils.isBlank(response.getEndorsementRemarks())?null:response.getEndorsementRemarks());
						ps.setString(19, StringUtils.isBlank(response.getEndorsementType())?null:response.getEndorsementType());
						ps.setString(20, StringUtils.isBlank(response.getEndorsementTypeDesc())?null:response.getEndorsementTypeDesc());
						ps.setString(21, StringUtils.isBlank(response.getEndtCategoryDesc())?null:response.getEndtCategoryDesc());							
						ps.setString(22, StringUtils.isBlank(response.getEndtCount())?null:response.getEndtCount());
						ps.setString(23, StringUtils.isBlank(response.getEndtPrevPolicyNo())?null:response.getEndtPrevPolicyNo());
						ps.setString(24, StringUtils.isBlank(response.getEndtStatus())?null:response.getEndtStatus());							
						ps.setString(25, StringUtils.isBlank(response.getIsFinanceEndt())?null:response.getIsFinanceEndt());
						ps.setString(26, StringUtils.isBlank(response.getOrginalPolicyNo())?null:response.getOrginalPolicyNo());
						ps.setString(27, StringUtils.isBlank(response.getExchangeRate())?null:response.getExchangeRate());
						ps.setString(28, StringUtils.isBlank(response.getHavePromoCode())?null:response.getHavePromoCode());						
						ps.setString(29, StringUtils.isBlank(response.getNoOfVehicles())?null:response.getNoOfVehicles());
						ps.setString(30, StringUtils.isBlank(response.getPolicyStartDate())?null:response.getPolicyStartDate());
						ps.setString(31, StringUtils.isBlank(response.getPolicyEndDate())?null:response.getPolicyEndDate());
						ps.setString(32, StringUtils.isBlank(response.getPromoCode())?null:response.getPromoCode());
						ps.setString(33, StringUtils.isBlank(response.getCurrency())?null:response.getCurrency());
						ps.setString(34, StringUtils.isBlank(response.getBranchCode())?null:response.getBranchCode());
						ps.setString(35, StringUtils.isBlank(response.getAgencyCode())?null:response.getAgencyCode());
						ps.setString(36, StringUtils.isBlank(response.getIdnumber())?null:response.getIdnumber());
						ps.setString(37, StringUtils.isBlank(response.getUserType())?null:response.getUserType());
						ps.setString(38, response.getNcdYn());
						ps.setString(39, StringUtils.isBlank(response.getSourceType())?null:response.getSourceType());
						ps.setString(40, StringUtils.isBlank(response.getCustomerCode())?null:response.getCustomerCode());
						ps.setString(41, StringUtils.isBlank(response.getResOwnerName())?null:response.getResOwnerName());
						ps.setString(42, StringUtils.isBlank(response.getBdmCode())?null:response.getBdmCode());
						ps.setString(43, StringUtils.isBlank(response.getOwnerCategory())?"None":response.getOwnerCategory());
						ps.setString(44, StringUtils.isBlank(response.getSourceTypeId())?"":response.getSourceTypeId());
						
						
						for (int i = 1; i <= length; i++) {
							ps.setString(i + 44, argument.getColumnByIndex(i - 1) == null ? null
									: argument.getColumnByIndex(i - 1).toString().trim());
							log.info("rowid: "+i+", rowvalue : "+argument.getColumnByIndex(i - 1) );
						}
						log.info("entryDate:"+dateformat.format(new Date()) );
						log.info("finalquery:"+finalquery);
					}
				});
		//log.info("batchSize:"+batchSize+", updateCounts : "+updateCounts +finalquery);
		 
		return updateCounts;
	} catch (Exception e) {
		log.error(e);
		e.printStackTrace();
		
	}finally {
		try {
		jdbcTemplate.getDataSource().getConnection().close();
			
		} catch (SQLException e) {
			log.error(e);
		}
	}
	return null;
}

protected int[][] batchInsert_2(List<Record> records, JdbcTemplate jdbcTemplate,	EwayUploadRes response, TransactionControlDetailsRepository fleetTempRepo) {
	try {
		System.out.println("batchInsert:");
		String excelTableName = response.getExcelrawtablename();
		int batchSize = records.size();
		
		
		HashMap<String,String> map =getTableColumns(response.getXlConfigData());
		String rawTableFields = map.get("RAW_COLUMNS").toString();
		String[] listcol = rawTableFields.split(",");
		int length = listcol.length;
		String prepareValues =map.get("PREPARE_VALUES").toString();
			
		String finalquery = "INSERT INTO " + excelTableName + "(COMPANY_ID,PRODUCT_ID,REQUEST_REFERENCE_NO,TYPEID,RISK_ID,"
				+ "QUOTE_NO,CREATED_BY,ERROR_DESC,STATUS,SECTION_ID,PASS_STATE_CODE,ENDORSEMENT_TYPE,EMPLOYEE_TYPE,UPLOAD_TYPE,"+ rawTableFields + ") VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,"+prepareValues + ")";					
		
		
			int[][] updateCounts = jdbcTemplate.batchUpdate(finalquery, records, batchSize,
				new ParameterizedPreparedStatementSetter<Record>() {
					public void setValues(PreparedStatement ps, Record argument) throws SQLException {
						log.info("Eway batch data========>"+argument==null?"":print.toJson(argument));
						final String validationRes =validateDetails(argument,response);
						DateFormat dateformat = new SimpleDateFormat("dd/MM/yyyy");
						ps.setInt(1, StringUtils.isBlank(response.getCompanyId())?null:Integer.valueOf(response.getCompanyId()));
						ps.setInt(2, StringUtils.isBlank(response.getProductId())?null:Integer.valueOf(response.getProductId()));
						ps.setString(3,  StringUtils.isBlank(response.getRequestReferenceNo())?null:response.getRequestReferenceNo());
						ps.setInt(4, StringUtils.isBlank(response.getTypeId())?null:Integer.valueOf(response.getTypeId()));
						ps.setInt(5, StringUtils.isBlank(response.getRiskId())?0:Integer.valueOf(response.getRiskId()));
						ps.setString(6,  StringUtils.isBlank(response.getQuoteNo()) ?null:response.getQuoteNo());
						ps.setString(7,  StringUtils.isBlank(response.getLoginId()) ?null:response.getLoginId());
						ps.setString(8,  StringUtils.isBlank(validationRes) ?null:validationRes);
						ps.setString(9,  StringUtils.isBlank(validationRes) ?"Y":"E");
						ps.setString(10, StringUtils.isBlank(response.getSectionId()) ?null:response.getSectionId());
						ps.setString(11, StringUtils.isBlank(response.getStateCode()) ?null:response.getStateCode());
						ps.setString(12, response.getEndorsementYn());
						ps.setString(13, "N");
						ps.setString(14,response.getUploadType());
						for (int i = 1; i <= length; i++) {
							ps.setString(i + 14, argument.getColumnByIndex(i - 1) == null ? null
									: argument.getColumnByIndex(i - 1).toString().trim());
							log.info("rowid: "+i+", rowvalue : "+argument.getColumnByIndex(i - 1) );
						}
						log.info("entryDate:"+dateformat.format(new Date()) );
						log.info("finalquery:"+finalquery);
					}
				});
		log.info("batchSize:"+batchSize+", updateCounts : "+updateCounts +finalquery);
		 
		return updateCounts;
	} catch (Exception e) {
		log.error(e);
		e.printStackTrace();
		
	}finally {
		try {
		jdbcTemplate.getDataSource().getConnection().close();
			
		} catch (SQLException e) {
			log.error(e);
		}
	}
	return null;
}
private HashMap<String,String> getTableColumns(List<XlConfigData> xlConfigData) {
	StringJoiner prepare = new StringJoiner(",");
	StringJoiner rwaTableColumns =new StringJoiner(",");
	HashMap<String,String> hashMap =new HashMap<String,String>();
	try {
		for(XlConfigData col :xlConfigData) {
			if("NUMBER".equalsIgnoreCase(col.getDatatype())) {
				prepare.add("REPLACE(?,',','')");
			}else {
				prepare.add("?");
			}
			
			rwaTableColumns.add(col.getRawTableColumns());
		}
		hashMap.put("PREPARE_VALUES", String.valueOf(prepare));
		hashMap.put("RAW_COLUMNS", String.valueOf(rwaTableColumns));
		
		//log.info("EwayBatchWriter || getTableColumns || PrepareStatement values"+prepare.toString());
		//log.info("EwayBatchWriter || getTableColumns || RawTableColumns"+rwaTableColumns.toString());
	}catch (Exception e) {
		log.error(e);
	}
	return hashMap;
}


private String validateDetails(Record items, EwayUploadRes response) {
	try {
		
		Object[] excelValueList =items.getColumns();//.get(0)
		String[] datatypeList = response.getTableColumnsDataType().split("~");
		String[] dateformatList = response.getExceldateformatlist().replaceAll("\\~$","").split("\\~",-1);//.split("~")
		String[] excelHeaderList = response.getExcelHeaderColumns().split(",");
		String[] mandatoryList = response.getExcelmandatorylist().split("~");
		String[] fieldLength = response.getDataFieldLength().split("~");
		String[] rangeColumn = response.getDataRange().split("~");
		String errorDesc ="";
		for(int i=0;i<excelValueList.length;i++) {
			
			String headername = excelHeaderList.length>0?excelHeaderList[i].replace("\"", ""):"";
			String excelValue = items.getColumnByIndex(i).toString();//.get(0)
			String mandatoryYn = mandatoryList.length>0?mandatoryList[i]:"";
			String datatype = datatypeList.length>0?datatypeList[i]:"";
			String dateFormat = dateformatList.length>0?dateformatList[i]:"";
			String dataLength =fieldLength.length>0?fieldLength[i]:"";
			String rangeCondition =rangeColumn.length>0?rangeColumn[i]:"";
			String errors = "";
			if(StringUtils.isNotBlank(mandatoryYn)&&mandatoryYn.equalsIgnoreCase("Y")&&StringUtils.isBlank(excelValue)) {
				errors = headername + " value is Mandatory";
			}else if(StringUtils.isNotBlank(excelValue)) {
				if("NUMBER".equalsIgnoreCase(datatype)){
					String excelCellValue =excelValue.replaceAll(",", "").trim();
					if(!StringUtils.isNumeric(excelCellValue)) {
						errors = headername + " value is Must be Numeric";
					}else {
						 if(!"0".equals(dataLength)) {
							Integer number =new BigDecimal(excelValue).toPlainString().length();
							errors =number>Integer.valueOf(dataLength)?""+headername + " value length should be in range of "+dataLength+" digits":"";
							
						}if(!"0".equals(rangeCondition)) {
							String[] rangeVal =rangeCondition.split("-");
							Range<Long> range =Range.between(Long.valueOf(rangeVal[0]), Long.valueOf(rangeVal[1]));
							if(!range.contains(Long.valueOf(excelValue))) {
								errors = headername + " value should between this range " +rangeCondition+"";
							}
						}
					}
				}else if("DOUBLE".equalsIgnoreCase(datatype)) {
					String excelCellValue =excelValue.replaceAll(",", "").trim();
					if(!isDouble(excelCellValue)) {
						errors = headername + " value is Must be Double or Numeric";
					}else {
						if(!"0".equals(dataLength)) {	
							errors =excelValue.length()>Integer.valueOf(dataLength)?headername + " value length should be in range of "+dataLength+" digits"
									:"";
						}if(!"0".equals(rangeCondition)) {
							String[] rangeVal =rangeCondition.split("-");
							Range<Double> range =Range.between(Double.valueOf(rangeVal[0]), Double.valueOf(rangeVal[1]));
							if(!range.contains(Double.valueOf(excelValue))) {
								errors = headername + " value should between this range " +rangeCondition+"";
							}
						}
					}
				}
				if("DATE".equalsIgnoreCase(datatype)) {
					if(!excelValue.matches("([0-9]{2})/([0-9]{2})/([0-9]{4})") && !excelValue.matches("([0-9]{2})-([0-9]{2})-([0-9]{4})")) {
						errors="Please Enter Valid Date format in "+ headername + ": Given Value is - >  " + excelValue +"Excepted date fromat is DD/MM/YYYY";
				   }else if(!"0".equals(rangeCondition)){
						LocalDate sysdate =LocalDate.now();
						LocalDate dateOfbirth =LocalDate.parse(excelValue, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
						Integer years =Period.between(dateOfbirth,sysdate).getYears();
						String[] rangeVal =rangeCondition.split("-");
						Range<Integer> range =Range.between(Integer.valueOf(rangeVal[0]), Integer.valueOf(rangeVal[1]));
						if(!range.contains(Integer.valueOf(years))) {
							errors = headername + " value should between this age range " +rangeCondition+"";
						}
					}
							
					
				}else if("VARCHAR".equalsIgnoreCase(datatype)) {
					if(StringUtils.isNotBlank(excelValue)) {
						//if(excelValue.matches("[$&+,:;=?@#|]")) {
							//errors="Special characters is not allowed in "+ headername + ": Given Value is - >  " + excelValue +" ";
						//}
						if(!"0".equals(dataLength)) {							
							if(excelValue.length()>Long.valueOf(dataLength)) {
								errors = headername + " value length should be in range of " +dataLength+"character";
							}
						}
					}
				}
			}
			if(StringUtils.isNotBlank(errors)) {
				errorDesc +=errors+"~";
			}
		
		}	
		
		return errorDesc;
	}catch (Exception e) {
		e.printStackTrace();
	}
	return "";
}


private boolean isDouble(String value) {
	try{
	       Double.parseDouble(value);
	      return true;
	   }
	   catch (Exception ex){
		   return false;
	   }
}

protected int[][] batchInsert_3(List<Record> records, JdbcTemplate jdbcTemplate,EwayUploadRes response, TransactionControlDetailsRepository fleetTempRepo) {
	try {
		System.out.println("batchInsert:");
		String excelTableName = response.getExcelrawtablename();
		int batchSize = records.size();
		
		
		HashMap<String,String> map =getTableColumns(response.getXlConfigData());
		String rawTableFields = map.get("RAW_COLUMNS").toString();
		String[] listcol = rawTableFields.split(",");
		int length = listcol.length;
		String prepareValues =map.get("PREPARE_VALUES").toString();
					
		String finalquery = "INSERT INTO " + excelTableName + "(COMPANY_ID,PRODUCT_ID,TYPE_ID,REQUEST_REFERENCE_NO,"
				+ "CREATED_BY,ERROR_DESC,STATUS,UPLOAD_TYPE,BRANCH_CODE,BROKER_BRANCH_CODE,CUSTOMER_CODE,"
				+ "BDM_CODE,BROKER_CODE,APPLICATION_ID,CUSTOMER_REFERENCE_NO,ID_NUMBER,AGENCY_CODE,SOURCE_TYPE_ID,"
				+ "POLICY_START_DATE,POLICY_END_DATE,CURRENCY,EXCHANGE_RATE,HAVE_PROMOCODE,FLEET_OWNER_YN,USERTYPE,"
				+ "SAVE_OR_SUBMIT,CAR_ALARM_YN,SUB_USER_TYPE,LOGIN_ID,CUSTOMER_NAME,OWNER_CATEGORY,"+ rawTableFields + ") VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,"+prepareValues + ")";
								
		
		
			int[][] updateCounts = jdbcTemplate.batchUpdate(finalquery, records, batchSize,
				new ParameterizedPreparedStatementSetter<Record>() {
					public void setValues(PreparedStatement ps, Record argument) throws SQLException {
						log.info("Eway batch data========>"+argument==null?"":print.toJson(argument));
						
						String error =validateDetails(argument,response);
						DateFormat dateformat = new SimpleDateFormat("dd/MM/yyyy");
						ps.setInt(1, StringUtils.isBlank(response.getCompanyId())?null:Integer.valueOf(response.getCompanyId()));
						ps.setInt(2, StringUtils.isBlank(response.getProductId())?null:Integer.valueOf(response.getProductId()));
						ps.setInt(3, StringUtils.isBlank(response.getTypeId())?null:Integer.valueOf(response.getTypeId()));
						ps.setString(4,  StringUtils.isBlank(response.getRequestReferenceNo())?null:response.getRequestReferenceNo());
						ps.setString(5,  StringUtils.isBlank(response.getLoginId()) ?null:response.getLoginId());
						ps.setString(6,  StringUtils.isBlank(error) ?null:error);
						ps.setString(7,  StringUtils.isBlank(error) ?"Y":"E");
						ps.setString(8,response.getUploadType());
						ps.setString(9,response.getBranchCode());
						ps.setString(10, response.getBrokerBranchCode());
						ps.setString(11, response.getCustomerCode());
						ps.setString(12, response.getBdmCode());
						ps.setString(13, response.getBeokerCode());
						ps.setString(14, response.getApplicationId());
						ps.setString(15, response.getCustomerRefNo());
						ps.setString(16, response.getIdnumber());
						ps.setString(17, response.getAgencyCode());
						ps.setString(18, response.getUserType());
						ps.setString(19, response.getPolicyStartDate());
						ps.setString(20, response.getPolicyEndDate());
						ps.setString(21, response.getCurrency());
						ps.setString(22, response.getExchangeRate());
						ps.setString(23, response.getHavePromoCode());
						ps.setString(24, "N");
						ps.setString(25, response.getUserType());
						ps.setString(26, "Save");
						ps.setString(27, "Y");
						ps.setString(28, response.getSubUserType());
						ps.setString(29, response.getLoginId());
						ps.setString(30, StringUtils.isBlank(response.getResOwnerName())?"None":response.getResOwnerName());
						ps.setString(31, StringUtils.isBlank(response.getOwnerCategory())?"None":response.getOwnerCategory());
						
						for (int i = 1; i <= length; i++) {
							ps.setString(i + 31, argument.getColumnByIndex(i - 1) == null ? null
									: argument.getColumnByIndex(i - 1).toString().trim());
							log.info("rowid: "+i+", rowvalue : "+argument.getColumnByIndex(i - 1) );
						}
						log.info("entryDate:"+dateformat.format(new Date()) );
						log.info("finalquery:"+finalquery);
					}
				});
		log.info("batchSize:"+batchSize+", updateCounts : "+updateCounts +finalquery);
		 
		return updateCounts;
	} catch (Exception e) {
		log.error(e);
		e.printStackTrace();
		
	}finally {
		try {
		jdbcTemplate.getDataSource().getConnection().close();
			
		} catch (SQLException e) {
			log.error(e);
		}
	}
	return null;
}

}
