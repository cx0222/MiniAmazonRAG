package com.nova.rag.service;

import com.nova.rag.entity.Product;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

@Service
public class CSVParserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CSVParserService.class);

    public List<Product> parseCSV(String fileName) {
        CSVFormat format = CSVFormat.DEFAULT.builder()
                .setHeader()
                .setSkipHeaderRecord(false)
                .setTrim(true)
                .setIgnoreEmptyLines(true)
                .setIgnoreHeaderCase(true)
                .get();
        List<Product> productList = new ArrayList<>();
        try (
                BufferedReader reader = new BufferedReader(new FileReader(fileName));
                CSVParser csvParser = CSVParser.parse(reader, format)
        ) {
            for (CSVRecord csvRecord : csvParser) {
                Product product = new Product(
                        Long.parseLong(csvRecord.get("id")),
                        Long.parseLong(csvRecord.get("category_id")),
                        csvRecord.get("name"),
                        Double.parseDouble(csvRecord.get("price")),
                        csvRecord.get("description")
                );
                productList.add(product);
            }
            LOGGER.info("Total products to add: {}", productList.size());
        } catch (FileNotFoundException fileNotFoundException) {
            LOGGER.error("File not found", fileNotFoundException);
        } catch (Exception exception) {
            LOGGER.error("Failed to parse CSV file", exception);
        }
        return productList;
    }
}
