package com.nova.rag.service;

import com.nova.rag.model.Product;
import com.nova.rag.model.UserSearchRequest;
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
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DocumentService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DocumentService.class);
    private static final int MAX_DOCUMENT_TEXT_LENGTH = 500;

    private final DocumentRepository documentRepository;
    @Value("${options.prompt}")
    private String systemPrompt;

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
        documentRepository.addDocuments(documentList);
        int count = documentList.size();
        LOGGER.info("Added {} documents", count);
        return count;
    }

    public int deleteProduct(long productId) {
        String documentId = new UUID(0, productId).toString();
        return deleteDocuments(List.of(documentId));
    }

    public int deleteDocuments(List<String> idList) {
        documentRepository.deleteDocuments(idList);
        int count = idList.size();
        LOGGER.info("Deleted {} documents", count);
        return count;
    }

    private Message getSystemMessage(List<Document> documentList) {
        String documents = documentList.stream()
                .map(document -> """
                        - Product description:
                          %s
                          Attributes:
                          %s
                        """.formatted(truncateText(document.getText()), document.getMetadata()))
                .collect(Collectors.joining("\n\n"));
        SystemPromptTemplate template = new SystemPromptTemplate(systemPrompt);
        return template.createMessage(Map.of("documents", documents));
    }

    private String truncateText(String text) {
        if (text == null || text.length() <= MAX_DOCUMENT_TEXT_LENGTH) {
            return text;
        }
        return text.substring(0, MAX_DOCUMENT_TEXT_LENGTH) + "...";
    }

    public Prompt generatePromptFromRequest(UserSearchRequest request) {
        LOGGER.info("Generating prompt for {}", request);
        List<Document> documentList = documentRepository.searchDocuments(request.getQuery(), request.getLimit());
        Message systemMessage = getSystemMessage(documentList);
        UserMessage userMessage = new UserMessage(request.getQuery());
        return new Prompt(systemMessage, userMessage);
    }
}
