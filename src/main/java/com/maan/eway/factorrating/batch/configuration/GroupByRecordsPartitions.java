package com.maan.eway.factorrating.batch.configuration;


import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;

import com.maan.eway.factorrating.batch.FactorRatingBatchServiceImpl;
import com.maan.eway.springbatch.FactorRateRawInsert;
import com.maan.eway.springbatch.FactorRateRawMasterRepository;

public class GroupByRecordsPartitions implements Partitioner {

    private FactorRateRawMasterRepository factorRateRawMasterRepository;
    private String factor_id;
    private String discreate_columns;
    private String isDiscreate;
    private Long total_records;

    
    public GroupByRecordsPartitions(FactorRateRawMasterRepository factorRateRawMasterRepository, String factor_id,
			String discreate_columns, String isDiscreate, Long total_records) {
    	  this.factorRateRawMasterRepository=factorRateRawMasterRepository;
    	  this.factor_id=factor_id;
    	  this.discreate_columns=discreate_columns;
    	  this.isDiscreate=isDiscreate;
    	  this.total_records=total_records;
	}

	

    @Override
    public Map<String, ExecutionContext> partition(int gridSize) {
        Map<String, ExecutionContext> result = new HashMap<>();
        Map<String, List<FactorRateRawInsert>> dataMap = loadAndGroupRecords();
        gridSize=20;
        int partitionSize = (dataMap.size() + gridSize - 1) / gridSize;
        int partitionNumber = 0;

        Map<String, List<FactorRateRawInsert>> partition = new HashMap<>();
        for (Map.Entry<String, List<FactorRateRawInsert>> entry : dataMap.entrySet()) {
            partition.put(entry.getKey(), entry.getValue());
            if (partition.size() >= partitionSize) {
                ExecutionContext context = new ExecutionContext();
                context.put("data", new ArrayList<>(partition.keySet()));
                result.put("partition" + partitionNumber, context);
                partition.clear();
                partitionNumber++;
            }
        }

        // Add remaining data to a partition if any
        if (!partition.isEmpty()) {
            ExecutionContext context = new ExecutionContext();
            context.put("data", new HashMap<>(partition));
            result.put("partition" + partitionNumber, context);
        }

        return result;
    }

    private Map<String, List<FactorRateRawInsert>> loadAndGroupRecords() {
		List<FactorRateRawInsert> records = new ArrayList<>(total_records.intValue());		
		Map<String,List<FactorRateRawInsert>> groupByData = new HashMap<>();
		records = factorRateRawMasterRepository.findByTranId(factor_id);
		
		if("Y".equals(isDiscreate)) {
			
			String[] discreate_array =discreate_columns.split("~");
			List<String> fieldList =Arrays.asList(discreate_array);
			
			groupByData =groupByRecords(records, fieldList);
			
			FactorRatingBatchServiceImpl.LOCAL_DATA_STORAGE.put(factor_id, groupByData);
			
			return groupByData;
		}else if("N".equals(isDiscreate)){
			
			int partitionSize =records.size();
		    List<List<FactorRateRawInsert>> partitions = partition(records, partitionSize);

		    int index=0;
		    for(List<FactorRateRawInsert> data : partitions) {
		    	groupByData.put(String.valueOf(index), data);   	
		    	index++;
		    }
		    

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
                .mapToObj(i -> new ArrayList<T>(list.subList(i * partitionSize, Math.min((i + 1) * partitionSize, listSize))))
                .collect(Collectors.toList());
    }
}
