package com.nova.rag.runner;

import com.nova.rag.model.Product;
import com.nova.rag.service.CSVParserService;
import com.nova.rag.service.DocumentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {
    private static final Logger LOGGER = LoggerFactory.getLogger(DataLoader.class);
    private final DocumentService documentService;
    private final CSVParserService csvParserService;
    @Value("${options.dataloader.enable}")
    public boolean shouldInit;
    @Value("${options.dataloader.filename}")
    public String resourceName;

    @Autowired
    public DataLoader(DocumentService documentService, CSVParserService csvParserService) {
        this.documentService = documentService;
        this.csvParserService = csvParserService;
    }

    @Override
    public void run(String... args) {
        if (!shouldInit) {
            LOGGER.warn("Skipped product information initialization of process");
            return;
        }
        LOGGER.info("Loading product information from CSV file");
        List<Product> documentList = csvParserService.parseCSV(resourceName);
        documentService.addProducts(documentList);
        LOGGER.info("Loaded product information from CSV file");
    }
}
