package com.maan.eway.factorrating.batch.configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.partition.PartitionHandler;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.core.partition.support.TaskExecutorPartitionHandler;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.maan.eway.springbatch.FactorRateRawInsert;
import com.maan.eway.springbatch.FactorRateRawMasterRepository;

@Configuration
public class FactorValidationSpringBatchConfig {
	
	@Autowired
	private JobRepository jobRepository;	
	 
	 @Autowired
	 @Qualifier("groupingStep")
	 @Lazy
	 private Step groupingStep;
	 
	 @Autowired
	 private FactorRateRawMasterRepository factorRateRawMasterRepository;
	

    @Bean("groupingJob")
    public Job groupingJob() {
        return new JobBuilder("groupingJob",jobRepository)
        		.incrementer(new RunIdIncrementer())
                .start(masterStep())
                .listener(listener())
                .build();
    }

    @Bean("groupingMasterStep")
    public Step masterStep() {
        return new StepBuilder("groupingMasterStep",jobRepository)
                .partitioner(groupingStep.getName(), groupingPartitions("","","",0L))
                .partitionHandler(partitionHandler())
                .build();
    }
    
    @Bean("groupingStep")
    public Step groupingStep() {
        return new StepBuilder("groupingStep",jobRepository)
                .<Map.Entry<String, List<FactorRateRawInsert>>, List<FactorRateRawInsert>>chunk(1000,new ResourcelessTransactionManager())
                .reader(groupingItemReader(null))
                .processor(itemProcessor(""))
                .writer(itemWriter())
                .build();
    }

    /*@Bean("groupingItemReader")
    @StepScope
    public ItemReader<List<FactorRateRawInsert>> groupingItemReader(@Value("#{jobParameters[factor_id]}") String factor_id,@Value("#{jobParameters[discreate_columns]}") String discreate_columns,
    		@Value("#{jobParameters[isDiscreate]}") String isDiscreate,@Value("#{jobParameters[total_records]}") Long total_records) {
       
    	//GroupingItemReader gir = new GroupingItemReader(factorRateRawMasterRepository,factor_id,discreate_columns,isDiscreate,total_records);
    	//List<FactorRateRawInsert> records = gir.read();
    	
    	return new GroupingItemReader(factorRateRawMasterRepository,factor_id,discreate_columns,isDiscreate,total_records);
    }*/
    
    @Bean("groupingItemReader")
    @StepScope
    public ItemReader<Map.Entry<String, List<FactorRateRawInsert>>> groupingItemReader(@Value("#{stepExecutionContext['data']}") Map<String, List<FactorRateRawInsert>> dataMap) {
        List<Map.Entry<String, List<FactorRateRawInsert>>> entries = new ArrayList<>(dataMap.entrySet());
        return new ListItemReader<>(entries);
    }
    
    @Bean("groupingPartitions")
    @StepScope
    public Partitioner groupingPartitions(@Value("#{jobParameters[factor_id]}") String factor_id,@Value("#{jobParameters[discreate_columns]}") String discreate_columns,
    		@Value("#{jobParameters[isDiscreate]}") String isDiscreate,@Value("#{jobParameters[total_records]}") Long total_records) {
    	
    	return new GroupByRecordsPartitions(factorRateRawMasterRepository,factor_id,discreate_columns,isDiscreate,total_records);
    }

    @Bean("groupingItemProcessor")
    @StepScope
    public ItemProcessor<Map.Entry<String, List<FactorRateRawInsert>>, List<FactorRateRawInsert>> itemProcessor(@Value("#{jobParameters[dropdown_data]}") String dropdown_data) {    	
    	return new GroupByItemProcessor(dropdown_data);         	
    }

    @Bean("groupingItemWriter")
    public ItemWriter<List<FactorRateRawInsert>> itemWriter() {
       return new GroppingItemWriter();
    }

    @Bean("groupingExecutor")
	 public TaskExecutor taskExecutor(){
    	ThreadPoolTaskExecutor asyncTaskExecutor=new ThreadPoolTaskExecutor();
		 	asyncTaskExecutor.setCorePoolSize(100);
		 	asyncTaskExecutor.setMaxPoolSize(500);
		 	asyncTaskExecutor.setWaitForTasksToCompleteOnShutdown(true);
		 	asyncTaskExecutor.setAwaitTerminationSeconds(5);	
		 	asyncTaskExecutor.setQueueCapacity(1000);
		 	asyncTaskExecutor.setThreadNamePrefix("GROUP_DATA");
		 	asyncTaskExecutor.initialize();
   	return asyncTaskExecutor;
	 }
    
    @Bean("groupingJobListener")
    public GroupingJobListener listener() {
    	return new GroupingJobListener();
    }
    
    @Bean("groupingPartitionHandler")
    public PartitionHandler partitionHandler() {
        TaskExecutorPartitionHandler handler = new TaskExecutorPartitionHandler();
        handler.setTaskExecutor(taskExecutor());
        handler.setStep(groupingStep);
        return handler;
    }

}
