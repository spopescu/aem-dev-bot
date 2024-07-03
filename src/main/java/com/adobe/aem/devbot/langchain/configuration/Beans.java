package com.adobe.aem.devbot.langchain.configuration;

import com.adobe.aem.devbot.langchain.service.LangchainRagService;
import dev.langchain4j.model.azure.AzureOpenAiChatModel;
import dev.langchain4j.model.chat.ChatLanguageModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class Beans {
    @Bean
    ChatLanguageModel chatLanguageModel(AzureOpenAICredentials azureOpenAICredentials) {
        return AzureOpenAiChatModel.builder()
                .apiKey(azureOpenAICredentials.getApiKey())
                .deploymentName(azureOpenAICredentials.getDeploymentName())
                .endpoint(azureOpenAICredentials.getEndpoint())
                .build();
    }

    @Bean
    LangchainRagService langchainRagService(ChatLanguageModel chatLanguageModel) {
        return new LangchainRagService(chatLanguageModel);
    }

    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
