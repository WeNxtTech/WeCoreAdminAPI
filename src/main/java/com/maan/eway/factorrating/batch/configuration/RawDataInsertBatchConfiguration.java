package com.maan.eway.factorrating.batch.configuration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.partition.PartitionHandler;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.core.partition.support.TaskExecutorPartitionHandler;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maan.eway.springbatch.BlankLineRecordSeparatorPolicy;
import com.maan.eway.springbatch.FactorRateRawInsert;
import com.maan.eway.springbatch.SpringBatchMapperResponse;

@Configuration
public class RawDataInsertBatchConfiguration {

	
	 Logger log =LogManager.getLogger(getClass());
	 
	 private static final String item_reader_input =null;
	 private static final String DELIMITER="~";
	 private ThreadPoolTaskExecutor asyncTaskExecutor;

	 @Autowired
	 private JobRepository jobRepository;

	 @Autowired
	 @Qualifier("rawdataInsertBatchStep")
	 private Step slaveStep;
	 
	 
	 @Bean(name="rawDataInsertReader")
	 @StepScope
	 public FlatFileItemReader<FactorRateRawInsert> reader(@Value("#{jobParameters[ewayBatchData]}") String data
			  ,@Value("#{stepExecutionContext['fromId']}") int fromId,
	            @Value("#{stepExecutionContext['toId']}") int toId)  {
		 
		log.info("Enter || FlatFileItemReader || " +data);
		SpringBatchMapperResponse factorData =new SpringBatchMapperResponse();
	    ObjectMapper mapper = new ObjectMapper();
	    log.info("Enter || fromId || " +fromId);
	    log.info("Enter || toId || " +toId);
	    try {
	    	factorData = mapper.readValue(data, SpringBatchMapperResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		} 
	    
	    String columns =factorData.getColumns();
	    String[] arrayofColumns =columns.split(DELIMITER);
	    
	    FlatFileItemReader<FactorRateRawInsert> reader= new FlatFileItemReader<>();
	    reader. setResource(new FileSystemResource(factorData.getCsvFilePath()));
	    reader.setName("CSV_FILE_READER");
	    
	    if(fromId==0)fromId=1;
	    
	    reader.setCurrentItemCount(fromId);
	    reader.setMaxItemCount(toId);
	    @SuppressWarnings("unchecked")
		DefaultLineMapper<FactorRateRawInsert> lineMapper = new DefaultLineMapper();
	    DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
	    tokenizer.setDelimiter(DELIMITER);
	    tokenizer.setStrict(false);
	    tokenizer.setNames(arrayofColumns);
	    
	    @SuppressWarnings("unchecked")
		BeanWrapperFieldSetMapper<FactorRateRawInsert> fieldSetMapper = new BeanWrapperFieldSetMapper();
	    fieldSetMapper.setTargetType(FactorRateRawInsert.class);
	    
	    lineMapper.setLineTokenizer(tokenizer);
	    lineMapper.setFieldSetMapper(fieldSetMapper);

	    reader.setLineMapper(lineMapper);
	    reader.setRecordSeparatorPolicy(new BlankLineRecordSeparatorPolicy());
	    return reader;
	    
	    }
	 
		 @Bean(name="rawDataInsertItemProcessor")
		 @StepScope
		 public ItemProcessor<FactorRateRawInsert, FactorRateRawInsert> processor(@Value("#{jobParameters[ewayBatchData]}") String data){
			 return new RawDataItemProcessor(data);
		 }
		 
		 
		@Bean(name="rawDataInsertItemItemWriter")
		@StepScope
	 	public ItemWriter<FactorRateRawInsert> writer(@Value("#{jobParameters[ewayBatchData]}") String data) {
			return new RawDataItemWritter(data);
	 	}
	
		 @Bean(name = "rawdataInsertBatchJob")
		    public Job rawdataBatchInsert(@Qualifier("rawdataInsertBatchStep") Step step1) {
		        return new JobBuilder("rawdataInsertBatchJob",jobRepository)
		                .start(masterStep())
		                .listener(listener())
		                .build();
		    }

		    @Bean(name = "rawdataInsertBatchStep")
		    public Step slaveStep(@Qualifier("rawDataInsertReader")FlatFileItemReader<FactorRateRawInsert> reader, 
		    		@Qualifier("rawDataInsertItemProcessor")ItemProcessor<FactorRateRawInsert, FactorRateRawInsert> processor
		    	   ,@Qualifier("rawDataInsertItemItemWriter")ItemWriter<FactorRateRawInsert> writer) {
		        return new StepBuilder("slaveStep",jobRepository)
		                .<FactorRateRawInsert, FactorRateRawInsert>chunk(1000,new ResourcelessTransactionManager())
		                .reader(reader)
		                .processor(processor)
		                .writer(writer)
		                .transactionManager(new ResourcelessTransactionManager())
		                .build();
		    }

			    
	  @Bean
		 public TaskExecutor taskExecutor(){
			  asyncTaskExecutor=new ThreadPoolTaskExecutor();
			 	asyncTaskExecutor.setCorePoolSize(100);
			 	asyncTaskExecutor.setMaxPoolSize(500);
			 	asyncTaskExecutor.setWaitForTasksToCompleteOnShutdown(true);
			 	asyncTaskExecutor.setAwaitTerminationSeconds(5);	
			 	asyncTaskExecutor.setQueueCapacity(1000);
			 	asyncTaskExecutor.setThreadNamePrefix("rawdata_batchinsert");
			 	asyncTaskExecutor.initialize();
	    	return asyncTaskExecutor;
		 }
	    
	    
	    
	    @Bean(name="rawDataBatchInsertListener")
	    public JobExecutionListener listener() {
	       return new RawDataBatchInsertListener();
	    }
	    

	    
	    @Bean("rawdataMasterSetup")
	    public Step masterStep() {
	        return new StepBuilder("masterStep",jobRepository)
	                .partitioner(slaveStep)
	                .partitioner(slaveStep.getName(), partitioner(0,0))
	                .partitionHandler(partitionHandler())
	                .build();
	    }
	    
	    @Bean("partitioner")
	    @StepScope
	    public Partitioner partitioner(@Value("#{jobParameters[totalRecords]}") int totalRecords,
                @Value("#{jobParameters[gridSize]}") int gridSize ) {	    	
	    	RangePartitioner partitioner = new RangePartitioner();
	    	partitioner.setTotalRecord(totalRecords);
	        partitioner.setGridSize(gridSize);
	    	return partitioner;
	    }
	    
	    
	    @Bean("partitionHandler")
	    public PartitionHandler partitionHandler() {
	        TaskExecutorPartitionHandler handler = new TaskExecutorPartitionHandler();
	        handler.setTaskExecutor(taskExecutor());
	        handler.setStep(slaveStep);
	       // handler.setGridSize(0); // Adjust the grid size as needed
	        return handler;
	    }
	    
	    //@Bean
	    public TaskExecutor asyncTaskExecutor() {
	        SimpleAsyncTaskExecutor taskExecutor = new SimpleAsyncTaskExecutor();
	        taskExecutor.setConcurrencyLimit(3);
	        return taskExecutor;
	    }
}
