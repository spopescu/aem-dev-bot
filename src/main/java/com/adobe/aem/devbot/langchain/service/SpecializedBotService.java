package com.adobe.aem.devbot.langchain.service;

import com.adobe.aem.devbot.langchain.configuration.SpecializedBotsConfiguration;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class SpecializedBotService {
    private final SpecializedBotsConfiguration specializedBotsConfiguration;
    private final RestTemplate restTemplate;

    public SpecializedBotService(SpecializedBotsConfiguration specializedBotsConfiguration, RestTemplate restTemplate) {
        this.specializedBotsConfiguration = specializedBotsConfiguration;
        this.restTemplate = restTemplate;
    }

    public String getAnswerForOakCode(String question) {
        return getAnswer(question, specializedBotsConfiguration.getOakBotUrl());
    }

    public String getAnswerForSlingCode(String question) {
        return getAnswer(question, specializedBotsConfiguration.getSlingBotUrl());
    }

    private @Nullable String getAnswer(String question, String botUrl) {
        return restTemplate.postForObject(botUrl, new BotRequest(question), String.class);
    }

    private record BotRequest(String prompt) {}
}
