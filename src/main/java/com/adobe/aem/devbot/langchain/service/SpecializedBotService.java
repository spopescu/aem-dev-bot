package com.adobe.aem.devbot.langchain.service;

import com.adobe.aem.devbot.langchain.configuration.SpecializedBotsConfiguration;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.logging.Logger;

@Service
public class SpecializedBotService {
    private final SpecializedBotsConfiguration specializedBotsConfiguration;
    private final RestTemplate restTemplate;

    Logger logger = Logger.getLogger(SpecializedBotService.class.getName());

    public SpecializedBotService(SpecializedBotsConfiguration specializedBotsConfiguration, RestTemplate restTemplate) {
        this.specializedBotsConfiguration = specializedBotsConfiguration;
        this.restTemplate = restTemplate;
    }

    public String getAnswerForOakCode(String question) {
        logger.info("Calling oak bot for answer to question: "  + question);
        String oakBotAnswer = getAnswer(question, specializedBotsConfiguration.getOakBotUrl());
        logger.info("Oak bot answer: " + oakBotAnswer);
        return getAnswer(question, specializedBotsConfiguration.getOakBotUrl());
    }

    public String getAnswerForSlingCode(String question) {
        logger.info("Calling sling bot for answer to question: " + question);
        String slingBotAnswer = getAnswer(question, specializedBotsConfiguration.getSlingBotUrl());
        logger.info("Sling bot answer: " + slingBotAnswer);
        return slingBotAnswer;
    }

    private @Nullable String getAnswer(String question, String botUrl) {
        return restTemplate.postForObject(botUrl, new BotRequest(question), String.class);
    }

    private record BotRequest(String question) {}
}
