package com.adobe.aem.devbot.langchain.service;

import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import kotlin.text.Charsets;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class LangchainPromptService {
    private final ChatLanguageModel chatLanguageModel;

    @Value("classpath:/prompts/system-message.st")
    private Resource systemResource;

    public LangchainPromptService(ChatLanguageModel chatLanguageModel) {
        this.chatLanguageModel = chatLanguageModel;
    }

    public String call(String message, String name, String voice) throws IOException {
        // Create a UserMessage object using the message parameter
        var userMessage = UserMessage.from(message);

        // Create a SystemMessage object with the systemResource parameter
        var systemPromptText = systemResource.getContentAsString(Charsets.UTF_8);

        // Call the chatClient object's call method giving both the messages as parameters
        return chatLanguageModel.generate(SystemMessage.from(systemPromptText), userMessage).content().text();
    }
}
