package com.maan.eway.springbatch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@EnableAsync
public class SpringBatchConfig_2 {
	
	@Autowired
	public JobRepository jobRepository;

	@Autowired
	public  ItemReader_2 itemReader_2;
	@Autowired
	public ItemProcessor_2 itemProcessor_2;
	
	@Autowired
	public ItemWritter itemWritter;
	
	@Bean("MainTableStep")
	public Step mainTableStep() {
	    return new StepBuilder("MainTableStep", jobRepository)
				.<FactorBatchRecordRes, FactorBatchRecordRes> chunk(2000,new ResourcelessTransactionManager())
				.reader(itemReader_2)
				.processor(itemProcessor_2)
				.writer(itemWritter)
				.build();
	}
	
	@Bean(name = "MainTableJob")
	public Job mainTableJob() {
		return new JobBuilder("MainTableJob", jobRepository)
				.incrementer(new RunIdIncrementer())
				.start(mainTableStep())
				.build();
	}
	
	
//	 @Bean
//	 public TaskExecutor taskExecutor(){
//		 ThreadPoolTaskExecutor asyncTaskExecutor=new ThreadPoolTaskExecutor();
//		 	asyncTaskExecutor.setCorePoolSize(100);
//		 	asyncTaskExecutor.setMaxPoolSize(200);
//		 	asyncTaskExecutor.setWaitForTasksToCompleteOnShutdown(true);
//		 	asyncTaskExecutor.setAwaitTerminationSeconds(15);	
//		 	asyncTaskExecutor.setQueueCapacity(1000);
//		 	asyncTaskExecutor.setThreadNamePrefix("eway_main_table");
//		 	asyncTaskExecutor.initialize();
//    	return asyncTaskExecutor;
//	 }
	

}
