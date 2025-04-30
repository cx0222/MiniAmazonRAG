package com.nova.rag.service;

import com.nova.rag.entity.Product;
import com.nova.rag.entity.UserSearchRequest;
import com.nova.rag.repository.DocumentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DocumentService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DocumentService.class);

    private final DocumentRepository documentRepository;
    @Value("classpath:prompts/default_prompt.st")
    private Resource systemPrompt;

    @Autowired
    public DocumentService(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    public int addProduct(Product product) {
        return addProducts(List.of(product));
    }

    public int addProducts(List<Product> productList) {
        List<Document> documentList = productList.stream()
                .map(Product::toDocument)
                .toList();
        return addDocuments(documentList);
    }

    public int addDocuments(List<Document> documentList) {
        int count = 0;
        try {
            documentRepository.addDocuments(documentList);
            count = documentList.size();
            LOGGER.info("Added {} documents", count);
        } catch (Exception exception) {
            LOGGER.error("Failed to add documents", exception);
        }
        return count;
    }

    public int deleteDocument(String idList) {
        return deleteDocuments(List.of(idList));
    }

    public int deleteDocuments(List<String> idList) {
        int count = 0;
        try {
            documentRepository.deleteDocuments(idList);
            count = idList.size();
            LOGGER.info("Deleted {} documents", count);
        } catch (Exception exception) {
            LOGGER.error("Failed to delete documents", exception);
        }
        return count;
    }

    private Message getSystemMessage(List<Document> documentList) {
        String documents = documentList.stream()
                .map(document -> "- The product description is %s, and the attributes are %s"
                        .formatted(document.getText(), document.getMetadata()))
                .collect(Collectors.joining("\n\n"));
        SystemPromptTemplate template = new SystemPromptTemplate(systemPrompt);
        return template.createMessage(Map.of("documents", documents));
    }

    public Prompt generatePromptFromRequest(UserSearchRequest request) {
        LOGGER.info("Generating prompt for {}", request);
        List<Document> documentList = documentRepository.searchDocuments(request.getQuery(), request.getLimit());
        Message systemMessage = getSystemMessage(documentList);
        UserMessage userMessage = new UserMessage(request.getQuery());
        return new Prompt(systemMessage, userMessage);
    }
}
