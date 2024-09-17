package com.maan.eway.factorrating.batch.configuration;


import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.support.ListItemReader;

import com.maan.eway.springbatch.FactorRateRawInsert;
import com.maan.eway.springbatch.FactorRateRawMasterRepository;

public class GroupingItemReader implements ItemReader<List<FactorRateRawInsert>> {
	
    private FactorRateRawMasterRepository factorRateRawMasterRepository;
    private String factor_id;
    private String discreate_columns;
    private String isDiscreate;
    private Long total_records;
    private Iterator<Map.Entry<String, List<FactorRateRawInsert>>> groupIterator;

    
    public GroupingItemReader(FactorRateRawMasterRepository factorRateRawMasterRepository, String factor_id,
			String discreate_columns, String isDiscreate, Long total_records) {
    	  this.factorRateRawMasterRepository=factorRateRawMasterRepository;
    	  this.factor_id=factor_id;
    	  this.discreate_columns=discreate_columns;
    	  this.isDiscreate=isDiscreate;
    	  this.total_records=total_records;
	}

	@Override
    public List<FactorRateRawInsert> read() {
		 if (groupIterator == null) {
	            // Load data from the database and group it
			 	Map<String, List<FactorRateRawInsert>> groupedRecords = loadAndGroupRecords();
	            groupIterator = groupedRecords.entrySet().iterator();
	        }
	        if (groupIterator.hasNext()) {
	            return groupIterator.next().getValue();
	        } else {
	            return null;
	        }
		
    }
	
	private Map<String, List<FactorRateRawInsert>> loadAndGroupRecords() {
		List<FactorRateRawInsert> records = new ArrayList<>(total_records.intValue());		
		Map<String,List<FactorRateRawInsert>> groupByData = new HashMap<>();
		records = factorRateRawMasterRepository.findByTranId(factor_id);
				
			if("Y".equals(isDiscreate)) {
				
				String[] discreate_array =discreate_columns.split("~");
				List<String> fieldList =Arrays.asList(discreate_array);
				
				groupByData =groupByRecords(records, fieldList);
				
				return groupByData;
			}else if("N".equals(isDiscreate)){
				
				groupByData = records.stream().collect(Collectors.groupingBy(FactorRateRawInsert :: getAgencyCode));
				
			    return groupByData;
			}
		
	       
		return null;
	}

	public static Map<String, List<FactorRateRawInsert>> groupByRecords(List<FactorRateRawInsert> list, List<String> fieldNames) {
        return list.stream().collect(Collectors.groupingBy(obj -> {
           StringJoiner key = new StringJoiner("~");
            for (String fieldName : fieldNames) {
                try {
                    Field field = obj.getClass().getDeclaredField(fieldName);
                    field.setAccessible(true);
                    key.add(String.valueOf(field.get(obj)));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            return key.toString();
        }));
    }
	
	
	public static <T> List<List<T>> partition(List<T> list, int partitionSize) {
        if (partitionSize <= 0) {
            throw new IllegalArgumentException("Partition size must be greater than zero.");
        }

        int listSize = list.size();
        int numPartitions = (listSize + partitionSize - 1) / partitionSize; // Ceiling division

        return IntStream.range(0, numPartitions)
                .mapToObj(i -> list.subList(i * partitionSize, Math.min((i + 1) * partitionSize, listSize)))
                .collect(Collectors.toList());
    }
}