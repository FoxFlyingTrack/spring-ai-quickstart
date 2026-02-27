package com.example.demo.controller;

import com.example.demo.pojo.ActorFilms;
import org.springframework.ai.chat.client.AdvisorParams;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/ai")
public class AiController {

    private final ChatClient defaultChatClient;
    private final ChatClient creativeChatClient;
    private final ChatClient preciseChatClient;

    public AiController(
            @Qualifier("defaultChatClient") ChatClient defaultChatClient,
            @Qualifier("creativeChatClient") ChatClient creativeChatClient,
            @Qualifier("preciseChatClient") ChatClient preciseChatClient) {
        this.defaultChatClient = defaultChatClient;
        this.creativeChatClient = creativeChatClient;
        this.preciseChatClient = preciseChatClient;
    }

    @GetMapping("/chat")
    public String chat(@RequestParam String userInput) {
        return defaultChatClient.prompt()
                .user(userInput)
                .call()
                .content();
    }

    @GetMapping("/chat/entity")
    public ActorFilms chatEntity(@RequestParam String userInput) {
//        return defaultChatClient.prompt()
//                .user(userInput)
//                .call()
//                .entity(ActorFilms.class);

        // 原生化结构化输出
        return defaultChatClient.prompt()
//                .advisors(AdvisorParams.ENABLE_NATIVE_STRUCTURED_OUTPUT)
                // 官方写法如上 但失败。ai提示为下成功
                .advisors(a -> a.param("enable_native_structured_output", true))
                // 在提示词中明确告诉模型需要 "JSON" 格式
                .user("Generate the filmography for a random actor. **Output the result strictly in JSON format.**")
                .call()
                .entity(ActorFilms.class);
    }

    @GetMapping("/chat/entityList")
    public List<ActorFilms> entityList() {
        return defaultChatClient.prompt()
                .user("Generate the filmography of 5 movies for Tom Hanks and Bill Murray.")
                .call()
                .entity(new ParameterizedTypeReference<List<ActorFilms>>() {
                });
    }

    @GetMapping("/chat/creative")
    public String creativeChat(@RequestParam String userInput) {
        return creativeChatClient.prompt()
                .user(userInput)
                .call()
                .content();
    }

    @GetMapping("/chat/precise")
    public String preciseChat(@RequestParam String userInput) {
        return preciseChatClient.prompt()
                .user(userInput)
                .call()
                .content();
    }
}