package com.nova.rag.service;

import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.StreamingChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class ChatModelService {
    private final StreamingChatModel streamingChatModel;

    @Autowired
    public ChatModelService(StreamingChatModel streamingChatModel) {
        this.streamingChatModel = streamingChatModel;
    }

    public Flux<ChatResponse> generateStream(Prompt prompt) {
        return streamingChatModel.stream(prompt);
    }
}
