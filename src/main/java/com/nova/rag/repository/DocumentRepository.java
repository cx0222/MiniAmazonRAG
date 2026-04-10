package com.nova.rag.repository;

import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DocumentRepository {
    private static final int MAX_TOP_K = 10;

    private final VectorStore vectorStore;

    @Autowired
    public DocumentRepository(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    public void addDocuments(List<Document> documentList) {
        vectorStore.add(documentList);
    }

    public void deleteDocuments(List<String> idList) {
        vectorStore.delete(idList);
    }

    public List<Document> searchDocuments(String prompt, int limit) {
        int topK = Math.min(limit, MAX_TOP_K);
        SearchRequest request = SearchRequest.builder()
                .query(prompt)
                .topK(topK)
                .build();
        return vectorStore.similaritySearch(request);
    }
}
