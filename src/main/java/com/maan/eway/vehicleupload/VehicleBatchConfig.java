package com.maan.eway.vehicleupload;

import java.io.IOException;

//import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
//import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.listener.JobExecutionListener;
import org.springframework.batch.core.partition.PartitionHandler;
import org.springframework.batch.core.partition.Partitioner;
import org.springframework.batch.core.partition.support.TaskExecutorPartitionHandler;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.infrastructure.item.file.FlatFileItemReader;
import org.springframework.batch.infrastructure.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.infrastructure.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.infrastructure.support.transaction.ResourcelessTransactionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.maan.eway.batch.repository.TransactionControlDetailsRepository;
import com.maan.eway.batch.req.EwayBatchReq;
import com.maan.eway.batch.res.EwayUploadRes;
import com.maan.eway.factorrating.batch.configuration.RangePartitioner;


@Configuration
public class VehicleBatchConfig{
		
		@Autowired
		private JobRepository jobRepository;
		
		@Autowired
		private JdbcTemplate jdbcTemplate;  
		
		private VehicleBatchWriter batchWriter =new VehicleBatchWriter();
		
		@Autowired
		private TransactionControlDetailsRepository transactionDetailRepo;
		
		private ThreadPoolTaskExecutor asyncTaskExecutor;
		
		@Autowired
		@Lazy
		@Qualifier("veh_stepexceution")
		private Step veh_stepexceution;

		
		@Bean("vehicle_raw_job")
		public Job vehicle_raw_job() {			
			return new JobBuilder("vehicle_raw_job",jobRepository)
					.start(masterJob())
					.listener(listener())
					.build();
					
		}
		
		@Bean("vehicleraw_master_Step")
		public Step masterJob() {
			return new StepBuilder("vehicleraw_master_Step",jobRepository)
					.partitioner(veh_stepexceution)
					.partitioner(veh_stepexceution.getName(), partitioner(0,0))
					.partitionHandler(partitionHandler())
					.build();
		}


		@Bean("veh_partitionHandler")
		public PartitionHandler partitionHandler() {
			 TaskExecutorPartitionHandler handler = new TaskExecutorPartitionHandler();
		        handler.setTaskExecutor(taskExecutor());
		        handler.setStep(veh_stepexceution);
		        return handler;
		  }

		@StepScope
		@Bean("veh_partitioner")
		public Partitioner partitioner(@Value("#{jobParameters[totalRecords]}") int totalRecords,
                @Value("#{jobParameters[gridSize]}") int gridSize ) {	    	
	    	RangePartitioner partitioner = new RangePartitioner();
	    	partitioner.setTotalRecord(totalRecords);
	        partitioner.setGridSize(gridSize);
	    	return partitioner;
	    }

		@SuppressWarnings("unchecked")
		@StepScope
		@Bean("vehraw_itemReader")
		public FlatFileItemReader<Record> reader(
		        @Value("#{jobParameters['EwayBatchReq']}") String fileName,
		        @Value("#{jobParameters['ExcelHeaderNames']}") String ExcelHeaderNames,
		        @Value("#{stepExecutionContext['fromId']}") int fromId,
		        @Value("#{stepExecutionContext['toId']}") int toId) {

		    String csvFilePath = "";
		    ObjectMapper mapper = new ObjectMapper();

		    try {

		        EwayBatchReq request = mapper.readValue(fileName, EwayBatchReq.class);
		        batchWriter.setEwayRequest(fileName);

		        EwayUploadRes response = request.getEwayUploadRes();
		        csvFilePath = response.getCsvfilepath();

		    } catch (Exception e) {
		        e.printStackTrace();
		    }

		    DefaultLineMapper<Record> lineMapper = new DefaultLineMapper<>();

		    DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer("~");
		    tokenizer.setNames(ExcelHeaderNames.split(","));

		    lineMapper.setLineTokenizer(tokenizer);
		    lineMapper.setFieldSetMapper(new VehicleCustomFieldSetMapper());

		    FlatFileItemReader<Record> reader =
		            new FlatFileItemReader(new FileSystemResource(csvFilePath), lineMapper);

		    reader.setName("CSV_FILE_READER");

		    if (fromId == 0) {
		        fromId = 1;
		    }

		    reader.setCurrentItemCount(fromId);
		    reader.setMaxItemCount(toId);

		    return reader;
		}
		
		@Bean
		public TaskExecutor taskExecutor(){
			  asyncTaskExecutor=new ThreadPoolTaskExecutor();
			 	asyncTaskExecutor.setCorePoolSize(20);
			 	asyncTaskExecutor.setMaxPoolSize(30);
			 	asyncTaskExecutor.setWaitForTasksToCompleteOnShutdown(true);
			 	asyncTaskExecutor.setAwaitTerminationSeconds(15);	
			 	asyncTaskExecutor.setQueueCapacity(1000);
			 	asyncTaskExecutor.setThreadNamePrefix("veh_batch");
			 	asyncTaskExecutor.initialize();
		    return asyncTaskExecutor;
		}

		
		   @Bean("veh_stepexceution")
		   public Step veh_stepexceution() {
		         return new StepBuilder("veh_stepexceution",jobRepository)
		            .<Record, Record>chunk(1000 , new ResourcelessTransactionManager())
		            .reader(reader(null,null,0,0))
		            .writer(batchWriter.itemWriter(transactionDetailRepo,jdbcTemplate))
		            .build();
		   }
		
		    @Bean(name="veh_VehicleJobListener")
			public JobExecutionListener listener() {
				 return new VehicleJobListener();
			}
		    
		    @Bean
		    public Gson printReq() {
		    	return new Gson();
		    }

}
