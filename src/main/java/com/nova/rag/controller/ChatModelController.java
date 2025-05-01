package com.nova.rag.controller;

import com.nova.rag.model.UserSearchRequest;
import com.nova.rag.service.ChatModelService;
import com.nova.rag.service.DocumentService;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping(path = "/chat")
@CrossOrigin
public class ChatModelController {
    private final ChatModelService chatModelService;
    private final DocumentService documentService;

    @Autowired
    public ChatModelController(ChatModelService chatModelService, DocumentService documentService) {
        this.chatModelService = chatModelService;
        this.documentService = documentService;
    }

    @PostMapping(produces = "text/html")
    public Flux<String> chatWithModel(@RequestBody UserSearchRequest request) {
        Prompt prompt = documentService.generatePromptFromRequest(request);
        return chatModelService.generateStream(prompt)
                .map(response -> response.getResult().getOutput().getText());
    }
}
