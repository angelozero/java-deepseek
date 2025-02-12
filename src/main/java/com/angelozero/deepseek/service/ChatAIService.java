package com.angelozero.deepseek.service;

import lombok.AllArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@AllArgsConstructor
public class ChatAIService {

    private final ChatClient chatClient;

    public String askToDeepSeekAI(String question) {
        return chatClient.prompt(question).call().content();
    }

    public Flux<String> askToDeepSeekAiWithStream(String question) {
        return chatClient.prompt(question).stream().content();
    }
}
