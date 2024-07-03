package com.adobe.aem.devbot.langchain.service;

import com.adobe.aem.devbot.langchain.configuration.LangchainDelegationConfig;
import com.adobe.aem.devbot.langchain.configuration.SpecializedBotsConfiguration;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.service.AiServices;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class LangchainDelegationService {
    private final ChatLanguageModel chatLanguageModel;
    private final LangchainDelegationConfig localizedBotsConfig;

    public LangchainDelegationService(ChatLanguageModel chatLanguageModel, LangchainDelegationConfig localizedBotsConfig) {
        this.chatLanguageModel = chatLanguageModel;
        this.localizedBotsConfig = localizedBotsConfig;
    }
    public String call(String message) {
        DelegationAssistant delegationAssistant = AiServices.builder(DelegationAssistant.class)
                .chatLanguageModel(chatLanguageModel)
                // use .tools() to pass in an instance of `LangchainPaymentConfig`
                .tools(localizedBotsConfig)
                .build();
        return delegationAssistant.assist(message);
    }

    interface DelegationAssistant {
        String assist(String question);
    }
}
