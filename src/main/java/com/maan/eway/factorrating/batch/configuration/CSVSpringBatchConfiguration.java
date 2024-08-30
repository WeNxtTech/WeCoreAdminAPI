package com.maan.eway.factorrating.batch.configuration;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;


@Configuration
public class CSVSpringBatchConfiguration {
	
	
	private  static String NEW_LINE_CHARACTER = "\r\n";
	private final static String OUTPUT_DATE_FORMAT="dd/MM/yyyy";
	private final static String CVS_SEPERATOR_CHAR = "\t";
	
	@Autowired
	private JobRepository jobRepository;
	
	    @Bean(name = "excelToCsvJob")
	    public Job excelToCsvJob(@Qualifier("excelToCsvJobStep1")Step step1) {
	        return new JobBuilder("excelToCsvJob",jobRepository)
	                .incrementer(new RunIdIncrementer())
	                .start(step1)
	                .listener(listener())
	                .build();
	    }

	    @Bean(name = "excelToCsvJobStep1")
	    public Step step1(@Qualifier("excelToCsvJobItemReader")ItemReader<String> reader,  @Qualifier("excelToCsvJobItemWriter")ItemWriter<String> writer) {
	        return new StepBuilder("excelToCsvJobStep1",jobRepository)
	                .<String, String>chunk(1000,new ResourcelessTransactionManager())
	                .reader(reader)
	                .writer(writer)
	                .transactionManager(new ResourcelessTransactionManager())
	                .build();
	    }

		public TaskExecutor taskExecutor() {
			ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
			executor.setCorePoolSize(50);
			executor.setMaxPoolSize(100);
			executor.setQueueCapacity(1000);
			executor.setThreadNamePrefix("csv_exeuter");
			executor.setAwaitTerminationMillis(15);
			executor.setWaitForTasksToCompleteOnShutdown(true);
			executor.initialize();
			return executor;
		}
		

		@Bean(name = "excelToCsvJobItemReader")
	    @StepScope
	    public ItemReader<String> excelItemReader(@Value("#{jobParameters[excel_file_path]}") String file,@Value("#{jobParameters[file_extension]}") String file_extension) {
	        return new ListItemReader<>(readExcel(file,file_extension));
	    }

	    @Bean(name = "excelToCsvJobItemProcessor")
	    public ItemProcessor<String, String> excelItemProcessor() {
	        return items -> items;
	    }

	    @Bean(name = "excelToCsvJobItemWriter")
	    @StepScope
	    public ItemWriter<String> csvItemWriter(@Value("#{jobParameters[csv_file_path]}") String outputFilePath) {
	        return new CSVBatchWriter(outputFilePath);
	    }

	    private List<String> readExcel(String inputFilePath,String file_extension) {
	        List<String> rows = new ArrayList<>();
	       
	        if("xlsx".equals(file_extension)) {
		        
	        	try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(new File(inputFilePath)));
		             Workbook workbook = new XSSFWorkbook(bis)) {
			            DataFormatter dataFormatter = new DataFormatter();
			            Sheet sheet = workbook.getSheetAt(0);
			            
		            	int cell_count =sheet.getRow(0).getLastCellNum();

			            int rowindex =0;
			            for (Row row : sheet) {
			            	

			            	String data ="";
			            	for (int i =0 ;i<cell_count ;i++) {
			            		Cell cell =row.getCell(i);
			            		data+=cell==null || cell.getCellType()==CellType.BLANK?""+"~":
			            			dataFormatter.formatCellValue(cell).
		                    		replace("\t", "").replace("\n", "").replace("\r", "").replace("'", "").replaceAll("^\"|\"$", "").replace(",", "")+"~";;
			            		
			            	}
			            	data =rowindex==0?"sNo~"+data:String.valueOf(rowindex)+"~"+data;
				            rows.add(data.substring(0,data.length()-1)+"\r\n");
				            rowindex++;
			            }
			            
		        } catch (IOException e) {
		            e.printStackTrace();
		        }
	        	
	        }else if ("xls".equals(file_extension)) {
	        	
	        	try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(new File(inputFilePath)));
			             Workbook workbook = new HSSFWorkbook(bis)) {
				           
	        				DataFormatter dataFormatter = new DataFormatter();
				            Sheet sheet = workbook.getSheetAt(0);
			            
				            
			            	int cell_count =sheet.getRow(0).getLastCellNum();

				            int rowindex =0;
				            for (Row row : sheet) {


				            	String data ="";
				            	for (int i =0 ;i<cell_count ;i++) {
				            		Cell cell =row.getCell(i);
				            		data+=cell==null || cell.getCellType()==CellType.BLANK?""+"~":
				            			dataFormatter.formatCellValue(cell).
			                    		replace("\t", "").replace("\n", "").replace("\r", "").replace("'", "").replaceAll("^\"|\"$", "").replace(",", "")+"~";;
				            		
				            	}
				            	data =rowindex==0?"sNo~"+data:String.valueOf(rowindex)+"~"+data;
					            rows.add(data.substring(0,data.length()-1)+"\r\n");
					            rowindex++;
				            }
				            
			        } catch (IOException e) {
			            e.printStackTrace();
			        }
	        }
	        return rows;
	    }
	    
	    
	    
	    @Bean(name="CsvBatchListener")
	    public JobExecutionListener listener() {
	       return new CsvBatchListener();
	    }

	    
	}
	


