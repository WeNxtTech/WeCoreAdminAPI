package com.maan.crm.thread;

import java.util.concurrent.Executor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class AsyncConfiguration  {

	private Logger log = LogManager.getLogger(AsyncConfiguration.class);
	
	@Bean
	public Executor taskExecuter() {
		ThreadPoolTaskExecutor taskExecuter = new ThreadPoolTaskExecutor();
		taskExecuter.setCorePoolSize(1);
		taskExecuter.setMaxPoolSize(1);
		taskExecuter.setQueueCapacity(100);
		taskExecuter.setThreadNamePrefix("CRM-NEW-");
		taskExecuter.initialize();
		log.info("Running Thread :" + taskExecuter);
		return taskExecuter;
	}
}
