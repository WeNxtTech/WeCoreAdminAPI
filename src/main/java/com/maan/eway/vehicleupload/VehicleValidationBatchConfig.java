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
import org.springframework.batch.item.data.builder.RepositoryItemWriterBuilder;
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
public class VehicleValidationBatchConfig {

	@Autowired
	private EserviceMotorDetailsRawRepository emdrRepository;
	
	@Autowired
	@Qualifier("veh_val_insertStep")
	@Lazy
	private Step veh_val_insertStep;
	
	@Autowired
	private JobRepository jobRepository;	
	
	@Bean("veh_validation_job")
	public Job vehValidationJob() {
		return new JobBuilder("veh_validation_job",jobRepository)
                .incrementer(new RunIdIncrementer())
				.start(veh_master_step())
				.listener(listener())
				.build();
				
	}
		
	@Bean("veh_val_master_step")
	public Step veh_master_step() {
		return new StepBuilder("veh_master_step",jobRepository)
				.partitioner(veh_val_insertStep)
				.partitioner(veh_val_insertStep.getName(), partitioner(0,0))
				.partitionHandler(partitionHandler())
				.build();
				
	}
	
	@Bean("veh_val_insertStep")
	public Step veh_val_insertStep(@Qualifier("veh_val_reader") ItemReader<EserviceMotorDetailsRaw> reader,
			@Qualifier("veh_val_ItemProcessor") ItemProcessor<EserviceMotorDetailsRaw, EserviceMotorDetailsRaw> processor,
			@Qualifier("veh_val_writer") ItemWriter<EserviceMotorDetailsRaw> writer) {
		return new StepBuilder("veh_val_insertStep",jobRepository)
				.<EserviceMotorDetailsRaw,EserviceMotorDetailsRaw>chunk(1000,new ResourcelessTransactionManager())
				.reader(reader)
				.processor(processor)
				.writer(writer)
				.listener(jobExecutionListener())
				.transactionManager(new ResourcelessTransactionManager())
				.build();
	}
	
	@Bean("veh_val_partitions")
	@StepScope
	public Partitioner partitioner(@Value("#{jobParameters[totalRecords]}") int totalRecords,@Value("#{jobParameters[gridSize]}") int gridSize) {
		MainTablePartitions rangePartitioner = new MainTablePartitions();
		rangePartitioner.setTotalRecord(totalRecords);
		rangePartitioner.setGridSize(gridSize);
		return rangePartitioner;
	}
	
	@Bean("veh_val_partitionsHandler")
	public PartitionHandler partitionHandler() {
		TaskExecutorPartitionHandler teph = new TaskExecutorPartitionHandler();
		teph.setStep(veh_val_insertStep);
		teph.setTaskExecutor(taskExecutor());
		return teph;
	}
	
	@Bean("veh_val_reader")
	@StepScope
    public ItemReader<EserviceMotorDetailsRaw> reader(@Value("#{stepExecutionContext['fromId']}") int fromId,@Value("#{stepExecutionContext['toId']}") int toId,
    		@Value("#{jobParameters[request_ref_no]}") String factor_id) {
      
		Map<String, Sort.Direction> sorts = new HashMap<>();
	    sorts.put("sno", Sort.Direction.ASC); // Replace "sNo" with the field you want to sort by
	    
		return new RepositoryItemReaderBuilder<EserviceMotorDetailsRaw>()
				.name("veh_val_reader")
				.repository(emdrRepository)
                .arguments(factor_id,fromId,toId)
                .methodName("findByRequestReferenceNoAndSnoBetween")               
                .pageSize(500)
                .sorts(sorts)
                .build();
    }

    @Bean("veh_val_ItemProcessor")
    @StepScope
    public ItemProcessor<EserviceMotorDetailsRaw, EserviceMotorDetailsRaw> processor() {       
    	return new VehValidationProcessor();
     
    }

    @Bean("veh_val_writer")
    public ItemWriter<EserviceMotorDetailsRaw> writer() {
        return new RepositoryItemWriterBuilder<EserviceMotorDetailsRaw>()
                .repository(emdrRepository)                
                .build();
    }
    
    @Bean("veh_val_TaskExecutor")
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
	
    @Bean(name="veh_val_Listener")
    public JobExecutionListener listener() {
       return new VehicleValBatchInsertListener();
    }
    
    @Bean("customStepExecutionListener")
    public CustomStepExecutionListener jobExecutionListener() {
        return new CustomStepExecutionListener();
    }


}
