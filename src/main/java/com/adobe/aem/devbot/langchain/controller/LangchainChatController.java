package com.adobe.aem.devbot.langchain.controller;

import com.adobe.aem.devbot.langchain.configuration.AzureOpenAICredentials;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.TokenWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiTokenizer;
import dev.langchain4j.model.output.FinishReason;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.model.output.TokenUsage;
import dev.langchain4j.service.AiServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/devbot")
public class LangchainChatController {

    private final Logger logger = LoggerFactory.getLogger(LangchainChatController.class);

    private final ChatLanguageModel chatLanguageModel;

    Assistant assistant;

    public LangchainChatController(ChatLanguageModel chatLanguageModel, AzureOpenAICredentials azureOpenAICredentials) {
        this.chatLanguageModel = chatLanguageModel;
        // Initialize the Assistant object using AIServices
        ChatMemory chatMemory = TokenWindowChatMemory.withMaxTokens(100,
                new OpenAiTokenizer(azureOpenAICredentials.getDeploymentName()));
        assistant = AiServices.builder(Assistant.class).chatLanguageModel(chatLanguageModel).build();
    }

    @GetMapping("/generate/1")
    public String simpleStringMessage(@RequestParam(value = "message", defaultValue = "Tell me a funny joke") String message) {
        // Pass the message to the generate method of the chatLanguageModel and return its output
        return chatLanguageModel.generate(message);
    }

    @GetMapping("/generate/2")
    public String simplePromptMessage(@RequestParam(value = "message", defaultValue = "Recommend me a good action movie for the weekend") String message) {
        // Wrap the message in a UserMessage object and pass it to the generate method of the chatLanguageModel
        // Try to also log useful information from the response
        Response<AiMessage> response = chatLanguageModel.generate(UserMessage.from(message));
        FinishReason finishReason = response.finishReason();
        logger.info("Finish reason: {}", finishReason);
        TokenUsage tokenUsage = response.tokenUsage();
        logger.info("Token usage: {}", tokenUsage);
        return response.content().text();
    }

    @GetMapping("/generate/3")
    public String simplePromptMultipleMessages(@RequestParam(value = "message", defaultValue = "What is the distance between Bucharest and Cluj-Napoca in km?") String[] message) {
        // Pass in multiple messages to the LLM
        List<ChatMessage> chatMessages = Arrays.stream(message).map(UserMessage::from).collect(Collectors.toList());
        return chatLanguageModel.generate(chatMessages).content().text();
    }

    @GetMapping("/generate/4")
    public String simplePromptForAIService(@RequestParam(value = "message", defaultValue = "What is the distance between Bucharest and Cluj-Napoca in km?") String message) {
        // Call the LLM via the Assistant object and return the response
        return assistant.chat(message);
    }

    interface Assistant {
        String chat(String userMessage);
    }
}
