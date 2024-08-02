package com.maan.eway.factorrating.batch.configuration;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStream;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemWriter;

public class CSVBatchWriter implements ItemWriter<String>, ItemStream {

	String outputFilePath ="";
	
	OutputStream outputStream=null;
	
    public CSVBatchWriter(String outputFilePath)  {
		this.outputFilePath=outputFilePath;
		OutputStream outputStream=null;
		try {
			outputStream = new FileOutputStream(outputFilePath,true);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.outputStream=outputStream;

	}

	@Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        // Open resources, e.g., file or database connections
        // Example: this.outputStream = new FileOutputStream("output.txt");
		//try {
			//this.outputStream=new FileOutputStream(outputFilePath);
		//} catch (FileNotFoundException e) {
			//e.printStackTrace();
		//}
		
		System.out.println("Writer open is called");
    }

    @Override
    public void write(Chunk<? extends String> items) throws Exception {
        items.getItems().stream().forEach(item ->{
        	try {
				this.outputStream.write(item.getBytes());
			} catch (IOException e) {
				e.printStackTrace();
			}
        });
        	 	
        	 
    }

    @Override
    public void update(ExecutionContext executionContext) throws ItemStreamException {
        // Update execution context if needed, e.g., to track progress
    }

    @Override
    public void close() throws ItemStreamException {
        // Close resources
       if (this.outputStream != null) { try {
		this.outputStream.close();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} }
    }
}