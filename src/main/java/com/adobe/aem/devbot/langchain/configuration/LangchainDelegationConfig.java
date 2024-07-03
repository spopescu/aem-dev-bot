package com.adobe.aem.devbot.langchain.configuration;

import dev.langchain4j.agent.tool.Tool;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class LangchainDelegationConfig {

    private final SpecializedBotsConfiguration specializedBotsConfiguration;
    private final RestTemplate restTemplate;

    public LangchainDelegationConfig(SpecializedBotsConfiguration specializedBotsConfiguration, RestTemplate restTemplate)  {
        this.specializedBotsConfiguration = specializedBotsConfiguration;
        this.restTemplate = restTemplate;
    }

    @Tool("Answers a question about JCR code.")
    public String getAnswerForJcrCode(String question) {
        return restTemplate.getForObject(specializedBotsConfiguration.getJcrBotUrl(), String.class, question);
    }

    @Tool("Answers a question about Sling code.")
    public String getAnswerForSlingCode(String question) {
        return restTemplate.getForObject(specializedBotsConfiguration.getSlingBotUrl(), String.class, question);
    }
}
