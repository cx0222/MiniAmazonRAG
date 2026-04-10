package com.nova.rag.service;

import com.nova.rag.model.UserSearchRequest;
import com.nova.rag.repository.DocumentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.document.Document;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DocumentServiceTest {
    @Test
    void shouldFormatAndTruncateDocumentsInSystemPrompt() {
        DocumentRepository repository = mock(DocumentRepository.class);
        DocumentService documentService = new DocumentService(repository);
        ReflectionTestUtils.setField(documentService, "systemPrompt", "DOCUMENTS:\n{documents}");

        String documentTextExceedingLimit = "a".repeat(550);
        when(repository.searchDocuments("find long product", 5))
                .thenReturn(List.of(new Document("doc-1", documentTextExceedingLimit, Map.of("id", 1))));

        Prompt prompt = documentService.generatePromptFromRequest(new UserSearchRequest("find long product", 5));
        String systemText = prompt.getInstructions().getFirst().getText();

        assertThat(systemText).contains("Product description:");
        assertThat(systemText).contains("Attributes:");
        assertThat(systemText).contains("a".repeat(500) + "...");
        assertThat(systemText).doesNotContain("a".repeat(510));
    }
}
