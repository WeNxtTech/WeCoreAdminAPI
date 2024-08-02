package com.maan.eway.factorrating.batch.configuration;

import java.util.HashMap;
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

import com.maan.eway.bean.FactorRateMaster;
import com.maan.eway.repository.FactorRateMasterRepository;
import com.maan.eway.springbatch.FactorRateRawInsert;
import com.maan.eway.springbatch.FactorRateRawMasterRepository;

@Configuration
public class MainTableInsertBatchConfiguration {
	
	
	@Autowired
	private FactorRateRawMasterRepository factorRateRawMasterRepository;
	
	@Autowired
	private FactorRateMasterRepository factorRateMaterTestRepository;
	
	@Autowired
	@Qualifier("mainDataInsertStep")
	@Lazy
	private Step mainDataInsertStep;
	
	@Autowired
	private JobRepository jobRepository;	
	
	@Bean("mainDataInsertJob")
	public Job mainDataInsertJob() {
		return new JobBuilder("mainDataInsertJob",jobRepository)
                .incrementer(new RunIdIncrementer())
				.start(mainDataInsertMasterStep())
				.listener(listener())
				.build();
				
	}
		
	@Bean("mainDataInsertMasterStep")
	public Step mainDataInsertMasterStep() {
		return new StepBuilder("mainDataInsertMasterStep",jobRepository)
				.partitioner(mainDataInsertStep)
				.partitioner(mainDataInsertStep.getName(), partitioner(0,0))
				.partitionHandler(partitionHandler())
				.build();
				
	}
	
	@Bean("mainDataInsertStep")
	public Step mainDataInsertStep(@Qualifier("mainDataItemReader") ItemReader<FactorRateRawInsert> reader,
			@Qualifier("mainDataItemProcessor") ItemProcessor<FactorRateRawInsert, FactorRateMaster> processor,
			@Qualifier("mainDataItemWriter") ItemWriter<FactorRateMaster> writer) {
		return new StepBuilder("mainDataInsertStep",jobRepository)
				.<FactorRateRawInsert,FactorRateMaster>chunk(1000,new ResourcelessTransactionManager())
				.reader(reader)
				.processor(processor)
				.writer(writer)
				.transactionManager(new ResourcelessTransactionManager())
				.build();
	}
	
	@Bean("mainDataPartitions")
	@StepScope
	public Partitioner partitioner(@Value("#{jobParameters[totalRecords]}") int totalRecords,@Value("#{jobParameters[gridSize]}") int gridSize) {
		MainTablePartitions rangePartitioner = new MainTablePartitions();
		rangePartitioner.setTotalRecord(totalRecords);
		rangePartitioner.setGridSize(gridSize);
		return rangePartitioner;
	}
	
	@Bean("mainDataPartitionsHandler")
	public PartitionHandler partitionHandler() {
		TaskExecutorPartitionHandler teph = new TaskExecutorPartitionHandler();
		teph.setStep(mainDataInsertStep);
		teph.setTaskExecutor(taskExecutor());
		return teph;
	}
	
	
	@Bean("mainDataItemReader")
	@StepScope
    public ItemReader<FactorRateRawInsert> reader(@Value("#{stepExecutionContext['fromId']}") int fromId,@Value("#{stepExecutionContext['toId']}") int toId,
    		@Value("#{jobParameters[factor_id]}") String factor_id) {
      
		Map<String, Sort.Direction> sorts = new HashMap<>();
	    sorts.put("sno", Sort.Direction.ASC); // Replace "sNo" with the field you want to sort by
	    
		return new RepositoryItemReaderBuilder<FactorRateRawInsert>()
				.name("mainDataItemReader")
				.repository(factorRateRawMasterRepository)
                .arguments(factor_id,fromId,toId)
                .methodName("findByTranIdAndSnoBetween")               
                .pageSize(500)
                .sorts(sorts)
                .build();
    }

    @Bean("mainDataItemProcessor")
    @StepScope
    public ItemProcessor<FactorRateRawInsert, FactorRateMaster> processor(@Value("#{jobParameters[listItemValue]}") String listItem,
    		@Value("#{jobParameters[coverDetails]}") String covers,@Value("#{jobParameters[amendId]}") Long amendId) {       
    	return  new MainTableInsertProcessor(listItem,covers,amendId);
     
    }

    @Bean("mainDataItemWriter")
    public ItemWriter<FactorRateMaster> writer() {
        return new RepositoryItemWriterBuilder<FactorRateMaster>()
                .repository(factorRateMaterTestRepository)                
                .build();
    }
    
    @Bean("mainDataTaskExecutor")
	 public TaskExecutor taskExecutor(){
    	ThreadPoolTaskExecutor  asyncTaskExecutor=new ThreadPoolTaskExecutor();
		 	asyncTaskExecutor.setCorePoolSize(50);
		 	asyncTaskExecutor.setMaxPoolSize(50);
		 	asyncTaskExecutor.setWaitForTasksToCompleteOnShutdown(true);
		 	asyncTaskExecutor.setAwaitTerminationSeconds(5);	
		 	asyncTaskExecutor.setQueueCapacity(100);
		 	asyncTaskExecutor.setThreadNamePrefix("main_data");
		 	asyncTaskExecutor.initialize();
		 	return asyncTaskExecutor;
	 }
	
    @Bean(name="mainDataBatchInsertListener")
    public MainDataBatchInsertListener listener() {
       return new MainDataBatchInsertListener();
    }

}
