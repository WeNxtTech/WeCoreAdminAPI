package com.maan.eway.springbatch;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
@EnableAsync
public class SpringBatchConfig {
	
	 @Autowired
	 public JobRepository jobRepository;
	 
	 Logger log =LogManager.getLogger(getClass());
	 
	 private static final String item_reader_input =null;
	 private static final String DELIMITER="~";
	 private ThreadPoolTaskExecutor asyncTaskExecutor;

	 @SuppressWarnings("rawtypes")
	 @Bean("itemReader")
	 @StepScope
	 public FlatFileItemReader<FactorRateRawInsert> reader(@Value("#{jobParameters[ewayBatchData]}") String data)  {
		log.info("Enter || FlatFileItemReader || " +data);
		SpringBatchMapperResponse factorData =new SpringBatchMapperResponse();
	    ObjectMapper mapper = new ObjectMapper();
	    
	    try {
	    	factorData = mapper.readValue(data, SpringBatchMapperResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		} 
	    
	    String columns =factorData.getColumns();
	    String[] arrayofColumns =columns.split("~");
	    
	    FlatFileItemReader<FactorRateRawInsert> reader= new FlatFileItemReader<>();
	    reader. setResource(new FileSystemResource(factorData.getCsvFilePath()));
	    reader.setName("CSV_FILE_READER");
	    reader.setLinesToSkip(1);
	    
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
	 

		 @Bean
		 @StepScope
		 public ItemProcessor<FactorRateRawInsert, FactorRateRawInsert> processor(){
			 return new EwayItemProcessor();
		 }
		 
		@Bean 
		@StepScope
	 	public ItemWriter<FactorRateRawInsert> writer() {
		 return new EwayItemWriter();
	 	}
	 	  
		
		//Step Object
		@Bean
	    public Step stepA() {
	       return new StepBuilder("stepA", jobRepository)
	               .<FactorRateRawInsert,FactorRateRawInsert>chunk(10000,new ResourcelessTransactionManager())
	               .reader(reader(item_reader_input))
	               .processor(processor())
	               .writer(writer())
	               .taskExecutor(taskExecutor())
	               .build(); 
	       
	    }
	    
	    
		//Job Object
	    @Bean(name = "ewayJobProcess")
		 public Job ewayJobProcess() {
		     return new JobBuilder("ewayJobProcess",jobRepository)
		             .incrementer(new RunIdIncrementer())
		             .start(stepA())
		             .listener(listener())
		             .build();
		 }
	    
	 
	
	 
	   
	   @Bean
		 public TaskExecutor taskExecutor(){
			  asyncTaskExecutor=new ThreadPoolTaskExecutor();
			 	asyncTaskExecutor.setCorePoolSize(100);
			 	asyncTaskExecutor.setMaxPoolSize(500);
			 	asyncTaskExecutor.setWaitForTasksToCompleteOnShutdown(true);
			 	asyncTaskExecutor.setAwaitTerminationSeconds(15);	
			 	asyncTaskExecutor.setQueueCapacity(1000);
			 	asyncTaskExecutor.setThreadNamePrefix("eway_spring_batch");
			 	asyncTaskExecutor.initialize();
	    	return asyncTaskExecutor;
		 }
	    
	    
	    
	   @Bean("FactorListener")
	    public Joblistener listener() {
	       return new Joblistener();
	    }
	    
}
