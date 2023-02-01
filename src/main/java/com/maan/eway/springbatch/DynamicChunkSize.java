package com.maan.eway.springbatch;

import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.repeat.CompletionPolicy;
import org.springframework.batch.repeat.RepeatContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

@Component
@Configuration
@JobScope
public class DynamicChunkSize implements CompletionPolicy  {
	
	
	private int chunkSize;
	private int totalrecordProcessed;
	
	EwayItemProcessor ewayItemProcessor = new EwayItemProcessor();
	
	Gson json = new Gson();

	public boolean isComplete(RepeatContext context) {          
	return totalrecordProcessed >= chunkSize;      
	}

	public boolean isComplete(RepeatContext context, RepeatStatus status) 
	{          
	    if (RepeatStatus.FINISHED == status) {              
	        return true;          
	    } else {              
	        return isComplete(context);          
	    }      
	}

	public RepeatContext start(RepeatContext context) {
		
		// Random random = new Random(100000);	
		 chunkSize =10000/4;
	     System.out.println("The chunk size has been set to " + chunkSize);  
	     return context;      
	}

	public void update(RepeatContext context) {          
		totalrecordProcessed++;      
	}
}
