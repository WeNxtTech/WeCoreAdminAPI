package com.maan.eway.vehicleupload;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
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
import org.springframework.context.annotation.Primary;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.maan.eway.batch.entity.EserviceMotorDetailsRaw;
import com.maan.eway.batch.repository.EserviceMotorDetailsRawRepository;
import com.maan.eway.factorrating.batch.configuration.MainTablePartitions;

@Configuration
public class VehApiCallBatchConfig {

	@Autowired
	public JobRepository jobRepository;

	@Autowired
	@Qualifier("veh_apicall_step")
	@Lazy
	private Step veh_apicall_step;
	
	@Autowired
	@Qualifier("veh_apicall_listener")
	private JobExecutionListener apiCallJobListener;
	
	@Autowired
	private EserviceMotorDetailsRawRepository eserviceMotorRawRepo;;


	@Bean("veh_apicall_job")
	public Job veh_apicall_job() {
		return new JobBuilder("veh_apicall_job",jobRepository)
                .incrementer(new RunIdIncrementer())
				.start(veh_apilcall_master_step())
				.listener(listener())
				.build();
				
	}
		
	@Bean("veh_apilcall_master_step")
	public Step veh_apilcall_master_step() {
	    return new StepBuilder("veh_apilcall_master_step", jobRepository)
	            .partitioner("veh_apicall_step", partitioner(null, null))
	            .step(veh_apicall_step)
	            .partitionHandler(partitionHandler())
	            .build();
	}
	
	@Bean("veh_apicall_step")
	public Step veh_apicall_step(@Qualifier("veh_apicall_reader") ItemReader<EserviceMotorDetailsRaw> reader,
			@Qualifier("veh_apicall_processor") ItemProcessor<EserviceMotorDetailsRaw, EserviceMotorDetailsRaw> processor,
			@Qualifier("veh_apicall_itemWriter") ItemWriter<EserviceMotorDetailsRaw> writer) {
		return new StepBuilder("veh_apicall_step",jobRepository)
				.<EserviceMotorDetailsRaw,EserviceMotorDetailsRaw>chunk(1000,new ResourcelessTransactionManager())
				.reader(reader)
				.processor(processor)
				.writer(writer)
				.transactionManager(new ResourcelessTransactionManager())
				.build();
	}
	
	@Bean("veh_apicall_partitions")
	@StepScope
	public Partitioner partitioner(@Value("#{jobParameters[totalRecords]}") Integer totalRecords,@Value("#{jobParameters[gridSize]}") Integer gridSize) {
		MainTablePartitions rangePartitioner = new MainTablePartitions();
		rangePartitioner.setTotalRecord(totalRecords);
		rangePartitioner.setGridSize(gridSize);
		return rangePartitioner;
	}
	
	@Bean("veh_apicall_partitionsHandler")
	public PartitionHandler partitionHandler() {
		TaskExecutorPartitionHandler teph = new TaskExecutorPartitionHandler();
		teph.setStep(veh_apicall_step);
		teph.setTaskExecutor(taskExecutor());
		return teph;
	}
	
	@Bean("veh_api_TaskExecutor")
	public TaskExecutor taskExecutor() {
	    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
	    executor.setCorePoolSize(10);
	    executor.setMaxPoolSize(15);
	    executor.setWaitForTasksToCompleteOnShutdown(true);
	    executor.setAwaitTerminationSeconds(120); // ← was 5 seconds — way too short for API calls
	    executor.setQueueCapacity(50);
	    executor.setThreadNamePrefix("veh_val");
	    executor.initialize();
	    return executor;
	}
	
	@Bean(name="veh_apicall_job_listener")
	public JobExecutionListener listener() {
	    return apiCallJobListener;  
	}
	
   
	   @Bean("veh_apicall_reader")
	   @Primary
	   @StepScope
	   public ItemReader<EserviceMotorDetailsRaw> reader(
	           @Value("#{jobParameters['request_ref_no']}") String requestRefNo,
	           @Value("#{stepExecutionContext['minValue'] ?: stepExecutionContext['fromId'] ?: 1}") Integer minValue,
	           @Value("#{stepExecutionContext['maxValue'] ?: stepExecutionContext['toId'] ?: 999999}") Integer maxValue) {

	       List<EserviceMotorDetailsRaw> allList =
	               eserviceMotorRawRepo.findByRequestReferenceNotest(requestRefNo)
	                   .stream()
	                   .filter(Objects::nonNull)
	                   .toList();

	       int fromIndex = Math.max(0, minValue - 1);
	       int toIndex   = Math.min(allList.size(), maxValue);

	       List<EserviceMotorDetailsRaw> partitionSlice =
	               (fromIndex >= toIndex || fromIndex >= allList.size())
	                   ? Collections.emptyList()
	                   : allList.subList(fromIndex, toIndex);

	       System.out.println("veh_apicall_reader || partition [" + minValue + "-" + maxValue + "] → " + partitionSlice.size() + " records");

	       return new ListItemReader<>(new ArrayList<>(partitionSlice));
	   }

 
   	 @Bean(name="veh_apicall_processor")
	 @StepScope
	 public ItemProcessor<EserviceMotorDetailsRaw, EserviceMotorDetailsRaw> processor(@Value("#{jobParameters[Authorization]}") String Authorization){
		 return new APIItemProcessor(Authorization);
	 }
	 
	 
	@Bean(name="veh_apicall_itemWriter")
	@StepScope
	public ItemWriter<EserviceMotorDetailsRaw> writer() {
		return new APIItemWriter();
	}


}