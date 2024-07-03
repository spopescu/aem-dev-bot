package com.adobe.aem.devbot.langchain.service;

import com.adobe.aem.devbot.langchain.configuration.LangchainDelegationConfig;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.service.AiServices;
import org.springframework.stereotype.Service;

@Service
public class LangchainDelegationService {
    private final ChatLanguageModel chatLanguageModel;

    public LangchainDelegationService(ChatLanguageModel chatLanguageModel) {
        this.chatLanguageModel = chatLanguageModel;
    }
    public String call(String message) {
        DelegationAssistant delegationAssistant = AiServices.builder(DelegationAssistant.class)
                .chatLanguageModel(chatLanguageModel)
                // use .tools() to pass in an instance of `LangchainPaymentConfig`
                .tools(new LangchainDelegationConfig())
                .build();
        return delegationAssistant.assist(message);
    }

    interface DelegationAssistant {
        String assist(String question);
    }
}
