package com.maan.eway.factorrating.batch.configuration;

import java.util.HashMap;
import java.util.Map;

import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;


public class RangePartitioner implements Partitioner {

	
	private Integer totalRecord;
	
	private Integer gridSize;
	
	public void setTotalRecord(Integer totalRecord) {
		this.totalRecord = totalRecord;
	}

	public void setGridSize(Integer gridSize) {
		this.gridSize = gridSize;
	}

	@Override
    public Map<String, ExecutionContext> partition(int gridSize) {
        Map<String, ExecutionContext> partitionMap = new HashMap<>();
        double remainder =((double) totalRecord / this.gridSize);
        double floor = Math.ceil(remainder); // Divide the 100,000 records by the grid size
        int range=(int) floor;
        int fromId = 1;
        int toId = range;
        for (int i = 1; i <=this.gridSize; i++) {
            ExecutionContext context = new ExecutionContext();
            context.putInt("fromId", fromId);
            context.putInt("toId", toId);
            partitionMap.put("partition" + i, context);

            fromId = toId ;
            toId = toId + range;
        }
        return partitionMap;
    }
}