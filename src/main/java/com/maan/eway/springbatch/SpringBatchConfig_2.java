package com.maan.eway.springbatch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class SpringBatchConfig_2 {
	
	@Autowired
	public JobBuilderFactory jobBuilderFactory;

	@Autowired
	public StepBuilderFactory stepBuilderFactory;
	
	@Autowired
	public  ItemReader_2 itemReader_2;
	@Autowired
	public ItemProcessor_2 itemProcessor_2;
	
	@Autowired
	public ItemWritter itemWritter;
	
	@Bean("MainTableStep")
	public Step mainTableStep() {
		return stepBuilderFactory.get("mainTableStep")
				.<FactorBatchRecordRes, FactorBatchRecordRes> chunk(2000)
				.reader(itemReader_2)
				.processor(itemProcessor_2)
				.writer(itemWritter)
				.build();
	}
	
	@Bean(name = "MainTableJob")
	public Job mainTableJob() {
		return jobBuilderFactory.get("mainTableJob")
				.incrementer(new RunIdIncrementer())
				.start(mainTableStep())
				.build();
	}
	
	
	 @Bean
	 public TaskExecutor taskExecutor(){
		 ThreadPoolTaskExecutor asyncTaskExecutor=new ThreadPoolTaskExecutor();
		 	asyncTaskExecutor.setCorePoolSize(100);
		 	asyncTaskExecutor.setMaxPoolSize(200);
		 	asyncTaskExecutor.setWaitForTasksToCompleteOnShutdown(true);
		 	asyncTaskExecutor.setAwaitTerminationSeconds(15);	
		 	asyncTaskExecutor.setQueueCapacity(1000);
		 	asyncTaskExecutor.setThreadNamePrefix("eway_main_table");
		 	asyncTaskExecutor.initialize();
    	return asyncTaskExecutor;
	 }
	

}
