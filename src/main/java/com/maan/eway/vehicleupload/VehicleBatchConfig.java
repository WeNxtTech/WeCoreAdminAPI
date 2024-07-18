package com.maan.eway.vehicleupload;

import java.io.IOException;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.maan.eway.batch.repository.TransactionControlDetailsRepository;
import com.maan.eway.batch.req.EwayBatchReq;
import com.maan.eway.batch.res.EwayUploadRes;


@Configuration
@EnableAsync
public class VehicleBatchConfig{

private static final String OVERRIDDEN_BY_EXPRESSION = null;

@Autowired
public JobBuilderFactory jobBuilderFactory;
@Autowired
public StepBuilderFactory stepBuilderFactory;
@Autowired
private JdbcTemplate jdbcTemplate;  

private VehicleBatchWriter batchWriter =new VehicleBatchWriter();
@Autowired
private TransactionControlDetailsRepository transactionDetailRepo;

private ThreadPoolTaskExecutor asyncTaskExecutor;


@StepScope
@SuppressWarnings({ "rawtypes", "unchecked" })
@Bean
public FlatFileItemReader<Record> reader(@Value("#{jobParameters[EwayBatchReq]}") String fileName,@Value("#{jobParameters[RequestReferenceNo]}") String TranId,@Value("#{jobParameters[ExcelHeaderNames]}") String ExcelHeaderNames) {
	 FlatFileItemReader<Record> reader = new FlatFileItemReader();
	 EwayUploadRes response = new EwayUploadRes();
	 try {
		 String csvFilePath = "";
		 EwayBatchReq request= new EwayBatchReq();
	     ObjectMapper mapper = new ObjectMapper();
	     try {
	    	 request = mapper.readValue(fileName, EwayBatchReq.class);
	    	 batchWriter.setEwayRequest(fileName);
	    	 response=request.getEwayUploadRes();
	    	 csvFilePath =response.getCsvfilepath();
		   } catch (JsonParseException e) {e.printStackTrace();}catch (JsonMappingException e) {e.printStackTrace();} 
	      	 catch (IOException e) {e.printStackTrace();}
	     	reader.setResource(new FileSystemResource(csvFilePath));
	     	reader.setLinesToSkip(1);
	     	reader.setLineMapper(new DefaultLineMapper() {{
		       setLineTokenizer(new DelimitedLineTokenizer("~") {{
		    	  // System.out.println("BatchExcelHeaderNames : " + ExcelHeaderNames);
		           setNames(ExcelHeaderNames.split(","));
		                }});
			      setFieldSetMapper(new VehicleCustomFieldSetMapper());
		       
		     }});
    }catch(Exception e) {e.printStackTrace();
    }
    return reader;
}

@Bean(name="VehicleJob")
public Job importUserJob(@Qualifier("VehicleListener")JobExecutionListener listener) {
    return jobBuilderFactory.get("VehicleJob")
            .incrementer(new RunIdIncrementer())
            .listener(listener)
            .flow(step1())
            .end()
            .build();
}
@Bean
public TaskExecutor taskExecutor(){
	  asyncTaskExecutor=new ThreadPoolTaskExecutor();
	 	asyncTaskExecutor.setCorePoolSize(10);
	 	asyncTaskExecutor.setMaxPoolSize(10);
	 	asyncTaskExecutor.setWaitForTasksToCompleteOnShutdown(true);
	 	asyncTaskExecutor.setAwaitTerminationSeconds(15);	
	 	asyncTaskExecutor.setQueueCapacity(1000);
	 	asyncTaskExecutor.setThreadNamePrefix("spring_batch");
	 	asyncTaskExecutor.initialize();
    return asyncTaskExecutor;
}

@Bean
public Step step1() {
    return stepBuilderFactory.get("VehicleJob")
            .<Record, Record>chunk(4000)
            .reader(reader(OVERRIDDEN_BY_EXPRESSION,OVERRIDDEN_BY_EXPRESSION,OVERRIDDEN_BY_EXPRESSION))
           // .processor(synchProcessor())
            .writer(batchWriter.itemWriter(transactionDetailRepo,jdbcTemplate))///em,dataSource,,emf
            .listener(listener())
            .taskExecutor(taskExecutor())
            .build();
}

    @Bean(name="VehicleListener")
	public VehicleJobListener listener() {
		 return new VehicleJobListener();
	}
    
    @Bean
    public Gson printReq() {
    	return new Gson();
    }

}
