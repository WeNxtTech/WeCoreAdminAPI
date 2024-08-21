package com.maan.eway.vehicleupload;

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
		return new StepBuilder("veh_apilcall_master_step",jobRepository)
				.partitioner(veh_apicall_step)
				.partitioner(veh_apicall_step.getName(), partitioner(0,0))
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
	public Partitioner partitioner(@Value("#{jobParameters[totalRecords]}") int totalRecords,@Value("#{jobParameters[gridSize]}") int gridSize) {
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
	
	   @Bean(name="veh_apicall_listener")
	   public JobExecutionListener listener() {
	      return new ApiCallJobListener();
	   }
	
   
		@Bean("veh_apicall_reader")
		@StepScope
	    public ItemReader<EserviceMotorDetailsRaw> reader(@Value("#{stepExecutionContext['fromId']}") int fromId,@Value("#{stepExecutionContext['toId']}") int toId,
	    		@Value("#{jobParameters[request_ref_no]}") String request_ref_no) {
	      
			Map<String, Sort.Direction> sorts = new HashMap<>();
		    sorts.put("sno", Sort.Direction.ASC); // Replace "sNo" with the field you want to sort by
		    
			return new RepositoryItemReaderBuilder<EserviceMotorDetailsRaw>()
					.name("veh_apicall_reader")
					.repository(eserviceMotorRawRepo)
	                .arguments(request_ref_no,"E",fromId,toId)
	                .methodName("findByRequestReferenceNoAndStatusNotAndSnoBetween")               
	                .pageSize(500)
	                .sorts(sorts)
	                .build();
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
