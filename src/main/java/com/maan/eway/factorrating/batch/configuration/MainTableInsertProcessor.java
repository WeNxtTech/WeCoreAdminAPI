package com.maan.eway.factorrating.batch.configuration;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.item.ItemProcessor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maan.eway.bean.FactorRateMaster;
import com.maan.eway.springbatch.FactorRateRawInsert;

public class MainTableInsertProcessor implements ItemProcessor<FactorRateRawInsert, FactorRateMaster> {

	static ObjectMapper mapper = new ObjectMapper();
	
	private List<Map<String,Object>> calcTypes = new ArrayList<>();
	
	private Map<String, Object> coverMD;
	
	private Long amendId ;
	
	public MainTableInsertProcessor(String listItem, String covers, Long amendId) {
		try {
			coverMD =mapper.readValue(covers, Map.class);
			calcTypes = mapper.readValue(listItem, List.class);
			this.amendId=amendId;
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}

	@Override
	public FactorRateMaster process(FactorRateRawInsert data) throws Exception {
    	FactorRateMaster saveData = new FactorRateMaster();
		saveData.setFactorTypeId(data.getFactorTypeId());
		saveData.setEffectiveDateStart(data.getEffectiveDateStart());
		saveData.setEffectiveDateEnd(data.getEffectiveDateEnd());
		saveData.setEntryDate(new Date());
		saveData.setAmendId(amendId.intValue());
		saveData.setStatus(data.getStatus());		
		saveData.setSNo(Integer.valueOf(data.getSno()));
		saveData.setCompanyId(data.getCompanyId());
		saveData.setProductId(data.getProductId());
		saveData.setCoverId(data.getCoverId());
		saveData.setSubCoverId(data.getSubCoverId()==null?0:data.getSubCoverId());
		saveData.setCreatedBy(data.getCreatedBy());
		saveData.setAgencyCode(data.getXlAgencyCode());
		saveData.setBranchCode(data.getBranchCode());
		saveData.setSectionId(data.getSectionId());
		
		// Range
		saveData.setParam1(StringUtils.isBlank(data.getParam1()) ? null :new BigDecimal(data.getParam1()) );
		saveData.setParam2(StringUtils.isBlank(data.getParam2()) ? null :new BigDecimal(data.getParam2()) );
		saveData.setParam3(StringUtils.isBlank(data.getParam3()) ? null :new BigDecimal(data.getParam3()) );
		saveData.setParam4(StringUtils.isBlank(data.getParam4()) ? null :new BigDecimal(data.getParam4()) );
		saveData.setParam5(StringUtils.isBlank(data.getParam5()) ? null :new BigDecimal(data.getParam5()) );
		saveData.setParam6(StringUtils.isBlank(data.getParam6()) ? null :new BigDecimal(data.getParam6()) );
		saveData.setParam7(StringUtils.isBlank(data.getParam7()) ? null :new BigDecimal(data.getParam7()) );
		saveData.setParam8(StringUtils.isBlank(data.getParam8()) ? null :new BigDecimal(data.getParam8()) );
		saveData.setParam21(StringUtils.isBlank(data.getParam21()) ? null :new BigDecimal(data.getParam21()) );
		saveData.setParam22(StringUtils.isBlank(data.getParam22()) ? null :new BigDecimal(data.getParam22()) );
		saveData.setParam23(StringUtils.isBlank(data.getParam23()) ? null :new BigDecimal(data.getParam23()) );
		saveData.setParam24(StringUtils.isBlank(data.getParam24()) ? null :new BigDecimal(data.getParam24()) );
		saveData.setParam25(StringUtils.isBlank(data.getParam25()) ? null :new BigDecimal(data.getParam25()) );
		saveData.setParam26(StringUtils.isBlank(data.getParam26()) ? null :new BigDecimal(data.getParam26()) );
		saveData.setParam27(StringUtils.isBlank(data.getParam27()) ? null :new BigDecimal(data.getParam27()) );
		saveData.setParam28(StringUtils.isBlank(data.getParam28()) ? null :new BigDecimal(data.getParam28()) );
		
		// Discrete
		saveData.setParam9(data.getParam9());
		saveData.setParam10(data.getParam10());
		saveData.setParam11(data.getParam11());
		saveData.setParam12(data.getParam12());
		saveData.setParam13(data.getParam13());
		saveData.setParam14(data.getParam14());
		saveData.setParam15(data.getParam15());
		saveData.setParam16(data.getParam16());
		saveData.setParam17(data.getParam17());
		saveData.setParam18(data.getParam18());
		saveData.setParam19(data.getParam19());
		saveData.setParam20(data.getParam20());
		
		try {
			saveData.setMinimumRate(StringUtils.isBlank(data.getMinimumRate()) ? null :new BigDecimal(data.getMinimumRate()));
			saveData.setRate(StringUtils.isBlank(data.getRate()) ? null :new BigDecimal(data.getRate()) );
			saveData.setMinPremium(StringUtils.isBlank(data.getMinPremium()) ? null :new BigDecimal(data.getMinPremium()) );
			saveData.setCalcType(data.getCalcType() );
		}catch (Exception e) {
			System.out.println(data.getRate());
			System.out.println(data.getSno());

			e.printStackTrace();
			
		}
		//saveData.setCalcTypeDesc(calcTypes.stream().filter( o -> o.getItemCode().equalsIgnoreCase(data.getCalcType()) ).collect(Collectors.toList()).get(0).getItemValue());		
		List<String> calcTypeArray =calcTypes.stream().filter(p -> p.get("itemCode").toString().equalsIgnoreCase(data.getCalcType()))
				.map(p ->p.get("itemValue").toString()).collect(Collectors.toList());
		saveData.setCalcTypeDesc(calcTypeArray.get(0));			
		saveData.setStatus(StringUtils.isBlank(data.getStatus()) ? "Y"  : data.getStatus());
		saveData.setCoverName(coverMD.size()>0 &&  coverMD.get("CoverName")!=null ? coverMD.get("CoverName").toString() : "") ;
		saveData.setCoverDesc(coverMD.size()>0 &&  coverMD.get("CoverDesc")!=null ? coverMD.get("CoverDesc").toString() : "") ;
		saveData.setSubCoverName(coverMD.size()>0 &&  coverMD.get("SubCoverName")!=null ? coverMD.get("SubCoverName").toString() : "") ;
		saveData.setSubCoverDesc(coverMD.size()>0 &&  coverMD.get("SubCoverDesc")!=null ? coverMD.get("SubCoverDesc").toString() : "") ;
		saveData.setFactorTypeName(coverMD.size()>0 &&  coverMD.get("FactorTypeName")!=null ? coverMD.get("FactorTypeName").toString() : "");
		saveData.setFactorTypeDesc(coverMD.size()>0 &&  coverMD.get("FactorTypeDesc")!=null  ? coverMD.get("FactorTypeDesc").toString() : "");
		saveData.setRegulatoryCode(data.getRegulatoryCode());
		// Excess
		saveData.setExcessAmount(StringUtils.isBlank(data.getExcessAmount()) ? BigDecimal.ZERO : new BigDecimal(data.getExcessAmount()) );
		saveData.setExcessPercent(StringUtils.isBlank(data.getExcessPercent()) ? BigDecimal.ZERO : new BigDecimal(data.getExcessPercent())  );
		saveData.setExcessDesc(data.getExcessDesc());
		return saveData;
	}

}
