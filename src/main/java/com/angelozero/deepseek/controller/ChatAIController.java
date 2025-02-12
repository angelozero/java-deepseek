package com.angelozero.deepseek.controller;

import com.angelozero.deepseek.service.ChatAIService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/ai-api")
@AllArgsConstructor
public class ChatAIController {

    private final ChatAIService service;

    @PostMapping
    public ResponseEntity<String> askToAi(@RequestBody QuestionRequest questionRequest) {
        return ResponseEntity.ok(service.askToDeepSeekAI(questionRequest.question()));
    }

    @PostMapping("/stream")
    public ResponseEntity<Flux<String>> askToAiWithStream(@RequestBody QuestionRequest questionRequest) {
        return ResponseEntity.ok(service.askToDeepSeekAiWithStream(questionRequest.question()));
    }
}
