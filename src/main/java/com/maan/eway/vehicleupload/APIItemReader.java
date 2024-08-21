package com.maan.eway.vehicleupload;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.RowMapper;

import com.maan.eway.springbatch.FactorBatchRecordRes;

public class APIItemReader extends JdbcCursorItemReader<FactorBatchRecordRes> implements ItemReader<FactorBatchRecordRes>{
	

	public APIItemReader(DataSource dataSource,String request_ref_no,Integer fromId,Integer toId)
	 {
		setDataSource(dataSource);
		setSql("SELECT FACTOR_TYPE_ID,SNO,COMPANY_ID,PRODUCT_ID,BRANCH_CODE,AGENCY_CODE,SECTION_ID,COVER_ID,SUB_COVER_ID,"
				+ "CREATED_BY,EFFECTIVE_DATE_START,EFFECTIVE_DATE_END,STATUS,REMARKS,PARAM_1,PARAM_2,PARAM_3,PARAM_4,PARAM_5,PARAM_6,"
				+ "PARAM_7,PARAM_8,PARAM_9,PARAM_10,PARAM_11,PARAM_12,UPDATED_BY,RATE,CALC_TYPE,MIN_PREMIUM,REGULATORY_CODE,MASTER_YN,"
				+ "PARAM_13,PARAM_14,PARAM_15,PARAM_16,PARAM_17,PARAM_18,PARAM_19,PARAM_20,PARAM_21,PARAM_22,PARAM_23,PARAM_24,PARAM_25,"
				+ "PARAM_26,PARAM_27,PARAM_28,API_URL,EXCESS_AMOUNT,EXCESS_PERCENT,EXCESS_DESC FROM factor_rate_raw_master where TRAN_ID='"+request_ref_no+"' and ERROR_STATUS!='E'");
		setCurrentItemCount(fromId);
		setMaxItemCount(toId);
		setRowMapper(new FactorRateRowMapper());
	}
	
	public class FactorRateRowMapper implements RowMapper<FactorBatchRecordRes> {
		@Override
		public FactorBatchRecordRes mapRow(ResultSet rs, int rowNum) throws SQLException {
			FactorBatchRecordRes factor  = FactorBatchRecordRes.builder()
					.agencyCode(rs.getString("AGENCY_CODE"))
					.apiUrl(StringUtils.isBlank(rs.getString("API_URL"))?"":rs.getString("API_URL"))
					.branchCode(rs.getString("BRANCH_CODE"))
					.calType(rs.getString("CALC_TYPE"))
					.companyId(String.valueOf(rs.getInt("COMPANY_ID")))
					.coverId(String.valueOf(rs.getInt("COVER_ID")))
					.createdBy(StringUtils.isBlank(rs.getString("CREATED_BY"))?"":rs.getString("CREATED_BY"))
					.effectiveDateEnd(rs.getDate("EFFECTIVE_DATE_END"))
					.effectiveDateStart(rs.getDate("EFFECTIVE_DATE_START"))
					.factorTypeId(String.valueOf(rs.getInt("FACTOR_TYPE_ID")))
					.masterYn(StringUtils.isBlank(rs.getString("MASTER_YN"))?"":rs.getString("MASTER_YN"))
					.minimumPremium(String.valueOf(rs.getDouble("MIN_PREMIUM")))
					.param1(rs.getDouble("PARAM_1")==0?"0":String.valueOf(rs.getDouble("PARAM_1")))
					.param2(rs.getDouble("PARAM_2")==0?"0":String.valueOf(rs.getDouble("PARAM_2")))
					.param3(rs.getDouble("PARAM_3")==0?"0":String.valueOf(rs.getDouble("PARAM_3")))
					.param4(rs.getDouble("PARAM_4")==0?"0":String.valueOf(rs.getDouble("PARAM_4")))
					.param5(rs.getDouble("PARAM_5")==0?"0":String.valueOf(rs.getDouble("PARAM_5")))
					.param6(rs.getDouble("PARAM_6")==0?"0":String.valueOf(rs.getDouble("PARAM_6")))
					.param7(rs.getDouble("PARAM_7")==0?"0":String.valueOf(rs.getDouble("PARAM_7")))
					.param8(rs.getDouble("PARAM_8")==0?"0":String.valueOf(rs.getDouble("PARAM_8")))
					.param9(StringUtils.isBlank(rs.getString("PARAM_9"))?"0":rs.getString("PARAM_9"))
					.param10(StringUtils.isBlank(rs.getString("PARAM_10"))?"0":rs.getString("PARAM_10"))
					.param11(StringUtils.isBlank(rs.getString("PARAM_11"))?"0":rs.getString("PARAM_11"))
					.param12(StringUtils.isBlank(rs.getString("PARAM_12"))?"0":rs.getString("PARAM_12"))
					.productId(String.valueOf(rs.getInt("PRODUCT_ID")))
					.rate(String.valueOf(rs.getDouble("RATE")))
					.regulatoryCode(rs.getString("REGULATORY_CODE"))
					.remarks(StringUtils.isBlank(rs.getString("REMARKS"))?"":rs.getString("REMARKS"))
					.sectionId(String.valueOf(rs.getInt("SECTION_ID")))
					.sno(String.valueOf(rs.getInt("SNO")))
					.status(StringUtils.isBlank(rs.getString("STATUS"))?"":rs.getString("STATUS"))
					.subCoverId(String.valueOf((rs.getInt("SUB_COVER_ID"))))
					.subCoverYn(rs.getInt("SUB_COVER_ID")==0?"Y":"N")
					.param13(StringUtils.isBlank(rs.getString("PARAM_13"))?null:rs.getString("PARAM_13"))
					.param14(StringUtils.isBlank(rs.getString("PARAM_14"))?null:rs.getString("PARAM_14"))
					.param15(StringUtils.isBlank(rs.getString("PARAM_15"))?null:rs.getString("PARAM_15"))
					.param16(StringUtils.isBlank(rs.getString("PARAM_16"))?null:rs.getString("PARAM_16"))
					.param17(StringUtils.isBlank(rs.getString("PARAM_17"))?null:rs.getString("PARAM_17"))
					.param18(StringUtils.isBlank(rs.getString("PARAM_18"))?null:rs.getString("PARAM_18"))
					.param19(StringUtils.isBlank(rs.getString("PARAM_19"))?null:rs.getString("PARAM_19"))
					.param20(StringUtils.isBlank(rs.getString("PARAM_20"))?null:rs.getString("PARAM_20"))
					.param21(rs.getBigDecimal("PARAM_21")==null?"0":String.valueOf(rs.getBigDecimal("PARAM_21")))
					.param22(rs.getBigDecimal("PARAM_22")==null?"0":String.valueOf(rs.getBigDecimal("PARAM_22")))
					.param23(rs.getBigDecimal("PARAM_23")==null?"0":String.valueOf(rs.getBigDecimal("PARAM_23")))
					.param24(rs.getBigDecimal("PARAM_24")==null?"0":String.valueOf(rs.getBigDecimal("PARAM_24")))
					.param25(rs.getBigDecimal("PARAM_25")==null?"0":String.valueOf(rs.getBigDecimal("PARAM_25")))
					.param26(rs.getBigDecimal("PARAM_26")==null?"0":String.valueOf(rs.getBigDecimal("PARAM_26")))
					.param27(rs.getBigDecimal("PARAM_27")==null?"0":String.valueOf(rs.getBigDecimal("PARAM_27")))
					.param28(rs.getBigDecimal("PARAM_28")==null?"0":String.valueOf(rs.getBigDecimal("PARAM_28")))
					.excessAmount(rs.getBigDecimal("EXCESS_AMOUNT")==null?"0":String.valueOf(rs.getBigDecimal("EXCESS_AMOUNT")))
					.excessPercent(rs.getBigDecimal("EXCESS_PERCENT")==null?"0":String.valueOf(rs.getBigDecimal("EXCESS_PERCENT")))
					.excessDesc(StringUtils.isBlank(rs.getString("EXCESS_DESC"))?"":rs.getString("EXCESS_DESC"))
					.build();
			return factor;
		}

		
	}
}