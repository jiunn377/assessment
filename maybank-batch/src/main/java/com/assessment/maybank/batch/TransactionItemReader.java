package com.assessment.maybank.batch;


import com.assessment.maybank.beanio.TransactionBeanIO;
import lombok.extern.slf4j.Slf4j;
import org.beanio.BeanReader;
import org.beanio.StreamFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

@Slf4j
@Component
public class TransactionItemReader implements ItemReader<TransactionBeanIO> {

    private BeanReader beanReader;
    private boolean skipHeader = true;

    @Value("${assessment.batch.beanio.path}")
    private String beanIOFilePath;

    @Value("${assessment.batch.datasource.path}")
    private String datasourceFilePath;

    @Autowired
    private ResourceLoader resourceLoader;

    @PostConstruct
    public void init(){

    	try {
        StreamFactory factory = StreamFactory.newInstance();
        factory.loadResource(beanIOFilePath);

        Resource resource = resourceLoader.getResource(datasourceFilePath);
        beanReader = factory.createReader("transactions", new FileReader(resource.getFile()));

        if (skipHeader) {
            beanReader.read();
            skipHeader = false;
        }
        
		} catch (IllegalArgumentException e) {
			log.error("Error reading file: " + e.getMessage());
			e.printStackTrace();
			
		} catch (FileNotFoundException e) {
			log.error("File not found: " + e.getMessage());
			e.printStackTrace();
			
		} catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public TransactionBeanIO read() throws Exception {

        TransactionBeanIO transactionBeanIo = (TransactionBeanIO) beanReader.read();
        
        if (transactionBeanIo != null) {
            log.info("Read transactionBeanIo: {}", transactionBeanIo);
        } else {
            log.info("No more transactions to read.");
        }
        
        return transactionBeanIo;
    }

    public void close() throws Exception {
        beanReader.close();
    }
}