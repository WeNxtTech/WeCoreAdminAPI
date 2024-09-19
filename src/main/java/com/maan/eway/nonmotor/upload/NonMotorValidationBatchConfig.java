package com.maan.eway.nonmotor.upload;

import java.util.HashMap;
import java.util.Map;

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
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.maan.eway.batch.entity.EwayEmplyeeDetailRaw;
import com.maan.eway.batch.repository.EwayEmplyeeDetailRawRepository;
import com.maan.eway.factorrating.batch.configuration.MainTablePartitions;

@Configuration
public class NonMotorValidationBatchConfig {
	
	@Autowired
	public JobRepository jobRepository;

	@Autowired
	@Qualifier("nonmot_validation_step")
	@Lazy
	private Step nonmot_validation_step;
	
	@Autowired
	private EwayEmplyeeDetailRawRepository emplyeeDetailRawRepo;;


	@Bean("nonmot_validation_job")
	public Job nonmot_validation_job() {
		return new JobBuilder("nonmot_validation_job",jobRepository)
                .incrementer(new RunIdIncrementer())
				.start(nonmot_validation_master_step())
				.listener(listener())
				.build();
				
	}
		
	@Bean("nonmot_validation_master_step")
	public Step nonmot_validation_master_step() {
		return new StepBuilder("nonmot_validation_master_step",jobRepository)
				.partitioner(nonmot_validation_step)
				.partitioner(nonmot_validation_step.getName(), partitioner(0,0))
				.partitionHandler(partitionHandler())
				.build();
				
	}
	
	@Bean("nonmot_validation_step")
	public Step nonmot_validation_step(@Qualifier("nonmot_validation_reader") ItemReader<EwayEmplyeeDetailRaw> reader,
			@Qualifier("nonmot_validation_processor") ItemProcessor<EwayEmplyeeDetailRaw, EwayEmplyeeDetailRaw> processor,
			@Qualifier("nonmot_validation_itemWriter") ItemWriter<EwayEmplyeeDetailRaw> writer) {
		return new StepBuilder("nonmot_validation_step",jobRepository)
				.<EwayEmplyeeDetailRaw,EwayEmplyeeDetailRaw>chunk(1000,new ResourcelessTransactionManager())
				.reader(reader)
				.processor(processor)
				.writer(writer)
				.listener(stepListener())
				.transactionManager(new ResourcelessTransactionManager())
				.build();
	}
	
	@Bean("nonmot_validation_partitions")
	@StepScope
	public Partitioner partitioner(@Value("#{jobParameters[totalRecords]}") int totalRecords,@Value("#{jobParameters[gridSize]}") int gridSize) {
		MainTablePartitions rangePartitioner = new MainTablePartitions();
		rangePartitioner.setTotalRecord(totalRecords);
		rangePartitioner.setGridSize(gridSize);
		return rangePartitioner;
	}
	
	@Bean("nonmot_validation_partitionsHandler")
	public PartitionHandler partitionHandler() {
		TaskExecutorPartitionHandler teph = new TaskExecutorPartitionHandler();
		teph.setStep(nonmot_validation_step);
		teph.setTaskExecutor(taskExecutor());
		return teph;
	}
	
	@Bean("nonmot_validation_TaskExecutor")
	 public TaskExecutor taskExecutor(){
   	ThreadPoolTaskExecutor  asyncTaskExecutor=new ThreadPoolTaskExecutor();
		 	asyncTaskExecutor.setCorePoolSize(15);
		 	asyncTaskExecutor.setMaxPoolSize(20);
		 	asyncTaskExecutor.setWaitForTasksToCompleteOnShutdown(true);
		 	asyncTaskExecutor.setAwaitTerminationSeconds(5);	
		 	asyncTaskExecutor.setQueueCapacity(100);
		 	asyncTaskExecutor.setThreadNamePrefix("veh_val");
		 	asyncTaskExecutor.initialize();
		 	return asyncTaskExecutor;
	 }
	
	   @Bean(name="nonmot_validation_listener")
	   public JobExecutionListener listener() {
	      return new NonMotValJobListener();
	   }
	   
	   @Bean(name="nonmot_validation_step_listener")
	   public NonMotStepExecutionListener stepListener() {
	      return new NonMotStepExecutionListener();
	   }
	   
	   
	
   
		@Bean("nonmot_validation_reader")
		@StepScope
	    public ItemReader<EwayEmplyeeDetailRaw> reader(@Value("#{stepExecutionContext['fromId']}") int fromId,@Value("#{stepExecutionContext['toId']}") int toId,
	    		@Value("#{jobParameters[request_ref_no]}") String request_ref_no) {
	      
			Map<String, Sort.Direction> sorts = new HashMap<>();
		    sorts.put("sno", Sort.Direction.ASC); // Replace "sNo" with the field you want to sort by
		    
			return new RepositoryItemReaderBuilder<EwayEmplyeeDetailRaw>()
					.name("nonmot_validation_reader")
					.repository(emplyeeDetailRawRepo)
	                .arguments(request_ref_no,"E",fromId,toId)
	                .methodName("findByRequestReferenceNoAndStatusNotAndSnoBetween")               
	                .pageSize(500)
	                .sorts(sorts)
	                .build();
	  }

 
   	 @Bean(name="nonmot_validation_processor")
	 @StepScope
	 public ItemProcessor<EwayEmplyeeDetailRaw, EwayEmplyeeDetailRaw> processor(){
		 return new NonMotorRawDataValProcess();
	 }
	 
	 
	@Bean(name="nonmot_validation_itemWriter")
	@StepScope
	public ItemWriter<EwayEmplyeeDetailRaw> writer() {
		return new NonMotorRawDataValWritter();
	}



}
