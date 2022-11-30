package com.maan.eway.fileupload;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.util.CollectionUtils;

import com.google.gson.Gson;
import com.maan.eway.common.res.CommonRes;
import com.maan.eway.error.Error;

@Service
public class EwayFileUploadServiceImpl implements EwayFileUploadService {
	
	
	Logger log =LogManager.getLogger(getClass());
	
	private static Gson json = new Gson();
	
	@Autowired
	private JpqlQueryServiceImpl queryService;

	@Override
	public com.maan.eway.res.CommonRes download(FileDownloadRequest req)  {
		log.info("File download request : "+json.toJson(req));
		byte byteArry[] =null ;
		com.maan.eway.res.CommonRes response = new com.maan.eway.res.CommonRes();
		FileDownloadRes  res =new FileDownloadRes();
		List<Error> errors = new ArrayList<Error>();
		try {
			String agencyCode =StringUtils.isBlank(req.getAgencyCode())?"99999":req.getAgencyCode();
			String branchCode=StringUtils.isBlank(req.getBranchCode())?"99999":req.getBranchCode();
			req.setAgencyCode(agencyCode);
			req.setBranchCode(branchCode);
			Map<String,Object> object=queryService.getFactorXlColumns(req);
			if(object.size()>0 && object!=null) {
				
				String columns =object.get("QUERY_COLUMNS").toString();
				String factorId =object.get("FACTOR_ID").toString();
				String xlColumns =object.get("XL_COLUMNS").toString();
				List<Object[][]> obj =queryService.getFactorRateDetails(req, columns, factorId);
				
				if(!CollectionUtils.isEmpty(obj)) { 
					
					XSSFWorkbook workbook = new XSSFWorkbook();
					XSSFSheet sheet =workbook.createSheet("FACTOR_RATE_DETAILS");
					
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
					
					for(Object [] ob :obj) {
						row =sheet.createRow(rowNum++);
						int col =0;
						for(Object str : ob) {
							Cell cell =row.createCell(col++);
							cell.setCellValue(str==null?"":str.toString());
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
				}else {
					errors.add(new Error("101", "FileDownload", "Records not Found.."));
					response.setErrorMessage(errors);
					return 	response;
				}
				
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

}
